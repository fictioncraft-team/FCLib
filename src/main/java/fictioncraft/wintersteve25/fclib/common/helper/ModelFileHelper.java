package fictioncraft.wintersteve25.fclib.common.helper;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class ModelFileHelper {
    public static ModelFile createModelFile(ResourceLocation location, boolean exists) {
        return new ModelFile(location) {
            @Override
            protected boolean exists() {
                return exists;
            }
        };
    }

    public static ModelFile createModelFile(ResourceLocation location) {
        return new ModelFile(location) {
            @Override
            protected boolean exists() {
                return true;
            }
        };
    }

    public static ModelFile getTopDifferModel(ResourceLocation baseName, String modelName, BlockModelProvider modelProvider, ResourceLocation parent) {
        return modelProvider.withExistingParent(modelName, parent).texture("particle", baseName).texture("bottom", baseName).texture("top", extend(baseName, "_top")).texture("side", extend(baseName, "_side"));
    }

    public static ModelFile getTopMoistModel(ResourceLocation baseName, String modelName, BlockModelProvider modelProvider, ResourceLocation parent) {
        return modelProvider.withExistingParent(modelName, parent).texture("particle", baseName).texture("bottom", baseName).texture("top", extend(baseName, "_top_moist")).texture("side", extend(baseName, "_side"));
    }

    //taken from BlockStateProvider class
    public static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}
