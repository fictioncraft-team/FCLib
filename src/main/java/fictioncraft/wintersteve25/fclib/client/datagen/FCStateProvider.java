package fictioncraft.wintersteve25.fclib.client.datagen;

import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModelFileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class FCStateProvider extends BlockStateProvider {
    public FCStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    /**
     * Particle: baseName
     * Bottom: baseName
     * Top: baseName_top
     * Side: baseName_side
     *
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
     *
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
}
