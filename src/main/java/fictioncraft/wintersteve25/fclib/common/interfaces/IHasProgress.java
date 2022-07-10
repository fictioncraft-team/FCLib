package fictioncraft.wintersteve25.fclib.common.interfaces;

/**
 * Should be implemented in a {@link net.minecraft.tileentity.TileEntity}
 */
public interface IHasProgress {
    int getProgress();

    void setProgress(int progress);

    int getTotalProgress();

    void setTotalProgress(int totalProgress);
}
