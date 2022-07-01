package fictioncraft.wintersteve25.fclib.content.base.interfaces;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface IHasRedstoneOutput {

    int lowThreshold();

    int highThreshold();

    void setLowThreshold(int in);

    void setHighThreshold(int in);
}
