package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IHasGui;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IWorkable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class FCLibTE extends TileEntity {
    public FCLibTE(TileEntityType<?> te) {
        super(te);
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 3, this.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, @Nonnull CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(super.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world == null) return;
        handleUpdateTag(world.getBlockState(pos), pkt.getNbtCompound());
        super.onDataPacket(net, pkt);
    }

    public void updateBlock(){
        if (world == null) return;
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 2);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        if (this instanceof IWorkable) {
            IWorkable workable = (IWorkable) this;
            workable.setWorking(tag.getBoolean("isWorking"));
            if (this instanceof IHasProgress) {
                IHasProgress hasProgress = (IHasProgress) this;
                hasProgress.setProgress(tag.getInt("progress"));
            }
        }
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (this instanceof IWorkable) {
            IWorkable workable = (IWorkable) this;
            tag.putBoolean("isWorking", workable.getWorking());
            if (this instanceof IHasProgress) {
                IHasProgress hasProgress = (IHasProgress) this;
                tag.putInt("progress", hasProgress.getProgress());
            }
        }
        return super.write(tag);
    }

    public boolean isServer() {
        return world != null && !world.isRemote();
    }

    public String machineName() {
        if (world != null) {
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof IHasGui) {
                IHasGui machine = (IHasGui) block;
                return machine.machineName();
            }
        }

        return "";
    }
}
