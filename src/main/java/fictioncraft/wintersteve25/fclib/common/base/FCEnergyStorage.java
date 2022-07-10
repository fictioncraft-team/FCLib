package fictioncraft.wintersteve25.fclib.common.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class FCEnergyStorage extends EnergyStorage {
    public FCEnergyStorage(int capacity) {
        super(capacity);
    }

    public FCEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public FCEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public FCEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CompoundNBT write() {
        CompoundNBT energy = new CompoundNBT();
        energy.putInt("fe_energy", super.energy);
        return energy;
    }

    public void read(CompoundNBT nbt) {
        super.energy = nbt.getInt("fe_energy");
    }
}
