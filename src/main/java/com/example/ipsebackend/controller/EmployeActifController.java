package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.EmployeActifDTO;
import com.example.ipsebackend.dto.EmployeActifReDTO;
import com.example.ipsebackend.enums.CategoriieAct;
import com.example.ipsebackend.service.EmployeActifService;
import com.example.ipsebackend.service.EmployeActifServices;
import com.example.ipsebackend.service.EmpoyeRetraiteService;
import com.example.ipsebackend.service.RetenueIpseService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/uploadactif")
@CrossOrigin("*")
public class EmployeActifController {
    @Autowired
    private EmployeActifService employeActifService;

    private final EmployeActifServices employeActifServices;
    private final RetenueIpseService retenueIpseService;
@Autowired
    public EmployeActifController(EmployeActifServices employeActifServices, RetenueIpseService retenueIpseService) {
        this.employeActifServices = employeActifServices;

    this.retenueIpseService = retenueIpseService;
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

    /*@GetMapping("/employeactif")
    public ResponseEntity<List<EmployeActifDTO>> getAllEmployeActif() {
        List<EmployeActifDTO> employeActifs = employeActifService.getAllEmployeActif();
        return ResponseEntity.ok(employeActifs);
    }*/
    @GetMapping("/employeactif")
    public ResponseEntity<Page<EmployeActifDTO>> getAllEmployeActif(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String matricule) {

        Page<EmployeActifDTO> employeActifs = employeActifService.getAllEmployeActif(page, size, matricule);
        return ResponseEntity.ok(employeActifs);
    }


    @GetMapping("/{matriculeEmp}")
    public EmployeActifReDTO getEmployeActifWithRetenues(@PathVariable String matriculeEmp) {
        return employeActifServices.getEmployeActifWithRetenues(matriculeEmp);
    }

    @DeleteMapping("/{matriculeEmp}")
    public ResponseEntity<Void> deleteEmployeActifByMatricule(@PathVariable String matriculeEmp) {
        employeActifService.deleteEmployeActifByMatricule(matriculeEmp);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmployesActifs(@RequestBody List<String> matriculesEmp) {
        employeActifService.deleteEmployesActifsByMatricules(matriculesEmp);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employes")
    public ResponseEntity<Void> exportEmployes(@RequestParam CategoriieAct categorie, HttpServletResponse response) {
        try {
            var in = employeActifServices.exportEmployesByCategorie(categorie);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employes_actifs.xlsx");
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
