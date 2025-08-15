package com.noctis.mod;

import com.noctis.mod.mask.MaskEffects;
import com.noctis.mod.registry.NoctisItems;
import com.noctis.mod.client.NoctisPackets;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoctisMod implements ModInitializer {
    public static final String MODID = "noctis";
    public static final Logger LOG = LoggerFactory.getLogger("Noctis");

    @Override
    public void onInitialize() {
        NoctisItems.register();
        MaskEffects.register();
        NoctisPackets.registerC2SPackets();
        LOG.info("Noctis initialized for 1.21.4");
    }
}
