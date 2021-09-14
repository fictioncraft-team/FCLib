package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleShrinkArg extends SimpleArgProvider {
    private final int amount;

    public SimpleShrinkArg(int amount) {
        super("E_Shrink");
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
