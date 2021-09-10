package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.RuntimeTypeAdapterFactory;

public class SimpleArgProviderJsonAdapter extends RuntimeTypeAdapterFactory<SimpleArgProvider> {

    public SimpleArgProviderJsonAdapter() {
        super(SimpleArgProvider.class, "argumentType", true);

        boolean isDefaultRegistered = false;

        for (ArgProviderType types : FCLibMod.ARG_PROVIDER_TYPES) {
            if (types.canArgumentBeLoaded()) {
                Class<? extends SimpleArgProvider> providerClass = types.getArgProviderClass();
                if (providerClass != SimpleArgProvider.class) {
                    this.registerSubtype(providerClass, types.getID());
                } else if (!isDefaultRegistered) {
                    this.registerSubtype(providerClass, "Default");
                    isDefaultRegistered = true;
                }
            }
        }
    }
}