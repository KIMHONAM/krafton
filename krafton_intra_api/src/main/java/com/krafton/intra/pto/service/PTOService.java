package com.krafton.intra.pto.service;

import com.krafton.intra.employee.dto.EmployeeRequest;

public interface PTOService {

    EmployeeRequest.EmployeeDto getEmployeePTOInfo(int id);

}
