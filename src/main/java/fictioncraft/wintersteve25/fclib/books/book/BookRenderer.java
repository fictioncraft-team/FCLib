package fictioncraft.wintersteve25.fclib.books.book;

import com.mojang.blaze3d.vertex.PoseStack;
import fictioncraft.wintersteve25.fclib.rendering.components.TextureElementRenderer;
import fictioncraft.wintersteve25.fclib.rendering.components.WidgetRenderer;
import fictioncraft.wintersteve25.fclib.rendering.layout.Distances4D;
import fictioncraft.wintersteve25.fclib.rendering.layout.GuiLayoutGroup;
import fictioncraft.wintersteve25.fclib.rendering.RenderingHelper;
import fictioncraft.wintersteve25.fclib.rendering.layout.RelativeSize;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class BookRenderer extends Screen {

    protected final Book book;
    protected final BookStyle style;

    protected GuiLayoutGroup.Builder guiLayoutBuilder;
    protected GuiLayoutGroup guiLayout;

    protected BookRenderer(Book book) {
        super(TextComponent.EMPTY);
        this.book = book;
        this.style = book.getStyle();
        this.guiLayoutBuilder = new GuiLayoutGroup.Builder(new TextureElementRenderer(style.background()));
    }

    @Override
    protected void init() {
        this.guiLayout = guiLayoutBuilder.build(width, height);
        this.guiLayout.init(this);

        for (AbstractWidget widget : this.guiLayout.createWidgets()) {
            addRenderableWidget(widget);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        if (minecraft == null || guiLayout == null) return;

        if (book.getStyle().drawBG()) {
            this.renderBackground(pPoseStack);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        RenderingHelper.bindTexture(style.background().textureLocation());
        RenderingHelper.renderLayout(this, pMouseX, pMouseY, pPartialTick, guiLayout, pPoseStack);
    }

    @Override
    public boolean isPauseScreen() {
        return book.getStyle().bookPausesGame();
    }

    public static void open(Book book) {
        Minecraft.getInstance().setScreen(null);
        Minecraft.getInstance().setScreen(new BookRenderer(book));
    }
}
