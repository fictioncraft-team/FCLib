package fictioncraft.wintersteve25.fclib.rendering.layout;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.rendering.IRenderComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuiLayoutGroup {

    private final List<GuiLayoutGroup> children;
    private final RelativeSize size;
    private final Distances4D margin;
    private final LayoutMode layoutMode;
    private final IRenderComponent renderComponent;
    private final Alignment.VerticalAlignment verticalAlignment;
    private final Alignment.HorizontalAlignment horizontalAlignment;

    private GuiLayoutGroup parent;
    private int xPos;
    private int yPos;

    public GuiLayoutGroup(int initialX, int initialY, List<GuiLayoutGroup> children, RelativeSize size, Distances4D margin, LayoutMode layoutMode, IRenderComponent renderComponent, Alignment.VerticalAlignment verticalAlignment, Alignment.HorizontalAlignment horizontalAlignment) {
        this.children = new ArrayList<>();
        this.size = size;
        this.margin = margin;
        this.layoutMode = layoutMode;
        this.renderComponent = renderComponent;
        this.verticalAlignment = verticalAlignment;
        this.horizontalAlignment = horizontalAlignment;
        this.parent = null;
        this.xPos = initialX + margin.left();
        this.yPos = initialY + margin.up();

        for (GuiLayoutGroup child : children) {
            addChildren(child);
        }
        
        updateChildrenLayout();

        if (renderComponent.getRenderWidth() > size.getWidth() || renderComponent.getRenderHeight() > size.getHeight()) {
            FCLibCore.LOGGER.warn("Tried creating a gui layout group, but given texture is larger than group size");
        }
    }

    public void init(Screen screen) {
        Tuple<Integer, Integer> pos = getPositionBasedOnAlignment();
        renderComponent.init(screen, pos.getA(), pos.getB());
        
        for (GuiLayoutGroup child : children) {
            child.init(screen);
        }
    }
    
    public Collection<AbstractWidget> createWidgets() {
        List<AbstractWidget> widgets = new ArrayList<>(renderComponent.createWidgets());
        
        for (GuiLayoutGroup child : children) {
            widgets.addAll(child.createWidgets());
        }

        return widgets;
    }

    public void addChildren(GuiLayoutGroup child) {
        child.parent = this;
        this.children.add(child);
    }

    public void updateChildrenLayout() {
        if (layoutMode == LayoutMode.HORIZONTAL) {
            int accumulatedX = 0;

            for (GuiLayoutGroup child : children) {
                child.setXPos(accumulatedX);
                accumulatedX += child.getWidth();
            }
        }

        if (layoutMode == LayoutMode.VERTICAL) {
            int accumulatedY = 0;

            for (GuiLayoutGroup child : children) {
                child.setYPos(accumulatedY);
                accumulatedY += child.getHeight();
            }
        }
    }

    public Tuple<Integer, Integer> getPositionBasedOnAlignment() {
        int x = getLeftX();
        int y = getTopY();

        switch (verticalAlignment) {
            case MIDDLE -> y = getMiddleY();
            case BOTTOM -> y = getBottomY();
        }

        switch (horizontalAlignment) {
            case CENTER -> x = getCenterX();
            case RIGHT -> x = getRightX();
        }

        return new Tuple<>(x, y);
    }

    /***
     * @return the x position of the center of the layout group, determined based on layout group size and texture size, margin is included.
     */
    public int getCenterX() {
        return getLeftX() + ((getWidth() - renderComponent.getRenderWidth()) / 2);
    }

    /***
     * @return the y position of the center of the layout group, determined based on layout group size and texture size, margin is included.
     */
    public int getMiddleY() {
        return getTopY() + ((getHeight() - renderComponent.getRenderHeight()) / 2);
    }

    /***
     * @return the x position of the left top corner of the layout group, margin is included.
     */
    public int getLeftX() {
        return xPos;
    }

    /***
     * @return the y position of the left top corner of the layout group, margin is included.
     */
    public int getTopY() {
        return yPos;
    }

    /***
     * @return the x position of the right top corner of the layout group, margin is included.
     */
    public int getRightX() {
        return getLeftX() + (getWidth() - renderComponent.getRenderWidth());
    }

    /***
     * @return the y position of the right top corner of the layout group, margin is included.
     */
    public int getBottomY() {
        return getTopY() + (getHeight() - renderComponent.getRenderHeight());
    }

    public int getWidth() {
        return size.getWidth() + this.margin.left() + this.margin.right();
    }

    public int getHeight() {
        return size.getHeight() + this.margin.up() + this.margin.down();
    }
    
    public void setXPos(int xPos) {
        this.xPos = xPos + this.margin.left();
    }

    public void setYPos(int yPos) {
        this.yPos = yPos + this.margin.up();
    }

    public List<GuiLayoutGroup> getChildren() {
        return children;
    }

    public IRenderComponent getRenderComponent() {
        return renderComponent;
    }

    public static class Builder {
        private final IRenderComponent renderComponent;
        private final List<GuiLayoutGroup> children;

        private RelativeSize size;
        private Distances4D margin;
        private LayoutMode layoutMode;
        private Alignment.VerticalAlignment verticalAlignment;
        private Alignment.HorizontalAlignment horizontalAlignment;
        private int initialX;
        private int initialY;

        public Builder(IRenderComponent renderComponent) {
            this.renderComponent = renderComponent;
            this.children = new ArrayList<>();
            this.size = RelativeSize.fixed(renderComponent.getRenderWidth(), renderComponent.getRenderHeight());
            this.margin = Distances4D.ZERO;
            this.layoutMode = LayoutMode.VERTICAL;
            this.verticalAlignment = Alignment.VerticalAlignment.TOP;
            this.horizontalAlignment = Alignment.HorizontalAlignment.LEFT;
            this.initialX = 0;
            this.initialY = 0;
        }

        public Builder setSize(RelativeSize size) {
            this.size = size;
            return this;
        }

        public Builder setMargin(Distances4D margin) {
            this.margin = margin;
            return this;
        }

        public Builder setLayoutMode(LayoutMode layoutMode) {
            this.layoutMode = layoutMode;
            return this;
        }

        public Builder setVerticalAlignment(Alignment.VerticalAlignment verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
            return this;
        }

        public Builder setHorizontalAlignment(Alignment.HorizontalAlignment horizontalAlignment) {
            this.horizontalAlignment = horizontalAlignment;
            return this;
        }

        public Builder setInitialX(int initialX) {
            this.initialX = initialX;
            return this;
        }

        public Builder setInitialY(int initialY) {
            this.initialY = initialY;
            return this;
        }

        public Builder addChild(GuiLayoutGroup child) {
            this.children.add(child);
            return this;
        }
        
        public Builder addChild(Builder child) {
            return addChild(child.build());
        }

        public GuiLayoutGroup build(int screenWidth, int screenHeight) {
            return new GuiLayoutGroup(
                    initialX,
                    initialY,
                    children,
                    RelativeSize.fixed(screenWidth, screenHeight),
                    margin,
                    layoutMode,
                    renderComponent,
                    verticalAlignment,
                    horizontalAlignment
            );
        }

        public GuiLayoutGroup build() {
            return new GuiLayoutGroup(
                    initialX,
                    initialY,
                    children,
                    size,
                    margin,
                    layoutMode,
                    renderComponent,
                    verticalAlignment,
                    horizontalAlignment
            );
        }
    }
}
