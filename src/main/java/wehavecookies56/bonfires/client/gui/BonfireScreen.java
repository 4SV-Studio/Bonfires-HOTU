package wehavecookies56.bonfires.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.glfw.GLFW;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.client.gui.widgets.*;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import javax.annotation.Nullable;

public class BonfireScreen extends Screen {

    private final ResourceLocation MENU = ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/gui/bonfire_menu.png");
    public final ResourceLocation TRAVEL_TEX = ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/gui/travel_menu.png");

    private BonfireCustomButton screenshot, info;

    private Button travel;
    private Button leave;
    private Button reinforce;
    @SuppressWarnings("unused")
    Button back;
    private Button next;
    private Button prev;
    private Button skill;
    private Button dreamteleport;

    public Map<ResourceKey<Level>, List<List<Bonfire>>> bonfires;

    private List<List<ResourceKey<Level>>> pages;

    private int currentPage = 0;
    public int bonfirePage = 0;

    private final BonfireTileEntity bonfire;
    private boolean travelOpen;

    private final int TRAVEL = 0;
    private final int LEAVE = 1;
    private final int REINFORCE = 20;
    private final int SKILL = 23;
    @SuppressWarnings("unused")
    public final int BACK = 2;
    private final int NEXT = 3;
    private final int PREV = 4;
    private final int TAB1 = 5;
    private final int TAB2 = 6;
    private final int TAB3 = 7;
    private final int TAB4 = 8;
    private final int TAB5 = 9;
    private final int TAB6 = 10;
    public final int BONFIRE1 = 11;
    private final int BONFIRE2 = 12;
    private final int BONFIRE3 = 13;
    private final int BONFIRE4 = 14;
    private final int BONFIRE5 = 15;
    private final int BONFIRE6 = 16;
    private final int BONFIRE7 = 17;
    private final int BONFIRE_NEXT = 18;
    private final int BONFIRE_PREV = 19;
    private final int DREAMTELEPORT = 24;

    private final int SCREENSHOT = 21;
    private final int INFO = 22;

    public int dimTabSelected = TAB1;
    public int bonfireSelected = 0;

    public Bonfire selectedInstance;

    public DimensionTabButton[] tabs;
    private BonfireButton[] bonfireButtons;
    private BonfirePageButton bonfire_next;
    private BonfirePageButton bonfire_prev;
    private DreamTeleport dream_teleport;

    private final int tex_height = 166;
    private final int travel_width = 195;
    public final int travel_height = 136;

    private static final ResourceLocation TITLE_BG = ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/gui/title_bg.png");

    private static final int TITLE_BG_TEX_WIDTH = 128;
    private static final int TITLE_BG_TEX_HEIGHT = 32;
    private static final int TITLE_BG_WIDTH = 192;
    private static final int TITLE_BG_HEIGHT = 32;

    private static final int TITLE_BG_PADDING = 16;

    private static final float TITLE_MAX_SCALE = 2.0F;
    private static final float TITLE_MIN_SCALE = 1.0F;
    private static final int TITLE_COLOR = new Color(248, 167, 0).getRGB();

