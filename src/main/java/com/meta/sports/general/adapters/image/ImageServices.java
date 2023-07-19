package com.meta.sports.general.adapters.image;

import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageServices  {

    private static final String DIRECTORIO_IMAGENES = "C://Users//Miguel//Desktop//prueba//";

    public String subirImagen(String nombre, String imagenBase64) {
        String rutaImagen = DIRECTORIO_IMAGENES + nombre;
        try (OutputStream out = new FileOutputStream(rutaImagen)) {
            byte[] imageBytes = Base64.getDecoder().decode(imagenBase64);
            out.write(imageBytes);
            System.out.println("Imagen guardada en: " + rutaImagen); // Registro de depuración
        } catch (IOException e) {
            throw new RuntimeException("No se pudo subir la imagen", e);
        }
        return rutaImagen;
    }

    public boolean validarBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String editarImagen(String nombreImagen, String imagenBase64) {
        String rutaImagen = DIRECTORIO_IMAGENES + nombreImagen;
        File imagenExistente = new File(rutaImagen);
        if (imagenExistente.exists()) {
            imagenExistente.delete();
        }
        try (OutputStream out = new FileOutputStream(rutaImagen)) {
            byte[] imageBytes = Base64.getDecoder().decode(imagenBase64);
            out.write(imageBytes);
            System.out.println("Imagen editada en: " + rutaImagen); // Registro de depuración
        } catch (IOException e) {
            throw new RuntimeException("No se pudo editar la imagen", e);
        }
        return rutaImagen;
    }



    public String obtenerImagenBase64(String nombre) {
        try {
            // Recuperar la imagen del directorio de imágenes
            File imagen = new File(DIRECTORIO_IMAGENES  + nombre + ".png");
            FileInputStream fis = new FileInputStream(imagen);
            byte[] imageBytes = new byte[(int) imagen.length()];
            fis.read(imageBytes, 0, imageBytes.length);
            fis.close();
            // Codificar la imagen en base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            // Devolver la imagen como una cadena base64
            return base64Image;
        } catch (IOException e) {
            throw new RuntimeException("No se encontró la imagen", e);
        }
    }


    public void eliminarImagen(String nombre) {

        // Eliminar la imagen del directorio de imágenes

        File fichero = new File(DIRECTORIO_IMAGENES + nombre);
        if (!fichero.delete()) {
            throw new RuntimeException("No se pudo eliminar la imagen");
        }

    }
}
