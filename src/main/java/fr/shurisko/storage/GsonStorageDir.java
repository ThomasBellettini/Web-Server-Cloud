package fr.shurisko.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.ressource.CloudPath;
import fr.shurisko.ressource.manager.CloudFileManager;
import fr.shurisko.session.CloudSession;

import java.io.*;
import java.util.List;
import java.util.Locale;

public class GsonStorageDir {

    private Gson gson;

    private final File _DIR_STORAGE;
    private final File _DIR_STORAGE_DIR;

    public GsonStorageDir() {
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        _DIR_STORAGE = new File("storage");
        _DIR_STORAGE.mkdir();
        _DIR_STORAGE_DIR = new File(_DIR_STORAGE, "cloudDir");
        _DIR_STORAGE_DIR.mkdir();
    }

    public void save(CloudPath path) {
        final CloudUser user = CloudSession.getUserByID(path.getOwnerID());
        System.out.println(user.getUsername());
        if (user == null) return;
        final File file = new File(_DIR_STORAGE_DIR, user.ID + "-" + user.getUsername());
        file.mkdirs();
        final File contentFile = new File(file, path.getName().toLowerCase(Locale.ROOT) + "-" + path.ID + ".json");
        final FileWriter fileWriter;
        try {
            if (!contentFile.exists())
                contentFile.createNewFile();
            fileWriter = new FileWriter(contentFile);
            fileWriter.write(serialize(path));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {}
    }

    public void saveAll() {
        for (List<CloudPath> path : CloudFileManager.PManager.userCloudPath.values()) {
            for (CloudPath tmp : path) {
                save(tmp);
            }
        }
    }

    public void load(File file) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null)
                stringBuilder.append(tmp);
            bufferedReader.close();
        } catch (IOException ignored) {  }

        CloudPath path = deserialize(stringBuilder.toString());
        if (path == null) return;
        CloudUser user = CloudSession.getUserByID(path.getOwnerID());
        if (user != null) CloudFileManager.PManager.addOrUpdatePath(user, path);
    }

    public void loadAll() {
        for (File file : _DIR_STORAGE.listFiles()) {
            if (file.isDirectory()) {
                for (File subFolder : file.listFiles()) {
                    if (subFolder.getName().endsWith(".json")) {
                        load(subFolder);
                    }
                }
            }
        }
    }

    private String serialize(CloudPath path) {
        return gson.toJson(path);
    }

    private CloudPath deserialize(String str) {
        return gson.fromJson(str, CloudPath.class);
    }

}
