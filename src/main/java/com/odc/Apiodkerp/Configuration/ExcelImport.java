package com.odc.Apiodkerp.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.odc.Apiodkerp.Enum.Genre;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
    public static String excelTypeXLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static String excelTypeXLS = "application/vnd.ms-excel";

    // public static String excelType = "application/vnd.ms-excel";

    // Methode qui verifi si le fichier est un fichier Excel
    public static Boolean verifier(MultipartFile file) {

        if (excelTypeXLSX.equals(file.getContentType()) || excelTypeXLS.equals(file.getContentType())) {
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

            if (excelTypeXLSX.equals(file.getContentType())) {
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
                                // deuxième colonne contenant le prenom
                                case 2:


                                    // String g= formatter.formatCellValue(colonneCourante);
                                    String g= colonneCourante.getStringCellValue();
                                    System.out.println(g);
                                    if(g.equals("M") || g.equals("Masculin") || g.equals("MASCULIN")|| g.equals("masculin")){
                                        postulant.setGenre(Genre.Masculin);
                                    }else{
                                        postulant.setGenre(Genre.Feminin);
                                    }


                                    break;
                                // deuxième colonne contenant le prenom
                                case 3:

                                  /*  = new SimpleDateFormat("dd/MM/yyyy").parse(formatter.formatCellValue(colonneCourante));
                                    DateTimeFormatter  a= DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate fet = LocalDate.parse(formatter.formatCellValue(colonneCourante),a);*/
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                    Date d=null;
                                    try {
                                        //d= sdf.parse(colonneCourante.getStringCellValue());
                                        d = colonneCourante.getDateCellValue();
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        d=null;
                                        e.printStackTrace();
                                        continue;
                                    }

                                    SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy"); // your template here
                                    //Date dateStr = formater.parse(formatter.formatCellValue(colonneCourante));

                                    //Date dateStr = formater.parse(formatter.formatCellValue(colonneCourante));
                                    //String date = new SimpleDateFormat("yyyy-MM-dd")
                                        //    .format(new Date(formatter.formatCellValue(colonneCourante)));

                                    postulant.setDateNaissance(d);


                                    //String date = request.getParameter("date");
                                    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // your template here
                                    //java.util.Date dateStr = formatter.parse(date);
                                    break;
                                // troixième colonne contenant le numero
                                case 4:
                                    postulant.setNumero(formatter.formatCellValue(colonneCourante));
                                    break;
                                // dernière colonne contenant l'adresse mail
                                case 5:
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
            } else if (excelTypeXLS.equals(file.getContentType())) {
                DataFormatter formatter = new DataFormatter();
                // ArrayList<Postulants> values = new ArrayList<Postulants>(); Variable
                // permettant de prendre toutes les donnes du tableau

                // Bloque permettant de lever les exception lors de l'importation du fichier
                // excel
                // InputStream fichier = new FileInputStream("fichier.xls"); // Recuperation du
                // fichier Execl sous forme de fichier simple

                POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream()); // conversion du fichier simple sous
                                                                                 // forme d'un fichier POI

                HSSFWorkbook wb = new HSSFWorkbook(fs); // Conversion du fichier POI sous format Workbook

                HSSFSheet sheet = wb.getSheetAt(0); // Recuperation du Premier page du fichier excel

                Iterator<Row> rows = sheet.rowIterator(); // Recuperation de tous les lignes de la page du fichier
                // Boucle permettant de parcours toutes lignes de la page

                rows.next();

                while (rows.hasNext()) {

                    HSSFRow row = (HSSFRow) rows.next(); // Recuperation d'une ligne du tableau

                    Iterator<Cell> cells = row.cellIterator(); // Recuperation de toutes toutes les colonnes de chaque
                                                               // ligne
                    Postulant p = new Postulant();

                    int numColun = 0;

                    // Boucle permettant de parcourut toutes les colonnes de chaque ligne
                    while (cells.hasNext()) {

                        HSSFCell cell = (HSSFCell) cells.next(); // Recuperation d'une colonne

                        switch (numColun) {

                            case 0:
                                p.setNom(formatter.formatCellValue(cell));
                                break;
                            case 1:

                                p.setPrenom(formatter.formatCellValue(cell));
                                break;
                            case 2:
                                p.setNumero(formatter.formatCellValue(cell));
                                break;
                            case 3:
                                p.setEmail(formatter.formatCellValue(cell));
                                break;
                            default:
                                break;
                        }
                        numColun++;

                        // Condition permettant de verifier le type de la colonne et effectuer une
                        // convesion si c'est type int
                        // if (cell.getCellType() == CellType.NUMERIC){
                        // values.add(Integer.toString((int) cell.getNumericCellValue()));
                        // }
                        // else{
                        // values.add(cell.getStringCellValue());
                        // }
                    }

                    // postulantRepository.INSERTPOSTULANT
                    // (values.get(3),values.get(1),values.get(2),values.get(0));
                    postulants.add(p);
                }

            }
            return postulants;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            // TODO: handle exception
            return null;
        }

    }

    private String modifyDateLayout(String inputDate) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(inputDate);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }
}
