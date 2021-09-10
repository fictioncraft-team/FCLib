package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleEffectArg extends SimpleArgProvider {
    private final String effectName;
    private final int effectDuration;
    private final int effectLevel;
    private final boolean isCondition;

    public SimpleEffectArg(String effectName, int effectDuration, int effectLevel, boolean isCondition) {
        super("Effects");
        this.effectName = effectName;
        this.effectDuration = effectDuration;
        this.effectLevel = effectLevel;
        this.isCondition = isCondition;
    }

    public String getEffectName() {
        return effectName;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public boolean isCondition() {
        return isCondition;
    }
}
