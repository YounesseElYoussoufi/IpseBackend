package com.example.ipsebackend.service;

import com.example.ipsebackend.dto.*;
import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.enums.CategorieRet;
import com.example.ipsebackend.repositories.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 private EmployeRepository employeRepository;
    private final EmployeRetraiteRepository employeRetraiterepository;
    private final DetteRepository detterepository;
private final PaiementRepository paiementrepository;

private final EtudiantRepository    etudiantrepository;
private final AnneeScolaireepository anneeScolaireepository ;
    public EmpoyeRetraiteService(EmployeRetraiteRepository employeRetraiterepository, DetteRepository detterepository, PaiementRepository paiementrepository, EtudiantRepository etudiantrepository, AnneeScolaireepository anneeScolaireepository) {
        this.employeRetraiterepository = employeRetraiterepository;
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
            List<EmployeRetraite> employeRetraites = rows.stream().map(row -> {
                EmployeRetraite employeRetraite = employeRetraiterepository.findByNRCAR(row.get(0));
                if (employeRetraite == null) {
                    employeRetraite = new EmployeRetraite();
                }

                // Définir les propriétés de EmployeRetraite
                employeRetraite.setNRCAR(row.get(0));
                employeRetraite.setMatricule(row.get(0));
                employeRetraite.setNomPrénomAgent(row.get(2 ));
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

                employeRetraite.setStatut(row.get(31));



/*// Process totalDette
                String totalPaiementString = row.get(23);  // Correct column for totalDette
                if (totalPaiementString != null) {
                    try {

                        employeRetraite.setTotalDette(a + e1.getTotalPaiement()+ row.get(23));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid totaldette value: " + totalPaiementString);
                   }
                }*/

               // employeRetraiterepository.save(employeRetraite);
// Récupération de l'employé retraité à partir de NRCAR
                EmployeRetraite e1 = employeRetraiterepository.findByNRCAR(row.get(0));
                String reliquatString = row.get(25);
                if (reliquatString != null) {
                    try {
                        Double reliquat = Double.parseDouble(reliquatString);
                        if (employeRetraite.getReliquat() == null) {
                            employeRetraite.setReliquat(reliquat);
                        } else {
                            employeRetraite.setReliquat(employeRetraite.getReliquat() + reliquat);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de reliquat invalide : " + reliquatString);
                    }
                }


                String totalpaiementString = row.get(24);
                if (totalpaiementString != null) {
                    try {
                        Double totalpaiementDouble = Double.parseDouble(totalpaiementString);
                        if (employeRetraite.getTotalPaiement() == null) {
                            employeRetraite.setTotalPaiement(totalpaiementDouble);
                        } else {
                            employeRetraite.setTotalPaiement(employeRetraite.getTotalPaiement() + totalpaiementDouble);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de reliquat invalide : " + reliquatString);
                    }
                }


                String totaldetteString = row.get(23);
                if (totaldetteString != null) {
                    try {
                        Double totaldettetDouble = Double.parseDouble(totaldetteString);
                        if (employeRetraite.getTotalDette() == null) {
                            employeRetraite.setTotalDette(totaldettetDouble);
                        } else {
                            employeRetraite.setTotalDette(employeRetraite.getTotalDette() + totaldettetDouble);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur de reliquat invalide : " + reliquatString);
                    }
                }

// Save the entity
                employeRetraiterepository.save(employeRetraite);

                // Sauvegarder l'entité EmployeRetraite avant d'ajouter les Dette
                employeRetraiterepository.save(employeRetraite);

                // Créer les objets Dette associés et les sauvegarder
                List<Dette> dettes = new ArrayList<>();

                String dette18String = row.get(11);
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

                String dette23String = row.get(21);
                if (dette23String != null) {
                    try {
                        Double dette23Double = Double.parseDouble(dette23String);
                        Dette dette23 = new Dette();
                        dette23.setAnneeDate("2023/2024");
                        dette23.setMontantAPayer(dette23Double);
                        dette23.setEmployeRetraite(employeRetraite); // Associe à l'employé retraité
                        dettes.add(dette23);
                        System.out.println("Dette 2023/2024 ajoutée avec succès: " + dette23.getMontantAPayer());
                    } catch (NumberFormatException e) {
                        System.err.println("Valeur dette 2023/2024 invalide: " + dette23String);
                    }
                }
                String dette20String = row.get(15);
                if (dette20String != null) {
                    try {
                        Double dette20Double = Double.parseDouble(dette20String);
                        Dette dette20 = new Dette();
                        dette20.setAnneeDate("2020/2021");
                        dette20.setMontantAPayer(dette20Double);
                        dette20.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        dettes.add(dette20);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + dette20String);
                    }
                }


               employeRetraite.setPension(row.get(32));
                System.out.println("Raw cell value: " + row.get(23));

                String dette21String = row.get(17);
                if (dette21String != null) {
                    try {
                        Double dette21Double = Double.parseDouble(dette21String);
                        Dette dette21 = new Dette();
                        dette21.setAnneeDate("2021/2022");
                        dette21.setMontantAPayer(dette21Double);
                        dette21.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        dettes.add(dette21);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + dette21String);
                    }
                }

                String dette22String = row.get(19);
                if (dette22String != null) {
                    try {
                        Double dette22Double = Double.parseDouble(dette22String);
                        Dette dette22 = new Dette();
                        dette22.setAnneeDate("2022/2023");
                        dette22.setMontantAPayer(dette22Double);
                        dette22.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        dettes.add(dette22);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + dette22String);
                    }
                }

                String dette19String = row.get(13);
                if (dette19String != null) {
                    try {
                        Double dette19Double = Double.parseDouble(dette19String);
                        Dette dette19 = new Dette();
                        dette19.setAnneeDate("2019/2020");
                        dette19.setMontantAPayer(dette19Double);
                        dette19.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        dettes.add(dette19);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + dette19String);
                    }
                }
                List<Paiement> paiements = new ArrayList<>();
                String paiement19String = row.get(12);
                if (paiement19String != null) {
                    try {
                        Double paiement19Double = Double.parseDouble(paiement19String);
                        Paiement paiement19 = new Paiement();
                        paiement19.setDatePaiement("2018/2019");
                        paiement19.setMontant(paiement19Double);
                        paiement19.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement19);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement19String);
                    }
                }

                String paiement20String = row.get(14);
                if (paiement20String != null) {
                    try {
                        Double paiement20Double = Double.parseDouble(paiement20String);
                        Paiement paiement20 = new Paiement();
                        paiement20.setDatePaiement("2019/2020");
                        paiement20.setMontant(paiement20Double);
                        paiement20.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement20);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement19String);
                    }
                }

                String paiement21String = row.get(16);
                if (paiement21String != null) {
                    try {
                        Double paiement21Double = Double.parseDouble(paiement21String);
                        Paiement paiement21 = new Paiement();
                        paiement21.setDatePaiement("2020/2021");
                        paiement21.setMontant(paiement21Double);
                        paiement21.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement21);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement19String);
                    }
                }

                String paiement22String = row.get(18);
                if (paiement22String != null) {
                    try {
                        Double paiement22Double = Double.parseDouble(paiement22String);
                        Paiement paiement22 = new Paiement();
                        paiement22.setDatePaiement("2021/2022");
                        paiement22.setMontant(paiement22Double);
                        paiement22.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement22);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement22String);
                    }
                }

                String paiement23String = row.get(20);
                if (paiement23String != null) {
                    try {
                        Double paiement23Double = Double.parseDouble(paiement23String);
                        Paiement paiement23 = new Paiement();
                        paiement23.setDatePaiement("2022/2023");
                        paiement23.setMontant(paiement23Double);
                        paiement23.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement23);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement22String);
                    }
                }

                String paiement24String = row.get(22);
                if (paiement24String != null) {
                    try {
                        Double paiement24Double = Double.parseDouble(paiement24String);
                        Paiement paiement24 = new Paiement();
                        paiement24.setDatePaiement("2023/2024");
                        paiement24.setMontant(paiement24Double);
                        paiement24.setEmployeRetraite(employeRetraite); // Associate with the saved EmployeRetraite
                        paiements.add(paiement24);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid dette value: " + paiement22String);
                    }
                }
















                List<Etudiant> etudiants = new ArrayList<>();
                String etudiantString = row.get(22);
                if (etudiantString != null) {
                    try {
                        Etudiant etudiant = new Etudiant();
                        etudiant.setNomComplet(row.get(3) + " " + row.get(4)); // Assurez-vous de bien concaténer le nom et prénom

                        List<AnneScolaire> la = new ArrayList<>();

                        AnneScolaire a1 = new AnneScolaire();
                        AnneScolaire a2 = new AnneScolaire();
                        AnneScolaire a3 = new AnneScolaire();

                        a1.setNiveau(row.get(5));
                        a1.setAnneScolaire("2021/2022");
                        a1.setEtablissement(row.get(9));
                        a1.setGroupe(row.get(5));

                        a2.setNiveau(row.get(6));
                        a2.setAnneScolaire("2022/2023");
                        a2.setEtablissement(row.get(9));
                        a2.setGroupe(row.get(5));

                        a3.setNiveau(row.get(7));
                        a3.setAnneScolaire("2023/2024");
                        a3.setEtablissement(row.get(9));
                        a3.setGroupe(row.get(5));
                        // Sauvegarder les instances d'AnnéeScolaire dans la base de données
                        anneeScolaireepository.save(a1);
                        anneeScolaireepository.save(a2);
                        anneeScolaireepository.save(a3);

                        // Ajouter les années scolaires sauvegardées à la liste
                        la.add(a1);
                        la.add(a2);
                        la.add(a3);

                        // Associer les années scolaires à l'étudiant
                        etudiant.setAnneScolaires(la);

                        // Associer l'étudiant à chaque année scolaire (il est préférable de faire cette association dans la méthode de sauvegarde de l'étudiant)
                        a1.getEtudiants().add(etudiant);
                        a2.getEtudiants().add(etudiant);
                        a3.getEtudiants().add(etudiant);

                        // Sauvegarder l'étudiant
                        etudiants.add(etudiant);
                        etudiant.setEmploye(employeRetraite);
                        List<Etudiant> le = new ArrayList<>();
                        le.add(etudiant);
                        employeRetraite.setEtudiants(le);

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










                // Ajoutez des objets Dette supplémentaires ici

                detterepository.saveAll(dettes);  // Sauvegarder tous les Dette
                paiementrepository.saveAll(paiements) ;
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


    public Page<EmployeRetraiteDTO> listEmployeRetraites( Pageable pageable ,String search) {
       // Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<EmployeRetraite> employeRetraitePage = employeRetraiterepository.findByNRCARContaining(search, pageable);
        return employeRetraitePage.map(this::convertToDTO);
    }

    private EmployeRetraiteDTO convertToDTO(EmployeRetraite employeRetraite) {
        EmployeRetraiteDTO dto = new EmployeRetraiteDTO();
        dto.setId(employeRetraite.getId());
        dto.setNRCAR(employeRetraite.getNRCAR());
        dto.setCategorieRet(employeRetraite.getCategorieRet());
        dto.setDateretraite(employeRetraite.getDateretraite());
        dto.setStatut(employeRetraite.getStatut());
        dto.setPension(employeRetraite.getPension());
        dto.setTotalDette(employeRetraite.getTotalDette());
        dto.setTotalPaiement(employeRetraite.getTotalPaiement());
        dto.setReliquat(employeRetraite.getReliquat());
        dto.setRemarque(employeRetraite.getRemarque());

        dto.setNomPrenomAgent(employeRetraite.getNomPrénomAgent());
        dto.setContact(employeRetraite.getContact());


        dto.setDettes(employeRetraite.getDettes().stream()
                .map(dette -> {
                    DetteDTO detteDTO = new DetteDTO();
                    detteDTO.setId(dette.getId());
                    detteDTO.setAnneeDate(dette.getAnneeDate());
                    detteDTO.setMontantAPayer(dette.getMontantAPayer());
                    return detteDTO;
                }).collect(Collectors.toList()));

        dto.setPaiements(employeRetraite.getPaiements().stream()
                .map(paiement -> {
                    PaiementDTO paiementDTO = new PaiementDTO();
                    paiementDTO.setId(paiement.getId());
                    paiementDTO.setDateDette(paiement.getDateDette());
                    paiementDTO.setDatePaiement(paiement.getDatePaiement());
                    paiementDTO.setMontant(paiement.getMontant());
                    return paiementDTO;
                }).collect(Collectors.toList()));

        dto.setEtudiants(employeRetraite.getEtudiants().stream()
                .map(etudiant -> {
                    EtudiantDTO etudiantDTO = new EtudiantDTO();
                    etudiantDTO.setId(etudiant.getId());
                    etudiantDTO.setNom(etudiant.getNom());
                    etudiantDTO.setPrenom(etudiant.getPrenom());
                    //etudiantDTO.setEmail(etudiant.get);
                    return etudiantDTO;
                }).collect(Collectors.toList()));
        return dto;
    }

    public List<DetteDTO> getDettesByNrcar(String nrcar) {
        EmployeRetraite employeRetraite = employeRetraiterepository.findByNRCAR(nrcar);
        if (employeRetraite == null) {
            return null; // ou lancer une exception selon le cas
        }

        return employeRetraite.getDettes().stream()
                .map(dette -> new DetteDTO(dette.getId(), dette.getAnneeDate(), dette.getMontantAPayer()))
                .collect(Collectors.toList());
    }
    public List<PaiementDTO> getPaiementsByNrcar(String nrcar) {
        EmployeRetraite employeRetraite = employeRetraiterepository.findByNRCAR(nrcar);
        if (employeRetraite == null) {
            return null; // ou lancer une exception selon le cas
        }

        return employeRetraite.getPaiements().stream()
                .map(paiement -> new PaiementDTO(paiement.getId(), paiement.getDateDette(), paiement.getDatePaiement(), paiement.getMontant()))
                .collect(Collectors.toList());
    }
}
