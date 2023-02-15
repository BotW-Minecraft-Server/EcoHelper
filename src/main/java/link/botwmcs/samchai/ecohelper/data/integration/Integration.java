package link.botwmcs.samchai.ecohelper.data.integration;

import net.minecraft.resources.ResourceLocation;

public class Integration {

    public static final String MC_MODID = "minecraft";

    public static ResourceLocation mcLoc(String path) {
        return new ResourceLocation(MC_MODID, path);
    }
}
