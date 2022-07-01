package fictioncraft.wintersteve25.fclib.content.base.builders;

import fictioncraft.wintersteve25.fclib.content.base.FCLibContainerImpl;
import fictioncraft.wintersteve25.fclib.utils.LangHelper;
import fictioncraft.wintersteve25.fclib.utils.SlotArrangement;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FCLibContainerBuilder {

    private boolean shouldAddPlayerSlots = true;
    private boolean shouldTrackWorking = false;
    private boolean shouldTrackProgress = false;
    private boolean shouldTrackTotalProgress = false;
    private boolean shouldAddInternalInventory = false;
    private List<SlotArrangement> internalSlotArrangement = new ArrayList<>();
    private Tuple<Integer, Integer> playerSlotStart = new Tuple<>(8, 88);

    private final String regName;
    private final RegistryObject<MenuType<FCLibContainerImpl>> registryObject;

    public FCLibContainerBuilder(String regName, DeferredRegister<MenuType<?>> deferredRegister) {
        this.regName = regName;
        registryObject = buildContainerType(deferredRegister);
    }

    public FCLibContainerBuilder disablePlayerSlots() {
        this.shouldAddPlayerSlots = false;
        return this;
    }

    public FCLibContainerBuilder trackWorking() {
        this.shouldTrackWorking = true;
        return this;
    }

    public FCLibContainerBuilder trackProgress() {
        this.shouldTrackProgress = true;
        return this;
    }

    public FCLibContainerBuilder trackTotalProgress() {
        this.shouldTrackTotalProgress = true;
        return this;
    }

    public FCLibContainerBuilder addInternalInventory() {
        this.shouldAddInternalInventory = true;
        return this;
    }

    public FCLibContainerBuilder setInternalSlotArrangement(SlotArrangement... internalSlotArrangement) {
        this.internalSlotArrangement = Arrays.asList(internalSlotArrangement);
        return this;
    }

    public FCLibContainerBuilder setPlayerSlotStart(Tuple<Integer, Integer> playerSlotStart) {
        this.playerSlotStart = playerSlotStart;
        return this;
    }

    private RegistryObject<MenuType<FCLibContainerImpl>> buildContainerType(DeferredRegister<MenuType<?>> deferredRegister) {
        return deferredRegister.register(LangHelper.langToReg(regName), () -> IForgeMenuType.create(buildFactory()));
    }

    public IContainerFactory<FCLibContainerImpl> buildFactory() {
        return (windowId, inv, data) -> buildNewInstance(windowId, inv.player.level, data.readBlockPos(), inv, inv.player);
    }

    public FCLibContainerImpl buildNewInstance(int windowID, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        return new FCLibContainerImpl(
                windowID,
                world,
                pos,
                playerInventory,
                player,
                registryObject.get(),
                shouldAddPlayerSlots,
                shouldTrackWorking,
                shouldTrackProgress,
                shouldTrackTotalProgress,
                shouldAddInternalInventory,
                internalSlotArrangement,
                playerSlotStart
        );
    }

    public RegistryObject<MenuType<FCLibContainerImpl>> getContainerTypeRegistryObject() {
        return registryObject;
    }
}
