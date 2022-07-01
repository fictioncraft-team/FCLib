package fictioncraft.wintersteve25.fclib.content.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.function.BiFunction;
import java.util.function.Function;

public record FCLibDataGen(
        Function<DataGenerator, DataProvider> loottable,
        BiFunction<DataGenerator, ExistingFileHelper, DataProvider> state,
        BiFunction<DataGenerator, ExistingFileHelper, DataProvider> model,
        Function<DataGenerator, DataProvider> lang
) {

    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            gen.addProvider(loottable.apply(gen));
        }

        if (event.includeClient()) {
            gen.addProvider(state.apply(gen, existingFileHelper));
            gen.addProvider(model.apply(gen, existingFileHelper));

            //en_US
            gen.addProvider(lang.apply(gen));
        }
    }
}
