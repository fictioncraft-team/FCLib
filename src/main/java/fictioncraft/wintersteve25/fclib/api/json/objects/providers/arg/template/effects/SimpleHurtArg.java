package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleHurtArg extends SimpleArgProvider {
    private final boolean isPlayer;
    private final float damage;
    private final boolean byPassAmour;
    private final boolean countPlayerKill;

    public SimpleHurtArg(boolean isPlayer, float damage, boolean byPassAmour, boolean countPlayerKill) {
        super("E_Hurt");
        this.isPlayer = isPlayer;
        this.damage = damage;
        this.byPassAmour = byPassAmour;
        this.countPlayerKill = countPlayerKill;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isByPassAmour() {
        return byPassAmour;
    }

    public boolean isCountPlayerKill() {
        return countPlayerKill;
    }
}
