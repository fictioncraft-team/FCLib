package fictioncraft.wintersteve25.fclib.api.crafting;

import com.google.gson.JsonObject;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleFluidProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FluidIngredient implements Predicate<FluidStack> {
    private final List<Fluid> validFluids;
    private final int amount;

    public FluidIngredient(int amount, Fluid... validFluids) {
        this.amount = amount;
        this.validFluids = Arrays.asList(validFluids);
    }

    public FluidIngredient(int amount, ITag<Fluid> tag) {
        this.amount = amount;
        this.validFluids = ForgeRegistries.FLUIDS.getValues().stream().filter((fluid) -> fluid.isIn(tag)).collect(Collectors.toList());
    }

    public FluidStack[] getMatchingStacks() {
        List<FluidStack> stacks = new ArrayList<>();
        for (Fluid fluid : validFluids) {
            stacks.add(new FluidStack(fluid, amount));
        }
        return stacks.toArray(new FluidStack[stacks.size()]);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.getAmount() >= amount && validFluids.contains(fluidStack.getFluid());
    }

    public static FluidIngredient deserialize(JsonObject object) {
        JsonSerializer.logger.info("Attempting to serialize vanilla style fluid json object");
        boolean isTag = object.has("tag");
        int amount = object.get("amount").getAsInt();

        if (isTag) {
            String tag = object.get("tag").getAsString();
            return new FluidIngredient(amount, JsonSerializer.getTagFromJson(tag, ObjProviderType.FLUID));
        } else {
            FluidStack stack = JsonSerializer.FluidSerializer.getFluidStackFromJson(new SimpleFluidProvider(object.get("name").getAsString(), amount, object.has("nbt") ? object.get("nbt").getAsString() : "", false));
            return new FluidIngredient(stack.getAmount(), stack.getFluid());
        }
    }

    public static FluidIngredient read(PacketBuffer buffer) {
        int amount = buffer.readInt();
        Fluid[] fluids = new Fluid[buffer.readVarInt()];
        for (int i = 0; i < fluids.length; i++) {
            fluids[i] = buffer.readFluidStack().getFluid();
        }
        return new FluidIngredient(amount, fluids);
    }

    public final void write(PacketBuffer buffer) {
        buffer.writeInt(amount);
        buffer.writeVarInt(validFluids.size());
        for (Fluid fluid : validFluids) {
            buffer.writeFluidStack(new FluidStack(fluid, 1));
        }
    }
}
