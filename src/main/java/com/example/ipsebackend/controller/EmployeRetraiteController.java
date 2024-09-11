package com.example.ipsebackend.controller;

import com.example.ipsebackend.dto.*;
import com.example.ipsebackend.enums.CategorieRet;
import com.example.ipsebackend.service.DetteService;
import com.example.ipsebackend.service.EmpoyeRetraiteService;
import com.example.ipsebackend.service.NiveauScolaireService;
import com.example.ipsebackend.service.PaiementService;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class EmployeRetraiteController {

    private final EmpoyeRetraiteService employeRetraiteService;

    private final PaiementService paiementService;
  private final DetteService detteService;
  private final NiveauScolaireService niveauScolaireService;
    @Autowired
    public EmployeRetraiteController(EmpoyeRetraiteService employeRetraiteService, PaiementService paiementService, DetteService detteService, NiveauScolaireService niveauScolaireService) {
        this.employeRetraiteService = employeRetraiteService;
        this.paiementService = paiementService;
        this.detteService = detteService;
        this.niveauScolaireService = niveauScolaireService;
    }



    @PostMapping("/ajouter/{nrcar}")
    public ResponseEntity<PaiementDTOR> ajouterPaiement(
            @PathVariable("nrcar") String nrcar,
            @RequestBody PaiementDTOR paiementDTO) {

        PaiementDTOR nouveauPaiement = paiementService.ajouterPaiement(nrcar, paiementDTO);
        return ResponseEntity.ok(nouveauPaiement);
    }



    @PostMapping(value = "/upload" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        employeRetraiteService.save(file);
        detteService.save(file);
        paiementService.save(file);
        niveauScolaireService.save(file);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/employe-retraite")
    public ResponseEntity<?> listEmployeRetraites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        System.out.println("Search Term: " + search);
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeRetraiteDTO> EmpPage = employeRetraiteService.listEmployeRetraites(pageable,search);
        return ResponseEntity.ok(EmpPage);
    }

    @GetMapping("/employe-retraite/dettes")
    public Page<DetteDTO> getDettesByNrcar(
            @RequestParam String nrcar,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return employeRetraiteService.getDettesByNrcar(nrcar, page, size);
    }



    /*@GetMapping("/employe-retraite/dettes")
    public List<DetteDTO> getDettesByNrcar(@RequestParam String nrcar) {
        return employeRetraiteService.getDettesByNrcar(nrcar);
    }*/

   /* @GetMapping("/employe-retraite/paiements")
    public List<PaiementDTO> getPaiementsByNrcar(@RequestParam String nrcar) {
        return employeRetraiteService.getPaiementsByNrcar(nrcar);
    }*/
   @GetMapping("/employe-retraite/paiements")
   public Page<PaiementDTO> getPaiementsByNrcar(
           @RequestParam String nrcar,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) {
       return employeRetraiteService.getPaiementsByNrcar(nrcar, page, size);
   }


    @DeleteMapping("/{nrcar}")
    public ResponseEntity<Void> deleteEmployeRetraiteByNRCAR(@PathVariable String nrcar) {
        employeRetraiteService.deleteEmployeRetraiteByNRCAR(nrcar);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteEmployesRetraites(@RequestBody List<String> NRCARs) {
        employeRetraiteService.deleteEmployesRetraitesByNRCARs(NRCARs);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportEmployeRetraitesByCategorie(
            @RequestParam("categorie") CategorieRet categorie) throws IOException {

        ByteArrayInputStream data = employeRetraiteService.exportEmployeRetraitesByCategorie(categorie);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employes_retraites.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(data));
    }
}
