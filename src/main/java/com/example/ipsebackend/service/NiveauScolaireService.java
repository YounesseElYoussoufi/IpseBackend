package com.example.ipsebackend.service;

import com.example.ipsebackend.entities.Dette;
import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.entities.Etudiant;
import com.example.ipsebackend.entities.NiveauScolaire;
import com.example.ipsebackend.repositories.AnneeScolaireepository;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import com.example.ipsebackend.repositories.NiveauScolaireRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class NiveauScolaireService {
   @Autowired
    private AnneeScolaireepository anneeScolaireepository;
    @Autowired
    private EmployeRetraiteRepository employeRetraiteRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private NiveauScolaireRepository niveauScolaireRepository;

    public static class De {
        public int id;  // l'emplacement dans la ligne (l'index)
        public String annee;
        public int eta ;
        public int niveau;
        public De(int id, String annee , int eta, int niveau) {
            this.id = id;
            this.annee = annee;
            this.eta = eta;
            this.niveau = niveau;

        }


        @Override
        public String toString() {
            return "De{" +
                    "id=" + id +
                    ", annee='" + annee + '\'' +
                    '}';
        }
    }

    public List<NiveauScolaireService.De> extractAnnee(MultipartFile file) throws IOException {
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
        List<NiveauScolaireService.De> Annesscolaire = firstRow.stream()
                .filter(str -> str.startsWith("Niveau")) // Filtrer les valeurs qui commencent par "Dette"

                .map(str -> {
                    String annee = str.substring(6); // Extraire l'année après "Dette"
                    int emplacement = firstRow.indexOf(str);
                    int emplacementEtablissement = firstRow.indexOf("Etablissement");
                    int emplacementNiveau = firstRow.indexOf("Niveau");

                    // Trouver l'emplacement (index) de la valeur
                    return new NiveauScolaireService.De(emplacement, annee,emplacementEtablissement ,emplacementNiveau); // Créer un objet `De` avec l'emplacement et l'année
                })
                .collect(Collectors.toList());

        workbook.close(); // Fermer le workbook après utilisation
        return Annesscolaire; // Retourner la liste des dettes
    }
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

            List<NiveauScolaire> niveauScolaires = rows.stream().map(row -> {

                EmployeRetraite empRetraite = employeRetraiteRepository.findByNRCAR(row.get(0));
                Etudiant etudiant = etudiantRepository.findByNomComplet(row.get(3) + " " + row.get(4));

                NiveauScolaire niveauScolaire = new NiveauScolaire();
                try {
                    List<NiveauScolaireService.De> des = extractAnnee(file) ;
                    for (NiveauScolaireService.De d :des){


                        NiveauScolaire niveauScolaire1 = niveauScolaireRepository.findByNiveauAndEtudiant_Prenom(d.annee, etudiant.getPrenom()); //= AnneeScolaireepository
                        if(niveauScolaire1==null){
                            niveauScolaire1=new NiveauScolaire();
                        }
                        niveauScolaire1.setEtablissement(row.get(d.eta));
                        niveauScolaire1.setEtudiant(etudiant);
                        //niveauScolaire1.setEmployeRetraite(empRetraite);
                        niveauScolaire1.setEtudiant(etudiant);
                       niveauScolaire1.setAnneScolaire(d.annee);
                       niveauScolaire1.setNiveau(row.get(d.id));

                        //detteRepository.save(dette1);
                        niveauScolaireRepository.save(niveauScolaire1);
                        etudiantRepository.save(etudiant);
                        employeRetraiteRepository.save(empRetraite);

                        //dette.setMontantAPayer(row.get(d.id));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Remplir les attributs de `dette` en fonction de la ligne `row`
                return niveauScolaire;
            }).collect(Collectors.toList()); // Ajouter la parenthèse de fermeture manquante
            //detteRepository.saveAll(Dettes) ;

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
