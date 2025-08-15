package com.noctis.mod.client;

import net.fabricmc.api.ClientModInitializer;

public class NoctisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NoctisKeybinds.register();
    }
}
