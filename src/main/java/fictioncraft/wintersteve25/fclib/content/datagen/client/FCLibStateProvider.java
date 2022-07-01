package fictioncraft.wintersteve25.fclib.content.datagen.client;

import fictioncraft.wintersteve25.fclib.content.base.blocks.FClibDirectionalBlock;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockRegistryObject;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibBlockDataGenContext;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibDirectionalBlockDataGenContext;
import fictioncraft.wintersteve25.fclib.utils.LangHelper;
import fictioncraft.wintersteve25.fclib.utils.ModelFileHelper;
import fictioncraft.wintersteve25.fclib.utils.ResoureceLocationHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FCLibStateProvider extends BlockStateProvider {
    private final FCLibBlockDeferredRegister register;

    public FCLibStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper, FCLibBlockDeferredRegister register) {
        super(gen, modid, exFileHelper);
        this.register = register;
    }

    @Override
    protected void registerStatesAndModels() {
        autoGenStatesAndModels();
    }

    private void autoGenStatesAndModels() {
        var objects = register.getAllRegisteredObjects();
        
        for (FCLibBlockRegistryObject<? extends Block, ? extends Item> b : objects.keySet()) {
            FCLibBlockDataGenContext data = objects.get(b);
            if (data.isDoStateGen()) {
                if (b.asBlock() instanceof FClibDirectionalBlock directional && data instanceof FCLibDirectionalBlockDataGenContext directionalData) {
                    directionalBlock(directional, directionalData.getModelFile(), directionalData.getAngleOffset());
                } else {
                    simpleBlock(b.asBlock());
                }
            }
        }
    }

    protected void weightedBlock(FCLibBlockRegistryObject<?, ?> block, int amoutOfAlts, ResourceLocation rootLocation) {
        String name = LangHelper.langToReg(block.getName());
        weightedState(block.asBlock(), name, ResoureceLocationHelper.extend(rootLocation, name), amoutOfAlts);
    }

    protected void weightedState(Block block, String modelBaseName, ResourceLocation textureBaseLocation, int amountOfAlts) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    ConfiguredModel.Builder<?> builder = ConfiguredModel.builder()
                            .modelFile(ModelFileHelper.cubeAll(modelBaseName, textureBaseLocation, models()))
                            .weight(100 / amountOfAlts);

                    for (int i = 0; i < amountOfAlts; i++) {
                        String name = "_alt" + amountOfAlts;
                        
                        builder.nextModel()
                                .modelFile(ModelFileHelper.cubeAll(modelBaseName + name, ResoureceLocationHelper.extend(textureBaseLocation, name), models()))
                                .weight(100 / amountOfAlts);
                    }

                    return builder.build();
                });
    }
}
