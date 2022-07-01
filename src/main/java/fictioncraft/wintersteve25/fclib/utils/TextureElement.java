package fictioncraft.wintersteve25.fclib.utils;

import net.minecraft.resources.ResourceLocation;

public record TextureElement(int U, int V, int width, int height, ResourceLocation textureLocation) {
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getU() {
        return U;
    }

    public int getV() {
        return V;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }
}
