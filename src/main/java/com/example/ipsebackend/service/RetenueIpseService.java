package com.example.ipsebackend.service;/*
package com.example.ipsebackend.service;

import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.enums.CategoriieAct;
import com.example.ipsebackend.repositories.EmployeActifRepository;
import com.example.ipsebackend.repositories.RetenueRepoitory;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class RetenueIpseService {

    private final EmployeActifRepository employeActifRepository;
    private final RetenueRepoitory retenueRepoitory;

    public RetenueIpseService( EmployeActifRepository employeActifRepository, RetenueRepoitory retenueRepoitory) {
        this.employeActifRepository = employeActifRepository;

        this.retenueRepoitory = retenueRepoitory;
    }

    public void save(MultipartFile file) throws IOException {
        // Créer un Workbook à partir du fichier
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

            List<Retenue> excelDataList = rows.stream().map(row -> {

                List<EmployeActif> employeActif = employeActifRepository.findByMatricul(row.get(0));

                Retenue retenue = new Retenue();

         if(employeActif != null) {
             List<Retenue>  retenues = new ArrayList<>() ;

                 try {



                     Date dateDebut = parseExcelDate(row.get(6));
                     Date dateFin = parseExcelDate(row.get(7));
                     retenue.setLib(row.get(1));
                     retenue.setMontant(montantDouble);
                     retenue.setDateDebut(dateDebut);

                     retenue.setEmployeActif(employeActif.get(0));
                     retenues.add(retenue);
                 } catch (NumberFormatException e) {
                     System.err.println("Invalid dette value: " );
                 } catch (ParseException e) {
                     throw new RuntimeException(e);
                 }
             employeActifRepository.save(employeActif.get(0));
             }




                return retenue;

            }).collect(Collectors.toList());

            // Sauvegarder la liste des employés après avoir ajouté les étudiants
          retenueRepoitory.saveAll(excelDataList) ;

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

*//*



import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RetenueIpseService {

    @Autowired
    private EmployeActifRepository employeActifRepository;

    @Autowired
    private RetenueRepository retenueRepository;

    public void save(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            List<List<String>> rows = StreamSupport.stream(sheet.spliterator(), false)
                    .skip(1)
                    .map(row -> StreamSupport
                            .stream(row.spliterator(), false)
                            .map(this::getCellStringValue)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            System.out.println("Feuille " + (i + 1) + " lignes :: " + rows);

            List<Retenue> retenues = rows.stream().map(row -> {
                List<EmployeActif> employesActifs = employeActifRepository.findByMatricul(row.get(0));

                Retenue retenue = new Retenue();

                if (employesActifs != null && !employesActifs.isEmpty()) {
                    EmployeActif employeActif = employesActifs.get(0);  // Choisissez le premier employé trouvé

                    try {
                        String montantString = row.get(5);

                        // Vérification si le montant n'est pas null ou vide
                        if (montantString != null && !montantString.trim().isEmpty()) {
                            Double montantDouble = Double.parseDouble(montantString.trim());
                            retenue.setMontant(montantDouble);
                        } else {
                            System.err.println("Montant vide ou null pour la ligne : " + row);
                        }

                        Date dateDebut = parseExcelDate(row.get(6));
                        Date dateFin = parseExcelDate(row.get(7));

                        retenue.setLib(row.get(1));
                        retenue.setDateDebut(dateDebut);
                        retenue.setEmployeActif(employeActif);

                        employeActif.getRetenues().add(retenue); // Assurez-vous que les retenues sont bien associées
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + montantString);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    employeActifRepository.save(employeActif);
                }

                return retenue;
            }).collect(Collectors.toList());

            retenueRepository.saveAll(retenues);
        }
    }

    private String getCellStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Date parseExcelDate(String dateStr) throws ParseException {
        SimpleDateFormat excelDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return excelDateFormat.parse(dateStr);
    }
}

*/
/*

import com.example.ipsebackend.entities.EmployeActif;
import com.example.ipsebackend.entities.Retenue;
import com.example.ipsebackend.repositories.EmployeActifRepository;
import com.example.ipsebackend.repositories.RetenueRepoitory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RetenueIpseService {

    @Autowired
    private EmployeActifRepository employeActifRepository;

    @Autowired
    private RetenueRepoitory retenueRepository;

    public void save(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        int numberOfSheets = workbook.getNumberOfSheets();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);

            List<List<String>> rows = StreamSupport.stream(sheet.spliterator(), false)
                    .skip(1)
                    .map(row -> StreamSupport
                            .stream(row.spliterator(), false)
                            .map(this::getCellStringValue)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            System.out.println("Feuille " + (i + 1) + " lignes :: " + rows);

            List<Retenue> retenues = rows.stream().map(row -> {
                EmployeActif employesActifs = employeActifRepository.findByMatricul(row.get(0));

                Retenue retenue = new Retenue();
                List<Retenue> retenue1 =new ArrayList<>() ;
                if(employesActifs==null){
                    employesActifs=new EmployeActif();
                    employesActifs.setMatricul("1444");
                }

                    System.err.println("EmployeActif not found for matricule: " + row.get(0));
                      // Choisissez le premier employé trouvé

                    try {
                        String montantString = row.get(5);

                        // Vérification si le montant n'est pas null ou vide

                            Double montantDouble = Double.parseDouble(montantString.trim());
                            retenue.setMontant(montantDouble);


                        Date dateDebut = parseExcelDate(row.get(6));
                        Date dateFin = parseExcelDate(row.get(7));

                        retenue.setLib(row.get(1));
                        retenue.setDateDebut(dateDebut);
                        retenue.setEmployeActif(employesActifs);
                        List<Retenue> lr  =new ArrayList<>() ;
                        lr.add(retenue);
                        employesActifs.setRetenues(lr);
                        retenue1.add(retenue) ;
                        // Assurez-vous que les retenues sont bien associées
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " );
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    retenueRepository.saveAll(retenue1);
                    employeActifRepository.save(employesActifs);


                return retenue;
            }).collect(Collectors.toList());

            retenueRepository.saveAll(retenues);
        }
    }

    private String getCellStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Date parseExcelDate(String dateStr) throws ParseException {
        SimpleDateFormat excelDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return excelDateFormat.parse(dateStr);
    }
}





*/





