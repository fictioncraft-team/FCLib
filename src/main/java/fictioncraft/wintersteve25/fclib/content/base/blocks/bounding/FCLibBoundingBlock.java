package fictioncraft.wintersteve25.fclib.content.base.blocks.bounding;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBlock;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBE;
import fictioncraft.wintersteve25.fclib.content.registries.FCLibBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.IBlockRenderProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Modified from https://github.com/mekanism/Mekanism/blob/1.18.x/src/main/java/mekanism/common/block/BlockBounding.java
 * Compatible with MIT License https://github.com/mekanism/Mekanism/blob/1.18.x/LICENSE
 */

@SuppressWarnings("deprecation")
public class FCLibBoundingBlock extends FCLibBlock implements EntityBlock {

    @Nullable
    public static BlockPos getMainBlockPos(BlockGetter world, BlockPos thisPos) {
        FClibBoundingBE te = (FClibBoundingBE) world.getBlockEntity(thisPos);
        return te != null && te.receivedCoords && !thisPos.equals(te.getMainPos()) ? te.getMainPos() : null;
    }

    public FCLibBoundingBlock() {
        this(BlockBehaviour.Properties.of(Material.METAL).strength(3.5F, 4.8F).requiresCorrectToolForDrops().dynamicShape().noOcclusion());
    }

    public FCLibBoundingBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void initializeClient(Consumer<IBlockRenderProperties> consumer) {
        consumer.accept(new IBlockRenderProperties() {
            @Override
            public boolean addHitEffects(BlockState state, Level world, HitResult target, ParticleEngine manager) {
                if (target.getType() == HitResult.Type.BLOCK && target instanceof BlockHitResult blockTarget) {
                    BlockPos pos = blockTarget.getBlockPos();
                    BlockPos mainPos = getMainBlockPos(world, pos);
                    if (mainPos != null) {
                        BlockState mainState = world.getBlockState(mainPos);
                        if (!mainState.isAir()) {
                            //Copy of ParticleManager#addBlockHitEffects except using the block state for the main position
                            AABB axisalignedbb = state.getShape(world, pos).bounds();
                            double x = pos.getX() + world.random.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.2) + 0.1 + axisalignedbb.minX;
                            double y = pos.getY() + world.random.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.2) + 0.1 + axisalignedbb.minY;
                            double z = pos.getZ() + world.random.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.2) + 0.1 + axisalignedbb.minZ;
                            Direction side = blockTarget.getDirection();
                            if (side == Direction.DOWN) {
                                y = pos.getY() + axisalignedbb.minY - 0.1;
                            } else if (side == Direction.UP) {
                                y = pos.getY() + axisalignedbb.maxY + 0.1;
                            } else if (side == Direction.NORTH) {
                                z = pos.getZ() + axisalignedbb.minZ - 0.1;
                            } else if (side == Direction.SOUTH) {
                                z = pos.getZ() + axisalignedbb.maxZ + 0.1;
                            } else if (side == Direction.WEST) {
                                x = pos.getX() + axisalignedbb.minX - 0.1;
                            } else if (side == Direction.EAST) {
                                x = pos.getX() + axisalignedbb.maxX + 0.1;
                            }
                            manager.add(new TerrainParticle((ClientLevel) world, x, y, z, 0, 0, 0, mainState)
                                    .updateSprite(mainState, mainPos).setPower(0.2F).scale(0.6F));
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Nonnull
    @Deprecated
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            return InteractionResult.FAIL;
        } else {
            BlockState state1 = world.getBlockState(mainPos);
            return state1.getBlock().use(state1, world, mainPos, player, hand, hit);
        }
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos mainPos = getMainBlockPos(world, pos);
            if (mainPos != null) {
                BlockState mainState = world.getBlockState(mainPos);
                if (!mainState.isAir()) {
                    world.removeBlock(mainPos, false);
                }
            }

            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Nonnull
    @Override
    public ItemStack getCloneItemStack(@Nonnull BlockState state, HitResult target, @Nonnull BlockGetter world, @Nonnull BlockPos pos, Player player) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            return ItemStack.EMPTY;
        }
        BlockState mainState = world.getBlockState(mainPos);
        return mainState.getBlock().getCloneItemStack(mainState, target, world, mainPos, player);
    }

    @Override
    public boolean onDestroyedByPlayer(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, boolean willHarvest, FluidState fluidState) {
        if (willHarvest) {
            return true;
        }
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            BlockState mainState = world.getBlockState(mainPos);
            if (!mainState.isAir()) {
                //Set the main block to air, which will invalidate the rest of the bounding blocks
                mainState.onDestroyedByPlayer(world, mainPos, player, false, mainState.getFluidState());
            }
        }
        return super.onDestroyedByPlayer(state, world, pos, player, false, fluidState);
    }

