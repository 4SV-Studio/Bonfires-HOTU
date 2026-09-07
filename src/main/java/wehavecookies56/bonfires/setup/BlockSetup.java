package wehavecookies56.bonfires.setup;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.SkintDownBlock;

import java.util.function.Supplier;

public class BlockSetup {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bonfires.modid);

    public static final Supplier<Block>
            skint = create("skint", SkintDownBlock::new);
    ;

    public static DeferredBlock<Block> create(String name, Supplier<? extends Block> block) {
        DeferredBlock<Block> newBlock = BLOCKS.register(name, block);
        ItemSetup.ITEMS.registerSimpleBlockItem(name, newBlock);
        return newBlock;
    }
}
