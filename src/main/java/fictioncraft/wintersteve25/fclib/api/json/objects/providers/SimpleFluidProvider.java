package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

public class SimpleFluidProvider extends SimpleObjProvider {
    private final int amount;
    private final String nbt;

    public SimpleFluidProvider(String name, int amount, String nbt, boolean isTag) {
        super(name, isTag, "Fluid");
        this.amount = amount;
        this.nbt = nbt;
    }

    public int getAmount() {
        return amount;
    }

    public String getNbt() {
        return nbt;
    }
}
