package fictioncraft.wintersteve25.fclib.content.registration.data;

import net.minecraftforge.client.model.generators.ModelFile;

public class FCLibDirectionalBlockDataGenContext extends FCLibBlockDataGenContext {
    private final int angleOffset;
    private final ModelFile modelFile;

    public FCLibDirectionalBlockDataGenContext(boolean doStateGen, boolean doModelGen, boolean doLangGen, boolean doLootTableGen, int angleOffset, ModelFile modelFile) {
        super(doStateGen, doModelGen, doLangGen, doLootTableGen);
        this.angleOffset = angleOffset;
        this.modelFile = modelFile;
    }

    public FCLibDirectionalBlockDataGenContext(int angleOffset, ModelFile modelFile) {
        this(false, true, true, true, angleOffset, modelFile);
    }

    public FCLibDirectionalBlockDataGenContext() {
        this.angleOffset = 0;
        this.modelFile = null;
    }

    public int getAngleOffset() {
        return angleOffset;
    }

    public ModelFile getModelFile() {
        return modelFile;
    }
}
