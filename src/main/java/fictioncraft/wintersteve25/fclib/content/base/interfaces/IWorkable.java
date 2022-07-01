package fictioncraft.wintersteve25.fclib.content.base.interfaces;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface IWorkable {
    boolean getWorking();

    void setWorking(boolean isWorking);
}
