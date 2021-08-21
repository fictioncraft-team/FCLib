package fictioncraft.wintersteve25.fclib.common.json.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.json.objects.providers.*;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("all")
public class JsonSerializer {
    public static Logger logger = LogManager.getLogger("FCLibJsonSerializer");

    public static ITag getTagFromJson(SimpleObjProvider jsonIn) {
        if (jsonIn.isTag() && MiscHelper.isStringValid(jsonIn.getName())) {
            switch (jsonIn.getType()) {
                case BLOCK:
                    return TagCollectionManager.getManager().getBlockTags().getTagByID(new ResourceLocation(jsonIn.getName()));
                case ITEM:
                    return TagCollectionManager.getManager().getItemTags().getTagByID(new ResourceLocation(jsonIn.getName()));
                case FLUID:
                    return TagCollectionManager.getManager().getFluidTags().getTagByID(new ResourceLocation(jsonIn.getName()));
                case ENTITY:
                    return TagCollectionManager.getManager().getEntityTypeTags().getTagByID(new ResourceLocation(jsonIn.getName()));
                case OTHERS:
                    return null;
            }
        }
        return null;
    }

    public static class ItemStackSerializer {

        /**
         * You shouldnt be using this for json input stack comparison, use doesItemStackMatch instead
         */
        @Deprecated
        public static boolean areStacksSame(ItemStack stack1, ItemStack stack2, SimpleItemProvider jsonIn) {
            return areStacksSame(stack1, stack2, jsonIn.getNBT() != null && !jsonIn.getNBT().isEmpty(), jsonIn.getAmount() != 0);
        }

