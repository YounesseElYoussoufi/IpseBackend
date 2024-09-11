package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.PaiementDTOR;
import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import com.example.ipsebackend.repositories.PaiementRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EmployeRetraiteRepository employeRetraiteRepository;

    public PaiementDTOR ajouterPaiement(String nrcar, PaiementDTOR paiementDTO) {
        // Recherche de l'employé retraité par NRCAR
        EmployeRetraite employeRetraite = employeRetraiteRepository.findByNRCAR(nrcar);

        // Création du paiement
        Paiement paiement = new Paiement();
        paiement.setDateDette(paiementDTO.getDateDette());
        paiement.setDatePaiement(paiementDTO.getDatePaiement());
        paiement.setMontant(paiementDTO.getMontant());
        paiement.setEmployeRetraite(employeRetraite);

        // Sauvegarde du paiement
        paiement = paiementRepository.save(paiement);




        // Mise à jour du DTO avec l'ID du paiement créé
        paiementDTO.setId(paiement.getId());
        return paiementDTO;
    }


    public static class De {
        public int id;  // l'emplacement dans la ligne (l'index)
        public String annee;

        public De(int id, String annee) {
            this.id = id;
            this.annee = annee;
        }

        @Override
        public String toString() {
            return "De{" +
                    "id=" + id +
                    ", annee='" + annee + '\'' +
                    '}';
        }
    }

    public List<PaiementService.De> extractPaiement(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Récupérer uniquement la première ligne
        List<String> firstRow = StreamSupport.stream(sheet.spliterator(), false)
                .findFirst()
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Retourner une liste vide si aucune ligne n'est trouvée

        // Filtrer et transformer les valeurs de "Dette"
        List<PaiementService.De> paiementes = firstRow.stream()
                .filter(str -> str.startsWith("Paiement")) // Filtrer les valeurs qui commencent par "Dette"
                .map(str -> {
                    String annee = str.substring(8); // Extraire l'année après "Dette"
                    int emplacement = firstRow.indexOf(str); // Trouver l'emplacement (index) de la valeur
                    return new PaiementService.De(emplacement, annee); // Créer un objet `De` avec l'emplacement et l'année
                })
                .collect(Collectors.toList());

        workbook.close(); // Fermer le workbook après utilisation
        return paiementes; // Retourner la liste des dettes
    }

    public List<DetteService.De> extractDettes(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Récupérer uniquement la première ligne
        List<String> firstRow = StreamSupport.stream(sheet.spliterator(), false)
                .findFirst()
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Retourner une liste vide si aucune ligne n'est trouvée

        // Filtrer et transformer les valeurs de "Dette"
        List<DetteService.De> dettes = firstRow.stream()
                .filter(str -> str.startsWith("Dettes")) // Filtrer les valeurs qui commencent par "Dette"
                .map(str -> {
                    String annee = str.substring(6); // Extraire l'année après "Dette"
                    int emplacement = firstRow.indexOf(str); // Trouver l'emplacement (index) de la valeur
                    return new DetteService.De(emplacement, annee); // Créer un objet `De` avec l'emplacement et l'année
                })
                .collect(Collectors.toList());

        workbook.close(); // Fermer le workbook après utilisation
        return dettes; // Retourner la liste des dettes
    }
    // Méthode pour récupérer la valeur des cellules
    public void save(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();

        // Parcourir chaque feuille
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            List<List<String>> rows = StreamSupport.stream(sheet.spliterator(), false)
                    .skip(1) // Ignorer la ligne d'en-tête
                    .map(row -> StreamSupport
                            .stream(row.spliterator(), false)
                            .map(this::getCellStringValue)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            System.out.println("Feuille " + (i + 1) + " lignes :: " + rows);

            List<Paiement> paiements = rows.stream().map(row -> {

                EmployeRetraite empRetraite = employeRetraiteRepository.findByNRCAR(row.get(0));
                Etudiant etudiant = etudiantRepository.findByNomComplet(row.get(3) + " " + row.get(4));

                Paiement paiement = new Paiement();
                try {
                    List<PaiementService.De> des = extractPaiement(file) ;
                    for (PaiementService.De d :des){
                        Paiement   paiement1 = paiementRepository.findByDatePaiementAndEmployeRetraiteIdAndEtudiant_Prenom(d.annee,empRetraite.getId(),etudiant.getPrenom());
                        if(paiement1==null){
                            paiement1 =new Paiement();
                        }
                        //Paiement paiement1= new Paiement();
                        paiement1.setEtudiant(etudiant);
                        paiement1.setEmployeRetraite(empRetraite);
                        paiement1.setDatePaiement(d.annee);
                        String montantpayerS = row.get(d.id);
                        if (montantpayerS != null) {
                            try {
                                Double montantpayerD = Double.parseDouble(montantpayerS);
                                paiement1.setMontant(montantpayerD);

                            } catch (NumberFormatException e) {
                                System.err.println("Valeur de dette invalide : " + montantpayerS);
                            }
                        }
                        etudiantRepository.save(etudiant);
                        paiementRepository.save(paiement1);
                        employeRetraiteRepository.save(empRetraite);
                        //dette.setMontantAPayer(row.get(d.id));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Remplir les attributs de `dette` en fonction de la ligne `row`
                return paiement;
            }).collect(Collectors.toList()); // Ajouter la parenthèse de fermeture manquante
            paiementRepository.saveAll(paiements) ;

        }
    }



    private String getCellStringValue(Cell cell) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    return new BigDecimal(cell.getNumericCellValue()).toPlainString();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
