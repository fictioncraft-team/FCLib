package fictioncraft.wintersteve25.fclib.api.json.utils;

import com.mojang.brigadier.StringReader;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.*;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.block.Block;
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

import java.util.ArrayList;
import java.util.List;

import static fictioncraft.wintersteve25.fclib.api.json.objects.providers.ProviderType.*;

@SuppressWarnings("all")
public class JsonSerializer {
    public static Logger logger = LogManager.getLogger("FCLibJsonSerializer");

    public static ITag getTagFromJson(SimpleObjProvider jsonIn) {
        if (jsonIn.isTag() && MiscHelper.isStringValid(jsonIn.getName())) {
            return getTagFromJson(jsonIn.getName(), jsonIn.getType());
        }
        return null;
    }

    public static ITag getTagFromJson(String name, ProviderType type) {
        if (MiscHelper.isStringValid(name)) {
            if (BLOCK.equals(type)) {
                return TagCollectionManager.getManager().getBlockTags().getTagByID(new ResourceLocation(name));
            } else if (ITEM.equals(type)) {
                return TagCollectionManager.getManager().getItemTags().getTagByID(new ResourceLocation(name));
            } else if (FLUID.equals(type)) {
                return TagCollectionManager.getManager().getFluidTags().getTagByID(new ResourceLocation(name));
            } else if (ENTITY.equals(type)) {
                return TagCollectionManager.getManager().getEntityTypeTags().getTagByID(new ResourceLocation(name));
            } else {
                if (type.getTagSerializer() == null) return null;
                return type.getTagSerializer().apply(new ResourceLocation(name));
            }
        }
        return null;
    }

    public static ITag getItemTagFromRL(ResourceLocation resourceLocation) {
        return TagCollectionManager.getManager().getItemTags().getTagByID(resourceLocation);
    }

