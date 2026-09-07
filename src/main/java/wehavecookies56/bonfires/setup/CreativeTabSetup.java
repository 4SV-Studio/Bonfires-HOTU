package wehavecookies56.bonfires.setup;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.blocks.SkintDownBlock;
import wehavecookies56.bonfires.items.EstusFlaskItem;

import java.util.function.Supplier;

public class CreativeTabSetup {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bonfires.modid);

    public static final Supplier<CreativeModeTab> tab = TABS.register(Bonfires.modid, () ->
        CreativeModeTab.builder()
                .title(Component.translatable(LocalStrings.ITEMGROUP_BONFIRES))
                .icon(() -> new ItemStack(BlockSetup.skint.get()))
                .displayItems((pParams, pOutput) -> {
                    ItemStack fullEstusFlask = new ItemStack(ItemSetup.estus_flask.get());
                    fullEstusFlask.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(3, 3));
                    pOutput.accept(fullEstusFlask);

                    ItemStack stack = new ItemStack(BlockSetup.skint.get());
                    stack.set(ComponentSetup.BONFIRE_DATA, new SkintDownBlock.BonfireData("", false));
                    stack.set(DataComponents.CUSTOM_NAME, Component.translatable(LocalStrings.TOOLTIP_UNLIT));
                    pOutput.accept(stack);
                }).build()
    );

}
