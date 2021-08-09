package fictioncraft.wintersteve25.fclib.common.base;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class FCLibTE extends TileEntity {

    protected int progress = progress();
    protected boolean isWorking = false;

    public FCLibTE(TileEntityType<?> te) {
        super(te);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    protected abstract int progress();

    public boolean getWorking() {
        return isWorking;
    }

    public void setWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(world.getBlockState(pos),pkt.getNbtCompound());
    }


    public void updateBlock(){
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 2);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {

        isWorking = tag.getBoolean("isWorking");
        progress = tag.getInt("progress");

        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        isWorking = tag.getBoolean("isWorking");
        progress = tag.getInt("progress");

        return super.write(tag);
    }
}
