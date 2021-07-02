package com.krafton.intra.pto.controller;

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
    public ResponseEntity<Object> getUserPtoInformation(@PathVariable int id){

        return ResponseEntity.ok(ptoService.getEmployeePTOInfo(id));
    }
}
