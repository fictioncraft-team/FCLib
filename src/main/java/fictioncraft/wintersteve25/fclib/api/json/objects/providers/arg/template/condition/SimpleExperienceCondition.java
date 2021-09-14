package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleExperienceCondition extends SimpleArgProvider {
    private final float amount;
    private final boolean level;
    private final boolean below;

    public SimpleExperienceCondition(float amount, boolean level, boolean below) {
        super("C_XP");
        this.amount = amount;
        this.level = level;
        this.below = below;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isLevel() {
        return level;
    }

    public boolean isBelow() {
        return below;
    }
}
