package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleParticleArg extends SimpleArgProvider {
    private final String particle;
    private final int amount;
    private final double speed;
    private final double xMod;
    private final double yMod;
    private final double zMod;

    public SimpleParticleArg(String particle, int amount, double speedX, double xMod, double yMod, double zMod) {
        super("E_Particle");
        this.particle = particle;
        this.amount = amount;
        this.speed = speedX;
        this.xMod = xMod;
        this.yMod = yMod;
        this.zMod = zMod;
    }

    public String getParticle() {
        return particle;
    }

    public int getAmount() {
        return amount;
    }

    public double getSpeed() {
        return speed;
    }

    public double getxMod() {
        return xMod;
    }

    public double getyMod() {
        return yMod;
    }

    public double getzMod() {
        return zMod;
    }
}
