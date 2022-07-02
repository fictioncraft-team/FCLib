package fictioncraft.wintersteve25.fclib.books.book;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BookItem extends Item {
    private final Book book;
    
    public BookItem(Properties pProperties, Book book) {
        super(pProperties);
        this.book = book;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide()) return InteractionResultHolder.pass(stack);
        BookRenderer.open(book);
        return InteractionResultHolder.sidedSuccess(stack, true);
    }
}
