package com.example.ipsebackend.controller;


import com.example.ipsebackend.dto.EtudiantDTORec;
import com.example.ipsebackend.dto.RecuPaiementDTO;
import com.example.ipsebackend.dto.RecuPaiementResponseDTO;
import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.entities.RecuPaiement;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import com.example.ipsebackend.service.RecuPaiementService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recupaiement")
public class RecuPaiementController {

    private final RecuPaiementService recuPaiementService;
    private final EmployeRetraiteRepository employeRetraiteService;

    public RecuPaiementController(RecuPaiementService recuPaiementService, EmployeRetraiteRepository employeRetraiteService) {
        this.recuPaiementService = recuPaiementService;
        this.employeRetraiteService = employeRetraiteService;
    }

    @GetMapping
    public ResponseEntity<Page<RecuPaiementResponseDTO>> getAllRecuPaiements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecuPaiementResponseDTO> recuPaiements = recuPaiementService.getAllRecuPaiements(pageable);
        return ResponseEntity.ok(recuPaiements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuPaiementResponseDTO> getRecuPaiementById(@PathVariable Long id) {
        RecuPaiementResponseDTO recuPaiement = recuPaiementService.getRecuPaiementById(id);
        if (recuPaiement != null) {
            return ResponseEntity.ok(recuPaiement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/employe-retraite/{nrcar}/etudiants")
    public ResponseEntity<List<EtudiantDTORec>> getEtudiantsByEmployeRetraite(@PathVariable String nrcar) {
        EmployeRetraite employeRetraite = employeRetraiteService.findByNRCAR(nrcar);
        if (employeRetraite != null) {
            List<EtudiantDTORec> etudiants = employeRetraite.getEtudiants()
                    .stream()
                    .map(etudiant -> new EtudiantDTORec(
                            etudiant.getId(),
                            etudiant.getNomComplet()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(etudiants);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RecuPaiementResponseDTO> createRecuPaiement(@RequestBody RecuPaiementDTO recuPaiementDTO) {
        RecuPaiementResponseDTO responseDTO = recuPaiementService.createRecuPaiement(recuPaiementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/download-pdf/{id}")
    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable Long id) {
        return recuPaiementService.generatePdf(id);
    }
    @GetMapping("/{id}/download-png")
    public ResponseEntity<ByteArrayResource> downloadRecuPaiementAsPng(@PathVariable Long id) {
        RecuPaiementResponseDTO recuPaiement = recuPaiementService.getRecuPaiementById(id);
        if (recuPaiement == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Créer une image en mémoire
            BufferedImage image = new BufferedImage(400, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Définir les propriétés de dessin
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 400, 600);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));

            // Ajouter les informations du reçu
            g2d.drawString("Reçu de Paiement", 100, 50);
            g2d.drawString("Nom du Parent: " + recuPaiement.getNomPrenomParent(), 20, 100);
            g2d.drawString("Nom de l'Enfant: " + recuPaiement.getNomPrenomEnfant(), 20, 150);
            g2d.drawString("Etablissement: " + recuPaiement.getEtablissement(), 20, 200);
            g2d.drawString("Classe: " + recuPaiement.getClasse(), 20, 250);
            g2d.drawString("Montant: " + recuPaiement.getMontant(), 20, 300);
            g2d.drawString("Mois: " + recuPaiement.getMois(), 20, 350);
            g2d.drawString("Date de Paiement: " + recuPaiement.getDatePaiement(), 20, 400);

            g2d.dispose();

            // Convertir l'image en un flux de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Préparer la réponse
            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recu_paiement_" + id + ".png")
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(imageBytes.length)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/creer")
    public ResponseEntity<RecuPaiement> creerRecuPaiement(
            @RequestParam String cne,
            @RequestParam Double montant,
            @RequestParam String niveau) {

        // Appeler le service pour créer un reçu de paiement
        RecuPaiement recuPaiement = recuPaiementService.creerRecuPaiement(cne, montant, niveau);

        // Retourner le reçu créé
        return ResponseEntity.ok(recuPaiement);
    }
}