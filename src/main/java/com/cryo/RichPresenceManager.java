package com.cryo;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import lombok.Getter;

public class RichPresenceManager {

    @Getter
    private DiscordRPC lib;

    public RichPresenceManager() {
        startRPC();
    }

    public void startRPC() {
        lib = DiscordRPC.INSTANCE;
        lib.Discord_Initialize("463640613850710032", new DiscordEventHandlers(), true, "");
    }

    public void updatePresence(DiscordRichPresence presence) {
        lib.Discord_UpdatePresence(presence);
    }
}
