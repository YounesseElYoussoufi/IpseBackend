package com.example.ipsebackend.controller;

import com.example.ipsebackend.service.DetteService;
import com.example.ipsebackend.service.PaiementService;
import lombok.AllArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/paiements")
@AllArgsConstructor
public class PaiementController {
    private final PaiementService paiementService;

    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
   /* public ResponseEntity<Map<Integer, DetteService.De>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<Integer, DetteService.De> dettesMap = detteService.save(file);
            return ResponseEntity.ok(dettesMap);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    public ResponseEntity<List<PaiementService.De>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Appel du service pour traiter le fichier et retourner les cellules "Dette"
            List<PaiementService.De> dettes = paiementService.extractPaiement(file);
            return ResponseEntity.ok(dettes);
        } catch (IOException e) {
            // En cas d'erreur lors de la lecture du fichier, renvoyer un statut HTTP 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping(value = "/uploadRetenue" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel1(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        paiementService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
