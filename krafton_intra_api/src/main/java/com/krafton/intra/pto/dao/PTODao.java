package com.krafton.intra.pto.dao;

import com.krafton.intra.core.dto.PagingDto;
import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;

import java.util.List;
import java.util.Map;

public interface PTODao {
    public PTOResponse.EmployeePTODto select(int id);
    public Object selectPtoType();
    public Map<String,Object> selectHolidayMap();
    int insertPTOHistory(PTORequest.PaidTimeOffDto pto);
    int checkPTOExists(int employeeId, String fromDate, String toDate, String ptoType);
    int insertPTOItems(Map<String,Object> ptoItems);
    int mergePTOSummary(Map<String, Object> ptoSummaryMap);

    List<PTOResponse.CancellablePaidTimeOffDto> getCancellablePTOs(int id);
    int cancelPTOHistories(PTORequest.CancelPaidTimeOffDto cancelPto);
    int rollbackPTOSummary(PTORequest.CancelPaidTimeOffDto cancelPto);
    float getSumUsePTOForRollback(PTORequest.CancelPaidTimeOffDto cancelPto);
    int deletePTOItems(PTORequest.CancelPaidTimeOffDto cancelPto);

    int countPTOHistories(PTORequest.PaidTimeOffHistoryDto historyDto);
    List<Object> selectPTOHistories(PagingDto<Object> pagingDto);
    List<PTOResponse.PaidTimeOffCalenderDto> getPTOSchedule(Map<String,String> paramMap);
}
