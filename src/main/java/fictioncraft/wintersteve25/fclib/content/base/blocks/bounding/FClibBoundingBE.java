package fictioncraft.wintersteve25.fclib.content.base.blocks.bounding;

import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBE;
import fictioncraft.wintersteve25.fclib.content.registries.FCLibBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Modified from https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/tile/TileEntityBoundingBlock.java
 * Compatible with MIT License https://github.com/mekanism/Mekanism/blob/1.16.x/LICENSE
 */
public class FClibBoundingBE extends FCLibBE {

    private BlockPos mainPos;
    public boolean receivedCoords;

    public FClibBoundingBE(BlockPos pos, BlockState state) {
        this(FCLibBlocks.BOUNDING_TE.get(), pos, state);
    }

    public FClibBoundingBE(BlockEntityType<FClibBoundingBE> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.mainPos = BlockPos.ZERO;
    }

    public void setMainLocation(BlockPos pos) {
        this.receivedCoords = pos != null;
        if (this.isServer()) {
            this.mainPos = pos;
            this.sendNBTUpdatePacket();
        }
    }

    public BlockPos getMainPos() {
        if (this.mainPos == null) {
            this.mainPos = BlockPos.ZERO;
        }

        return this.mainPos;
    }

    @Nullable
    public BlockEntity getMainTile() {
        return this.receivedCoords ? this.level.getBlockEntity(getMainPos()) : null;
    }

    @Nullable
    public FCLibBE getMainONITile() {
        return this.receivedCoords ? (FCLibBE) this.level.getBlockEntity(getMainPos()) : null;
    }

    @Override
    public void load(@Nonnull CompoundTag nbtTags) {
        super.load(nbtTags);
        if (nbtTags.contains("main")) {
            mainPos = NbtUtils.readBlockPos(nbtTags.getCompound("main"));
        }
        this.receivedCoords = nbtTags.getBoolean("receivedCoords");
    }

    @Nonnull
    @Override
    public void saveAdditional(@Nonnull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.put("main", NbtUtils.writeBlockPos(this.getMainPos()));
        nbtTags.putBoolean("receivedCoords", this.receivedCoords);
    }

    @Override
    public void handleUpdateTag(@Nonnull CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("main")) {
            mainPos = NbtUtils.readBlockPos(tag.getCompound("main"));
        }
        receivedCoords = tag.getBoolean("receivedCoords");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (getMainTile() != null) {
            return getMainTile().getCapability(cap, side);
        }

        return LazyOptional.empty();
    }
}
