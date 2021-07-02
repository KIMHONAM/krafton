package com.krafton.intra.pto.controller;

import com.krafton.intra.core.dto.ApiResponse;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUserPtoInformation(@PathVariable int id) {

        return ResponseEntity.ok(ApiResponse.builder().isSuccess(true).data(ptoService.getEmployeePTOInfo(id)).build());
    }

    @GetMapping("/error/test")
    public String testException() throws Exception {
          if(1==1)  throw new Exception("111");
        return "";
    }

    // 휴가 취소 리스트 조회 - get
    // 휴가 취소 - post

    // 휴가 신청 - post
    // 휴가 일정 보기 - get

    // 휴가 신청 내역 조회
    // 휴가 구분 조회

    // paging

}
