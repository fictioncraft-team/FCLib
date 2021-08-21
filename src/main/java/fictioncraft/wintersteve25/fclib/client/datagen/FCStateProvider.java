package fictioncraft.wintersteve25.fclib.client.datagen;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.common.helper.ResourceLocationHelper;
import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModelFileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public abstract class FCStateProvider extends BlockStateProvider {
    public FCStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    /**
     * Particle: baseName
     * Bottom: baseName
     * Top: baseName_top
     * Side: baseName_side
     * <p>
     * Parent: side top bottom model as parent
     */
    public void topDifferBlock(Block block, IFCDataGenObject fcObject, ResourceLocation baseName, ResourceLocation parent) {
        simpleBlock(block, ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent));
    }

    /**
     * Particle: baseName
     * Bottom: baseName
     * Top: baseName_top
     * Top Moist: baseName_top_moist
     * Side: baseName_side
     * <p>
     * Parent: side top bottom model as parent
     */
    public void farmBlock(Block block, IFCDataGenObject fcObject, ResourceLocation baseName, ResourceLocation parent) {
        getVariantBuilder(block)
                .partialState().with(FarmlandBlock.MOISTURE, 0)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 1)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 2)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 3)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 4)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 5)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 6)
                .modelForState().modelFile(ModelFileHelper.getTopDifferModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel()
                .partialState().with(FarmlandBlock.MOISTURE, 7)
                .modelForState().modelFile(ModelFileHelper.getTopMoistModel(baseName, MiscHelper.langToReg(fcObject.regName()), models(), parent)).addModel();
    }

    /**
     * @param block               BlockIn
     * @param modelBaseName       Base Model name
     * @param textureBaseLocation base location, alts will be baseName_alt, baseName_alt2, etc
     * @param amountOfAlts        max 10
     */
    public void weightedState(Block block, String modelBaseName, ResourceLocation textureBaseLocation, int amountOfAlts) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    switch (amountOfAlts) {
                        case 1:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).build();
                        case 2:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).build();
                        case 3:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).build();
                        case 4:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).build();
                        case 5:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).build();
                        case 6:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResourceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).build();
                        case 7:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResourceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResourceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).build();
                        case 8:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResourceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResourceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResourceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).build();
                        case 9:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResourceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResourceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResourceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResourceLocationHelper.extend(textureBaseLocation, "_alt9"), models())).weight((int) 100 / amountOfAlts).build();
                        case 10:
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt", ResourceLocationHelper.extend(textureBaseLocation, "_alt"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt2", ResourceLocationHelper.extend(textureBaseLocation, "_alt2"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt3", ResourceLocationHelper.extend(textureBaseLocation, "_alt3"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt4", ResourceLocationHelper.extend(textureBaseLocation, "_alt4"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt5", ResourceLocationHelper.extend(textureBaseLocation, "_alt5"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt6", ResourceLocationHelper.extend(textureBaseLocation, "_alt6"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt7", ResourceLocationHelper.extend(textureBaseLocation, "_alt7"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt8", ResourceLocationHelper.extend(textureBaseLocation, "_alt8"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResourceLocationHelper.extend(textureBaseLocation, "_alt9"), models())).weight((int) 100 / amountOfAlts).nextModel()
                                    .modelFile(ModelFileHelper.cubeAll(modelBaseName + "_alt9", ResourceLocationHelper.extend(textureBaseLocation, "_alt10"), models())).weight((int) 100 / amountOfAlts).build();
                        default:
                            LogManager.getLogger("FCLib").warn("Tried to create weighted state out of supported range");
                            return ConfiguredModel.builder().modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models())).weight((int) 100 / amountOfAlts).build();
                    }
                });
    }
}
