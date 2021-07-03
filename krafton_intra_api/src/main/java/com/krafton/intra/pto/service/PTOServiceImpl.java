package com.krafton.intra.pto.service;

import com.krafton.intra.core.exception.BusinessException;
import com.krafton.intra.pto.dao.PTODao;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PTOServiceImpl implements PTOService{

    private static final Logger LOGGER = LogManager.getLogger(PTOServiceImpl.class);

    final ImmutableConfiguration commonCode;

    @Autowired
    PTODao ptoDao;

    /**
     * 휴가 신청
     * @param pto
     * @return
     */
    @Override
    public int applicatePTO(PTORequest.PaidTimeOffDto pto) {

        Map<String,Object> passedPTO = validatePTO(pto);    // 검증 통과한 insert용 데이터

        StringBuffer ptoId = new StringBuffer().append(pto.getEmployeeId()).append("_").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        String nowDateString = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        passedPTO.put("ptoHistoryId",ptoId);

        float realUseDays = (Float)passedPTO.get("realUseDays");

        pto.setId(ptoId.toString());
        pto.setApplicant(pto.getEmployeeId());
        pto.setApplicateDate(nowDateString);
        pto.setStatus(commonCode.getString("pto.status.applicate"));
        pto.setRealUseDays(realUseDays);

        // 추후 결재 승인용
        pto.setApprover(pto.getEmployeeId());
        pto.setApproveDate(nowDateString);

        passedPTO.put("pto",pto);
        ptoDao.insertPTOHistory(pto); // 휴가 이력 추가
        ptoDao.insertPTOItems(passedPTO);   // 휴가 개별 내역 생성


        Map<String,Object> summaryMap = new HashMap<>();

        summaryMap.put("employeeId", pto.getEmployeeId());
        PTOResponse.EmployeePTODto employeeInfo = ((PTOResponse.EmployeePTODto)passedPTO.get("employeeInfo")); // 휴가 검증 시 조회된 사용자 기초 정보
        if(((PTOResponse.EmployeePTODto)passedPTO.get("employeeInfo")).getOccurDays() == 0){    // 기존 휴가 사용 내역 없을 경우 새로 insert
            float occurDays = commonCode.getFloat("pto.occurred.days");
            summaryMap.put("occurDays",occurDays);
            summaryMap.put("useDays",realUseDays);
            summaryMap.put("unusedDays", occurDays-realUseDays);
        }else{
            summaryMap.put("occurDays",employeeInfo.getOccurDays());
            summaryMap.put("useDays",employeeInfo.getUseDays()+realUseDays);
            summaryMap.put("unusedDays", employeeInfo.getOccurDays() - (employeeInfo.getUseDays()+realUseDays));
        }

        int mergeSummaryResult = ptoDao.mergePTOSummary(summaryMap);
        LOGGER.debug("mergeSummaryResult : "+mergeSummaryResult);
        // 이전 날짜 휴가 신청 제한 & 2년뒤 휴가 신청 제한

        // 해당 기간 내 휴가 겹치는 시간 있는지 체크 - employee_pto_items

        // 시작일 ~ 종료일 주말, 공휴일 제외한 날짜 계산
        // 신청 내역에 기록
        // 개별 employee_pto_items 테이블에 기록
        // 휴가 요약 테이블 갱신

        // 이후  컨트롤러에서 기본정보, 휴가 취소 가능 내역 재조회
        // 뷰에서 휴가 신청내역 재조회

        return 0;
    }

    @Override
    public PTOResponse.PaidTimeOffCalenderDto getPTOSchedule(int id) {
        return null;
    }

    @Override
    public int getCancellablePTOs(int id) {
        return 0;
    }

    /**
     * 기초 임직원 정보 조회 
     * @param id
     * @return
     */
    @Override
    public Object getEmployeePTOInfo(int id) {
        return ptoDao.select(id);
    }

    @Override
    public Object getPtoType() {
        return ptoDao.selectPtoType();
    }

    /**
     * 휴가 신청 전 검증
     * @param pto
     * @return
     */
    public Map<String,Object> validatePTO(PTORequest.PaidTimeOffDto pto){

        /*
        * 1.휴가 구분 검증
        * 2.휴가 기간 입력값 부재 검증
        * 3.사용자 정보 검증
        * 4.휴가 기간 입력값 검증
        * 5.휴가 기간 과거 불가
        * 6.휴가 기간 범위 제한 검증
        * 7.공휴일,주말 제외한 실제 사용일 계산
        * 8.반차 여부 확인
        * 9.잔여 연차 초과 여부 검증
        * 10.기존재 휴가일 체크
        * */

        Map<String,Object> passedPTO = new HashMap<>();
        List<String> realUseDayList = new ArrayList<>();

        if(StringUtils.isEmpty(pto.getPtoType())){
            throw new BusinessException("ERR100001"); // 1.휴가 구분 미선택
        }
        if(StringUtils.isEmpty(pto.getStartDate()) || StringUtils.isEmpty(pto.getEndDate())){
            throw new BusinessException("ERR100002"); // 2.휴가 시작, 종료일 정확히 기입 요망
        }

        PTOResponse.EmployeePTODto employeeInfo = ptoDao.select(pto.getEmployeeId());   // 3.사용자 정보 조회
        if(employeeInfo == null){
            throw new BusinessException("ERR100006");    // unknown user
        }

        long diffDays = 0;
        float realUseDays = 0;
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            startDate = LocalDate.parse(pto.getStartDate(), DateTimeFormatter.ISO_DATE);
            endDate = LocalDate.parse(pto.getEndDate(), DateTimeFormatter.ISO_DATE);
            diffDays = ChronoUnit.DAYS.between(startDate,endDate)+1;
        }catch (Exception e){
            throw new BusinessException("ERR100002"); // 4.휴가 시작, 종료일 정확히 기입 요망
        }

        if( startDate.isBefore(LocalDate.now()) ){
            throw new BusinessException("ERR100009");   // 5. 과거 기간 휴가 신청 불가
        }

        int yearDiff = (startDate.getYear() - LocalDate.now().getYear());
        if(yearDiff >= 2){
            throw new BusinessException("ERR100003"); // 6.휴가 신청 제한 기간 2년
        }

        Map<String,Object> holidayMap = ptoDao.selectHolidayMap(); // DB에서 공휴일 목록 조회
        for (int i = 0; i < diffDays; i++){
            LocalDate now = startDate.plusDays(i);
            LOGGER.debug(now);

            // 7.공휴일에 속하거나 주말인 경우 실제 사용일에 반영하지 않음.
            if( !(holidayMap.containsKey(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    ||
                    ("SATURDAY".equalsIgnoreCase(now.getDayOfWeek().toString()) || "SUNDAY".equalsIgnoreCase(now.getDayOfWeek().toString()))) ){
                realUseDays+=1;
                realUseDayList.add(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }

        if(realUseDays == 0){
            throw new BusinessException("ERR100007");   // 실제 휴가 사용일이 없습니다.
        }

        if(!"PTOT000001".equalsIgnoreCase(pto.getPtoType())){ // 8.현재 연차 아닐 경우 0.5일로 설정
            realUseDays = 0.5f;
        }

        LOGGER.debug("realUseDays : "+realUseDays);
        LOGGER.debug("diffDays : "+diffDays);

        if(realUseDays > employeeInfo.getUnusedDays()){
            throw new BusinessException("ERR100005");   // 9.잔여 연차 부족
        }

        int existPTOCount = ptoDao.checkPTOExists(pto.getEmployeeId(),pto.getStartDate(),pto.getEndDate(),pto.getPtoType());

        if(existPTOCount > 0){
            throw new BusinessException("ERR100008");   // 10.이미 해당 기간 휴가일 존재
        }


        passedPTO.put("realUseDayList",realUseDayList); // 실제 휴가일 리스트
        passedPTO.put("realUseDays",realUseDays);
        passedPTO.put("employeeInfo",employeeInfo);
        return passedPTO;
    }
}
