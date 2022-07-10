package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;

public class SimpleGiveItemArg extends SimpleArgProvider {
    private final SimpleItemProvider item;
    private final int chance;
    private final boolean dropToGround;

    public SimpleGiveItemArg(SimpleItemProvider item, int chance, boolean dropToGround) {
        super("E_ItemStack");
        this.item = item;
        this.chance = chance;
        this.dropToGround = dropToGround;
    }

    public SimpleItemProvider getItem() {
        return item;
    }

    public int getChance() {
        return chance;
    }

    public boolean isDropToGround() {
        return dropToGround;
    }
}
