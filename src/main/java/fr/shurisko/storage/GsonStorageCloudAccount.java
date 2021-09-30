package fr.shurisko.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;

import java.io.*;
import java.util.Locale;

public class GsonStorageCloudAccount {

    private Gson gson;
    private final File _DIR_STORAGE;
    private final File _DIR_STORAGE_ACCOUNT;

    public GsonStorageCloudAccount() {
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        _DIR_STORAGE = new File("storage");
        _DIR_STORAGE.mkdir();
        _DIR_STORAGE_ACCOUNT = new File(_DIR_STORAGE, "account");
        _DIR_STORAGE_ACCOUNT.mkdir();
    }

    public void loadAll() {
        for (File file : _DIR_STORAGE_ACCOUNT.listFiles()) {
            if (file.getName().endsWith(".json")) {
                CloudUser user = deserialize(read(file));
                if (user != null) UserManager.UManager.addUser(user);
            }
        }
    }

    public void saveAll() {
        for (CloudUser cloudUser : UserManager.UManager.cloudUsers) {
            write(cloudUser);
        }
    }

     public void write(CloudUser user) {
        final FileWriter fileWriter;
        File file = new File(_DIR_STORAGE_ACCOUNT, user.getUsername().toLowerCase(Locale.ROOT) + user.ID + ".json");
        try {
            if (!file.exists())
                file.createNewFile();
            fileWriter = new FileWriter(file);
            fileWriter.write(serialize(user));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ignored) {}
    }

    private String read(File file) {
        if (file.exists()) {
            try {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                final StringBuilder stringBuilder = new StringBuilder();
                String tmp;
                while ((tmp = bufferedReader.readLine()) != null)
                    stringBuilder.append(tmp);
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (IOException ignored) {  }
        }
        return null;
    }

    public String serialize(CloudUser user) {
        return gson.toJson(user);
    }

    public CloudUser deserialize(String content) {
        return gson.fromJson(content, CloudUser.class);
    }

}
