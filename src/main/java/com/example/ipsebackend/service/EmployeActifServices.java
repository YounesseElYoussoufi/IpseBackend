package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.EmployeActifReDTO;
import com.example.ipsebackend.dto.RetenueDto;
import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.enums.CategorieRet;
import com.example.ipsebackend.enums.CategoriieAct;
import com.example.ipsebackend.repositories.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
public class EmployeActifServices {

    private final EmployeActifRepository employeActifRepository;
    private final DetteRepository detterepository;
    private final PaiementRepository paiementrepository;

    private final EtudiantRepository etudiantrepository;
    private final AnneeScolaireepository anneeScolaireepository;

    public EmployeActifServices(EmployeActifRepository employeActifRepository, DetteRepository detterepository, PaiementRepository paiementrepository, EtudiantRepository etudiantrepository, AnneeScolaireepository anneeScolaireepository) {
        this.employeActifRepository = employeActifRepository;
        this.detterepository = detterepository;
        this.paiementrepository = paiementrepository;
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

            List<EmployeActif> excelDataList = rows.stream().map(row -> {


                EmployeActif employeActif = employeActifRepository.findByMatriculeEmp(row.get(0));
                if (employeActif == null ) {
                    employeActif = new EmployeActif();
                 //   employeActif.setMatricule(row.get(0));
                  //
                    //  employeActif.setMatriculeEmp(row.get(0));
                }

                employeActif.setMatricule(row.get(0));

                employeActif.setMatriculeEmp(row.get(0));
                employeActif.setMatricule(row.get(0));

                employeActif.setNomPrénomAgent(row.get(1));

                String typeString = row.get(2);
                if (typeString != null) {
                    try {
                        CategoriieAct typeEnum = CategoriieAct.valueOf(typeString.toUpperCase());
                        employeActif.setCategorieAct(typeEnum);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Type enum invalide: " + typeString);
                    }
                } else {
                    System.err.println("Type enum null pour la ligne: " + row);
                }


                // Sauvegarder l'objet employe avant de l'utiliser dans Etudiant
                EmployeActif savedEmploye = employeActifRepository.save(employeActif);

                Etudiant e1 = new Etudiant();
                e1.setNomComplet(row.get(3));
                e1.setEmploye(savedEmploye); // Associer l'étudiant à l'employé sauvegardé
                e1.setCne(row.get(6));

                AnneScolaire a = new AnneScolaire();


                List<Etudiant> etudiants = new ArrayList<>();
                String etudiantString = row.get(0);
                if (etudiantString != null) {
                    try {
                      /*  Etudiant etudiant = new Etudiant();
                        etudiant.setNomComplet(row.get(3)); // Assurez-vous de bien concaténer le nom et prénom
                        etudiant.setCne(row.get(6));*/
                        List<AnneScolaire> la = new ArrayList<>();

                        AnneScolaire a3 = new AnneScolaire();


                        a3.setNiveau(row.get(4));
                        a3.setAnneScolaire("2023/2024");
                        a3.setEtablissement(row.get(5));
                        //  a3.setGroupe(row.get(5));

                        anneeScolaireepository.save(a3);

                        la.add(a3);

                        // Associer les années scolaires à l'étudiant
                        e1.setAnneScolaires(la);


                        a3.getEtudiants().add(e1);

                        // Sauvegarder l'étudiant
                        etudiants.add(e1);
                        e1.setEmploye(employeActif);
                        List<Etudiant> le = new ArrayList<>();
                        le.add(e1);
                        employeActif.setEtudiants(le);
                        employeActif.setNbretudiant(employeActif.getEtudiants().size());
                        //employeActif.setNbretudiant(le.size());
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid etudiant value: " + etudiantString);
                    }
                }
// Sauvegarde des étudiants dans la base de données
                if (!etudiants.isEmpty()) {
                    etudiantrepository.saveAll(etudiants);
                }

// Sauvegarde des étudiants dans la base de données
                if (!etudiants.isEmpty()) {
                    etudiantrepository.saveAll(etudiants);
                }

              //  savedEmploye.setNbretudiant(savedEmploye.getEtudiants().size());


              /*  String ContactStr = row.get(7);
                if (ContactStr != null) {
                    try {
                        Double ContactDoub = Double.parseDouble(ContactStr);
                        employeActif.setContact(ContactDoub);
                    } catch (Exception e) {
                        System.err.println("Invalid contact value: " + ContactStr);
                    }
                }*/

                // Sauvegarder l'objet Etudiant
                etudiantrepository.save(e1);

                // Ajouter l'étudiant à la liste des étudiants de l'employé
                /*List<Etudiant> le = new ArrayList<>();
                le.add(e1);
                savedEmploye.setEtudiants(le);*/

                return savedEmploye;
            }).collect(Collectors.toList());

            // Sauvegarder la liste des employés après avoir ajouté les étudiants
            employeActifRepository.saveAll(excelDataList);
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

    public EmployeActifReDTO getEmployeActifWithRetenues(String matriculeEmp) {
        EmployeActif employeActif = employeActifRepository.findByMatriculeEmp(matriculeEmp);
        if (employeActif == null) {
            return null; // Ou lancer une exception si nécessaire
        }

        EmployeActifReDTO employeActifDTO = new EmployeActifReDTO();
        employeActifDTO.setId(employeActif.getId());
        employeActifDTO.setMatricule(employeActif.getMatriculeEmp());
        employeActifDTO.setCategorieAct(employeActif.getCategorieAct());

        List<RetenueDto> retenueDTOs = employeActif.getRetenues().stream().map(ret -> {
                RetenueDto  dto = new RetenueDto();
        dto.setId(ret.getId());
        dto.setLib(ret.getLib());
        dto.setMontant(ret.getMontant());
        dto.setDateDebut(ret.getDateDebut());
        dto.setDateFin(ret.getDateFin());
        return dto;
        }).collect(Collectors.toList());

        employeActifDTO.setRetenues(retenueDTOs);
        return employeActifDTO;
    }

    public ByteArrayInputStream exportEmployesByCategorie(CategoriieAct categorie) throws IOException {
        List<EmployeActif> employesActifs = employeActifRepository.findByCategorieAct(categorie);

        try (Workbook workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Employés Actifs");

            // Créez des en-têtes et remplissez les données dans le workbook
            // Exemple d'en-tête
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("Matricule");
            header.createCell(1).setCellValue("Nom Complet");
            header.createCell(2).setCellValue("nbretudiant");
            // Ajoutez plus d'en-têtes selon vos besoins

            // Ajoutez les données
            int rowNum = 1;
            for (EmployeActif employe : employesActifs) {
                var row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(employe.getMatricule());
                row.createCell(1).setCellValue(employe.getNomPrénomAgent());
                row.createCell(2).setCellValue(employe.getEtudiants().size());
                // Ajoutez plus de cellules selon vos besoins
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }
    }
}

