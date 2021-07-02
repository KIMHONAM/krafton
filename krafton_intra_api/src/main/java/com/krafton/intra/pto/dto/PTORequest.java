package com.krafton.intra.pto.dto;

import com.krafton.intra.employee.dto.EmployeeRequest;
import lombok.Data;


public class PTORequest {

    @Data
    public static class PaidTimeOffDto {

        private EmployeeRequest.EmployeeDto employee;

    }

}
