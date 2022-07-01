package fictioncraft.wintersteve25.fclib.content.registration.data;

public class FCLibBlockDataGenContext extends FCLibItemDataGenContext {
    private final boolean doStateGen;
    private final boolean doLootTableGen;

    public FCLibBlockDataGenContext(boolean doStateGen, boolean doModelGen, boolean doLangGen, boolean doLootTableGen) {
        super(doModelGen, doLangGen);
        this.doStateGen = doStateGen;
        this.doLootTableGen = doLootTableGen;
    }

    public FCLibBlockDataGenContext() {
        this(false, true, true, true);
    }

    public boolean isDoStateGen() {
        return doStateGen;
    }

    public boolean isDoLootTableGen() {
        return doLootTableGen;
    }
}
