package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.RuntimeTypeAdapterFactory;
import fictioncraft.wintersteve25.fclib.api.json.objects.ProviderType;

public class SimpleObjProviderJsonAdapter extends RuntimeTypeAdapterFactory<SimpleObjProvider> {

    public SimpleObjProviderJsonAdapter() {
        super(SimpleObjProvider.class, "type", true);

        boolean isDefaultRegistered = false;

        for (ProviderType types : FCLibMod.PROVIDER_TYPES) {
            Class<? extends SimpleObjProvider> providerClass = types.getProviderClass();
            if (providerClass != SimpleObjProvider.class) {
                this.registerSubtype(providerClass, types.getID());
            } else if (!isDefaultRegistered) {
                this.registerSubtype(providerClass, "Default");
                isDefaultRegistered = true;
            }
        }
    }
}