package fictioncraft.wintersteve25.fclib.content.base.blocks;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.base.blocks.bounding.IHasBoundingBlocks;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IHasGui;
import fictioncraft.wintersteve25.fclib.utils.ISHandlerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FCLibMachineBlock<BE extends BlockEntity> extends FClibDirectionalBlock implements EntityBlock {

    // block builder properties
    private IHasGui gui;
    private final Class<BE> beClass;
    private final RegistryObject<BlockEntityType<BE>> blockEntityType;

    public FCLibMachineBlock(Properties properties, Class<BE> beClass, RegistryObject<BlockEntityType<BE>> blockEntityType) {
        super(properties);
        this.beClass = beClass;
        this.blockEntityType = blockEntityType;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (!world.isClientSide()) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            ItemStack heldItem = player.getItemInHand(hand);
            super.use(state, world, pos, player, hand, rayTraceResult);
            if (isCorrectTe(tileEntity)) {
                if (gui != null || this instanceof IHasGui) {
                    if (gui == null) {
                        gui = (IHasGui) this;
                    }
                    if (gui.machineName() != null) {
                        MenuProvider containerProvider = new MenuProvider() {
                            @Override
                            public Component getDisplayName() {
                                return gui.machineName();
                            }

                            @Override
                            public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                                return gui.container(i, world, pos, playerInventory, playerEntity);
                            }
                        };
                        NetworkHooks.openGui((ServerPlayer) player, containerProvider, tileEntity.getBlockPos());
                    }
                }
            } else {
                FCLibCore.LOGGER.warn("Wrong tileEntity type found, failed to create container");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        BlockEntity tile = world.getBlockEntity(pos);
        if (isCorrectTe(tile) && tile instanceof FCLibBE) {
            return ((FCLibBE) tile).canConnectRedstone(state, world, pos, side);
        }
        return super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (isCorrectTe(world.getBlockEntity(pos))) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof FCLibBE baseTE) {
                baseTE.onHarvested(world, pos, state, player);
                if (baseTE instanceof FCLibInvBE) {
                    FCLibInvBE te = (FCLibInvBE) world.getBlockEntity(pos);
                    if (te != null) {
                        if (te.hasItem()) {
                            ISHandlerHelper.dropInventory(te, world, state, pos, te.getInvSize());
                        }
                    }
                }
            }
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (!state.is(newState.getBlock()) || !newState.hasBlockEntity())) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof IHasBoundingBlocks) {
                ((IHasBoundingBlocks) tile).onBreak(state);
            }
            if (isCorrectTe(tile) && tile instanceof FCLibBE) {
                ((FCLibBE) tile).onBroken(state, world, pos, newState, isMoving);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return blockEntityType.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return FCLibBE::clientTicker;
        }
        return FCLibBE::serverTicker;
    }

    public boolean isCorrectTe(BlockEntity tile) {
        return getBeClass() != null && getBeClass().isInstance(tile);
    }

    public Class<? extends BlockEntity> getBeClass() {
        return beClass;
    }

    @Nullable
    public IHasGui getGui() {
        return gui == null ? this instanceof IHasGui ? (IHasGui) this : null : gui;
    }

    public void setGui(IHasGui gui) {
        this.gui = gui;
    }
}