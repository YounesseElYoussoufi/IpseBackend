package com.example.ipsebackend.service;
import com.example.ipsebackend.dto.RecuPaiementDTO;
import com.example.ipsebackend.dto.RecuPaiementResponseDTO;
import com.example.ipsebackend.entities.Employe;
import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.entities.Etudiant;
import com.example.ipsebackend.entities.RecuPaiement;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import com.example.ipsebackend.repositories.RecuPaiementRepository;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RecuPaiementService {

    private final RecuPaiementRepository recuPaiementRepository;
    private final EmployeRetraiteRepository employeRetraiteRepository;
    private final EtudiantRepository etudiantRepository;

    public RecuPaiementService(RecuPaiementRepository recuPaiementRepository, EmployeRetraiteRepository employeRetraiteRepository, EtudiantRepository etudiantRepository) {
        this.recuPaiementRepository = recuPaiementRepository;
        this.employeRetraiteRepository = employeRetraiteRepository;
        this.etudiantRepository = etudiantRepository;
    }

    public RecuPaiementResponseDTO createRecuPaiement(RecuPaiementDTO recuPaiementDTO) {
        EmployeRetraite employeRetraite = employeRetraiteRepository.findByNRCAR(recuPaiementDTO.getNrcar());
        if (employeRetraite == null) {
            throw new RuntimeException("Employé retraité non trouvé");
        }

        Etudiant etudiant = etudiantRepository.findById(recuPaiementDTO.getEtudiantId())
                .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));

        RecuPaiement recuPaiement = new RecuPaiement();
        recuPaiement.setDatePaiement(LocalDate.now());
        recuPaiement.setNomPrenomParent(employeRetraite.getNomPrénomAgent());
        recuPaiement.setNomPrenomEnfant(etudiant.getNomComplet());
        recuPaiement.setEtablissement(recuPaiementDTO.getEtablissement());
        recuPaiement.setClasse(recuPaiementDTO.getClasse());
        recuPaiement.setMontant(recuPaiementDTO.getMontant());
        recuPaiement.setMois(recuPaiementDTO.getMois());
        recuPaiement.setEmployeRetraite(employeRetraite);

        recuPaiement = recuPaiementRepository.save(recuPaiement);

        return new RecuPaiementResponseDTO(
                recuPaiement.getId(),
                recuPaiement.getDatePaiement(),
                recuPaiement.getNomPrenomParent(),
                recuPaiement.getNomPrenomEnfant(),
                recuPaiement.getEtablissement(),
                recuPaiement.getClasse(),
                recuPaiement.getMontant(),
                recuPaiement.getMois()
        );
    }

   /* public List<RecuPaiementResponseDTO> getAllRecuPaiements() {
        List<RecuPaiement> recuPaiements = recuPaiementRepository.findAll();
        return recuPaiements.stream()
                .map(recuPaiement -> new RecuPaiementResponseDTO(
                        recuPaiement.getId(),
                        recuPaiement.getDatePaiement(),
                        recuPaiement.getNomPrenomParent(),
                        recuPaiement.getNomPrenomEnfant(),
                        recuPaiement.getEtablissement(),
                        recuPaiement.getClasse(),
                        recuPaiement.getMontant(),
                        recuPaiement.getMois()
                ))
                .collect(Collectors.toList());
    }
*/



    public Page<RecuPaiementResponseDTO> getAllRecuPaiements(Pageable pageable) {
        Page<RecuPaiement> recuPaiementsPage = recuPaiementRepository.findAll(pageable);
        return recuPaiementsPage.map(recuPaiement -> new RecuPaiementResponseDTO(
                recuPaiement.getId(),
                recuPaiement.getDatePaiement(),
                recuPaiement.getNomPrenomParent(),
                recuPaiement.getNomPrenomEnfant(),
                recuPaiement.getEtablissement(),
                recuPaiement.getClasse(),
                recuPaiement.getMontant(),
                recuPaiement.getMois()
        ));
    }
    public RecuPaiementResponseDTO getRecuPaiementById(Long id) {
        return recuPaiementRepository.findById(id)
                .map(recuPaiement -> new RecuPaiementResponseDTO(
                        recuPaiement.getId(),
                        recuPaiement.getDatePaiement(),
                        recuPaiement.getNomPrenomParent(),
                        recuPaiement.getNomPrenomEnfant(),
                        recuPaiement.getEtablissement(),
                        recuPaiement.getClasse(),
                        recuPaiement.getMontant(),
                        recuPaiement.getMois()
                ))
                .orElse(null);
    }

    public ResponseEntity<ByteArrayResource> generatePdf(Long id) {
        // Récupérer les données du reçu
        RecuPaiementResponseDTO recuPaiement = getRecuPaiementById(id);
        if (recuPaiement == null) {
            return ResponseEntity.notFound().build();
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            // Ajouter des paragraphes au document
            document.add(new Paragraph("Reçu de Paiement").setTextAlignment(TextAlignment.CENTER).setFontSize(20));
            document.add(new Paragraph("Nom du parent : " + recuPaiement.getNomPrenomParent()));
            document.add(new Paragraph("Nom de l'enfant : " + recuPaiement.getNomPrenomEnfant()));
            document.add(new Paragraph("Etablissement : " + recuPaiement.getEtablissement()));
            document.add(new Paragraph("Classe : " + recuPaiement.getClasse()));
            document.add(new Paragraph("Montant : " + recuPaiement.getMontant()));
            document.add(new Paragraph("Mois : " + recuPaiement.getMois()));

            document.close();

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recu_paiement.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    public RecuPaiement creerRecuPaiement(String cne, Double montant, String niveau) {
        // Rechercher l'étudiant par son CNE
        Etudiant etudiant = etudiantRepository.findByCne(cne)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec le CNE : " + cne));
        Employe emp = etudiant.getEmploye();
        // Créer un nouveau reçu de paiement
        RecuPaiement recuPaiement = new RecuPaiement();
        recuPaiement.setDatePaiement(LocalDate.now());  // Date actuelle
        recuPaiement.setNomPrenomEnfant(etudiant.getNomComplet());
       recuPaiement.setNomPrenomParent(etudiant.getEmploye().getNomPrénomAgent());
        recuPaiement.setMontant(montant);
        recuPaiement.setClasse(niveau);
        recuPaiement.setEtablissement(etudiant.getNiveauScolaire().get(0).getEtablissement());  // Exemple d'obtention de l'établissement
      //  recuPaiement.setEmployeRetraite(etudiant.getEmploye());
        // Associer l'étudiant au reçu
        recuPaiement.setEtudiant(etudiant);
     //  recuPaiement.setEmployeRetraite();
        // Sauvegarder le reçu dans la base de données
        return recuPaiementRepository.save(recuPaiement);
    }

}
