package com.krafton.intra.pto.dto;

import lombok.Data;


public class PTORequest {

    // 휴가 신청용
    @Data
    public static class PaidTimeOffDto {

        private int employeeId;
        private String startDate;
        private String endDate;
        private String ptoType;
        private String reason;
        private String applicateDate;   // 신청일
        private String applicant;       // 신청자

    }


}
