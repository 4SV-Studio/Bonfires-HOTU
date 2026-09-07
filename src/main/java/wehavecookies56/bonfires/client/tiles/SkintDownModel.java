package wehavecookies56.bonfires.client.tiles;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class SkintDownModel extends GeoModel<BonfireTileEntity> {
    @Override
    public ResourceLocation getModelResource(BonfireTileEntity bonfireTileEntity) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "geo/skint.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BonfireTileEntity bonfireTileEntity) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/block/skint.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BonfireTileEntity bonfireTileEntity) {
        return ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "animations/skint.animation.json");
    }

    @Override
    public void setCustomAnimations(BonfireTileEntity animatable, long instanceId, AnimationState<BonfireTileEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone mainCrystals = getAnimationProcessor().getBone("GuidingSkint");
        GeoBone cursedCrystals = getAnimationProcessor().getBone("InfectedSkint");
        if (mainCrystals != null) {
            mainCrystals.setHidden(!animatable.isLit());
        }
    }
}
