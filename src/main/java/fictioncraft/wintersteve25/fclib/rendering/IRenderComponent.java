package fictioncraft.wintersteve25.fclib.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;

public interface IRenderComponent {
    void render(PoseStack poseStack, GuiComponent guiComponent, @Nonnull Minecraft mc, int mouseX, int mouseY, int renderX, int renderY, float partialTicks);

    int getRenderWidth();
    
    int getRenderHeight();
    
    default void init(Screen screen, int renderX, int renderY) {
    }
    
    default Collection<AbstractWidget> createWidgets() {
        return new ArrayList<>();
    }
}
