package fictioncraft.wintersteve25.fclib.common.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FCLibGuiContainer<T extends FCLibContainer> extends ContainerScreen<T> {
    private static ResourceLocation bg = null;

    public FCLibGuiContainer(T container, PlayerInventory inv, ITextComponent name, ResourceLocation resourceLocation) {
        super(container, inv, name);

        bg = resourceLocation;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        if (bg != null) {
            RenderSystem.color4f(1F, 1F, 1F, 1F);
            this.minecraft.getTextureManager().bindTexture(bg);

            int j = (this.height - this.getYSize()) / 2;

            this.blit(matrixStack, this.guiLeft, j, 0, 0, this.getXSize(), this.getYSize() + 5);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        if (shouldKeepInventoryText()) {
            super.drawGuiContainerForegroundLayer(matrixStack, x, y);
        } else {
            this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
        }
    }

    protected void renderCustomToolTip(MatrixStack matrixStack, int mouseX, int mouseY, int i, int j, int cord1, int cord1Y, int cord2, int cord2Y, ITextComponent text) {
        if (mouseX > i + cord1 && mouseY > j + cord1Y && mouseX < i + cord2 && mouseY < j + cord2Y) {
            renderTooltip(matrixStack, text, mouseX, mouseY);
        }
    }

    protected int getScaledProgress(float pixels) {
        float totalProgress = container.getTotalProgress();
        float progress = totalProgress - container.getProgress();

        if (totalProgress != 0) {
            float result = progress*pixels/totalProgress;
            return Math.round(result);
        }

        return 0;
    }

    protected boolean shouldKeepInventoryText() {
        return false;
    }
}
