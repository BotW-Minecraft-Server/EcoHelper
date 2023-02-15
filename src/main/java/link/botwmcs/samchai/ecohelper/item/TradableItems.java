package link.botwmcs.samchai.ecohelper.item;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import java.lang.reflect.Type;

public record TradableItems(ResourceLocation loc, double worth) {

    public static Worth getWorth(TradableItems item) {
        return new Worth(item.worth());
    }

    public static class Serializer implements JsonDeserializer<TradableItems>, JsonSerializer<TradableItems> {

        @Override
        public TradableItems deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "data");

            return new TradableItems(
                    new ResourceLocation(json.get("type").getAsString()),
                    json.get("worth").getAsDouble()
            );
        }

        @Override
        public JsonElement serialize(TradableItems src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("type", src.loc().toString());
            json.addProperty("worth", src.worth());
            return json;
        }
    }
}
