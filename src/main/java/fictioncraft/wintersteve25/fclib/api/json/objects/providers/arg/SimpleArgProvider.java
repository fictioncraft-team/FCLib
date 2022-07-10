package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(SimpleArgProviderJsonAdapter.class)
public class SimpleArgProvider implements ISimpleArgProvider{
    private final String argumentType;

    public SimpleArgProvider(String argumentType) {
        this.argumentType = argumentType;
    }

    @Override
    public String getTypeName() {
        return argumentType;
    }

    @Override
    public ArgProviderType getType() {
        return ArgProviderType.getFromName(getTypeName());
    }
}
