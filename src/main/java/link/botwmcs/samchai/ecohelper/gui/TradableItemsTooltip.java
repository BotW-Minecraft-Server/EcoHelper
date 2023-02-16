package link.botwmcs.samchai.ecohelper.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Matrix4f;
import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.item.TradableItems;
import link.botwmcs.samchai.ecohelper.item.TradableItemsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EcoHelper.MODID)
public class TradableItemsTooltip {
    @SubscribeEvent
    public static void onRenderTooltipEvent(RenderTooltipEvent.GatherComponents event) {
        int x = 1;
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack itemStack = event.getItemStack();
        if (minecraft.level != null) {
            TradableItems item = TradableItemsManager.get(itemStack);
            if (item != null) {
                event.getTooltipElements().add(x, Either.right(new WorthComponent(itemStack)));
            }
        }
    }
    public final static ResourceLocation MONEY_ICON = new ResourceLocation(EcoHelper.MODID, "textures/gui/gold_coin.png");

    public record WorthComponent(ItemStack stack) implements ClientTooltipComponent, TooltipComponent {
        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @Nonnull PoseStack pose, @NotNull ItemRenderer itemRenderer, int something) {
            pose.pushPose();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, MONEY_ICON);

            pose.translate(tooltipX - 1, tooltipY - 1, 0);
//            pose.scale(0.5f, 0.5f, 1f);
            RenderSystem.enableBlend();
            GuiComponent.blit(pose, 0, 0, 0, 0, 8, 8, 8, 8);

            font.draw(pose, "100", tooltipX + 12, tooltipY, -1);
            pose.popPose();

        }
        @Override
        public void renderText(@NotNull Font font, int tooltipX, int tooltipY, @NotNull Matrix4f matrix4f, MultiBufferSource.@NotNull BufferSource bufferSource) {
            Minecraft mc = Minecraft.getInstance();
            double worth = TradableItemsManager.get(stack).worth() * stack.getCount();
            String text = "x" + worth;
            Color textColor = Color.decode("#A8A8A8");
            mc.font.drawInBatch(text, tooltipX + 10, tooltipY - 1, textColor.getRGB(), true, matrix4f, bufferSource, false, 0, 15728880);
        }

        @Override
        public int getHeight() {
            return 8;
        }
        @Override
        public int getWidth(@NotNull Font font) {
            return 8;
        }
    }




//
//    public static class TradableItemsComponent implements ClientTooltipComponent, TooltipComponent {
//        protected final ItemStack itemStack;
//        protected int width;
//        protected int height;
//        private static int tick = 0;
//
//
//        public TradableItemsComponent(ItemStack itemStack) {
//            this.itemStack = itemStack;
//            this.height = 9;
//            this.width = 9;
//        }
//
//        @Override
//        public int getHeight() {
//            return height;
//        }
//
//        @Override
//        public int getWidth(Font pFont) {
//            return width;
//        }
//
//
//        @Override
//        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull PoseStack poseStack, @NotNull ItemRenderer itemRenderer, int sth) {
//            tick++;
//            tick %= 10000;
//            TradableItems item = TradableItemsManager.get(itemStack);
//            if (item != null) {
//                poseStack.pushPose();
//                RenderSystem.setShader(GameRenderer::getPositionTexShader);
//                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                RenderSystem.setShaderTexture(0, MONEY_ICON);
//                RenderSystem.enableBlend();
//                GuiComponent.blit(poseStack, tooltipX, tooltipY, 36, 0, 9, 9, 9, 9);
//                poseStack.popPose();
//            }
//        }
//
//
//    }
}
