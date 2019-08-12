package com.cryo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Listener {

    @Getter
    private RichPresenceManager presenceManager;

    @Getter
    private WebSocketListener listener;

    @Getter
    private PlexManager plexManager;

    @Getter
    private static Listener instance;

    @Getter
    private static Properties properties;

    @Getter
    private static Gson gson;

    public void start() {
        gson = buildGson();
        loadProperties();
        listener = new WebSocketListener(8974);
        plexManager = new PlexManager();
        plexManager.start();
        presenceManager = new RichPresenceManager();
        listener.start();
        class MyTask extends TimerTask {

            public void run() {
                presenceManager.getLib().Discord_RunCallbacks();
                plexManager.tick();
                if (listener.isReady() && listener.isPaused())
                    listener.update();
            }

        }
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 500, 500);
    }

    public static Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setVersion(1.0)
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
    }

    public void loadProperties() {
        File file = new File("props.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = reader.readLine()) != null) json.append(line);
            properties = getGson().fromJson(json.toString(), Properties.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        instance = new Listener();
        instance.start();
    }
}
