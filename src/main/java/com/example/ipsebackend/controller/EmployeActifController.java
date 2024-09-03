package com.example.ipsebackend.controller;

import com.example.ipsebackend.service.EmployeActifServices;
import com.example.ipsebackend.service.EmpoyeRetraiteService;
import com.example.ipsebackend.service.RetenueIpseService;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/uploadactif")

public class EmployeActifController {

    private final EmployeActifServices employeActifServices;
    private final RetenueIpseService retenueIpseService;
@Autowired
    public EmployeActifController(EmployeActifServices employeActifServices, RetenueIpseService retenueIpseService) {
        this.employeActifServices = employeActifServices;

    this.retenueIpseService = retenueIpseService;
}
    @GetMapping("/")
    public String sayHello() {
        return " Hi.. Welcome";
    }

    @PostMapping(value = "/uploadactif" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        employeActifServices.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/uploadRetenue" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel1(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        retenueIpseService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
