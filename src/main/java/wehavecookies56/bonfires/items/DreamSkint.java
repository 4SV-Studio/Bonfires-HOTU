package wehavecookies56.bonfires.items;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DreamSkint extends Item {
    public DreamSkint(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide()) {
            CommandSourceStack sourceStack = pLevel.getServer().createCommandSourceStack();
            String command = "execute as " + pPlayer.getName().getString() + " in thoue:dreamworld run tp @s 0 100 0";
            ParseResults<CommandSourceStack> parseResults = pLevel.getServer().getCommands().getDispatcher().parse(command, sourceStack);
            pLevel.getServer().getCommands().performCommand(parseResults, command);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}