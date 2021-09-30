package fr.shurisko.utils;

import fr.shurisko.WebCloudServer;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CloudImageUtils {

    public static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static byte[] noPermission() {
        byte[] rawImage = new byte[0];
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi;
            File img = new File(WebCloudServer.CloudAPI.uploadDir, "no_permission.jpg");
            if (!img.getName().contains(".")) return null;
            String[] ext = {"jpeg", "png", "jpg", "gif", "svg"};
            String extension = "";
            for (String tmp : ext) {
                if (img.getName().endsWith(tmp)) {
                    extension = tmp;
                    break;
                }
            }
            bi = ImageIO.read(img);
            ImageIO.write( bi, extension, baos );

            baos.flush();
            rawImage = baos.toByteArray();
        } catch (IOException ignored) { }
        return rawImage;
    }

}
