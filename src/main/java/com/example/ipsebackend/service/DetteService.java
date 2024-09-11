/*
package com.example.ipsebackend.service;

import com.example.ipsebackend.repositories.DetteRepository;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class DetteService {
    private DetteRepository detteRepository;
    private EmployeRetraiteRepository employeRepository;

    public void save(MultipartFile file) throws IOException {
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


        AtomicInteger idCounter = new AtomicInteger(0); // Compteur pour générer les ids
        List<De> dettes = firstRow.stream()
                .filter(str -> str.startsWith("Dette")) // Filtrer les valeurs qui commencent par "Dette"
                .map(str -> {
                    String annee = str.substring(5); // Extraire l'année après "Dette"
                    return new De(idCounter.getAndIncrement(), annee); // Créer un objet `De` avec un id incrémenté
                })
                .collect(Collectors.toList());

        // Afficher la liste des dettes
        dettes.forEach(System.out::println);

        // Fermer le workbook après utilisation pour éviter les fuites de mémoire
        workbook.close();
    }

    // Méthode pour obtenir la valeur d'une cellule Excel sous forme de String
    private String getCellStringValue(Cell cell) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // Gérer les cellules de date
                    Date date = cell.getDateCellValue();
                    return formatDate(date);
                } else {
                    // Gérer les cellules numériques
                    return formatNumericValue(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Méthode pour formater une valeur numérique
    private String formatNumericValue(double value) {
        // Utiliser BigDecimal pour éviter la notation scientifique
        return new BigDecimal(value).toPlainString();
    }

    // Méthode pour formater une date en chaîne de caractères
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    // Méthode pour parser une date Excel (non utilisée dans ce cas)
    private Date parseExcelDate(String cellValue) throws ParseException {
        try {
            double serialDate = Double.parseDouble(cellValue);
            return DateUtil.getJavaDate(serialDate);
        } catch (NumberFormatException e) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(cellValue);
        }
    }
    public List<De> extractDettes(MultipartFile file) throws IOException {
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
        AtomicInteger idCounter = new AtomicInteger(0); // Compteur pour générer les ids
        List<De> dettes = firstRow.stream()
                .filter(str -> str.startsWith("Dette")) // Filtrer les valeurs qui commencent par "Dette"
                .map(str -> {
                    String annee = str.substring(5); // Extraire l'année après "Dette"
                    return new De(firstRow.get(0), annee); // Créer un objet `De` avec un id incrémenté
                })
                .collect(Collectors.toList());

        workbook.close(); // Fermer le workbook après utilisation
        return dettes; // Retourner la liste des dettes
    }

    // Classe interne pour représenter une dette
    public static class De {
        public String id;
        public String annee;

        public De(String id, String annee) {
            this.id = id;
            this.annee = annee;
        }

        @Override
        public String toString() {
            return "De{id=" + id + ", annee='" + annee + "'}";
        }
    }
}
*/


package com.example.ipsebackend.service;

import com.example.ipsebackend.entities.Dette;
import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.entities.Etudiant;
import com.example.ipsebackend.repositories.DetteRepository;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor

public class DetteService {
    private final DetteRepository detteRepository;
    private final EmployeRetraiteRepository employeRetraiteRepository;
    private final EtudiantRepository etudiantRepository;
    // Définir la classe de comme une classe publique statique
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

   /* public Map<Integer, De> save(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Récupérer la première ligne
        List<String> firstRow = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            row.forEach(cell -> firstRow.add(getCellStringValue(cell)));
        }

        // Créer une Map pour stocker l'emplacement et l'objet 'de'
        Map<Integer, De> dettesMap = new HashMap<>();

        // Filtrer les valeurs qui commencent par "Dette" et les stocker avec leur emplacement
        for (int i = 0; i < firstRow.size(); i++) {
            String value = firstRow.get(i);
            if (value.startsWith("Dette")) {
                String annee = value.substring(5); // Extraire l'année à partir de "Dette"
                dettesMap.put(i, new De(i, annee)); // Stocker dans la map avec l'index comme clé
            }
        }

        // Retourner la map avec les dettes et leur emplacement
        return dettesMap;
    }
*/
   public List<De> extractDettes(MultipartFile file) throws IOException {
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
       List<De> dettes = firstRow.stream()
               .filter(str -> str.startsWith("Dettes")) // Filtrer les valeurs qui commencent par "Dette"
               .map(str -> {
                   String annee = str.substring(6); // Extraire l'année après "Dette"
                   int emplacement = firstRow.indexOf(str); // Trouver l'emplacement (index) de la valeur
                   return new De(emplacement, annee); // Créer un objet `De` avec l'emplacement et l'année
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

            List<Dette> Dettes = rows.stream().map(row -> {

                EmployeRetraite empRetraite = employeRetraiteRepository.findByNRCAR(row.get(0));
                Etudiant etudiant = etudiantRepository.findByNomComplet(row.get(3) + " " + row.get(4));

                Dette dette = new Dette();
                try {
                    List<De> des = extractDettes(file) ;
                    for (De d :des){


                               Dette   dette1 = detteRepository.findByAnneeDateAndEmployeRetraiteIdAndEtudiant_Prenom(d.annee,empRetraite.getId(),etudiant.getPrenom());
                                   if(dette1==null){
                                       dette1=new Dette();
                                   }
                               dette1.setEtudiant(etudiant);
                                  dette1.setEmployeRetraite(empRetraite);
                              dette1.setEtudiant(etudiant);
                                  dette1.setAnneeDate(d.annee);
                                  String montantpayerS = row.get(d.id);
                                  if (montantpayerS != null) {
                                      try {
                                          Double montantpayerD = Double.parseDouble(montantpayerS);
                                          dette1.setMontantAPayer(montantpayerD);

                                      } catch (NumberFormatException e) {
                                          System.err.println("Valeur de dette invalide : " + montantpayerS);
                                      }
                                  }


                        detteRepository.save(dette1);
                        employeRetraiteRepository.save(empRetraite);
                        if(etudiant!=null){
                            etudiantRepository.save(etudiant);
                        }

                        //dette.setMontantAPayer(row.get(d.id));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Remplir les attributs de `dette` en fonction de la ligne `row`
                return dette;
            }).collect(Collectors.toList()); // Ajouter la parenthèse de fermeture manquante
        detteRepository.saveAll(Dettes) ;

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
