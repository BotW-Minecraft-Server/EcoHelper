package link.botwmcs.samchai.ecohelper.recipe;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EcoHelper.MODID);
    public static final RegistryObject<RecipeSerializer<TradableItemRecipe>> TRADABLE_ITEM_SERIALIZER = SERIALIZER.register("tradable_item", () -> TradableItemRecipe.Serializer.INSTANCE);
    public static void register(IEventBus bus) {
        SERIALIZER.register(bus);
    }
}
