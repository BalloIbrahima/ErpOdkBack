package com.odc.Apiodkerp.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

public class SaveImage {

    public static String server = "http://localhost/";
    public static String Activitelocation = "C:/xampp/htdocs/erpodk/images/activites";
    public static String Userlocation = "C:/xampp/htdocs/erpodk/images/utilisateurs";

    public static String save(String typeImage, MultipartFile file, String nomFichier) {
        String src = "";

        String location = "";
        if (typeImage == "user") {
            location = Userlocation;
        } else {
            location = Activitelocation;
        }

        /// debut de l'enregistrement
        try {
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");

            Path chemin = Paths.get(location);
            if (!Files.exists(chemin)) {
                // si le fichier n'existe pas deja
                Files.createDirectories(chemin);
                Files.copy(file.getInputStream(), chemin
                        .resolve(nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                src = server + nomFichier
                        + file.getOriginalFilename().substring(index).toLowerCase();
            } else {
                // si le fichier existe pas deja
                String newPath = location + nomFichier
                        + file.getOriginalFilename().substring(index).toLowerCase();
                Path newchemin = Paths.get(newPath);
                if (!Files.exists(newchemin)) {
                    // si le fichier n'existe pas deja
                    Files.copy(file.getInputStream(), chemin
                            .resolve(
                                    nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                    src = server + nomFichier
                            + file.getOriginalFilename().substring(index).toLowerCase();
                } else {
                    // si le fichier existe pas deja on le suprime et le recrèe
                    Files.delete(newchemin);

                    Files.copy(file.getInputStream(), chemin
                            .resolve(
                                    nomFichier + file.getOriginalFilename().substring(index).toLowerCase()));
                    src = server + nomFichier
                            + file.getOriginalFilename().substring(index).toLowerCase();
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            src = null;
        }

        return src;
    }
}
