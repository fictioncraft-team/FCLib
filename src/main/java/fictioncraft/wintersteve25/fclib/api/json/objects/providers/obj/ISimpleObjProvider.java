package fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj;

import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;

public interface ISimpleObjProvider {

    String getName();

    boolean isTag();

    /**
     * ObjProviderType is used by {@link JsonSerializer#getTagFromJson(ISimpleObjProvider)}
     */
    ObjProviderType getType();
}