    public static boolean isValidTarget(SimpleObjProvider jsonIn) {
        if (isModWildCard(jsonIn)) {
            String requiredModID = getModIDFromString(jsonIn);
            return ModListHelper.isModLoaded(requiredModID);
        }
        if (jsonIn.isTag()) return getTagFromJson(jsonIn) != null;

        ResourceLocation rl = new ResourceLocation(jsonIn.getName());

        if (ITEM.equals(jsonIn.getType())) {
            return MiscHelper.isItemValid(ForgeRegistries.ITEMS.getValue(rl));
        } else if (BLOCK.equals(jsonIn.getType())) {
            return MiscHelper.isBlockValid(ForgeRegistries.BLOCKS.getValue(rl));
        } else if (FLUID.equals(jsonIn.getType())) {
            return MiscHelper.isFluidValid(ForgeRegistries.FLUIDS.getValue(rl));
        } else if (ENTITY.equals(jsonIn.getType())) {
            return ForgeRegistries.ENTITIES.getValue(rl) != null;
        } else {
            if (jsonIn.getType().getIsTargetValidSerializer() == null) return false;
            return jsonIn.getType().getIsTargetValidSerializer().test(rl);
        }
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
                } catch (Exception e) {
                    logger.warn("NBT {} does not exist, created ItemStack without NBT", nbt);
                    return new ItemStack(item, amount);
                }
            }
            return ItemStack.EMPTY;
        }

        public static List<ITag> getTagFromItem(ItemStack stack) {
            return getTagFromItem(stack.getItem());
        }

        public static List<ITag> getTagFromItem(Item item) {
            List<ITag> tags = new ArrayList<>();
            for (ResourceLocation rl : TagCollectionManager.getManager().getItemTags().getOwningTags(item)) {
                ITag tag = getItemTagFromRL(rl);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            return tags.isEmpty() ? null : tags;
        }

        public static List<ResourceLocation> getTagRLFromItem(ItemStack item) {
            return getTagRLFromItem(item.getItem());
        }

        public static List<ResourceLocation> getTagRLFromItem(Item item) {
            List<ResourceLocation> list = new ArrayList<>();
            list.addAll(TagCollectionManager.getManager().getItemTags().getOwningTags(item));
            return list;
        }

        public static SimpleItemProvider fromStackToJson(ItemStack stack, boolean shouldOutputTag, int tagIndex) {
            if (shouldOutputTag) {
                String name = getTagRLFromItem(stack).get(tagIndex).toString();
                if (name == null || name.isEmpty()) {
                    logger.warn("Tag on {} index on {} is not found!", tagIndex, stack.getItem().getRegistryName());
                    return null;
                }

                return new SimpleItemProvider(name, 0, "", true);
            }

            String name = stack.getItem().getRegistryName().toString();
            int amount = stack.getCount();

            String nbt;

            if (stack.getTag() == null || stack.getTag().isEmpty()) {
                return new SimpleItemProvider(name, amount, "", false);
            }

            if (stack.getTag().toString() == null || stack.getTag().toString().isEmpty()) {
                return new SimpleItemProvider(name, amount, "", false);
            }

            nbt = stack.getTag().toString();

            return new SimpleItemProvider(name, amount, nbt, false);
        }
    }

    public static class BlockSerializer {

        public static List<ITag> getTagFromBlock(BlockState stack) {
            return getTagFromBlock(stack.getBlock());
        }

        public static List<ITag> getTagFromBlock(Block block) {
            List<ITag> tags = new ArrayList<>();
            for (ResourceLocation rl : TagCollectionManager.getManager().getBlockTags().getOwningTags(block)) {
                ITag tag = getItemTagFromRL(rl);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            return tags.isEmpty() ? null : tags;
        }

        public static List<ResourceLocation> getTagRLFromBlock(BlockState block) {
            return getTagRLFromBlock(block.getBlock());
        }

        public static List<ResourceLocation> getTagRLFromBlock(Block block) {
            List<ResourceLocation> list = new ArrayList<>();
            list.addAll(TagCollectionManager.getManager().getBlockTags().getOwningTags(block));
            return list;
        }

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
                        if (jsonIn.hasTE()) {
                            BlockStateParser parser = new BlockStateParser(new StringReader(jsonIn.getName()), true).parse(true);
                            BlockState state = parser.getState();
                            if (MiscHelper.isStateValid(state)) {
                                return state;
                            } else {
                                logger.warn("BlockState serialization failed, block with name {} is not valid", name);
                            }
                        } else {
                            BlockStateParser parser = new BlockStateParser(new StringReader(jsonIn.getName()), false);
                            BlockState state = parser.getState();
                            if (MiscHelper.isStateValid(state)) {
                                return state;
                            } else {
                                logger.warn("BlockState serialization failed, block with name {} is not valid", name);
                            }
                        }
                    } catch (Exception e) {
                        logger.warn("Tried to read NBT from TE but TE or NBT does not exist");
                    }
                    return null;
                } else {
                    logger.warn("ItemStack serialization failed, input name is null");
                }
            }
            return null;
        }

        public static BlockState getBlockStateFromJson(String nameIn, String nbtIn) {
            String name = nameIn;
            String nbt = nbtIn;

            if (MiscHelper.isStringValid(name)) {
                try {
                    if (MiscHelper.isStringValid(nbt)) {
                        BlockStateParser parser = new BlockStateParser(new StringReader(name), true).parse(true);
                        BlockState state = parser.getState();
                        if (MiscHelper.isStateValid(state)) {
                            return state;
                        } else {
                            logger.warn("BlockState serialization failed, block with name {} is not valid", name);
                        }
                    } else {
                        BlockStateParser parser = new BlockStateParser(new StringReader(name), false).parse(false);
                        BlockState state = parser.getState();
                        if (MiscHelper.isStateValid(state)) {
                            return state;
                        } else {
                            logger.warn("BlockState serialization failed, block with name {} is not valid", name);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Tried to read NBT from TE but TE or NBT does not exist");
                }
                return null;
            } else {
                logger.warn("ItemStack serialization failed, input name is null");
            }
            return null;
        }

        public static SimpleBlockProvider fromBlockToJson(ItemStack stack, BlockState block, boolean shouldOutputTag, int tagIndex) {
            if (shouldOutputTag) {
                String name = getTagRLFromBlock(block).get(tagIndex).toString();
                if (name == null || name.isEmpty()) {
                    logger.warn("Tag on {} index on {} is not found!", tagIndex, block.getBlock().getRegistryName().toString());
                    return null;
                }

                return new SimpleBlockProvider(name, false, "", true);
            }

            String name = block.getBlock().getRegistryName().toString();
            String nbt;
            String stateName = MiscHelper.removeChar(block.toString().substring(6, block.toString().length()), '}');

            if (block.getValues() == null || block.getValues().isEmpty() && !block.hasTileEntity()) {
                return new SimpleBlockProvider(name, false, "", false);
            }

            if (stack.getTag() == null || stack.getTag().isEmpty() || stack.getTag().toString() == null || stack.getTag().toString().isEmpty() || !block.hasTileEntity()) {
                return new SimpleBlockProvider(stateName, false, "", false);
            }

            nbt = stack.getTag().toString();

            return new SimpleBlockProvider(stateName, block.hasTileEntity(), nbt, false);
        }
    }

    public static class FluidSerializer {
        public static List<ITag> getTagFromFluid(FluidStack stack) {
            return getTagFromFluid(stack.getFluid());
        }

        public static List<ITag> getTagFromFluid(Fluid fluid) {
            List<ITag> tags = new ArrayList<>();
            for (ResourceLocation rl : TagCollectionManager.getManager().getFluidTags().getOwningTags(fluid)) {
                ITag tag = getItemTagFromRL(rl);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            return tags.isEmpty() ? null : tags;
        }

        public static List<ResourceLocation> getTagRLFromFluid(FluidStack fluid) {
            return getTagRLFromFluid(fluid.getFluid());
        }

        public static List<ResourceLocation> getTagRLFromFluid(Fluid fluid) {
            List<ResourceLocation> list = new ArrayList<>();
            list.addAll(TagCollectionManager.getManager().getFluidTags().getOwningTags(fluid));
            return list;
        }

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
                } catch (Exception e) {
                    logger.warn("NBT {} does not exist, created Fluid without NBT", nbt);
                    return new FluidStack(fluid, amount);
                }
            }
            return FluidStack.EMPTY;
        }

        public static SimpleFluidProvider fromFluidToJson(FluidStack stack, boolean shouldOutputTag, int tagIndex) {
            if (shouldOutputTag) {
                String name = getTagRLFromFluid(stack).get(tagIndex).toString();
                if (name == null || name.isEmpty()) {
                    logger.warn("Tag on {} index on {} is not found!", tagIndex, stack.getFluid().getRegistryName().toString());
                    return null;
                }

                return new SimpleFluidProvider(name, stack.getAmount(), "", true);
            }

            String name = stack.getFluid().getRegistryName().toString();
            String nbt;

            if (stack.getTag() == null || stack.getTag().isEmpty() || stack.getTag().toString() == null || stack.getTag().toString().isEmpty()) {
                return new SimpleFluidProvider(name, stack.getAmount(), "", false);
            }

            nbt = stack.getTag().toString();

            return new SimpleFluidProvider(name, stack.getAmount(), nbt, false);
        }
    }

    public static class EntitySerialization {
        public static List<ITag> getTagFromEntity(EntityType<?> type) {
            List<ITag> tags = new ArrayList<>();
            for (ResourceLocation rl : TagCollectionManager.getManager().getEntityTypeTags().getOwningTags(type)) {
                ITag tag = getItemTagFromRL(rl);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            return tags.isEmpty() ? null : tags;
        }

        public static List<ResourceLocation> getTagRLFromEntity(EntityType<?> type) {
            List<ResourceLocation> list = new ArrayList<>();
            list.addAll(TagCollectionManager.getManager().getEntityTypeTags().getOwningTags(type));
            return list;
        }

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

        public static EntityType<?> getEntityTypeFromJson(SimpleEntityProvider jsonIn) {
            String name = jsonIn.getName();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    ResourceLocation rl = new ResourceLocation(name);
                    EntityType<?> type = ForgeRegistries.ENTITIES.getValue(rl);
                    return type;
                }
            }
            return null;
        }

        public static SimpleEntityProvider fromEntityToJson(EntityType<?> type, boolean shouldOutputTag, int tagIndex) {
            if (shouldOutputTag) {
                String name = getTagRLFromEntity(type).get(tagIndex).toString();
                if (name == null || name.isEmpty()) {
                    logger.warn("Tag on {} index on {} is not found!", tagIndex, type.getRegistryName().toString());
                    return null;
                }

                return new SimpleEntityProvider(name, true);
            }

            String name = type.getRegistryName().toString();

            return new SimpleEntityProvider(name, false);
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