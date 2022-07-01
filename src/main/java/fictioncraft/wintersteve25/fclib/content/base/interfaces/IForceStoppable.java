package fictioncraft.wintersteve25.fclib.content.base.interfaces;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface IForceStoppable extends IWorkable {
    boolean getForceStopped();

    void setForceStopped(boolean forceStopped);

    boolean isInverted();

    void toggleInverted();
}
