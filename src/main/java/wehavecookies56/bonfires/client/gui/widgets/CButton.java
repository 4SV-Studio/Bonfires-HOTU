package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import wehavecookies56.bonfires.Bonfires;

public class CButton extends Button {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    Bonfires.modid,
                    "textures/gui/cButton.png"
            );

    public CButton(int x, int y, int width, int height,
                   Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int textureWidth = 64;
        int textureHeight = 24;

        guiGraphics.blit(
                TEXTURE,
                getX(), getY(),
                0, 0,
                getWidth(), getHeight(),
                textureWidth, textureHeight
        );

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                getMessage(),
                getX() + getWidth() / 2,
                getY() + (getHeight() - 8) / 2,
                0xFFFFFFFF
        );
    }
}