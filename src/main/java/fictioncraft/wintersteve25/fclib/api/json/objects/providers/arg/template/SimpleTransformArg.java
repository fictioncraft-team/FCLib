package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;

public class SimpleTransformArg extends SimpleArgProvider {
    private final SimpleEntityProvider provider;
    private final boolean allowPeaceful;

    public SimpleTransformArg(SimpleEntityProvider provider, boolean allowPeaceful) {
        super("Transform");
        this.provider = provider;
        this.allowPeaceful = allowPeaceful;
    }

    public SimpleEntityProvider getProvider() {
        return provider;
    }

    public boolean isAllowPeaceful() {
        return allowPeaceful;
    }
}
