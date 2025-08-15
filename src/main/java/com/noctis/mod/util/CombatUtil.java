package com.noctis.mod.util;

import com.noctis.mod.mask.MaskItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class CombatUtil {
    private CombatUtil() {}

    public static MaskItem getWornMask(PlayerEntity player) {
        ItemStack head = player.getInventory().getArmorStack(3);
        if (head != null && head.getItem() instanceof MaskItem m) return m;
        return null;
    }

    public static void heal(LivingEntity e, float hearts) {
        e.setHealth(Math.min(e.getMaxHealth(), e.getHealth() + (hearts * 2f)));
    }
}
