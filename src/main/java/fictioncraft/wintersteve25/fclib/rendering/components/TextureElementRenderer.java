package fictioncraft.wintersteve25.fclib.rendering.components;

import com.mojang.blaze3d.vertex.PoseStack;
import fictioncraft.wintersteve25.fclib.rendering.IRenderComponent;
import fictioncraft.wintersteve25.fclib.rendering.RenderingHelper;
import fictioncraft.wintersteve25.fclib.utils.TextureElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;

public record TextureElementRenderer(TextureElement textureElement) implements IRenderComponent {
    @Override
    public void render(PoseStack poseStack, GuiComponent guiComponent, @NotNull Minecraft mc, int mouseX, int mouseY, int renderX, int renderY, float partialTicks) {
        RenderingHelper.renderWidget(guiComponent, textureElement, poseStack, renderX, renderY);
    }

    @Override
    public int getRenderWidth() {
        return textureElement.width();
    }

    @Override
    public int getRenderHeight() {
        return textureElement.height();
    }
}
