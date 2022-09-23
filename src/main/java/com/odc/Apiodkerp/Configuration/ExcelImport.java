package com.odc.Apiodkerp.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.odc.Apiodkerp.Models.Postulant;

public class ExcelImport {

    // remise des branches
    public static String excelType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    // public static String excelType = "application/vnd.ms-excel";

    // Methode qui verifi si le fichier est un fichier Excel
    public static Boolean verifier(MultipartFile file) {

        if (excelType.equals(file.getContentType())) {
            return true;
        } else {
            return false;
        }
    }

    // Methode qui retourne la liste des postulants à travers le fichier excel
    // fournit
    public static List<Postulant> postulantsExcel(MultipartFile file) {

        try {
            // creation d'une liste dans la quelle on va mettre la liste recuperée
            List<Postulant> postulants = new ArrayList<Postulant>();

            // lecture du fichier
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Iterator<Sheet> sheet = workbook.sheetIterator();

            DataFormatter formatter = new DataFormatter();

            while (sheet.hasNext()) {

                int numeroLigne = 0;

                // System.out.println(ligne.next().getRowNum());

                Sheet sh = sheet.next();
                Iterator<Row> iterator = sh.iterator();
                // parcour du fichier excel ligne par ligne
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Iterator<Cell> cellIterator = row.iterator();
                    // Recuperation de la ligne courante
                    // Row ligneCourante = ligne.next();
                    // on lui dit de sauter la première ligne du fichier, qui est l'entête
                    if (numeroLigne == 0) {
                        numeroLigne++;
                        continue;
                    }

                    // Après avoir recuperer une ligne, on crée un postulant et on recupère ses
                    // attributs;
                    Postulant postulant = new Postulant();

                    int numeroColonne = 0;
                    // parcour des colonnes d'une ligne
                    while (cellIterator.hasNext()) {
                        // Recuperation de la colonne courante
                        Cell colonneCourante = cellIterator.next();
                        // recuperation des infos de chaque colonne
                        switch (numeroColonne) {
                            // première colonne contenant le nom
                            case 0:
                                postulant.setNom(formatter.formatCellValue(colonneCourante));
                                // System.out.println(colonneCourante.getStringCellValue());
                                break;
                            // deuxième colonne contenant le prenom
                            case 1:
                                postulant.setPrenom(formatter.formatCellValue(colonneCourante));
                                break;
                            // troixième colonne contenant le numero
                            case 2:
                                postulant.setNumero(formatter.formatCellValue(colonneCourante));
                                break;
                            // dernière colonne contenant l'adresse mail
                            case 3:
                                postulant.setEmail(formatter.formatCellValue(colonneCourante));
                                break;
                            default:
                                break;
                        }
                        numeroColonne++;
                    }
                    postulants.add(postulant);
                    // numeroLigne++;
                }
            }

            workbook.close();
            return postulants;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            // TODO: handle exception
            return null;
        }

    }
}
