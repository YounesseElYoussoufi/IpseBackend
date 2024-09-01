package com.example.ipsebackend.service;

import com.example.ipsebackend.entities.Dette;
import com.example.ipsebackend.entities.EmployeRetraite;
import com.example.ipsebackend.enums.CategorieRet;
import com.example.ipsebackend.repositories.DetteRepository;
import com.example.ipsebackend.repositories.EmployeRetraiteRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmpoyeRetraiteService {

    private final EmployeRetraiteRepository employeRetraiterepository;
    private final DetteRepository detterepository;

    public EmpoyeRetraiteService(EmployeRetraiteRepository employeRetraiterepository, DetteRepository detterepository) {
        this.employeRetraiterepository = employeRetraiterepository;
        this.detterepository = detterepository;
    }

    public void save(MultipartFile file) throws IOException {
        // Créer un Workbook à partir du fichier
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();

        // Parcourir chaque feuille
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            // Lire les lignes et les convertir en liste de listes
            List<List<String>> rows = StreamSupport.stream(sheet.spliterator(), false)
                    .skip(1) // Ignorer la ligne d'en-tête
                    .map(row -> StreamSupport
                            .stream(row.spliterator(), false)
                            .map(this::getCellStringValue)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            System.out.println("Feuille " + (i + 1) + " lignes :: " + rows);

            // Convertir les lignes en objets EmployeRetraite
            List<EmployeRetraite> employeRetraites = rows.stream().map(row -> {
                EmployeRetraite employeRetraite = employeRetraiterepository.findByNRCAR(row.get(0));
                if (employeRetraite == null) {
                    employeRetraite = new EmployeRetraite();
                }

                // Définir les propriétés de EmployeRetraite
                employeRetraite.setNRCAR(row.get(0));
                employeRetraite.setMatricule(row.get(0));
                employeRetraite.setNomPrénomAgent(row.get(3));
              //  employeRetraite.setType(CategorieRet.);
                employeRetraite.setRemarque(row.get(28));
                employeRetraite.setContact(row.get(29));
/*
                String totalPaiementString = row.get(24);
                if (totalPaiementString != null) {
                    try {
                        Double totalPaiement = Double.parseDouble(totalPaiementString);
                        employeRetraite.setTotalPaiement(totalPaiement);
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de totalPaiement invalide : " + totalPaiementString);
                    }
                }*/

                /*String totalDetteString = row.get(23);
                if (totalDetteString != null) {
                    try {
                        Double totalDette = Double.parseDouble(totalDetteString);
                        employeRetraite.setTotalDette(totalDette);
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de totalDette invalide : " + totalDetteString);
                    }
                }
*/
               /* String reliquatString = row.get(25);
                if (reliquatString != null) {
                    try {
                        Double reliquat = Double.parseDouble(reliquatString);
                        employeRetraite.setReliquat(reliquat);
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de reliquat invalide : " + reliquatString);
                    }
                }*/

                String dateStr = row.get(27);
                if (dateStr != null) {
                    try {
                        Date dateRetraite = parseExcelDate(dateStr);
                        employeRetraite.setDateretraite(dateRetraite);
                    } catch (Exception e) {
                        System.err.println("Valeur de date invalide : " + dateStr);
                    }
                }

                String catString = row.get(1);
                if (catString != null) {
                    try {
                        CategorieRet categorieRet = CategorieRet.valueOf(catString.toUpperCase());
                        employeRetraite.setCategorieRet(categorieRet);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valeur de CategorieRet invalide : " + catString);
                    }
                }
                employeRetraiterepository.save(employeRetraite);
                employeRetraite.setStatut(row.get(31));

                EmployeRetraite e1 = employeRetraiterepository.findByNRCAR(row.get(0));
                int a = 0;

// Process totalDette
                String totalPaiementString = row.get(23);  // Correct column for totalDette
                if (totalPaiementString != null) {
                    try {
                        Double totalPaiementDouble = Double.parseDouble(totalPaiementString);
                        // Ensure e1.getTotalPaiement() is not null
                        Double totalPaiement = e1.getTotalPaiement() != null ? e1.getTotalPaiement() : 0.0;
                        employeRetraite.setTotalDette(a + e1.getTotalPaiement()+ row.get(23));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid totaldette value: " + totalPaiementString);
                   }
                }

// Process reliquat
               /*String reliquatString = row.get(25);
                if (reliquatString != null) {
                    try {
                        Double reliquatDouble = Double.parseDouble(reliquatString);
                        // Ensure e1.getReliquat() is not null
                       Double reliquat = e1.getReliquat() != null ? e1.getReliquat() : 0.0;
                        employeRetraite.setReliquat(a + e1.getReliquat() + row.get(25));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid reliquat value: " + reliquatString);
                    }
                }*/

// Save the entity
                employeRetraiterepository.save(employeRetraite);

                // Sauvegarder l'entité EmployeRetraite avant d'ajouter les Dette
                employeRetraiterepository.save(employeRetraite);

                // Créer les objets Dette associés et les sauvegarder
                List<Dette> dettes = new ArrayList<>();

                String dette18String = row.get(13);
                if (dette18String != null) {
                    try {
                        Double dette18 = Double.parseDouble(dette18String);
                        Dette d18 = new Dette();
                        d18.setAnneeDate("2018/2019");
                        d18.setMontantAPayer(dette18);
                        d18.setEmployeRetraite(employeRetraite);  // Associer à EmployeRetraite
                        dettes.add(d18);
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de dette18 invalide : " + dette18String);
                    }
                }

                // Ajoutez des objets Dette supplémentaires ici

                detterepository.saveAll(dettes);  // Sauvegarder tous les Dette

                return employeRetraite;
            }).collect(Collectors.toList());

            // Sauvegarder tous les EmployeRetraite
            employeRetraiterepository.saveAll(employeRetraites);
        }
    }

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
                return null;
        }
    }

    private String formatNumericValue(double value) {
        // Utiliser BigDecimal pour formater le nombre sans notation scientifique
        return new BigDecimal(value).toPlainString();
    }

    private Date parseExcelDate(String cellValue) throws ParseException {
        try {
            double serialDate = Double.parseDouble(cellValue);
            return DateUtil.getJavaDate(serialDate);
        } catch (NumberFormatException e) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(cellValue);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
