package fictioncraft.wintersteve25.fclib.common.json.objects.providers;

public class SimpleItemProvider extends SimpleObjProvider {
    private final int amount;
    private final String nbt;

    public SimpleItemProvider(String name, int amount, String nbt, boolean isTag) {
        super(name, isTag, ProviderType.ITEM);
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
