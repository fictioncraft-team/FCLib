package fictioncraft.wintersteve25.fclib.content.datagen.client;

import fictioncraft.wintersteve25.fclib.content.registration.FCLibDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockRegistryObject;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibDirectionalBlockDataGenContext;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibItemDataGenContext;
import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import fictioncraft.wintersteve25.fclib.content.registration.items.FCLibItemRegistryObject;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FCLibModelProvider extends ItemModelProvider {
    private final FCLibDeferredRegister<?, ?, ?> register;

    public FCLibModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper, FCLibDeferredRegister<?, ?, ?> register) {
        super(generator, modid, existingFileHelper);
        this.register = register;
    }

    @Override
    protected void registerModels() {
        autoGenModels();
    }

    protected ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }

    protected ItemModelBuilder builder(String path, String name) {
        return getBuilder(path).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", "item/" + name);
    }

    protected void autoGenModels() {
        var objects = register.getAllRegisteredObjects();
        
        for (IRegistryObjectWrapper<?> o : objects.keySet()) {
            Object data = objects.get(o);
            if (data instanceof FCLibItemDataGenContext context) {
                if (context.isDoModelGen()) {
                    if (o instanceof FCLibItemRegistryObject<?>) {
                        String name = o.getName();
                        builder(name, name);
                    } else if (o instanceof FCLibBlockRegistryObject<?, ?> b) {
                        if (data instanceof FCLibDirectionalBlockDataGenContext directionalData) {
                            withExistingParent(b.getName(), directionalData.getModelFile().getLocation());
                        } else {
                            withExistingParent(b.getName(), modLoc("block/" + b.getName()));
                        }
                    }
                }
            }
        }
    }
}