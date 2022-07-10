package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;

public class SimpleTransformArg extends SimpleArgProvider {
    private final SimpleEntityProvider entityOutput;
    private final boolean allowPeaceful;

    public SimpleTransformArg(SimpleEntityProvider entityOutput, boolean allowPeaceful) {
        super("E_Transform");
        this.entityOutput = entityOutput;
        this.allowPeaceful = allowPeaceful;
    }

    public SimpleEntityProvider getProvider() {
        return entityOutput;
    }

    public boolean isAllowPeaceful() {
        return allowPeaceful;
    }
}
