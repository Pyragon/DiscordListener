package com.cryo;

import com.cryo.entities.Episode;
import com.cryo.entities.TVShow;
import com.cryo.entities.Video;
import lombok.Getter;

import java.util.Properties;

public class PlexManager {

    @Getter
    private PlexAPI api;

    private boolean using;

    public PlexManager() {

    }

    public void start() {
        Properties prop = new Properties();
        prop.put("username", Listener.getProperties().getProperty("plex-username"));
        prop.put("token", Listener.getProperties().getProperty("plex-token"));
        api = new PlexAPI(prop);
    }

    public void tick() {
        if (isPlaying()) {
            using = true;
            updatePresence();
        }
        if (using && !isPlaying()) {
            using = false;
            Listener.getInstance().getPresenceManager().getLib().Discord_ClearPresence();
        }
    }

    public boolean isPlaying() {
        return api.getNowPlaying() != null && api.getNowPlaying().size() > 0;
    }

    public void updatePresence() {
        Video video = api.getNowPlaying().get(0);
        TVShow show = api.getTVShow(video.getGrandparentRatingKey());
        Episode episode = show.getEpisode(video.getRatingKey());
        String SE = "S" + episode.getSeason().getIndex() + "E" + episode.getIndex();
        SE += " - " + episode.getTitle();
        PresenceBuilder builder = new PresenceBuilder("plex", (byte) 1, show.getTitle(), SE, "", 0, 0);
        Listener.getInstance().getPresenceManager().updatePresence(builder.buildPresence());
    }

}
