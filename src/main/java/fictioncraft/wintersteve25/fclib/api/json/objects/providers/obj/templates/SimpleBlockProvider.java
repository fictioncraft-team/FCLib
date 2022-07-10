package fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;

public class SimpleBlockProvider extends SimpleObjProvider {

    private final boolean hasTE;
    private final String nbt;

    public SimpleBlockProvider(String name, boolean hasTE, String nbt, boolean isTag) {
        super(name, isTag, "Block");
        this.hasTE = hasTE;
        this.nbt = nbt;
    }

    @Override
    public boolean isTag() {
        if (hasTE()) {
            return false;
        }
        return super.isTag();
    }

    public boolean hasTE() {
        return hasTE;
    }

    public String getNbt() {
        return nbt;
    }
}