    private static final ResourceLocation FADE_TEX = ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "textures/gui/fade.png");

    public Map<UUID, String> ownerNames = new HashMap<>();

    public BonfireRegistry registry;
    public List<ResourceKey<Level>> dimensions;
    public final boolean canReinforce;

    Screenshot screenshotImage;

    boolean showInfo = false;

    public BonfireScreen(BonfireTileEntity bonfire, Map<UUID, String> ownerNames, List<ResourceKey<Level>> dimensions, BonfireRegistry registry, boolean canReinforce) {
        super(Component.empty());
        this.bonfire = bonfire;
        this.ownerNames = ownerNames;
        this.registry = registry;
        minecraft = Minecraft.getInstance();
        this.dimensions = dimensions.stream().sorted((o1, o2) -> {
            if (o1.equals(Level.OVERWORLD)) {
                return -1;
            } else {
                return 0;
            }
        }).sorted((o1, o2) -> {
            if (o1.equals(Level.NETHER)) {
                if (o2.equals(Level.OVERWORLD)) {
                    return 1;
                }
                return -1;
            } else {
                return 0;
            }
        }).sorted((o1, o2) -> {
            if (o1.equals(Level.END)) {
                if (o2.equals(Level.NETHER)) {
                    return 1;
                } else if (o2.equals(Level.OVERWORLD)) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }).toList();
        this.canReinforce = canReinforce;
        if (BonfiresConfig.Client.renderScreenshotsInGui) {
            screenshotImage = new Screenshot(Minecraft.getInstance().getTextureManager(), ResourceLocation.fromNamespaceAndPath(Bonfires.modid, bonfire.getID().toString()));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            Minecraft.getInstance().setScreen(new BonfireScreen(bonfire, ownerNames, dimensions, registry, canReinforce));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void drawCenteredStringNoShadow(GuiGraphics guiGraphics, Font fr, String text, int x, int y, int color) {
        guiGraphics.drawString(fr, text,(x - (fr.width(text) / 2F)), (y - (fr.lineHeight / 2F)), color, false);
    }

    private Map<ResourceKey<Level>, List<List<Bonfire>>> createSeries(ResourceKey<Level> dimension) {
        List<Bonfire> bonfires = BonfireRegistry.sortBonfiresByTime(registry.getPrivateBonfiresByOwnerAndPublicPerDimension(Minecraft.getInstance().player.getUUID(), dimension.location()));
        bonfires.sort((o1, o2) -> {
            if (o1.getId().equals(bonfire.getID())) {
                return -1;
            } else {
                return 0;
            }
        });
        if (!bonfires.isEmpty()) {
            List<List<Bonfire>> book = new ArrayList<>();

            int plus = 1;
            if (bonfires.size() % 7 == 0)
                plus = 0;
            for (int i = 0; i < (bonfires.size() / 7) + plus; i++) {
                List<Bonfire> page;
                int start = i * 7;
                if (bonfires.size() < 7)
                    start = 0;
                if ((start) + 7 > bonfires.size())
                    page = bonfires.subList(start, bonfires.size());
                else
                    page = bonfires.subList(start, (start) + 7);
                book.add(page);
            }
            Map<ResourceKey<Level>, List<List<Bonfire>>> series = new HashMap<>();
            series.put(dimension, book);
            return series;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        if (bonfire.isRemoved()) {
            onClose();
        }
        if (bonfire.getBlockPos().distManhattan(new Vec3i((int) minecraft.player.position().x, (int) minecraft.player.position().y, (int) minecraft.player.position().z)) > minecraft.player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)+3) {
            onClose();
        }
    }

    @Override
    public void onClose() {
        if (screenshotImage != null) {
            screenshotImage.close();
        }
        super.onClose();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!ScreenshotUtils.isTakingScreenshot()) {
            //renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.setColor(1, 1, 1, 1);
            Font font = Minecraft.getInstance().font;
            if (travelOpen) {
                drawTravelMenu(guiGraphics, mouseX, mouseY, partialTicks);

                String formattedName;
                if (I18n.exists(LocalStrings.getDimensionKey(tabs[dimTabSelected - 5].getDimension()))) {
                    String dimName = (tabs[dimTabSelected - 5].getDimension().location().getPath().replaceAll("_", " "));
                    formattedName = WordUtils.capitalizeFully(dimName);
                } else {
                    formattedName = I18n.get(LocalStrings.getDimensionKey(tabs[dimTabSelected - 5].getDimension()));
                }
                //guiGraphics.drawString(font, formattedName + " (" + tabs[dimTabSelected - 5].getDimension().location() + ")", (int)((width / 2F) - 100), (int)((height / 2F) - 62), 1184274, false);

                if (bonfireSelected >= BONFIRE1) {
                    drawSelectedBonfire(guiGraphics, mouseX, mouseY, partialTicks);
                    for(Renderable renderable : this.renderables) {
                        renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
                    }                } else {
                    for(Renderable renderable : this.renderables) {
                        renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
                    }
                }
                if (selectedInstance != null) {
                    int nameX = (width / 2) - 10 + 12;
                    int nameY = (height / 2) - 45;
                    int nameEndX = nameX + font.width(selectedInstance.getName());
                    int nameEndY = nameY + font.lineHeight;
                    if (mouseX >= nameX && mouseX <= nameEndX && mouseY >= nameY && mouseY <= nameEndY) {
                        List<FormattedCharSequence> lines = new ArrayList<>();
                        lines.add(Component.translatable("ID: " + selectedInstance.getId()).getVisualOrderText());
                        lines.add(Component.translatable("TIME: " + selectedInstance.getTimeCreated().toString()).getVisualOrderText());
                        //guiGraphics.renderTooltip(font, lines, mouseX, mouseY);
                    }
                }
                for (DimensionTabButton currentTab : tabs) {
                    if (currentTab.visible) {
                        if (I18n.exists(LocalStrings.getDimensionKey(currentTab.getDimension()))) {
                            String dimName = (currentTab.getDimension().location().getPath().replaceAll("_", " "));
                            formattedName = WordUtils.capitalizeFully(dimName);
                        } else {
                            formattedName = I18n.get(LocalStrings.getDimensionKey(currentTab.getDimension()));
                        }
                        if (mouseX >= currentTab.getX() && mouseX <= currentTab.getX() + currentTab.getWidth() && mouseY >= currentTab.getY() && mouseY <= currentTab.getY() + currentTab.getHeight()) {
                            guiGraphics.renderTooltip(font, Component.translatable(formattedName), mouseX, mouseY);
                        }
                    }
                }

                String pages = "0/0";
                if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                    pages = (bonfirePage + 1) + "/" + bonfires.get(tabs[dimTabSelected - 5].getDimension()).size();
                }
                int xZero = (width / 2) - (travel_width / 2) + 16;
                int yZero = (height / 2) - (travel_height / 2) + 128 - 17;
                guiGraphics.drawString(font, pages, xZero + (55 / 2) - font.width(pages) / 2, yZero + (14 / 2) - font.lineHeight / 2, 0xFFFFFF);
            } else {
                int tex_height = 32;

                //guiGraphics.blit(MENU, (width / 4) - (tex_width / 2), (height / 2) - (tex_height / 2), 0, 0, tex_width, tex_height);
                for(Renderable renderable : this.renderables) {
                    renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
                }
                String name = "";
                Bonfire currentBonfire = registry.getBonfire(bonfire.getID());
                if (currentBonfire != null) {
                    name = currentBonfire.getName();
                    if (!currentBonfire.isPublic()) {
                        drawCenteredStringNoShadow(guiGraphics, font, Component.translatable(LocalStrings.TEXT_PRIVATE).getString(), (width / 4), (height / 2) - (tex_height / 2) + 20, new Color(255, 255, 255).getRGB());
                    }
                }

                blitTransparent(FADE_TEX, 0, 0, 32, Minecraft.getInstance().getWindow().getGuiScaledHeight(), 0F, 0F, 32, 32, 32, 32, guiGraphics);

                blitTransparent(TITLE_BG, 5, 10, TITLE_BG_WIDTH, TITLE_BG_HEIGHT, 0F, 0F, TITLE_BG_TEX_WIDTH, TITLE_BG_TEX_HEIGHT, TITLE_BG_TEX_WIDTH, TITLE_BG_TEX_HEIGHT, guiGraphics);
                drawTitle(guiGraphics, font, name);
            }
        }
    }

    private void blitTransparent(ResourceLocation atlasLocation, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, GuiGraphics guiGraphics) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(atlasLocation, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }

    private void drawTitle(GuiGraphics guiGraphics, Font font, String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        int maxWidth = TITLE_BG_WIDTH - (TITLE_BG_PADDING * 2);
        int textWidth = font.width(name);
        float scale = TITLE_MAX_SCALE;
        if (textWidth * scale > maxWidth) {
            scale = Math.max(TITLE_MIN_SCALE, maxWidth / (float) textWidth);
        }
        String text = name;
        if (textWidth * scale > maxWidth) {
            String ellipsis = "...";
            int room = (int) (maxWidth / scale) - font.width(ellipsis);
            text = font.plainSubstrByWidth(text, Math.max(room, 0)) + ellipsis;
            textWidth = font.width(text);
        }

        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(5 + (TITLE_BG_WIDTH / 2F), 10 + (TITLE_BG_HEIGHT / 2F), 0F);
        guiGraphics.pose().scale(scale, scale, 1F);
        guiGraphics.drawString(font, text, -textWidth / 2F, -(font.lineHeight - 1) / 2F, TITLE_COLOR, false);
        guiGraphics.pose().popPose();
    }

    public Bonfire getSelectedBonfire() {
        if (bonfireSelected >= BONFIRE1) {
            if (bonfires != null) {
                if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                    return bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).get(bonfireSelected - 11);
                }
            }
        }
        return null;
    }

    private void drawSelectedBonfire(GuiGraphics guiGraphics, int mouseX, int mouseY, @SuppressWarnings("unused") float partialTicks) {
        if (selectedInstance != null) {
            int nameX = (width / 2) - 10 + 12;
            int nameY = (height / 2) - 45;
            if (BonfiresConfig.Client.renderScreenshotsInGui && screenshotImage != null && screenshotImage.textureLocation() != null && !noScreenshot) {
                guiGraphics.blit(screenshotImage.textureLocation(), nameX-3, nameY-5, (float) ScreenshotUtils.width /2, 0, ScreenshotUtils.width, ScreenshotUtils.height, ScreenshotUtils.width*2, ScreenshotUtils.height);
            }

            /*if (showInfo) {
                Font font = Minecraft.getInstance().font;
                guiGraphics.drawString(font, selectedInstance.getName(), nameX, nameY, new Color(255, 255, 255).getRGB());
                guiGraphics.drawString(font, "X:" + selectedInstance.getPos().getX() + " Y:" + selectedInstance.getPos().getY() + " Z:" + selectedInstance.getPos().getZ(), nameX, nameY + font.lineHeight + 3, new Color(255, 255, 255).getRGB());
                guiGraphics.drawString(font, ownerNames.get(selectedInstance.getOwner()), nameX, nameY + (font.lineHeight + 3) * 2, new Color(255, 255, 255).getRGB());
            }*/
        }
    }

    @Nullable
    File getBonfireScreenshot(String bonfireName, UUID bonfireUUID) {
        Path screenshotsDir = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "bonfires/");
        if (Files.exists(screenshotsDir)) {
            File screenshot = null;
            File[] files = screenshotsDir.toFile().listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String nameNoInvalid = bonfireName.replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
                    if (file.isFile() && file.getName().equals(nameNoInvalid + "_" + bonfireUUID.toString() + ".png")) {
                        screenshot = file;
                        break;
                    }
                }
            }
            return screenshot;
        }
        return null;
    }

    private void drawTravelMenu(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int trueWidth = 219;
        //RenderHelper.enableGUIStandardItemLighting();
        for (DimensionTabButton tab : tabs) {
            tab.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        guiGraphics.blit(TRAVEL_TEX, (width / 2) - (trueWidth / 2), (height / 2) - (travel_height / 2), 0, 0, trueWidth, travel_height);
    }

    public void action(int id) {
        action(id, false);
    }

    public void action(int id, boolean closesScreen) {
        switch (id) {
            case SCREENSHOT:
                if (selectedInstance != null) {
                    if (bonfire.getID().equals(selectedInstance.getId())) {
                        ScreenshotUtils.startScreenshotTimer(selectedInstance.getName(), selectedInstance.getId());
                    }
                }
                break;
            case INFO:
                showInfo = !showInfo;
                break;
            case TRAVEL:
                if (!travelOpen) {
                    travelOpen = true;
                    PacketHandler.sendToServer(new RequestDimensionsFromServer());
                } else {
                    if (selectedInstance != null) {
                        Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, Minecraft.getInstance().player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                        Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, selectedInstance.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                        PacketHandler.sendToServer(new Travel(selectedInstance));
                        String formattedDimName;
                        if (I18n.exists(LocalStrings.getDimensionKey(selectedInstance.getDimension()))) {
                            String dimName = (selectedInstance.getDimension().location().getPath().replaceAll("_", " "));
                            formattedDimName = WordUtils.capitalizeFully(dimName);
                        } else {
                            formattedDimName = I18n.get(LocalStrings.getDimensionKey(selectedInstance.getDimension()));
                        }
                        Gui gui = Minecraft.getInstance().gui;
                        gui.setTitle(Component.translatable(selectedInstance.getName()));
                        gui.setSubtitle(Component.translatable(formattedDimName));
                        gui.setTimes(10, 20, 10);
                        onClose();
                        closesScreen = true;
                    }
                }
                break;
            case LEAVE:
                onClose();
                break;
            case SKILL:
                onClose();

                simulateKeyPress(InputConstants.KEY_F25); // "-"
                break;

            case NEXT:
                if (currentPage != pages.size()-1) {
                    currentPage++;
                    dimTabSelected = TAB1;
                    bonfireSelected = 0;
                }
                break;
            case PREV:
                if (currentPage != 0) {
                    currentPage--;
                    dimTabSelected = TAB1;
                    bonfireSelected = 0;
                }
                break;
            case BONFIRE_NEXT:
                if (bonfirePage != bonfires.get(tabs[dimTabSelected - 5].getDimension()).size()-1) {
                    bonfirePage++;
                    bonfireSelected = 0;
                }
                break;
            case BONFIRE_PREV:
                if (bonfirePage != 0) {
                    bonfirePage--;
                    bonfireSelected = 0;
                }
                break;
            case TAB1:
            case TAB2:
            case TAB3:
            case TAB4:
            case TAB5:
            case TAB6:
                dimTabSelected = id;
                bonfireSelected = 0;
                if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                    if (!bonfires.get(tabs[dimTabSelected - 5].getDimension()).isEmpty()) {
                        if (!bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(0).isEmpty()) {
                            bonfireSelected = BONFIRE1;
                            selectedInstance = registry.getBonfires().get(bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(0).get(0).getId());
                            loadBonfireScreenshot();
                        }
                    }
                }
                bonfirePage = 0;
                break;
            case BONFIRE1:
            case BONFIRE2:
            case BONFIRE3:
            case BONFIRE4:
            case BONFIRE5:
            case BONFIRE6:
            case BONFIRE7:
                bonfireSelected = id;
                selectedInstance = getSelectedBonfire();
                if (BonfiresConfig.Client.renderScreenshotsInGui) {
                    loadBonfireScreenshot();
                }
                break;
            case REINFORCE:
                minecraft.setScreen(new ReinforceScreen(this));
                break;
            case DREAMTELEPORT:
                
                break;
        }
        updateButtons();
        if (!closesScreen) {
            PacketHandler.sendToServer(new RequestDimensionsFromServer());
        }
    }

    private void simulateKeyPress(int key) {
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();

        InputConstants.Key inputKey = InputConstants.Type.KEYSYM.getOrCreate(key);

        GLFW.glfwPostEmptyEvent();
        Minecraft.getInstance().keyboardHandler.keyPress(windowHandle, inputKey.getValue(), 0, GLFW.GLFW_PRESS, 0);

        Minecraft.getInstance().keyboardHandler.keyPress(windowHandle, inputKey.getValue(), 0, GLFW.GLFW_RELEASE, 0);
    }


    public void loadBonfireScreenshot() {
        if (selectedInstance != null) {
            File screenshotFile = getBonfireScreenshot(selectedInstance.getName(), selectedInstance.getId());
            if (screenshotFile != null) {
                try {
                    if (screenshotImage != null) {
                        screenshotImage.close();
                        screenshotImage = new Screenshot(Minecraft.getInstance().getTextureManager(), ResourceLocation.fromNamespaceAndPath(Bonfires.modid, bonfire.getID().toString()));
                    }
                    screenshotImage.upload(NativeImage.read(new FileInputStream(screenshotFile)));
                    noScreenshot = false;
                    info.visible = true;
                    info.active = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                noScreenshot = true;
            }
        }
    }

    boolean noScreenshot = false;

    private void updateButtons() {
        for (DimensionTabButton tab : tabs) {
            tab.visible = false;
        }
        if (travelOpen) {
            if (bonfireSelected >= BONFIRE1) {
                travel.visible = true;
                travel.active = selectedInstance != null && !selectedInstance.getId().equals(bonfire.getID());
                info.visible = !noScreenshot;
                info.active = !noScreenshot;
                if (BonfiresConfig.Client.renderScreenshotsInGui && selectedInstance != null) {
                    if (bonfire.getID().equals(selectedInstance.getId())) {
                        screenshot.visible = true;
                        screenshot.active = true;
                        if (noScreenshot) {
                            screenshot.setY((height / 2) - 50);
                        } else {
                            screenshot.setY((height / 2) - 36);
                        }
                    } else {
                        screenshot.visible = false;
                        screenshot.active = false;
                    }
                }
            } else {
                travel.visible = false;
                info.visible = false;
                screenshot.visible = false;
                info.active = false;
                screenshot.active = false;
            }
            for (int i = 0; i < tabs.length; i++) {
                if (i < pages.get(currentPage).size()) {
                    tabs[i].visible = true;
                    tabs[i].setDimension(pages.get(currentPage).get(i));
                }
            }
            for (int i = 0; i < bonfireButtons.length; i++) {
                if (tabs[dimTabSelected - 5] != null) {
                    if (bonfires != null) {
                        if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                            if (i < bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).size()) {
                                bonfireButtons[i].visible = true;
                                bonfireButtons[i].setBonfire(bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage).get(i));
                            } else {
                                bonfireButtons[i].visible = false;
                            }
                        } else {
                            bonfireButtons[i].visible = false;
                        }
                    }
                }
            }
            leave.visible = false;
            travel.visible = false;
            //dreamteleport.visible = false;
            next.visible = true;
            prev.visible = true;
            bonfire_prev.visible = true;
            bonfire_next.visible = true;
            prev.active = currentPage != 0;
            next.active = currentPage != pages.size() - 1;
            bonfire_prev.active = bonfirePage != 0;
            bonfire_next.active = bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null && bonfirePage != bonfires.get(tabs[dimTabSelected - 5].getDimension()).size() - 1;

        } else {
            bonfire_prev.visible = false;
            bonfire_prev.active = false;
            bonfire_next.visible = false;
            bonfire_next.active = false;
            travel.visible = true;
            if (registry.getBonfire(bonfire.getID()) != null) {
                if (!registry.getBonfire(bonfire.getID()).isPublic()) {
                    travel.setY((height / 2) - (tex_height / 2) + 30);
                    reinforce.setY((height / 2) - (tex_height / 2) + 51);
                    leave.setY((height / 2) - (tex_height / 2) + 72);
                    skill.setY((height / 2) - (tex_height / 2) + 72 + 21);
                }
            }
            leave.visible = true;
            //dreamteleport.visible = false;
            next.visible = false;
            prev.visible = false;
            prev.active = false;
            next.active = false;
            info.visible = false;
            info.active = false;
            screenshot.visible = false;
            screenshot.active = false;
            for (DimensionTabButton tab : tabs) {
                tab.visible = false;
            }
            for (BonfireButton bonfire : bonfireButtons) {
                bonfire.visible = false;
            }
        }
        if (!canReinforce) {
            reinforce.visible = false;
            leave.setY(reinforce.getY());
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !ScreenshotUtils.isTakingScreenshot();
    }

    @Override
    protected void init() {
        super.init();
        pages = new ArrayList<>();
        bonfires = new HashMap<>();
        int selectedX = (width / 2) - 17;
        int selectedY = (height / 2) - 50;
        addRenderableWidget(screenshot = new BonfireCustomButton(SCREENSHOT, selectedX + 16 + (103 - 16), selectedY, BonfireCustomButton.ButtonType.SCREENSHOT, button -> action(SCREENSHOT)));
        addRenderableWidget(info = new BonfireCustomButton(INFO, selectedX + 16 + (103 - 16), selectedY, BonfireCustomButton.ButtonType.INFO, button -> action(INFO)));

        addRenderableWidget(travel = new CButton(5, (height / 2) - (tex_height / 2) + 25, 124, 25, Component.translatable(LocalStrings.BUTTON_TRAVEL), button -> action(TRAVEL)));
        addRenderableWidget(leave = new CButton(5, (height / 2) - (tex_height / 2) + 62, 124, 25, Component.translatable(LocalStrings.BUTTON_LEAVE), button -> action(LEAVE, true)));

        addRenderableWidget(skill = Button.builder(Component.translatable(LocalStrings.BUTTON_SKILL), button -> action(SKILL, true)).pos((width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 62 + 21).size(80, 20).build());
        addRenderableWidget(reinforce = Button.builder(Component.translatable(LocalStrings.BUTTON_REINFORCE), button -> action(REINFORCE, true)).pos((width / 4) - (80 / 2), (height / 2) - (tex_height / 2) + 41).size(80, 20).build());

        skill.visible = false;
        reinforce.visible = false;

        addRenderableWidget(next = Button.builder(Component.literal(">"), button -> action(NEXT)).pos(0, 0).size(20, 20).build());
        addRenderableWidget(prev = Button.builder(Component.literal("<"), button -> action(PREV)).pos(20, 0).size(20, 20).build());
        addRenderableWidget(bonfire_next = new BonfirePageButton(this, BONFIRE_NEXT, 0, 0, true));
        addRenderableWidget(bonfire_prev = new BonfirePageButton(this, BONFIRE_PREV, 8, 0, false));
        tabs = new DimensionTabButton[] {
                new DimensionTabButton(this, TAB1, 0, 0),
                new DimensionTabButton(this, TAB2, 0, 0),
                new DimensionTabButton(this, TAB3, 0, 0),
                new DimensionTabButton(this, TAB4, 0, 0),
                new DimensionTabButton(this, TAB5, 0, 0),
                new DimensionTabButton(this, TAB6, 0, 0)
        };
        bonfireButtons = new BonfireButton[] {
                new BonfireButton(this, BONFIRE1, 0, 0),
                new BonfireButton(this, BONFIRE2, 0, 0),
                new BonfireButton(this, BONFIRE3, 0, 0),
                new BonfireButton(this, BONFIRE4, 0, 0),
                new BonfireButton(this, BONFIRE5, 0, 0),
                new BonfireButton(this, BONFIRE6, 0, 0),
                new BonfireButton(this, BONFIRE7, 0, 0)
        };
        for (int i = 0; i < tabs.length; i++) {
            addRenderableWidget(tabs[i]);
            int sixTabs = 6 * 28;
            int gap = travel_width - sixTabs;
            tabs[i].setX(((width) / 2 - (travel_width / 2) + (i * 28) + gap / 2));
            tabs[i].setY((height / 2) - (travel_width / 2) + 1);
        }
        for (int i = 0; i < bonfireButtons.length; i++) {
            addRenderableWidget(bonfireButtons[i]);
            bonfireButtons[i].setX((width / 2) - 88 - 12);
            bonfireButtons[i].setY((height / 2) + (bonfireButtons[i].getHeight()) * i - 50);
        }
        prev.setX(((width) / 2 - (travel_width / 2)) - 8);
        prev.setY((height / 2) - (travel_width / 2) + 6);
        int sixTabs = 6 * 28;
        int gap = travel_width - sixTabs;
        next.setX(((width) / 2 - (travel_width / 2) + (6 * 28) + gap / 2));
        next.setY((height / 2) - (travel_width / 2) + 6);
        bonfire_prev.setX((width / 2) - (travel_width / 2) + 16);
        bonfire_prev.setY((height / 2) - (travel_height / 2) + 128 - 17);
        bonfire_next.setX((width / 2) - (travel_width / 2) + 63);
        bonfire_next.setY((height / 2) - (travel_height / 2) + 128 - 17);
        updateBonfires();
        //dimensions = Lists.reverse(dimensions);
        int plus = 1;
        if (dimensions.size() % 6 == 0)
            plus = 0;
        for (int i = 0; i < (dimensions.size() / 6)+ plus; i++) {
            int start = i * 6;
            if (dimensions.size() < 6)
                start = 0;
            if ((start)+6 > dimensions.size())
                pages.add(dimensions.subList(start, dimensions.size()));
            else {
                pages.add(dimensions.subList(start, (start) + 6));
            }
        }
        for (int i = 0; i < pages.size(); i++) {
            for (int j = 0; j < pages.get(i).size(); j++) {
                if (Minecraft.getInstance().level.dimension().location().equals(pages.get(i).get(j).location())) {
                    currentPage = i;
                    dimTabSelected = j+TAB1;
                }
            }
        }
        bonfireSelected = BONFIRE1;
        selectedInstance = registry.getBonfire(bonfire.getID());
        loadBonfireScreenshot();
        updateButtons();
    }

    public void updateDimensionsFromServer(BonfireRegistry registry, List<ResourceKey<Level>> dimensions, Map<UUID, String> ownerNames) {
        this.dimensions = dimensions.stream().sorted((o1, o2) -> {
            if (o1.equals(Level.OVERWORLD)) {
                return -1;
            } else {
                return 0;
            }
        }).sorted((o1, o2) -> {
            if (o1.equals(Level.NETHER)) {
                if (o2.equals(Level.OVERWORLD)) {
                    return 1;
                }
                return -1;
            } else {
                return 0;
            }
        }).sorted((o1, o2) -> {
            if (o1.equals(Level.END)) {
                if (o2.equals(Level.NETHER)) {
                    return 1;
                } else if (o2.equals(Level.OVERWORLD)) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }).toList();
        this.registry = registry;
        this.ownerNames = ownerNames;
        updateBonfires();
        updateButtons();
    }

    private void updateBonfires() {
        bonfires.clear();
        for (ResourceKey<Level> dim : dimensions) {
            Map<ResourceKey<Level>, List<List<Bonfire>>> series = createSeries(dim);
            if (series != null) {
                if (series.get(dim) != null) {
                    bonfires.put(dim, series.get(dim));
                }
            }
        }
        if (selectedInstance != null && bonfireSelected != 0) {
            if (bonfires.get(tabs[dimTabSelected - 5].getDimension()) != null) {
                List<Bonfire> bonfiresInCurrentPage = bonfires.get(tabs[dimTabSelected - 5].getDimension()).get(bonfirePage);
                if (bonfiresInCurrentPage.stream().filter(b -> selectedInstance.getId().equals(b.getId())).toList().isEmpty()) {
                    selectedInstance = null;
                    bonfireSelected = 0;
                }
            } else {
                selectedInstance = null;
                bonfireSelected = 0;
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static class Screenshot implements AutoCloseable {
        private static final ResourceLocation MISSING_LOCATION = null;
        private final TextureManager textureManager;
        private final ResourceLocation textureLocation;

        @Nullable
        private DynamicTexture texture;
        private boolean closed;
        private Screenshot(TextureManager pTextureManager, ResourceLocation pTextureLocation) {
            this.textureManager = pTextureManager;
            this.textureLocation = pTextureLocation;
        }

        public void upload(NativeImage pImage) {
            try {
                this.checkOpen();
                if (this.texture == null) {
                    this.texture = new DynamicTexture(pImage);
                } else {
                    this.texture.setPixels(pImage);
                    this.texture.upload();
                }

                this.textureManager.register(this.textureLocation, this.texture);
            } catch (Throwable throwable) {
                pImage.close();
                this.clear();
                throw throwable;
            }
        }

        public void clear() {
            this.checkOpen();
            if (this.texture != null) {
                this.textureManager.release(this.textureLocation);
                this.texture.close();
                this.texture = null;
            }
        }

        public int getHeight() {
            if (texture != null) {
                return texture.getPixels().getHeight();
            } else {
                return 0;
            }
        }

        public int getWidth() {
            if (texture != null) {
                return texture.getPixels().getWidth();
            } else {
                return 0;
            }
        }

        public ResourceLocation textureLocation() {
            return this.texture != null ? this.textureLocation : MISSING_LOCATION;
        }

        public void close() {
            this.clear();
            this.closed = true;
        }

        private void checkOpen() {
            if (this.closed) {
                throw new IllegalStateException("Icon already closed");
            }
        }
    }
}
