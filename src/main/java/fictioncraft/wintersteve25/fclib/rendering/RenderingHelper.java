package fictioncraft.wintersteve25.fclib.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fictioncraft.wintersteve25.fclib.rendering.layout.GuiLayoutGroup;
import fictioncraft.wintersteve25.fclib.utils.TextureElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

public class RenderingHelper {
    public static void bindTexture(ResourceLocation resourceLocation) {
        RenderSystem.setShaderTexture(0, resourceLocation);
    }

    public static void renderRenderComponent(GuiComponent gui, int x, int y, int mouseX, int mouseY, float partialTicks, IRenderComponent component, PoseStack poseStack) {
        component.render(poseStack, gui, Minecraft.getInstance(), mouseX, mouseY, x, y, partialTicks);
    }
    
    public static void renderWidget(GuiComponent gui, TextureElement textureElement, PoseStack matrixStack, int x, int y) {
        gui.blit(matrixStack, x, y, textureElement.u(), textureElement.v(), textureElement.width(), textureElement.height());
    }
    
    public static void renderLayout(GuiComponent gui, int mouseX, int mouseY, float partialTicks, GuiLayoutGroup layoutGroup, PoseStack poseStack) {
        Tuple<Integer, Integer> pos = layoutGroup.getPositionBasedOnAlignment();
        renderRenderComponent(gui, pos.getA(), pos.getB(), mouseX, mouseY, partialTicks, layoutGroup.getRenderComponent(), poseStack);
        
        for (GuiLayoutGroup child : layoutGroup.getChildren()) {
            renderLayout(gui, mouseX, mouseY, partialTicks, child, poseStack);    
        }
    }
}
