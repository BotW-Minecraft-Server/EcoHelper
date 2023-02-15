package link.botwmcs.samchai.ecohelper.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import link.botwmcs.samchai.ecohelper.item.TradableItems;
import link.botwmcs.samchai.ecohelper.item.TradableItemsManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TradableItemsProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<ResourceLocation, TradableItems> TRADABLE_ITEMS = new HashMap<>();
    private final DataGenerator dataGenerator;
    private final String modid;
    public static ResourceLocation mcLoc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public TradableItemsProvider(DataGenerator dataGenerator, String modid) {
        this.dataGenerator = dataGenerator;
        this.modid = modid;
    }

    protected void addTradableItems() {
        // Default from config
        String location = EcoHelperConfig.CONFIG.default_balance_unit.get();
        double value = EcoHelperConfig.CONFIG.default_balance_unit_worth.get();
        add(mcLoc(location), value);

    }

    protected void add(ResourceLocation location, double worth) {
        TRADABLE_ITEMS.put(location, new TradableItems(location, worth));
    }

    @Override
    public void run(HashCache pCache) throws IOException {
        addTradableItems();
        Path output = dataGenerator.getOutputFolder();
        for (Map.Entry<ResourceLocation, TradableItems> entry : TRADABLE_ITEMS.entrySet()) {
            Path path = getPath(output, entry.getKey());
            try {
                DataProvider.save(GSON, pCache, TradableItemsManager.parseEconomyItem(entry.getValue()), path);
            } catch (IOException e) {
                EcoHelper.LOGGER.error("Couldn't save tradable item {}", path, e);
            }
        }

    }

    @Override
    public String getName() {
        return "EcoHelper - Tradable Items";
    }

    private static Path getPath(Path output, ResourceLocation loc) {
        return output.resolve("data/" + loc.getNamespace() + "/environment/economy/" + loc.getPath() + ".json");
    }
}
