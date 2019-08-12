package com.cryo;

import club.minnced.discord.rpc.DiscordRichPresence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class PresenceBuilder {

    private final String player;
    private final byte state;
    private final String title;
    private final String artist;
    private final String secondary;
    private int duration;
    private int position;

    public DiscordRichPresence buildPresence() {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.details = title;
        presence.state = artist;
        if (duration != 0)
            presence.endTimestamp = (System.currentTimeMillis() / 1000) + (duration - position);
        presence.smallImageKey = (state == 1 ? "play" : "pause") + "_icon";
        presence.smallImageText = state == 1 ? "Playing" : "Paused";
        presence.largeImageKey = getImageKey();
        presence.largeImageText = getImageText();
        return presence;
    }

    public String getImageKey() {
        return player;
    }

    public String getImageText() {
        return String.valueOf(player.charAt(0)).toUpperCase() + player.substring(1);
    }
}
