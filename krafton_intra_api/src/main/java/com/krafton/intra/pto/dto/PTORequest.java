package com.krafton.intra.pto.dto;

import lombok.Data;

import java.util.List;


public class PTORequest {

    // 휴가 신청용
    @Data
    public static class PaidTimeOffDto {

        private String id;
        private int employeeId;
        private String startDate;
        private String endDate;
        private String ptoType;
        private String reason;
        private String status;
        private String applicateDate;   // 신청일
        private int applicant;       // 신청자
        private int approver;        // 승인자
        private String approveDate;     // 승인일자
        private float realUseDays;       // 신청자

    }

    // 휴가 취소
    @Data
    public static class CancelPaidTimeOffDto {
        private List<String> ptoHistoryIds;
        private int employeeId;
        private String cancelReason;
        private String status;
        private String currentHistoryId;
        private float rollbackDays;
    }

}
