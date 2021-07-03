package com.krafton.intra.pto.service;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;

import java.util.List;

public interface PTOService {

    Object getEmployeePTOInfo(int id);
    void applicatePTO(PTORequest.PaidTimeOffDto pto);
    PTOResponse.PaidTimeOffCalenderDto getPTOSchedule(int id);
    List<PTOResponse.CancellablePaidTimeOffDto> getCancellablePTOs(int id);
    Object getPtoType();
    void cancelPTO(PTORequest.CancelPaidTimeOffDto cancelPto);

}
