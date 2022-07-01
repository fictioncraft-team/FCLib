package fictioncraft.wintersteve25.fclib.content.registration.data;

public class FCLibItemDataGenContext {
    private final boolean doModelGen;
    private final boolean doLangGen;

    public FCLibItemDataGenContext(boolean doModelGen, boolean doLangGen) {
        this.doModelGen = doModelGen;
        this.doLangGen = doLangGen;
    }
    
    public FCLibItemDataGenContext() {
        this(true, true);
    }

    public boolean isDoModelGen() {
        return doModelGen;
    }

    public boolean isDoLangGen() {
        return doLangGen;
    }
}
