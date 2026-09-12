package wehavecookies56.bonfires.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wehavecookies56.bonfires.Bonfires;

public class CButton extends Button {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    Bonfires.modid,
                    "textures/gui/cbutton.png"
            );

    public CButton(int x, int y, int width, int height,
                   Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int textureWidth = 64;
        int textureHeight = 24;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (isHoveredOrFocused()) {
            guiGraphics.setColor(1, 1, 1, 1);
        } else {
            guiGraphics.setColor(0.88F, 0.88F, 0.88F, 1);
        }
        guiGraphics.blit(
                TEXTURE,
                getX(), getY(),
                getWidth(), getHeight(),
                0, 0,
                textureWidth, textureHeight,
                textureWidth, textureHeight
        );

        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                getMessage(),
                getX() + 15,
                getY() + (getHeight() - 8) / 2,
                0xFFFFFFFF
        );
    }
}
