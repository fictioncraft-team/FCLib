package fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;

public class SimpleItemProvider extends SimpleObjProvider {
    private final int amount;
    private final String nbt;

    public SimpleItemProvider(String name, int amount, String nbt, boolean isTag) {
        super(name, isTag, "Item");
        this.amount = amount;
        this.nbt = nbt;
    }

    public int getAmount() {
        return amount;
    }

    public String getNBT() {
        return nbt;
    }
}
