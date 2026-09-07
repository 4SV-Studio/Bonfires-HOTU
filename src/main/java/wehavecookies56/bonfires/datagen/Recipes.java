package wehavecookies56.bonfires.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.setup.BlockSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

import java.util.concurrent.CompletableFuture;

public class Recipes extends RecipeProvider {

    DataGenerator generator;

    public Recipes(DataGenerator generator, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        super(generator.getPackOutput(), providerCompletableFuture);
        this.generator = generator;
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeConsumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockSetup.skint.get())
                .pattern("QQQ")
                .pattern("QLQ")
                .pattern("QQQ")
                .define('L', Items.AMETHYST_SHARD)
                .define('Q', ItemSetup.skint_little.get())
                .group(Bonfires.modid)
                .unlockedBy("has_amethyst_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemSetup.skint_little.get())
                .pattern("QQQ")
                .pattern("QLQ")
                .pattern("QQQ")
                .define('Q', Items.GOLD_INGOT)
                .define('L', Items.AMETHYST_SHARD)
                .group(Bonfires.modid)
                .unlockedBy("has_amethyst_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
                .save(recipeConsumer);

        /*ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemSetup.skint_up.get())
                .pattern("QQQ")
                .pattern("QLQ")
                .pattern("QQQ")
                .define('Q', ItemSetup.skint_little.get())
                .define('L', Items.AMETHYST_SHARD)
                .group(Bonfires.MOD_ID)
                .unlockedBy("has_amethyst_shard", InventoryChangeTrigger.TriggerInstance.hasItems(Items.AMETHYST_SHARD))
                .save(recipeConsumer);*/

    }
}