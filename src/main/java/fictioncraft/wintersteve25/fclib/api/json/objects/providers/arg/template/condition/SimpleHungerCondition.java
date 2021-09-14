package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleHungerCondition extends SimpleArgProvider {
    private final float amount;
    private final boolean saturation;
    private final boolean below;

    public SimpleHungerCondition(float amount, boolean saturation, boolean below) {
        super("C_Hunger");
        this.amount = amount;
        this.saturation = saturation;
        this.below = below;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isSaturation() {
        return saturation;
    }

    public boolean isBelow() {
        return below;
    }
}
