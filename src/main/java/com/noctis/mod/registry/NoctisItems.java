package com.noctis.mod.registry;

import com.noctis.mod.NoctisMod;
import com.noctis.mod.mask.MaskItem;
import com.noctis.mod.mask.MaskType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NoctisItems {
    public static Item FLAME_MASK;
    public static Item FROST_MASK;
    public static Item STORM_MASK;
    public static Item BLOOM_MASK;
    public static Item BLOOD_MASK;
    public static Item ROGUE_MASK;
    public static Item RAGE_MASK;
    public static Item HUNTER_MASK;
    public static Item VOID_MASK;
    public static Item SUN_MASK;
    public static Item MOON_MASK;
    public static Item FOG_MASK;
    public static Item GLASS_MASK;

    public static void register() {
        FLAME_MASK = reg("flame_mask", MaskType.FLAME);
        FROST_MASK = reg("frost_mask", MaskType.FROST);
        STORM_MASK = reg("storm_mask", MaskType.STORM);
        BLOOM_MASK = reg("bloom_mask", MaskType.BLOOM);
        BLOOD_MASK = reg("blood_mask", MaskType.BLOOD);
        ROGUE_MASK = reg("rogue_mask", MaskType.ROGUE);
        RAGE_MASK = reg("rage_mask", MaskType.RAGE);
        HUNTER_MASK = reg("hunter_mask", MaskType.HUNTER);
        VOID_MASK = reg("void_mask", MaskType.VOID);
        SUN_MASK = reg("sun_mask", MaskType.SUN);
        MOON_MASK = reg("moon_mask", MaskType.MOON);
        FOG_MASK = reg("fog_mask", MaskType.FOG);
        GLASS_MASK = reg("glass_mask", MaskType.GLASS);
    }

    private static Item reg(String path, MaskType type) {
        Item item = new MaskItem(ArmorMaterials.LEATHER, new FabricItemSettings().maxCount(1), type);
        return Registry.register(Registries.ITEM, new Identifier(NoctisMod.MODID, path), item);
    }
}
