package com.example.ipsebackend.controller;

import com.example.ipsebackend.service.DetteService;
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
import java.util.Map;

@RestController
@RequestMapping("/dettes")
@AllArgsConstructor
public class DetteController {
    // Endpoint pour uploader un fichier Excel et récupérer les cellules "Dette"
    private final DetteService detteService;

    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
   /* public ResponseEntity<Map<Integer, DetteService.De>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<Integer, DetteService.De> dettesMap = detteService.save(file);
            return ResponseEntity.ok(dettesMap);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    public ResponseEntity<List<DetteService.De>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Appel du service pour traiter le fichier et retourner les cellules "Dette"
            List<DetteService.De> dettes = detteService.extractDettes(file);
            return ResponseEntity.ok(dettes);
        } catch (IOException e) {
            // En cas d'erreur lors de la lecture du fichier, renvoyer un statut HTTP 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/uploadRetenue" ,consumes = {"multipart/form-data"})
    public ResponseEntity<List<List<String>>> uploadExcel1(@RequestParam("file") MultipartFile file)
            throws EncryptedDocumentException, IOException {

        detteService.save(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
