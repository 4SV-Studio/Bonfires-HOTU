package wehavecookies56.bonfires.client.items;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.items.SkintUpItem;

public class SkintUpModel extends GeoModel<SkintUpItem> {
    @Override
    public ResourceLocation getModelResource(SkintUpItem skintUpItem) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "geo/skint_up.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SkintUpItem skintUpItem) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/item/skint_up.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SkintUpItem skintUpItem) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "animations/skint_up.animation.json");
    }
}
