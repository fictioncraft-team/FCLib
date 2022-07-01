package fictioncraft.wintersteve25.fclib.content.datagen.server;

import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockRegistryObject;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibBlockDataGenContext;
import net.minecraft.data.DataGenerator;

public class FCLibLootTableProvider extends LootTableBase {
    private final FCLibBlockDeferredRegister register;

    public FCLibLootTableProvider(DataGenerator gen, String modid, FCLibBlockDeferredRegister register) {
        super(modid, gen);
        this.register = register;
    }

    @Override
    protected void addTables() {
        standardTables();
    }

    private void standardTables() {
        var objects = register.getAllRegisteredObjects();
        
        for (FCLibBlockRegistryObject<?, ?> b : objects.keySet()) {
            FCLibBlockDataGenContext data = objects.get(b);
            if (data.isDoLootTableGen()) lootTables.putIfAbsent(b.asBlock(), createStandardTable(b.getName(), b.asBlock()));
        }
    }
}