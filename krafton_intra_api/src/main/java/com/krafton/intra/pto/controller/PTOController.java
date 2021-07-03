package com.krafton.intra.pto.controller;

import com.krafton.intra.core.dto.ApiResponse;
import com.krafton.intra.pto.dto.PTORequest;
import com.krafton.intra.pto.service.PTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/pto")
public class PTOController {

    @Autowired
    PTOService ptoService;

    // 휴가 기본 정보 조회
    @GetMapping("/info/id/{id}")
    public ResponseEntity<Object> getUserPTOInformation(@PathVariable int id) {
        Object obj = ptoService.getEmployeePTOInfo(id);
        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).payload(obj).build());
    }

    // 휴가 신청
    @PostMapping("")
    public ResponseEntity<Object> applicatePTO(@RequestBody PTORequest.PaidTimeOffDto ptoRequest) throws InterruptedException {
        ptoService.applicatePTO(ptoRequest);
        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).build());
    }

    // 휴가 일정 조회
    @GetMapping("/calendar/id/{id}")
    public ResponseEntity<Object> getPTOSchedule(@PathVariable int id) {

        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).payload(ptoService.getCancellablePTOs(id)).build());
    }

    // 휴가 취소 가능 리스트 조회
    @GetMapping("/cancel/id/{id}")
    public ResponseEntity<Object> getCancellablePTOs(@PathVariable int id) {

        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).payload(ptoService.getCancellablePTOs(id)).build());
    }

    // 휴가 취소
    @PutMapping("")
    public ResponseEntity<Object> cancelPTOs(@RequestBody PTORequest.CancelPaidTimeOffDto cancelPto) {

        ptoService.cancelPTO(cancelPto);
        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).build());
    }

    // 휴가 신청 리스트 조회
    @GetMapping("/list/id/{id}")
    public ResponseEntity<Object> getUserPTOList(@PathVariable int id) {

        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).payload(ptoService.getEmployeePTOInfo(id)).build());
    }

    // 휴가 구분 조회
    @GetMapping("/types")
    public ResponseEntity<Object> getPsTOTypes() {

        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).payload(ptoService.getPtoType()).build());
    }

    // 휴가 취소 리스트 조회 - get
    // 휴가 취소 - post

    // 휴가 신청 - post
    // 휴가 일정 보기 - get

    // 휴가 신청 내역 조회
    // 휴가 구분 조회

    // paging

}
