package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import net.minecraft.util.SoundCategory;

public class SimpleSoundArg extends SimpleArgProvider {
    private final String sound;
    private SoundCategory soundCategory = SoundCategory.PLAYERS;
    private final float volume;
    private final float pitch;

    public SimpleSoundArg(String sound, float volume, float pitch) {
        super("E_Sound");
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SimpleSoundArg(String sound, SoundCategory soundCategory, float volume, float pitch) {
        super("E_Sound");
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getSound() {
        return sound;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
