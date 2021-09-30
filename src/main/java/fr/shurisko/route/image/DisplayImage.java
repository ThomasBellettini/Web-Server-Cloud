package fr.shurisko.route.image;

import fr.shurisko.WebCloudServer;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.utils.CloudImageUtils;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DisplayImage {
    public static byte[] sendImage(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response, false)) {
            response.type("image/jpeg");
            return CloudImageUtils.noPermission();
        } else {
            byte[] rawImage = new byte[0];
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                BufferedImage bi;
                File img = new File(WebCloudServer.CloudAPI.uploadDir, request.params("id"));
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
                ImageIO.write(bi, extension, baos);

                baos.flush();
                rawImage = baos.toByteArray();
            } catch (IOException ignored) {
            }
            response.type("image/jpeg");
            return rawImage;
        }
    }

}
