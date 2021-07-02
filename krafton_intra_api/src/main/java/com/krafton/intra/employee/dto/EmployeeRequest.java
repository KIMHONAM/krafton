package com.krafton.intra.employee.dto;

import lombok.Data;

public class EmployeeRequest {

    @Data
    public static class EmployeeDto{
        private Integer id;
        private String employeeNumber;
        private String departmentName;
        private String departmentCode;
        private String name;
        private String position;
        private String hireDate;

    }

}
