package fictioncraft.wintersteve25.fclib.api.json.utils;

import com.mojang.brigadier.StringReader;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.SimpleCommandArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.SimpleEffectArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.SimpleSwingHandArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.SimpleTransformArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ISimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleBlockProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleFluidProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.common.helper.CommandsHelper;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.state.Property;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType.*;

@SuppressWarnings("all")
public class JsonSerializer {
    public static Logger logger = LogManager.getLogger("FCLibJsonSerializer");

    public static ITag getTagFromJson(ISimpleObjProvider jsonIn) {
        if (jsonIn.isTag() && MiscHelper.isStringValid(jsonIn.getName())) {
            return getTagFromJson(jsonIn.getName(), jsonIn.getType());
        }
        return null;
    }

    public static ITag getTagFromJson(String name, ObjProviderType type) {
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

    public static ITag getTagFromRL(ResourceLocation resourceLocation, ObjProviderType type) {
        return getTagFromJson(resourceLocation.toString(), type);
    }

    public static boolean isValidTarget(ISimpleObjProvider jsonIn) {
        if (isModWildCard(jsonIn)) {
            String requiredModID = getModIDFromString(jsonIn);
            return ModListHelper.isModLoaded(requiredModID);
        }
        if (jsonIn.isTag()) return getTagFromJson(jsonIn) != null;

        String jsonInName = jsonIn.getName();
        String processedName = jsonInName;

        if (jsonInName.indexOf('[') != -1 || jsonInName.indexOf(']') != -1) {
            processedName = jsonInName.substring(0, jsonInName.indexOf('['));
        }

        ResourceLocation rl = new ResourceLocation(processedName);

        if (ITEM.equals(jsonIn.getType())) {
            return MiscHelper.isItemValid(ForgeRegistries.ITEMS.getValue(rl));
        } else if (BLOCK.equals(jsonIn.getType())) {
            return MiscHelper.isBlockValid(ForgeRegistries.BLOCKS.getValue(rl));
        } else if (FLUID.equals(jsonIn.getType())) {
            return MiscHelper.isFluidValid(ForgeRegistries.FLUIDS.getValue(rl));
        } else if (ENTITY.equals(jsonIn.getType())) {
            return ForgeRegistries.ENTITIES.getValue(rl) != null;
        } else {
            if (jsonIn.getType() == null) return true;
            if (jsonIn.getType().getIsTargetValidSerializer() == null) return true;
            return jsonIn.getType().getIsTargetValidSerializer().test(rl);
        }
    }

    public static class ItemStackSerializer {
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
                return areStacksTheSame(stackIn, jsonIn);
            }
        }

        public static boolean areStacksTheSame(ItemStack stack, SimpleItemProvider provider) {
            return areStacksTheSame(stack, getItemStackFromJsonItemStack(provider), provider.getAmount() != 0, provider.getNBT() != null && !provider.getNBT().isEmpty());
        }

