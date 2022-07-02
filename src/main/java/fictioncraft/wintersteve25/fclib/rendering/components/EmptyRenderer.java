package fictioncraft.wintersteve25.fclib.rendering.components;

import com.mojang.blaze3d.vertex.PoseStack;
import fictioncraft.wintersteve25.fclib.rendering.IRenderComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;

public record EmptyRenderer() implements IRenderComponent {
    @Override
    public void render(PoseStack poseStack, GuiComponent guiComponent, @NotNull Minecraft mc, int mouseX, int mouseY, int renderX, int renderY, float partialTicks) {
    }

    @Override
    public int getRenderWidth() {
        return 0;
    }

    @Override
    public int getRenderHeight() {
        return 0;
    }
}
