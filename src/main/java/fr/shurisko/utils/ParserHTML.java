package fr.shurisko.utils;

import fr.shurisko.WebCloudServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ParserHTML {

    private static String URLParser(String htmlContent) {
        return htmlContent
                .replace("%url", WebCloudServer.CloudAPI.ressourceURL);
    }

    public static String parseHTML(String htmlName) {
        File f = new File("src/main/resources/" + htmlName + ".html");
        if (f.exists()) {
            try {
                final BufferedReader r = new BufferedReader(new FileReader(f));
                final StringBuilder t = new StringBuilder();
                String l;
                while ((l = r.readLine()) != null)
                    t.append(l);
                r.close();
                return URLParser(t.toString());
            } catch (IOException ignored) {
            }
        }
        return null;
    }

}
