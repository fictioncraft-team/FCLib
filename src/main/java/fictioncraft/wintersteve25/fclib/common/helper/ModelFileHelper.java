package fictioncraft.wintersteve25.fclib.common.helper;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.IExtensibleEnum;

import static fictioncraft.wintersteve25.fclib.common.helper.ResourceLocationHelper.extend;

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
        return createModelFile(location, true);
    }

    public static ModelFile getTopDifferModel(ResourceLocation baseName, String modelName, BlockModelProvider modelProvider, ResourceLocation parent) {
        return modelProvider.withExistingParent(modelName, parent).texture("particle", baseName).texture("bottom", baseName).texture("top", extend(baseName, "_top")).texture("side", extend(baseName, "_side"));
    }

    public static ModelFile getTopMoistModel(ResourceLocation baseName, String modelName, BlockModelProvider modelProvider, ResourceLocation parent) {
        return modelProvider.withExistingParent(modelName, parent).texture("particle", baseName).texture("bottom", baseName).texture("top", extend(baseName, "_top_moist")).texture("side", extend(baseName, "_side"));
    }

    public static ModelFile cubeAll(String modelName, ResourceLocation textureLoc, BlockModelProvider modelProvider) {
        return modelProvider.withExistingParent(modelName, "block/cube_all").texture("all", textureLoc);
    }

    public static ModelFile cubeAll(String modelName, String modId, BlockModelProvider modelProvider) {
        return modelProvider.withExistingParent(modelName, "block/cube_all").texture("all", new ResourceLocation(modId, modelName));
    }
}
