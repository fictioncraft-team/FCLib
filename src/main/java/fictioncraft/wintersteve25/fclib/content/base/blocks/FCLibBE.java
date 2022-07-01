package fictioncraft.wintersteve25.fclib.content.base.blocks;

import fictioncraft.wintersteve25.fclib.content.base.interfaces.*;
import fictioncraft.wintersteve25.fclib.content.network.FCLibNetworking;
import fictioncraft.wintersteve25.fclib.content.network.PacketUpdateClientBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FCLibBE extends BlockEntity {

    public FCLibBE(BlockEntityType<?> te, BlockPos pos, BlockState state) {
        super(te, pos, state);
    }

    public static <BE extends BlockEntity> void clientTicker(Level level, BlockPos blockPos, BlockState blockState, BE be) {
        if (be instanceof ITickableClient tickable) {
            tickable.clientTick();
        }

        if (be instanceof ITickableCommon tickable) {
            tickable.commonTick();
        }
    }

    public static <BE extends BlockEntity> void serverTicker(Level level, BlockPos blockPos, BlockState blockState, BE be) {
        if (be instanceof ITickableServer tickable) {
            tickable.serverTick();
        }

        if (be instanceof ITickableCommon tickable) {
            tickable.commonTick();
        }

        if (level == null) return;

        if (be instanceof IForceStoppable forceStoppable) {
            if (forceStoppable.isInverted()) {
                forceStoppable.setForceStopped(!level.hasNeighborSignal(blockPos));
            } else {
                forceStoppable.setForceStopped(level.hasNeighborSignal(blockPos));
            }

            if (forceStoppable.getForceStopped()) {
                forceStoppable.setWorking(false);
                if (be instanceof IHasProgress hasProgress) {
                    hasProgress.setProgress(0);
                }
            }
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (!isServer() && net.getDirection() == PacketFlow.CLIENTBOUND) {
            handleUpdateTag(pkt.getTag());
        }
    }

    protected void sendNBTUpdatePacket() {
        if (isClient()) return;
        if (isRemoved()) return;
        if (getLevel() instanceof ServerLevel) {
            ((ServerLevel) getLevel()).getChunkSource().chunkMap.getPlayers(new ChunkPos(getBlockPos()), false).forEach((p) -> {
                if (!(p instanceof FakePlayer)) {
                    FCLibNetworking.sendToClient(new PacketUpdateClientBE(this, getUpdateTag()), p);
                }
            });
        } else {
            FCLibNetworking.getInstance().send(PacketDistributor.TRACKING_CHUNK.with(() -> getLevel().getChunk(getBlockPos().getX() >> 4, getBlockPos().getZ() >> 4)), new PacketUpdateClientBE(this, getUpdateTag()));
        }
    }


    public void updateBlock(){
        if (level == null) return;
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, 2);
        setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        if (this instanceof IWorkable workable) {
            workable.setWorking(tag.getBoolean("isWorking"));
            if (this instanceof IHasProgress hasProgress) {
                hasProgress.setProgress(tag.getInt("progress"));
            }
            if (this instanceof IForceStoppable forceStoppable) {
                forceStoppable.setForceStopped(tag.getBoolean("isForceStopped"));
            }
        }
        if (this instanceof IHasRedstoneOutput redstoneOutput) {
            redstoneOutput.setLowThreshold(tag.getInt("low_threshold"));
            redstoneOutput.setHighThreshold(tag.getInt("high_threshold"));
        }
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        if (this instanceof IWorkable workable) {
            tag.putBoolean("isWorking", workable.getWorking());
            if (this instanceof IHasProgress hasProgress) {
                tag.putInt("progress", hasProgress.getProgress());
            }
            if (this instanceof IForceStoppable forceStoppable) {
                tag.putBoolean("isForceStopped", forceStoppable.getForceStopped());
            }
        }
        if (this instanceof IHasRedstoneOutput redstoneOutput) {
            tag.putInt("low_threshold", redstoneOutput.lowThreshold());
            tag.putInt("high_threshold", redstoneOutput.highThreshold());
        }
    }

    public boolean isServer() {
        return level != null && !level.isClientSide();
    }

    public boolean isClient() {
        return level != null && level.isClientSide();
    }

    public Component machineName() {
        if (level != null) {
            Block block = level.getBlockState(worldPosition).getBlock();
            if (block instanceof FCLibMachineBlock b) {
                IHasGui gui = b.getGui();
                return gui != null ? gui.machineName() : new TextComponent("");
            }
        }

        return new TextComponent("");
    }

    public void onHarvested(Level world, BlockPos pos, BlockState state, Player player) {
    }

    public void onBroken(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
    }

    public void onPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    }

    public void getWeakPower(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    }

    public void onBlockActivated(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
    }

    public boolean onTriggerBlockEntityEvent(BlockState state, Level level, BlockPos pos, int id, int params) {
        return false;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return world.getBlockState(pos).getBlock().canConnectRedstone(state, world, pos, side);
    }
}
