package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;

public class SimpleSummonArg extends SimpleArgProvider {
    private final SimpleEntityProvider entityToSummon;

    public SimpleSummonArg(SimpleEntityProvider entityToSummon) {
        super("E_Summon");
        this.entityToSummon = entityToSummon;
    }

    public SimpleEntityProvider getEntityToSummon() {
        return entityToSummon;
    }
}