        public static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2, boolean matchSize, boolean matchNBT) {
            boolean output = false;
            if (MiscHelper.isItemValid(stack1.getItem()) && MiscHelper.isItemValid(stack2.getItem())) {
                output = areStacksRegistryNameTheSame(stack1, stack2);

                if (matchSize) {
                    output = output && areStacksSizeTheSame(stack1, stack2);
                }

                if (matchNBT) {
                    output = output && areStacksNBTTheSame(stack1, stack2);
                }

                return output;
            }
            return false;
        }

        public static boolean areStacksRegistryNameTheSame(ItemStack stack1, ItemStack stack2) {
            return stack1.getItem().getRegistryName().toString().equals(stack2.getItem().getRegistryName().toString());
        }

        public static boolean areStacksSizeTheSame(ItemStack stack1, ItemStack stack2) {
            return stack1.getCount() == stack2.getCount();
        }

        public static boolean areStacksNBTTheSame(ItemStack stack1, ItemStack stack2) {
            CompoundNBT nbt1 = stack1.getTag();
            CompoundNBT nbt2 = stack2.getTag();

            if (nbt1 == null || nbt1.isEmpty() || nbt2 == null || nbt2.isEmpty()) return false;
            return stack1.getTag().equals(stack2.getTag());
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
                    ItemStack stack = new ItemStack(item, amount);
                    stack.setTag(nbtSerializer.readStruct());
                    return stack;
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
                ITag tag = getTagFromRL(rl, ITEM);
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
                ITag tag = getTagFromRL(rl, BLOCK);
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

        public static Pair<BlockState, Optional<CompoundNBT>> getPair(World world, BlockPos pos) {
            TileEntity te = world.getTileEntity(pos);
            BlockState state = world.getBlockState(pos);

            if (te != null) {
                CompoundNBT nbt = te.getTileData();
                if (nbt != null && !nbt.isEmpty()) {
                    return new Pair<>(state, Optional.of(nbt));
                }
            }
            return new Pair<>(state, Optional.empty());
        }

        public static boolean doesBlockMatch(Pair<BlockState, Optional<CompoundNBT>> pairIn, SimpleBlockProvider jsonIn) {
            BlockState stateIn = pairIn.getKey();
            Optional<CompoundNBT> nbtOptional = pairIn.getValue();

            boolean matchNBT = jsonIn.hasTE() && jsonIn.getNbt() != null && !jsonIn.getNbt().isEmpty();

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

                Pair<BlockState, Optional<CompoundNBT>> jsonPair = getBlockStateFromJson(jsonIn);
                //dirty hack
                List<Boolean> checks = new ArrayList<>();
                BlockState stateRequired = jsonPair.getKey();
                Optional<CompoundNBT> optionalRequiredNBT = jsonPair.getValue();
                CompoundNBT requiredNBT = null;

                if (optionalRequiredNBT.isPresent()) {
                    requiredNBT = optionalRequiredNBT.get();
                }

                String blockNameJsonIn = jsonIn.getName();

                if (blockNameJsonIn.indexOf('[') == -1 || blockNameJsonIn.indexOf(']') == -1) {
                    return stateRequired.getBlock().getRegistryName().toString().equals(stateIn.getBlock().getRegistryName().toString());
                }

                if (stateRequired.getProperties().isEmpty()) {
                    return stateRequired.getBlock().getRegistryName().toString().equals(stateIn.getBlock().getRegistryName().toString());
                }

                for (Property<?> properties : stateRequired.getProperties()) {
                    checks.add(stateIn.hasProperty(properties) && stateIn.get(properties) == stateRequired.get(properties));
                }

                if (matchNBT) {
                    if (nbtOptional.isPresent() && requiredNBT != null) {
                        CompoundNBT compoundNBT = nbtOptional.get();
                        for (String nbtNames : requiredNBT.keySet()) {
                            checks.add(compoundNBT.contains(nbtNames) && NBTUtil.areNBTEquals(compoundNBT.get(nbtNames), requiredNBT.get(nbtNames), true));
                        }
                    }
                    return !checks.contains(false);
                } else {
                    return !checks.contains(false);
                }
            }
        }

        public static Pair<BlockState, Optional<CompoundNBT>> getBlockStateFromJson(SimpleBlockProvider jsonIn) {
            String name = jsonIn.getName();
            String nbt = jsonIn.getNbt();

            if (!jsonIn.isTag()) {
                return getBlockStateFromJson(name, nbt, jsonIn.hasTE());
            }
            return null;
        }

        public static Pair<BlockState, Optional<CompoundNBT>> getBlockStateFromJson(String nameIn, String nbtIn, boolean hasTe) {
            if (MiscHelper.isStringValid(nameIn)) {
                try {
                    if (hasTe) {
                        BlockStateParser parser = new BlockStateParser(new StringReader(nameIn + nbtIn), true).parse(true);
                        BlockState state = parser.getState();
                        if (MiscHelper.isStateValid(state)) {
                            return new Pair<BlockState, Optional<CompoundNBT>>(state, Optional.of(parser.getNbt()));
                        } else {
                            logger.warn("BlockState serialization failed, block with name {} is not valid", nameIn);
                        }
                    } else {
                        BlockStateParser parser = new BlockStateParser(new StringReader(nameIn), false).parse(false);
                        BlockState state = parser.getState();
                        if (MiscHelper.isStateValid(state)) {
                            return new Pair<BlockState, Optional<CompoundNBT>>(state, Optional.ofNullable(parser.getNbt()));
                        } else {
                            logger.warn("BlockState serialization failed, block with name {} is not valid", nameIn);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Tried to read NBT from TE but TE or NBT does not exist");
                }
                return null;
            } else {
                logger.warn("Blockstate serialization failed, input name is null");
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
                ITag tag = getTagFromRL(rl, FLUID);
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
                return MiscHelper.areFluidsSame(stackIn, getFluidStackFromJson(jsonIn));
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
                ITag tag = getTagFromRL(rl, ENTITY);
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

        public static boolean doesEntitiesMatch(Entity entity, SimpleEntityProvider jsonIn) {
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
                return entity.getType().getRegistryName().toString().equals(jsonIn.getName());
            }
        }

        public static<T extends Entity> EntityType<T> getEntityTypeFromJson(SimpleEntityProvider jsonIn) {
            String name = jsonIn.getName();

            if (!jsonIn.isTag()) {
                if (MiscHelper.isStringValid(name)) {
                    ResourceLocation rl = new ResourceLocation(name);
                    try {
                        EntityType<T> type = (EntityType<T>) ForgeRegistries.ENTITIES.getValue(rl);
                        return type;
                    } catch (Exception e) {
                        FCLibMod.logger.warn("Caught an casting exception from serialized entity type to entity childrens");
                        e.printStackTrace();
                    }
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

    public static class EffectSerializer {
        public static Effect getEffectFromJson(SimpleEffectArg jsonIn) {
            String name = jsonIn.getEffectName();
            return ForgeRegistries.POTIONS.getValue(new ResourceLocation(name));
        }

        public static EffectInstance getEffectInstanceFromJson(SimpleEffectArg jsonIn) {
            Effect effect = getEffectFromJson(jsonIn);
            if (effect == null) return null;
            return new EffectInstance(effect, jsonIn.getEffectDuration(), jsonIn.getEffectLevel());
        }

        public static boolean effectPredicate(LivingEntity entity, SimpleEffectArg arg) {
            EffectInstance effect = getEffectInstanceFromJson(arg);
            Collection<EffectInstance> effects = entity.getActivePotionEffects();
            return effects.isEmpty() ? false : effects.contains(effect);
        }
    }

    public static class Args {
        public static void execute(PlayerEntity player, @Nullable Entity entity, SimpleArgProvider arg) {
            if (arg == null || player == null) return;
            if (arg instanceof SimpleCommandArg) {
                SimpleCommandArg commandArg = (SimpleCommandArg) arg;
                executeCommand(player, commandArg);
                return;
            } else if (arg instanceof SimpleEffectArg) {
                SimpleEffectArg effectArg = (SimpleEffectArg) arg;
                if (effectArg.isCondition()) return;
                executeEffect(player, effectArg);
                return;
            } else if (arg instanceof SimpleTransformArg) {
                SimpleTransformArg transformArg = (SimpleTransformArg) arg;
                executeTransform(entity, transformArg);
                return;
            } else if (arg instanceof SimpleSwingHandArg) {
                SimpleSwingHandArg swingHandArg = (SimpleSwingHandArg) arg;

            }
            arg.getType().getArgExecutor().accept(player, arg);
        }

        public static void executeCommand(PlayerEntity playerEntity, SimpleCommandArg arg) {
            World world = playerEntity.getEntityWorld();
            String command = arg.getCommand();
            if (!world.isRemote()) {
                CommandsHelper.executeCommand(playerEntity, command, arg.isRunAsPlayer());
            }
        }

        public static void executeEffect(LivingEntity entity, SimpleEffectArg arg) {
            if (entity.getEntityWorld().isRemote()) return;
            EffectInstance effect = EffectSerializer.getEffectInstanceFromJson(arg);
            if (effect == null) return;
            entity.addPotionEffect(effect);
        }

        public static <T extends Entity> void executeTransform(Entity entityOld, SimpleTransformArg arg) {
            World world = entityOld.getEntityWorld();
            if (world.isRemote()) return;
            ServerWorld serverWorld = (ServerWorld) world;
            SimpleEntityProvider entityProvider = arg.getProvider();
            if (entityOld == null || entityProvider == null) return;
            EntityType<T> entityType = EntitySerialization.getEntityTypeFromJson(entityProvider);

            if (entityType == null) return;

            if (!arg.isAllowPeaceful()) {
                if (world.getDifficulty() != Difficulty.PEACEFUL && Hooks.onEntityTransformPre(entityOld, entityType)) {
                    T entityToCreate = entityType.create(serverWorld, entityOld.serializeNBT(), null, null, new BlockPos(entityOld.getPosX(), entityOld.getPosY(), entityOld.getPosZ()), SpawnReason.CONVERSION, false, false);
                    entityToCreate.setLocationAndAngles(entityOld.getPosX(), entityOld.getPosY(), entityOld.getPosZ(), entityOld.rotationYaw, entityOld.rotationPitch);
                    Hooks.onEntityTransformPost(entityOld, entityType);
                    entityOld.remove();
                }
            }

            if (Hooks.onEntityTransformPre(entityOld, entityType)) {
                T entityToCreate = entityType.create(serverWorld, entityOld.serializeNBT(), null, null, new BlockPos(entityOld.getPosX(), entityOld.getPosY(), entityOld.getPosZ()), SpawnReason.CONVERSION, false, false);
                entityToCreate.setLocationAndAngles(entityOld.getPosX(), entityOld.getPosY(), entityOld.getPosZ(), entityOld.rotationYaw, entityOld.rotationPitch);
                Hooks.onEntityTransformPost(entityOld, entityType);
                entityOld.remove();
            }
        }

        public static void executeKill(Entity entityOld) {
            World world = entityOld.getEntityWorld();
            if (world.isRemote()) return;
            if (entityOld == null) return;
            entityOld.onKillCommand();
        }

        public static void executeSwingHand(LivingEntity entity, SimpleSwingHandArg arg) {
            SimpleSwingHandArg.JsonHandTypes handType = arg.handType();
            if (!entity.getEntityWorld().isRemote()) {
                switch (handType) {
                    case MAIN:
                        entity.swingArm(Hand.MAIN_HAND);
                        break;
                    case OFF:
                        entity.swingArm(Hand.OFF_HAND);
                        break;
                    case DEFAULT:
                        entity.swingArm(entity.getActiveHand());
                        break;
                }
            }
        }
    }

    public static boolean isModWildCard(ISimpleObjProvider jsonIn) {
        return isModWildCard(jsonIn.getName());
    }

    public static boolean isModWildCard(String stringIn) {
        return stringIn.charAt(0) == '*';
    }

    public static String getModIDFromString(ISimpleObjProvider jsonIn) {
        return getModIDFromString(jsonIn.getName());
    }

    public static String getModIDFromString(String stringIn) {
        if (isModWildCard(stringIn)) {
            return MiscHelper.removeCharAt(stringIn, 0);
        }
        return null;
    }
}