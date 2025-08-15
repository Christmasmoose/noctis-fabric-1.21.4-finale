package com.noctis.mod.mask;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

public class MaskItem extends ArmorItem {
    private final MaskType type;

    public MaskItem(ArmorMaterial material, Settings settings, MaskType type) {
        super(material, Type.HELMET, settings);
        this.type = type;
    }

    public MaskType getMaskType() { return type; }

    @Override
    public boolean hasGlint(ItemStack stack) { return true; }
}
