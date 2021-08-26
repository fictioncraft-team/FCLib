package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import fictioncraft.wintersteve25.fclib.api.json.objects.ProviderType;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;

public interface ISimpleObjProvider {

    String getName();

    boolean isTag();

    /**
     * ProviderType is used by {@link JsonSerializer#getTagFromJson(ISimpleObjProvider)}
     */
    ProviderType getType();
}
