package link.botwmcs.samchai.ecohelper.item;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TagManager {
    public static final class Items {
        public static final TagKey<Item> WORTH = create("worth");
    }

    private static TagKey<Item> create(String id) {
        return ItemTags.create(identifier(id));
    }

    public static ResourceLocation identifier(String path) {
        return EcoHelper.resourceLocation(path);
    }
}
