package fictioncraft.wintersteve25.fclib.rendering.components;

import com.mojang.blaze3d.vertex.PoseStack;
import fictioncraft.wintersteve25.fclib.rendering.IRenderComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public record WidgetRenderer(AbstractWidget widget) implements IRenderComponent {
    @Override
    public void render(PoseStack poseStack, GuiComponent guiComponent, @NotNull Minecraft mc, int mouseX, int mouseY, int renderX, int renderY, float partialTicks) {
    }

    @Override
    public int getRenderWidth() {
        return widget.getWidth();
    }

    @Override
    public int getRenderHeight() {
        return widget.getHeight();
    }

    @Override
    public void init(Screen screen, int renderX, int renderY) {
        widget.x = renderX;
        widget.y = renderY;
    }

    @Override
    public Collection<AbstractWidget> createWidgets() {
        return List.of(widget);
    }
}
