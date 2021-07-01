package com.krafton.intra.pto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/pto")
public class PTOController {

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUserPtoInformation(@PathVariable String id){

        return ResponseEntity.ok().build();
    }
}
