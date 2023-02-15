package link.botwmcs.samchai.ecohelper.item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.core.Registry;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TradableItemsManager extends SimpleJsonResourceReloadListener {
    private static final Map<ResourceLocation, TradableItems> ITEMS = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(TradableItems.class, new TradableItems.Serializer()).create();
    private static TradableItemsManager INSTANCE;

    public TradableItemsManager() {
        super(GSON, "environment/economy");
    }

    public static JsonElement parseEconomyItem(TradableItems economyItem) {
        return GSON.toJsonTree(economyItem);
    }

    public static TradableItems get(ItemStack itemStack) {
        ResourceLocation location = Registry.ITEM.getKey(itemStack.getItem());
        TradableItems economyItem = ITEMS.get(location);

        if (economyItem != null) {
            return economyItem;
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ITEMS.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            try {
                TradableItems economyItem = GSON.fromJson(entry.getValue(), TradableItems.class);
                ITEMS.put(economyItem.loc(), economyItem);
            } catch (Exception e) {
                EcoHelper.LOGGER.error("Couldn't parse tradable item %s %s", entry.getKey(), e);
            }
        }

        EcoHelper.LOGGER.info("Loaded {} tradable items", ITEMS.size());
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        EcoHelper.LOGGER.info("Datapack enabled!");
        event.addListener(INSTANCE = new TradableItemsManager());
    }


}
