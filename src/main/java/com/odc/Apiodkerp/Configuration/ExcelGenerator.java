package com.odc.Apiodkerp.Configuration;

import com.odc.Apiodkerp.Models.Postulant;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelGenerator {
    //Besoin d'une liste de postulants à exporter
    private List<Postulant> listepostulant;
    //Déclaration des classes pour manipuler le fichier excel
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    //Constructeur pour générer le fichier excel en prenant une liste de postulant en paramètre
    public ExcelGenerator(List<Postulant> listepostulant) {
        this.listepostulant = listepostulant;
        workbook = new XSSFWorkbook();
    }
    //Méthode pour styliser l'entête de la feuille
    private void creerEntete() {
        sheet = workbook.createSheet("Postulants tirés "+new Date());
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        //Nom des cellules de l'entête
        creerCellule(row,0,"ID",style);
        creerCellule(row,1,"NOM",style);
        creerCellule(row,0,"PRENOM",style);
        creerCellule(row,0,"EMAIL",style);
        creerCellule(row,0,"NUMERO",style);
        creerCellule(row,0,"GENRE",style);
    }
    //Méthode pour créer une cellulle
    private void creerCellule(Row row, int numeroColonne, Object valeurCellule, CellStyle style) {
        sheet.autoSizeColumn(numeroColonne);
        Cell cell = row.createCell(numeroColonne);
        if(valeurCellule instanceof Integer) {
            cell.setCellValue((Integer) valeurCellule);
        } else if (valeurCellule instanceof Long) {
            cell.setCellValue((Long) valeurCellule);
        } else if (valeurCellule instanceof String) {
            cell.setCellValue((String) valeurCellule);
        } else cell.setCellValue((Boolean) valeurCellule);
        cell.setCellStyle(style);
    }
    private void enregistrer() {
        int numeroLigne = 0;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Postulant postulantcourant:
             listepostulant) {
            Row row = sheet.createRow(numeroLigne++);
            int numeroColonne = 0;
            creerCellule(row,numeroColonne++,postulantcourant.getId(),style);
            creerCellule(row,numeroColonne++,postulantcourant.getNom(),style);
            creerCellule(row,numeroColonne++,postulantcourant.getPrenom(),style);
            creerCellule(row,numeroColonne++,postulantcourant.getEmail(),style);
            creerCellule(row,numeroColonne++,postulantcourant.getNumero(),style);
            creerCellule(row,numeroColonne++,postulantcourant.getGenre(),style);
        }
    }
    public void genererFichierExcel(HttpServletResponse reponse) throws IOException {
        creerEntete();
        enregistrer();
        ServletOutputStream outputStream = reponse.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