    @Override
    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            BlockState mainState = world.getBlockState(mainPos);
            if (!mainState.isAir()) {
                //Proxy the explosion to the main block which, will set it to air causing it to invalidate the rest of the bounding blocks
                LootContext.Builder lootContextBuilder = new LootContext.Builder((ServerLevel) world)
                        .withRandom(world.random)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(mainPos))
                        .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, mainState.hasBlockEntity() ? world.getBlockEntity(mainPos) : null)
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getExploder());
                if (explosion.blockInteraction == Explosion.BlockInteraction.DESTROY) {
                    lootContextBuilder.withParameter(LootContextParams.EXPLOSION_RADIUS, explosion.radius);
                }
                mainState.getDrops(lootContextBuilder).forEach(stack -> Block.popResource(world, mainPos, stack));
                mainState.onBlockExploded(world, mainPos, explosion);
            }
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public void playerDestroy(@Nonnull Level world, @Nonnull Player player, @Nonnull BlockPos pos, @Nonnull BlockState state, BlockEntity te, @Nonnull ItemStack stack) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            BlockState mainState = world.getBlockState(mainPos);
            mainState.getBlock().playerDestroy(world, player, mainPos, mainState, world.getBlockEntity(mainPos), stack);
        } else {
            super.playerDestroy(world, player, pos, state, te, stack);
        }

        world.removeBlock(pos, false);
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos != null) {
            world.getBlockState(mainPos).neighborChanged(world, mainPos, neighborBlock, neighborPos, isMoving);
        }
    }

    @Override
    public float getDestroyProgress(@Nonnull BlockState state, @Nonnull Player player, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        return mainPos == null ? super.getDestroyProgress(state, player, world, pos) : world.getBlockState(mainPos).getDestroyProgress(player, world, mainPos);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        return mainPos == null ? super.getExplosionResistance(state, world, pos, explosion) : world.getBlockState(mainPos).getExplosionResistance(world, mainPos, explosion);
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        BlockPos mainPos = getMainBlockPos(world, pos);
        if (mainPos == null) {
            return Shapes.empty();
        } else {
            BlockState mainState;
            try {
                mainState = (world).getBlockState(mainPos);
            } catch (ArrayIndexOutOfBoundsException var9) {
                if (!(world instanceof RenderChunkRegion)) {
                    FCLibCore.LOGGER.error("Error getting bounding block shape, for position {}, with main position {}. World of type {}", pos, mainPos, world.getClass().getName());
                    return Shapes.empty();
                }

                world = ((RenderChunkRegion) world).level;
                mainState = (world).getBlockState(mainPos);
            }

            VoxelShape shape = mainState.getShape(world, mainPos, context);
            BlockPos offset = pos.subtract(mainPos);
            return shape.move(-offset.getX(), -offset.getY(), -offset.getZ());
        }
    }

    @Override
    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        FCLibBE te = (FCLibBE) pLevel.getBlockEntity(pPos);
        if (te != null) {
            super.triggerEvent(pState, pLevel, pPos, pId, pParam);
            return te.onTriggerBlockEntityEvent(pState, pLevel, pPos, pId, pParam);
        }
        return super.triggerEvent(pState, pLevel, pPos, pId, pParam);
    }

    @Override
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull PathComputationType type) {
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return FCLibBlocks.BOUNDING_TE.get().create(pPos, pState);
    }
}
