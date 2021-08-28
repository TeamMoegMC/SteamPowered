package com.teammoeg.steampowered.client;

import com.teammoeg.steampowered.create.SPPonderIndex;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class SteamPoweredClient {
    public static void addClientListeners(IEventBus forgeEventBus, IEventBus modEventBus) {
        modEventBus.addListener(SteamPoweredClient::clientInit);
    }

    public static void clientInit(FMLClientSetupEvent event) {
        SPPonderIndex.register();
    }
}
