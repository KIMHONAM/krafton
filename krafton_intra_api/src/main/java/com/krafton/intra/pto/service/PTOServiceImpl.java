package com.krafton.intra.pto.service;

import com.krafton.intra.core.dto.PagingDto;
import com.krafton.intra.core.dto.PagingRequest;
import com.krafton.intra.core.dto.PagingResponse;
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
import org.springframework.transaction.annotation.Transactional;

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
public class PTOServiceImpl implements PTOService {

    private static final Logger LOGGER = LogManager.getLogger(PTOServiceImpl.class);

    /**
     * 휴가 신청 내역 조회
     *
     * @param pagingRequest
     * @return
     */
    @Override
    public PagingResponse getPTOHistory(PagingRequest<PTORequest.PaidTimeOffHistoryDto> pagingRequest) {

        int page = pagingRequest.getPage();
        int limit = pagingRequest.getLimit();
        PTORequest.PaidTimeOffHistoryDto historyDto = pagingRequest.getPayload();

        int totalCount = ptoDao.countPTOHistories(historyDto);
        PagingDto<Object> pagingDto = PagingDto.builder()
                .paramData(historyDto)
                .limit(limit)
                .offset(page == 1 ? 0 : ((page-1)*limit))
                .build();
        List<Object> histories = ptoDao.selectPTOHistories(pagingDto);

        return PagingResponse.builder().list(histories).totalItems(totalCount).build();
    }

    final ImmutableConfiguration commonCode;

    @Autowired
    PTODao ptoDao;

    /**
     * 휴가 취소
     * @param cancelPto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelPTO(PTORequest.CancelPaidTimeOffDto cancelPto) {

        if(StringUtils.isEmpty(cancelPto.getCancelReason())){
            throw new BusinessException("ERR100011");   // 취소사유 필수
        }

        if(cancelPto.getPtoHistoryIds() == null || cancelPto.getPtoHistoryIds().size() == 0){
            throw new BusinessException("ERR100012");   // 취소 데이터 확인 필요
        }

        cancelPto.setStatus(commonCode.getString("pto.status.cancel"));
        int cancelHistoriesRows = ptoDao.cancelPTOHistories(cancelPto);

        if( cancelHistoriesRows != cancelPto.getPtoHistoryIds().size()){
            throw new BusinessException("ERR100012");   // 취소 데이터 확인 필요
        }

        float sumUsedPTODaysForRollback = ptoDao.getSumUsePTOForRollback(cancelPto);
        if( !(sumUsedPTODaysForRollback >= 0)){         // 사용 휴가일이 0보다 크지 않으면 데이터 확인 필요
            throw new BusinessException("ERR100012");
        }
        cancelPto.setRollbackDays(sumUsedPTODaysForRollback);
        int effectedSummaryRows = ptoDao.rollbackPTOSummary(cancelPto);

        if(effectedSummaryRows != 1){                   //  요약 테이블 업데이트 갱신 건수 체크
            throw new BusinessException("ERR100012");
        }
        int deletedPtoItems = ptoDao.deletePTOItems(cancelPto);

    }

    /**
     * 휴가 신청
     * @param pto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applicatePTO(PTORequest.PaidTimeOffDto pto) {

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
        passedPTO.put("employeeId",pto.getEmployeeId());

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
            summaryMap.put("unusedDays", employeeInfo.getOccurDays() - (employeeInfo.getUseDays() + realUseDays));
        }

        int mergeSummaryResult = ptoDao.mergePTOSummary(summaryMap);
        if (mergeSummaryResult != 2) {    // insert into on duplicate key --> 정상 결과 : 2
            throw new BusinessException("ERR100010");   // 마지막 이력까지 정상 업데이트 실패 시 모두 롤백
        }

    }

    /**
     * 휴가 캘린터 일정 조회
     *
     * @param deptCode
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<PTOResponse.PaidTimeOffCalenderDto> getPTOSchedule(String deptCode, String start, String end) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("deptCode", deptCode);
        paramMap.put("start", start);
        paramMap.put("end", end);
        return ptoDao.getPTOSchedule(paramMap);
    }

    /**
     * 휴가 취소 가능 리스트 조회
     *
     * @param id
     * @return
     */
    @Override
    public List<PTOResponse.CancellablePaidTimeOffDto> getCancellablePTOs(int id) {
        return ptoDao.getCancellablePTOs(id);
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
