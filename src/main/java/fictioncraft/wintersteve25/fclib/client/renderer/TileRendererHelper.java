package fictioncraft.wintersteve25.fclib.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.fluid.Fluid;

public class TileRendererHelper {
    public static void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        renderer.pos(stack.getLast().getMatrix(), x, y, z)
                .color(r, g, b, a)
                .tex(u, v)
                .lightmap(0, 240)
                .normal(1, 0, 0)
                .endVertex();
    }

    public static int getLight(int initialLight, Fluid fluidIn) {
        int glow = fluidIn.getAttributes().getLuminosity();
        if (glow > 15) {
            return 0xF000F0;
        }

        return MiscHelper.average(initialLight, glow);
    }
}