        @Deprecated
        public static boolean areStacksSame(ItemStack stack1, ItemStack stack2, boolean matchNBT, boolean matchSize) {
            if (stack1.isItemEqual(stack2)) {
                if (!matchNBT && !matchSize) {
                    return true;
                }

                if (!matchSize && matchNBT) {
                    if (!stack1.getTag().isEmpty() && !stack2.getTag().isEmpty()) {
                        NBTPredicate predicate = new NBTPredicate(stack1.getTag());
                        if (predicate.test(stack2)) {
                            return true;
                        }
                    }
                }

                if (matchSize && !matchNBT) {
                    if (stack1.getCount() == stack2.getCount()) {
                        return true;
                    }
                }

                if (matchSize && matchNBT) {
                    if (!stack1.getTag().isEmpty() && !stack2.getTag().isEmpty() && stack1.getCount() == stack2.getCount()) {
                        NBTPredicate predicate = new NBTPredicate(stack1.getTag());
                        if (predicate.test(stack2)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        public static boolean doesItemStackMatch(ItemStack stackIn, SimpleItemProvider jsonIn) {
            if (isModWildCard(jsonIn)) {
                String stateModID = stackIn.getItem().getRegistryName().getNamespace();
                String requiredModID = getModIDFromString(jsonIn);
                return stateModID.equals(requiredModID);
            }

            if (jsonIn.isTag()) {
                if (getTagFromJson(jsonIn) == null) {
                    return false;
                }
                return stackIn.getItem().isIn(getTagFromJson(jsonIn));
            } else {
                return areStacksSame(getItemStackFromJsonItemStack(jsonIn), stackIn, jsonIn);
            }
        }

        public static ItemStack getItemStackFromJsonItemStack(SimpleItemProvider jsonIn) {
            String name = jsonIn.getName();
            int amount = jsonIn.getAmount();
            String nbt = jsonIn.getNBT();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    ResourceLocation itemInRL = new ResourceLocation(name);
                    Item item = ForgeRegistries.ITEMS.getValue(itemInRL);

                    if (MiscHelper.isItemValid(item)) {
                        if (amount > 0 && amount <= 64) {
                            if (!tryCreateItemStack(item, amount, nbt).isEmpty()) {
                                return tryCreateItemStack(item, amount, nbt);
                            }
                            return new ItemStack(item, amount);
                        }
                        return new ItemStack(item);
                    } else {
                        logger.warn("ItemStack serialization failed, can not find item with name {}", name);
                    }
                } else {
                    logger.warn("ItemStack serialization failed, input name is null");
                }
            } else {
                logger.warn("Tried to create itemstack from tag! Returning empty itemstack");
            }

            return ItemStack.EMPTY;
        }

        public static ItemStack tryCreateItemStack(Item item, int amount, String nbt) {
            if (MiscHelper.isStringValid(nbt)) {
                JsonToNBT nbtSerializer = new JsonToNBT(new StringReader(nbt));
                try {
                    return new ItemStack(item, amount, nbtSerializer.readStruct());
                } catch (CommandSyntaxException e) {
                    logger.warn("NBT {} does not exist, created ItemStack without NBT", nbt);
                    return new ItemStack(item, amount);
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public static class BlockSerializer {
        public static boolean doesBlockMatch(BlockState stateIn, SimpleBlockProvider jsonIn) {
            if (isModWildCard(jsonIn)) {
                String stateModID = stateIn.getBlock().getRegistryName().getNamespace();
                String requiredModID = getModIDFromString(jsonIn);
                return stateModID.equals(requiredModID);
            }

            if (jsonIn.isTag()) {
                if (getTagFromJson(jsonIn) == null) {
                    return false;
                }
                return stateIn.isIn(getTagFromJson(jsonIn));
            } else {
                return getBlockStateFromJson(jsonIn) != null ? stateIn.equals(getBlockStateFromJson(jsonIn)) : false;
            }
        }

        public static BlockState getBlockStateFromJson(SimpleBlockProvider jsonIn) {
            String name = jsonIn.getName();
            String nbt = jsonIn.getNbt();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    try {
                        BlockStateParser parser = new BlockStateParser(new StringReader(jsonIn.getName()), true).parse(jsonIn.hasTE());
                        BlockState state = parser.getState();
                        if (MiscHelper.isStateValid(state)) {
                            return state;
                        } else {
                            logger.warn("BlockState serialization failed, block with name {} is not valid", name);
                        }
                    } catch (CommandSyntaxException e) {
                        logger.warn("Tried to read NBT from TE but TE or NBT does not exist");
                    }
                    return null;
                } else {
                    logger.warn("ItemStack serialization failed, input name is null");
                }
            }
            return null;
        }
    }

    public static class FluidSerializer {
        public static boolean doesFluidMatch(FluidStack stackIn, SimpleFluidProvider jsonIn) {
            if (isModWildCard(jsonIn)) {
                String fluidModID = stackIn.getFluid().getRegistryName().getNamespace();
                String requiredModID = getModIDFromString(jsonIn);
                return fluidModID.equals(requiredModID);
            }

            if (jsonIn.isTag()) {
                if (getTagFromJson(jsonIn) == null) {
                    return false;
                }
                return stackIn.getFluid().isIn(getTagFromJson(jsonIn));
            } else {
                return stackIn.isFluidStackIdentical(getFluidStackFromJson(jsonIn));
            }
        }

        public static FluidStack getFluidStackFromJson(SimpleFluidProvider jsonIn) {
            String name = jsonIn.getName();
            int amount = jsonIn.getAmount();
            String nbt = jsonIn.getNbt();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    ResourceLocation rl = new ResourceLocation(name);
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(rl);
                    if (MiscHelper.isFluidValid(fluid)) {
                        if (amount > 0) {
                            if (!tryCreateFluidStack(fluid, amount, nbt).isEmpty()) {
                                return tryCreateFluidStack(fluid, amount, nbt);
                            } else {
                                logger.warn("Can not provided nbt: {}", nbt);
                            }
                            return new FluidStack(fluid, amount);
                        }
                        return new FluidStack(fluid, 1);
                    }
                }
                logger.warn("Provided fluid name is invalid {}", name);
            }
            return FluidStack.EMPTY;
        }

        public static FluidStack tryCreateFluidStack(Fluid fluid, int amount, String nbt) {
            if (MiscHelper.isStringValid(nbt)) {
                JsonToNBT nbtSerializer = new JsonToNBT(new StringReader(nbt));
                try {
                    return new FluidStack(fluid, amount, nbtSerializer.readStruct());
                } catch (CommandSyntaxException e) {
                    logger.warn("NBT {} does not exist, created Fluid without NBT", nbt);
                    return new FluidStack(fluid, amount);
                }
            }
            return FluidStack.EMPTY;
        }
    }

    public static class EntitySerialization {
        public static boolean doesEntitiesMatch(Entity entity, SimpleEntityProvider jsonIn, World world) {
            if (isModWildCard(jsonIn)) {
                String entityModID = entity.getType().getRegistryName().getNamespace();
                String requiredModID = getModIDFromString(jsonIn);
                return entityModID.equals(requiredModID);
            }
            if (jsonIn.isTag()) {
                if (getTagFromJson(jsonIn) == null) {
                    return false;
                }
                return entity.getType().getTags().contains(getTagFromJson(jsonIn));
            } else {
                return entity.isEntityEqual(getEntityFromJson(jsonIn, world));
            }
        }

        public static Entity getEntityFromJson(SimpleEntityProvider jsonIn, World world) {
            String name = jsonIn.getName();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    ResourceLocation rl = new ResourceLocation(name);
                    EntityType<?> type = ForgeRegistries.ENTITIES.getValue(rl);
                    if (type != null) {
                        return type.create(world);
                    }
                }
            }
            return null;
        }
    }

    public static boolean isModWildCard(SimpleObjProvider jsonIn) {
        return isModWildCard(jsonIn.getName());
    }

    public static boolean isModWildCard(String stringIn) {
        return stringIn.charAt(0) == '*';
    }

    public static String getModIDFromString(SimpleObjProvider jsonIn) {
        return getModIDFromString(jsonIn.getName());
    }

    public static String getModIDFromString(String stringIn) {
        if (isModWildCard(stringIn)) {
            return MiscHelper.removeCharAt(stringIn, 0);
        }
        return null;
    }
}
