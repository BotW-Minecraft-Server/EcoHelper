package link.botwmcs.samchai.ecohelper.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TradableItemRecipe implements Recipe<Inventory> {
    protected final double worth;
    protected final ResourceLocation id;
    protected Ingredient ingredient;

    public TradableItemRecipe(ResourceLocation id, Ingredient ingredient, double worth) {
        this.id = id;
        this.ingredient = ingredient;
        this.worth = worth;
    }

    public double getWorth() {
        return worth;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(Inventory pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Inventory pContainer) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TradableItemRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "tradable_item";
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<TradableItemRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(EcoHelper.MODID, "tradable_item");

        @Override
        public TradableItemRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            double worth = 0;
            // Ingredient
            Ingredient ingredient = Ingredient.EMPTY;
            if (GsonHelper.isValidNode(pSerializedRecipe, "ingredient")) {
                JsonElement jsonElement = GsonHelper.isArrayNode(pSerializedRecipe, "ingredient") ? GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredient") : GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient");
                ingredient = Ingredient.fromJson(jsonElement);
            }
            // Worth
            worth = GsonHelper.getAsDouble(pSerializedRecipe, "worth", 0);

            return new TradableItemRecipe(pRecipeId, ingredient, worth);
        }

        @Override
        public TradableItemRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            double worth = pBuffer.readDouble();

            return new TradableItemRecipe(pRecipeId, ingredient, worth);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, TradableItemRecipe pRecipe) {
            pRecipe.getIngredient().toNetwork(pBuffer);
            pBuffer.writeDouble(pRecipe.getWorth());
        }

    }

    public static TradableItemRecipe getRecipeFromItem(Level level, ItemStack itemStack) {
        List<TradableItemRecipe> recipeList = new ArrayList<>();
        for (TradableItemRecipe recipe : level.getRecipeManager().getAllRecipesFor(Type.INSTANCE)) {
            if (recipe.getIngredient().test(itemStack)) {
                recipeList.add(recipe);
                if (recipe.getWorth() != 0) {
                    return recipe;
                }
            }
        }
        return null;
    }

}
