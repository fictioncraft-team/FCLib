package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleHungerArg extends SimpleArgProvider {
    private final float amount;
    private final boolean exhaust;

    public SimpleHungerArg(float amount, boolean exhaust) {
        super("E_Hunger");
        this.amount = amount;
        this.exhaust = exhaust;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isExhaust() {
        return exhaust;
    }
}
