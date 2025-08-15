package com.noctis.mod.client;

import com.noctis.mod.NoctisMod;
import com.noctis.mod.mask.MaskItem;
import com.noctis.mod.mask.MaskType;
import com.noctis.mod.util.Cooldowns;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class NoctisPackets {
    public static final Identifier FOG_ABILITY = new Identifier(NoctisMod.MODID, "fog_ability");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(FOG_ABILITY, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> handleFogAbility(player));
        });
    }

    private static void handleFogAbility(ServerPlayerEntity player) {
        if (player == null) return;
        if (!(player.getInventory().getArmorStack(3).getItem() instanceof MaskItem mi)) return;
        if (mi.getMaskType() != MaskType.FOG) return;
        if (!Cooldowns.ready(player, "fog_ability", 180_000)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 5 * 20, 0));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 5 * 20, 0));
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS, 1f, 1f);
    }
}
