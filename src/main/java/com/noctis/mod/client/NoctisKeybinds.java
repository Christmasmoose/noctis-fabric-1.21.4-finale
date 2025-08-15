package com.noctis.mod.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NoctisKeybinds {
    public static KeyBinding FOG_ABILITY = KeyBindingHelper.registerKeyBinding(
        new KeyBinding("key.noctis.fog_ability", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.noctis")
    );

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (FOG_ABILITY.wasPressed()) {
                ClientPlayNetworking.send(NoctisPackets.FOG_ABILITY, net.fabricmc.fabric.api.networking.v1.PacketByteBufs.empty());
            }
        });
    }
}
