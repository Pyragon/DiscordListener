package com.cryo;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketListener extends WebSocketServer {

    private int port;

    private String player;
    private byte state;
    private String title;
    private String artist;
    private String secondary;
    private int duration;
    private int position;

    private PresenceBuilder builder;

    public WebSocketListener(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    public boolean isReady() {
        return player != null && state != 0 && title != null && artist != null && secondary != null && duration != 0 && position != 0;
    }

    public boolean isPaused() {
        return state == 2;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.contains("PLAYER:")) {
            player = message.replace("PLAYER:", "").toLowerCase();
            conn.send("version:0.4.1");
            update();
        } else if (message.contains("STATE:")) {
            state = Byte.parseByte(message.replace("STATE:", ""));
            update();
        } else if (message.contains("TITLE:")) {
            title = message.replace("TITLE:", "");
            position = 0;
            update();
        } else if (message.contains("ARTIST:")) {
            artist = message.replace("ARTIST:", "");
            update();
        } else if (message.contains("ALBUM:")) {
            secondary = message.replace("ALBUM:", "");
            update();
        } else if (message.contains("DURATION:")) {
            String durationString = message.replace("DURATION:", "");
            String[] spl = durationString.split(":");
            duration = Integer.parseInt(spl[0]) * 60;
            duration += Integer.parseInt(spl[1]);
            update();
        } else if (message.contains("POSITION:")) {
            String positionString = message.replace("POSITION:", "");
            String[] spl = positionString.split(":");
            position = Integer.parseInt(spl[0]) * 60;
            position += Integer.parseInt(spl[1]);
            update();
        }
    }

    public void update() {
        if (!isReady() || Listener.getInstance().getPlexManager().isPlaying()) return;
        builder = new PresenceBuilder(player, state, title, artist, secondary, duration, position);
        Listener.getInstance().getPresenceManager().updatePresence(builder.buildPresence());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {
        System.out.println("WebSocketServer started on port: " + port);
    }
}
