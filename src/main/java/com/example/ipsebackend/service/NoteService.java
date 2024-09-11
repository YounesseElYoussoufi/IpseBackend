package com.example.ipsebackend.service;

import com.example.ipsebackend.entities.*;
import com.example.ipsebackend.repositories.AnneeScolaireepository;
import com.example.ipsebackend.repositories.EtudiantRepository;
import com.example.ipsebackend.repositories.MatiereRepository;
import com.example.ipsebackend.repositories.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor

public class NoteService {

    public final NoteRepository noteRepository ;
    public  final AnneeScolaireepository anneeScolaireepository;
    public  final MatiereRepository matiereRepository;
    public final EtudiantRepository etudiantRepository;
    public void save(MultipartFile file) throws EncryptedDocumentException, IOException {
        List<List<String>> rows = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(8);
        // Iterator<Row> rowIterator = sheet.iterator();
        rows = StreamSupport.stream(sheet.spliterator(), false)
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        System.out.println("rows :: " + rows);
        // Save data to the database
        List<Etudiant> excelDataList = rows.stream().map(row -> {
            Etudiant excelData = etudiantRepository.findByCneOrCne(row.get(1),row.get(1));
           if(excelData == null) {
               excelData = new Etudiant();
               excelData.setCne(row.get(1));

           }
           List<Matiere> matieres =new ArrayList<>() ;
            Matiere m1 =new Matiere();
            m1.setNom("اللغة العربية");
            List<Note> notes = new ArrayList<>() ;
           Note n1 = new Note();
           n1.setMatiere(m1);
           n1.setEtudiant(excelData);

            String narabes = row.get(2);
            if (narabes != null) {
                try {
                    Float narabef = Float.parseFloat(narabes);
                    n1.setValeur(narabef);
                } catch (NumberFormatException e) {
                    System.err.println("Valeur de narabe invalide : " + narabes);
                }
            }
            notes.add(n1);
            noteRepository.saveAll(notes);
            matiereRepository.saveAll(matieres);
            return excelData;
        }).collect(Collectors.toList());
        etudiantRepository.saveAll(excelDataList);
    }
    private String getCellStringValue(Cell cell) {
        CellType cellType = cell.getCellType();

        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }

        return null;
    }
}