import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.enums.CategorieRet;
import com.example.ipsebackend.enums.CategoriieAct;
import com.example.ipsebackend.repositories.*;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
public class RetenueIpseService {

    private final EmployeActifRepository employeActifRepository;
    private final DetteRepository detterepository;
    private final PaiementRepository paiementrepository;
private final RetenueRepoitory retenuerepository;
    private final EtudiantRepository etudiantrepository;
    private final AnneeScolaireepository anneeScolaireepository ;
    public RetenueIpseService(EmployeActifRepository employeActifRepository, DetteRepository detterepository, PaiementRepository paiementrepository, RetenueRepoitory retenuerepository, EtudiantRepository etudiantrepository, AnneeScolaireepository anneeScolaireepository) {
        this.employeActifRepository = employeActifRepository;
        this.detterepository = detterepository;
        this.paiementrepository = paiementrepository;
        this.retenuerepository = retenuerepository;
        this.etudiantrepository = etudiantrepository;
        this.anneeScolaireepository = anneeScolaireepository;
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

            List<Retenue> excelDataList = rows.stream().map(row -> {

             EmployeActif employeActif =employeActifRepository.findByMatriculeEmp(row.get(0));

               if(employeActif==null){
                   EmployeActif employeActif1 = new EmployeActif();
                  /* employeActif1.setMatriculeEmp(row.get(0));
                   employeActif1.setMatricule(row.get(0));*/
               }



                 Retenue retenue = new Retenue();
                 List<Retenue> retenues =new ArrayList<>();

                 retenue.setLib(row.get(1));



                String montantString = row.size() > 5 ? row.get(5) : null;
                if (montantString != null) {
                    try {
                        Double montanDouble = Double.parseDouble(montantString);
                        retenue.setMontant(montanDouble);
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de reliquat invalide : " + montantString);
                    }
                }


                String dateDebS = row.size() > 6 ? row.get(6) : null;
                if (dateDebS != null) {
                    try {
                        Date dateDebD = parseExcelDate(dateDebS);
                       retenue.setDateDebut(dateDebD);
                    } catch (Exception e) {
                        System.err.println("Valeur de date invalide : " + dateDebS);
                    }
                }



                String dateFinS = row.size() > 6 ? row.get(6) : null;
                if (dateFinS != null) {
                    try {
                        Date dateFinD = parseExcelDate(dateFinS);
                        retenue.setDateFin(dateFinD);
                    } catch (Exception e) {
                        System.err.println("Valeur de date invalide : " + dateFinS);
                    }
                }
                retenue.setEmployeActif(employeActif);
                retenues.add(retenue);

                 Retenue savedRetenue = retenuerepository.save(retenue);
                retenuerepository.saveAll(retenues);
                System.out.println("Retenue : " + row.size());
                return savedRetenue;
            }).collect(Collectors.toList());

            // Sauvegarder la liste des employés après avoir ajouté les étudiants
            retenuerepository.saveAll(excelDataList);
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
