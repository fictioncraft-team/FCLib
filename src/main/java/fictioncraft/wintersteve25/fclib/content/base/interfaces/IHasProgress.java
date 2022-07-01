package fictioncraft.wintersteve25.fclib.content.base.interfaces;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface IHasProgress {
    int getProgress();

    void setProgress(int progress);

    int getTotalProgress();

    void setTotalProgress(int totalProgress);
}
