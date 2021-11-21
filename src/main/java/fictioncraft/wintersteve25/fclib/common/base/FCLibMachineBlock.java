package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasGui;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FCLibMachineBlock extends FCLibDirectionalBlock {

    public FCLibMachineBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material, String regName) {
        super(harvestLevel, hardness, resistance, soundType, material, regName);
    }

    public FCLibMachineBlock(AbstractBlock.Properties properties, String regName) {
        super(properties, regName);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        return this.getDefaultState().with(FACING, blockItemUseContext.getPlacementHorizontalFacing());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (!world.isRemote()) {
            if (state.getBlock() instanceof IHasGui) {
                IHasGui hasGui = (IHasGui) state.getBlock();
                if (hasGui.machineName() != null) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    INamedContainerProvider containerProvider = new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent(hasGui.machineName());
                        }

                        @Override
                        public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return hasGui.container(i, world, pos, playerInventory, playerEntity);
                        }
                    };
                    NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
                }
            }
        } else {
            FCLibMod.logger.warn("Wrong tileEntity type found, failed to create container");
        }
        return ActionResultType.SUCCESS;
    }
}
