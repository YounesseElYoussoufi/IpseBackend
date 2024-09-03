package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.DetteDTO;
import com.example.ipsebackend.dto.EmployeRetraiteDTO;
import com.example.ipsebackend.dto.PaiementDTO;
import com.example.ipsebackend.service.EmpoyeRetraiteService;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class EmployeRetraiteController {

    private final EmpoyeRetraiteService employeRetraiteService;

    @Autowired
    public EmployeRetraiteController(EmpoyeRetraiteService employeRetraiteService) {
        this.employeRetraiteService = employeRetraiteService;
    }
    @GetMapping("/")
    public String sayHello() {
        return " Hi.. Welcome";
    }

    @PostMapping(value = "/upload" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        employeRetraiteService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/employe-retraite")
    public Page<EmployeRetraiteDTO> listEmployeRetraites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        return employeRetraiteService.listEmployeRetraites(page, size, search);
    }


    @GetMapping("/employe-retraite/dettes")
    public List<DetteDTO> getDettesByNrcar(@RequestParam String nrcar) {
        return employeRetraiteService.getDettesByNrcar(nrcar);
    }

    @GetMapping("/employe-retraite/paiements")
    public List<PaiementDTO> getPaiementsByNrcar(@RequestParam String nrcar) {
        return employeRetraiteService.getPaiementsByNrcar(nrcar);
    }
}
