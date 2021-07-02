package com.krafton.intra.pto.dto;

import com.krafton.intra.employee.dto.EmployeeResponse;
import lombok.Data;

public class PTOResponse {

    // 휴가 Calendar
    @Data
    public static class PaidTimeOffCalenderDto {

        private String name;
        private String startDate;
        private String endDate;
        private String ptoType;

    }

    @Data
    public static class EmployeePTODto {

        EmployeeResponse.EmployeeDto employee;
        private float occurDays;
        private float useDays;
        private float unusedDays;
    }

    @Data
    public static class CancellablePaidTimeOffDto {

        private String employeeId;
        private String startDate;
        private String endDate;
        private String days;
        private String ptoType;

    }

}
