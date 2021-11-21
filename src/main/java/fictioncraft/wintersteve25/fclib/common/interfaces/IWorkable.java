package fictioncraft.wintersteve25.fclib.common.interfaces;

/**
 * Should be implemented in a {@link net.minecraft.tileentity.TileEntity}
 */
public interface IWorkable {
    boolean getWorking();

    void setWorking(boolean isWorking);
}
