package fictioncraft.wintersteve25.fclib.common.mixin;

import fictioncraft.wintersteve25.fclib.api.events.PlayerInventoryChangedEvent;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
public class InventoryChangeTriggerMixin {
    @Inject(method = "trigger", at = @At(value = "HEAD", target = "Lnet/minecraft/advancements/criterion/InventoryChangeTrigger;trigger(Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;III)V"))
    public void onTrigger(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied, CallbackInfo ci) {
        if (!player.getEntityWorld().isRemote()) {
            PlayerInventoryChangedEvent event = new PlayerInventoryChangedEvent(player, inventory, stack, full, empty, occupied);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }
}
