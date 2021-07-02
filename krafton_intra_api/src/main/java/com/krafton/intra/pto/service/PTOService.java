package com.krafton.intra.pto.service;

import com.krafton.intra.employee.dto.EmployeeResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.dto.PTOResponse;

public interface PTOService {

    Object getEmployeePTOInfo(int id);
    int applicatePTO(PTORequest.PaidTimeOffDto pto);
    PTOResponse.PaidTimeOffCalenderDto getPTOSchedule(int id);
    int getCancellablePTOs(int id);

}
