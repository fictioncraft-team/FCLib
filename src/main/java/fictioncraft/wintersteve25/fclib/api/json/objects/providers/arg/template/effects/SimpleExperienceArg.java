package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleExperienceArg extends SimpleArgProvider {
    private final int amount;
    private final boolean level;

    public SimpleExperienceArg(int amount, boolean level) {
        super("E_XP");
        this.amount = amount;
        this.level = level;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isLevel() {
        return level;
    }
}
