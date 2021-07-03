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

    // 휴가 기본 정보 조회
    @Data
    public static class EmployeePTODto {

        EmployeeResponse.EmployeeDto employee;
        private float occurDays;    // 발생연차
        private float useDays;      // 사용연차
        private float unusedDays;   // 잔여연차
    }

    @Data
    public static class CancellablePaidTimeOffDto {

        private String employeeId;
        private String startDate;
        private String endDate;
        private String days;
        private String ptoType;

    }

    // 휴가 구분
    @Data
    public static class PaidTimeOffTypeDto{
        private String code; // 휴가 코드
        private String codeName; // 휴가 이름
    }

    // 공통코드
    @Data
    public static class CommonCodeDto{
        private String code; // 휴가 코드
        private String codeName; // 휴가 이름
    }

}
