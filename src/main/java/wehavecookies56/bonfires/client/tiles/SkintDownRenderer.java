package wehavecookies56.bonfires.client.tiles;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class SkintDownRenderer extends GeoBlockRenderer<BonfireTileEntity> {
    public SkintDownRenderer(BlockEntityRendererProvider.Context context) {
        super(new SkintDownModel());
    }
}
