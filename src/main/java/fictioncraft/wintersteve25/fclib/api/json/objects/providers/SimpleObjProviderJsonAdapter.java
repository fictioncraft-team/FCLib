package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.RuntimeTypeAdapterFactory;
import fictioncraft.wintersteve25.fclib.api.json.objects.ProviderType;

import java.util.List;

public class SimpleObjProviderJsonAdapter extends RuntimeTypeAdapterFactory<SimpleObjProvider> {
    public SimpleObjProviderJsonAdapter() {
        super(SimpleObjProvider.class, "type", true);
        List<ProviderType> list = FCLibMod.PROVIDER_TYPES;
        for (ProviderType types : FCLibMod.PROVIDER_TYPES) {
            if (types.getProviderClass() != SimpleObjProvider.class) {
                registerSubtype(types.getProviderClass(), types.getID());
            }
        }
    }
}
