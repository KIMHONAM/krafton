package com.krafton.intra.pto.dao;

import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PTODaoImpl implements PTODao{

    private final SqlSession sqlSession;
    private final ImmutableConfiguration commonCode;

    // 휴가 구분 조회
    @Override
    public Object selectPtoType() {
        return this.sqlSession.selectList("getCommonCode", commonCode.getString("pto.type"));
    }

    @Override
    public int insertPTOHistory(PTORequest.PaidTimeOffDto pto) {
        return this.sqlSession.insert("insertPTOHistory",pto);
    }

    @Override
    public PTOResponse.EmployeePTODto select(int id) {
        return this.sqlSession.selectOne("getUserPtoInfoById", id);
    }


    @Override
    public Map<String, Object> selectHolidayMap() {
        String fromDate = LocalDate.now().getYear() + "0101";
        String toDate = LocalDate.now().plusYears(1).getYear() + "1231";    // 휴가 신청 제한 2년
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("fromDate",fromDate);
        paramMap.put("toDate",toDate);
        List<Map<String, Object>> map = this.sqlSession.selectList("getHolidayMap",paramMap);
        return converListToMap(map,"HOLIDAY","HOLIDAY_DETAIL");
    }

    // 두개 컬럼 리스트 데이터 키,벨류 맵으로 변환
    private Map<String, Object> converListToMap(List<Map<String, Object>> map, String aColumnName, String bColumnName) {
        Map<String, Object> result = new HashMap<>();
        for(Map<String, Object> entry:map) {
            result.put(entry.get(aColumnName).toString(), entry.get(bColumnName));
        }
        return result;
    }

    @Override
    public int insertPTOItems(Map<String, Object> ptoItems) {
        return this.sqlSession.insert("insertPTOItems",ptoItems);
    }

    @Override
    public int mergePTOSummary(Map<String, Object> ptoSummaryMap) {
        return this.sqlSession.insert("mergePTOSummary",ptoSummaryMap);
    }

    @Override
    public int rollbackPTOSummary(PTORequest.CancelPaidTimeOffDto cancelPto) {
        return this.sqlSession.update("rollbackUseDays",cancelPto);
    }

    @Override
    public int deletePTOItems(PTORequest.CancelPaidTimeOffDto cancelPto) {
        return this.sqlSession.delete("deletePTOItems",cancelPto);
    }

    @Override
    public int cancelPTOHistories(PTORequest.CancelPaidTimeOffDto cancelPto) {
        return this.sqlSession.update("cancelPTOHistories", cancelPto);
    }

    @Override
    public float getSumUsePTOForRollback(PTORequest.CancelPaidTimeOffDto cancelPto) {
        return this.sqlSession.selectOne("selectSumUsePtoForRollback",cancelPto);
    }

    @Override
    public List<PTOResponse.CancellablePaidTimeOffDto> getCancellablePTOs(int id) {
        return this.sqlSession.selectList("selectCancellablePTOs",id);
    }

    @Override
    public int checkPTOExists(int employeeId, String fromDate, String toDate, String ptoType) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("fromDate",fromDate);
        paramMap.put("toDate",toDate);
        paramMap.put("ptoType",ptoType);
        paramMap.put("employeeId",employeeId);

        if(!commonCode.getString("pto.alldays.type").equalsIgnoreCase(ptoType)){
            paramMap.put("allDayPtoType",commonCode.getString("pto.alldays.type"));
        }

        return this.sqlSession.selectOne("checkPTOExists", paramMap);
    }
}
