package com.lunazstudios.cobblefurnies.client.screen;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import com.lunazstudios.cobblefurnies.mixin.client.GuiGraphicsInvoker;
import com.lunazstudios.cobblefurnies.network.CFNetwork;
import com.lunazstudios.cobblefurnies.network.message.CraftRecipeMessage;
import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.lunazstudios.cobblefurnies.recipe.FurniCraftingRecipe;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class FurniCrafterScreen extends AbstractContainerScreen<FurniCrafterMenu> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CobbleFurnies.MOD_ID, "textures/gui/container/furnicrafter.png");

    private static final int RECIPES_PER_ROW = 6;
    private static final int BUTTON_SIZE = 20;
    private static final int GRID_X_OFFSET = 7;
    private static final int GRID_Y_OFFSET = 18;
    private static final int WINDOW_WIDTH = RECIPES_PER_ROW * BUTTON_SIZE;
    private static final int WINDOW_HEIGHT = 88;
    private static final int SCROLL_SPEED = 10;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int SCROLLBAR_AREA = 88;

    private static final int SCROLLBAR_TEXTURE_ENABLED_X = 176;
    private static final int SCROLLBAR_TEXTURE_DISABLED_X = 188;
    private static final int SCROLLBAR_TEXTURE_Y = 40;

    private static final int Y_OFFSET_CORRECTION = 0;

    private List<RecipeHolder<FurniCraftingRecipe>> allRecipes = new ArrayList<>();
    private List<RecipeHolder<FurniCraftingRecipe>> visibleRecipes = new ArrayList<>();

    private EditBox searchBox;
    private double scroll = 0;
    private int hoveredRecipeIndex = -1;
    private int clickedY = -1;
    private int scrollbarDragOffset = 0;
    private String lastQuery = "";

    public FurniCrafterScreen(FurniCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 204;
        this.inventoryLabelY = 110;
        updateRecipes();
    }

    @Override
    protected void init() {
        super.init();

        int x = this.leftPos + 80;
        int y = this.topPos + 5;

        this.searchBox = new EditBox(
                this.font,
                x,
                y,
                WINDOW_WIDTH - 30,
                12,
                Component.translatable("gui.cobblefurnies.furnicrafter.search")
        );
        this.searchBox.setMaxLength(64);
        this.searchBox.setBordered(true);
        this.searchBox.setResponder(s -> applySearchFilter());

        this.addRenderableWidget(this.searchBox);

        applySearchFilter();
    }

    private void updateRecipes() {
        this.allRecipes = new ArrayList<>(menu.getAvailableRecipes());
        applySearchFilter();
    }

    private void applySearchFilter() {
        String query = "";
        if (searchBox != null) {
            query = searchBox.getValue().trim().toLowerCase(Locale.ROOT);
        }

        if (!query.equals(lastQuery)) {
            this.scroll = 0;
            this.hoveredRecipeIndex = -1;
            lastQuery = query;
        }

        if (query.isEmpty()) {
            this.visibleRecipes = new ArrayList<>(this.allRecipes);
        } else {
            String q = query;
            this.visibleRecipes = this.allRecipes.stream()
                    .filter(holder -> holder.value()
                            .getResultItem(null)
                            .getHoverName()
                            .getString()
                            .toLowerCase(Locale.ROOT)
                            .contains(q))
                    .toList();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        if (hoveredRecipeIndex != -1 && isMouseWithinBounds(mouseX, mouseY, leftPos + GRID_X_OFFSET, topPos + GRID_Y_OFFSET, WINDOW_WIDTH, WINDOW_HEIGHT)) {
            renderRecipeTooltip(graphics, mouseX, mouseY, hoveredRecipeIndex);
        }
    }

    private void renderRecipeTooltip(GuiGraphics graphics, int mouseX, int mouseY, int recipeIndex) {
        RecipeHolder<FurniCraftingRecipe> holder = visibleRecipes.get(recipeIndex);
        FurniCraftingRecipe recipe = holder.value();

        List<ClientTooltipComponent> components = new ArrayList<>();
        components.add(new ClientTextTooltip(
                recipe.getResultItem(null).getHoverName().getVisualOrderText()
        ));

        if (!Screen.hasShiftDown()) {
            components.add(new ClientFurnicrafterRecipeTooltip(menu, recipe));
            components.add(new ClientTextTooltip(
                    Component.translatable("gui.cobblefurnies.hold_shift_for_details")
                            .withStyle(net.minecraft.ChatFormatting.GRAY)
                            .getVisualOrderText()
            ));
        } else {
            Map<Integer, Integer> counted = new HashMap<>();
            for (CountedIngredient ingredient : recipe.getMaterials()) {
                components.add(new ClientFurnicrafterRecipeIngredientTooltip(menu, ingredient, counted));
            }
        }

        ((GuiGraphicsInvoker) graphics)
                .invokeRenderTooltipInternal(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderRecipes(graphics, mouseX, mouseY);
        renderScrollbar(graphics);
    }

    private void renderRecipes(GuiGraphics graphics, int mouseX, int mouseY) {
        hoveredRecipeIndex = -1;
        int clipX = leftPos + GRID_X_OFFSET;
        int clipY = topPos + GRID_Y_OFFSET;
        graphics.enableScissor(clipX, clipY, clipX + WINDOW_WIDTH, clipY + WINDOW_HEIGHT);

        int totalRecipes = visibleRecipes.size();
        int startRow = (int) (scroll / BUTTON_SIZE);
        int startIndex = startRow * RECIPES_PER_ROW;
        int rowsToDraw = (int) Math.ceil(WINDOW_HEIGHT / (double) BUTTON_SIZE) + 1;
        int endIndex = Math.min(totalRecipes, startIndex + rowsToDraw * RECIPES_PER_ROW);

        boolean mouseInGrid = isMouseWithinBounds(mouseX, mouseY, clipX, clipY, WINDOW_WIDTH, WINDOW_HEIGHT);

        for (int i = startIndex; i < endIndex; i++) {
            int row = i / RECIPES_PER_ROW;
            int col = i % RECIPES_PER_ROW;
            int x = leftPos + GRID_X_OFFSET + col * BUTTON_SIZE;
            int y = topPos + GRID_Y_OFFSET + row * BUTTON_SIZE - (int) scroll - Y_OFFSET_CORRECTION;

            FurniCraftingRecipe recipe = visibleRecipes.get(i).value();
            boolean canCraft = menu.canCraft(recipe);

            int textureU = 176 + (!canCraft ? BUTTON_SIZE : 0);
            int textureV = 0;
            graphics.blit(TEXTURE, x, y, textureU, textureV, BUTTON_SIZE, BUTTON_SIZE, 256, 256);
            graphics.renderFakeItem(recipe.getResultItem(null), x + 2, y + 2);

            if (mouseInGrid && mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= y && mouseY < y + BUTTON_SIZE) {
                hoveredRecipeIndex = i;
            }
        }
        graphics.disableScissor();
    }

    private void renderScrollbar(GuiGraphics graphics) {
        int maxScroll = getMaxScroll();
        int scrollbarX = leftPos + GRID_X_OFFSET + WINDOW_WIDTH + 3;
        int scrollbarY = topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION;
        int trackHeight = SCROLLBAR_AREA - SCROLLBAR_HEIGHT;
        int currentHandlePos = maxScroll > 0 ? (int) ((scroll / (double) maxScroll) * trackHeight) : 0;
        int textureX = maxScroll > 0 ? SCROLLBAR_TEXTURE_ENABLED_X : SCROLLBAR_TEXTURE_DISABLED_X;
        graphics.blit(TEXTURE, scrollbarX, scrollbarY + currentHandlePos, textureX, SCROLLBAR_TEXTURE_Y, 12, SCROLLBAR_HEIGHT, 256, 256);
    }

    private int getMaxScroll() {
        int totalRows = (int) Math.ceil(visibleRecipes.size() / (double) RECIPES_PER_ROW);
        return Math.max(0, totalRows * BUTTON_SIZE - WINDOW_HEIGHT);
    }

    private boolean isMouseWithinBounds(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (hoveredRecipeIndex != -1) {
                int amount = getRequestedAmount();
                craftItem(hoveredRecipeIndex, amount);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            int scrollbarX = leftPos + GRID_X_OFFSET + WINDOW_WIDTH + 3;
            int scrollbarY = topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION;
            int maxScroll = getMaxScroll();
            int trackHeight = SCROLLBAR_AREA - SCROLLBAR_HEIGHT;
            int currentHandlePos = maxScroll > 0 ? (int) ((scroll / (double) maxScroll) * trackHeight) : 0;
            if (isMouseWithinBounds(mouseX, mouseY, scrollbarX, scrollbarY + currentHandlePos, 12, SCROLLBAR_HEIGHT)) {
                scrollbarDragOffset = (int) mouseY - (scrollbarY + currentHandlePos);
                clickedY = (int) mouseY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {

            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.searchBox.setFocused(false);
                return true;
            }

            if (this.minecraft != null
                    && this.minecraft.options != null
                    && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }

            if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.searchBox.charTyped(codePoint, modifiers)) {
                return true;
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    private int getRequestedAmount() {
        if (Screen.hasShiftDown()) {
            return 64;
        } else if (Screen.hasControlDown()) {
            return 16;
        }
        return 1;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (clickedY != -1) {
            int scrollbarY = topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION;
            int trackHeight = SCROLLBAR_AREA - SCROLLBAR_HEIGHT;
            int newHandlePos = (int) mouseY - scrollbarY - scrollbarDragOffset;
            newHandlePos = Mth.clamp(newHandlePos, 0, trackHeight);
            scroll = (newHandlePos / (double) trackHeight) * getMaxScroll();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && clickedY != -1) {
            clickedY = -1;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (clickedY == -1 && isMouseWithinBounds(mouseX, mouseY, leftPos + GRID_X_OFFSET, topPos + GRID_Y_OFFSET, WINDOW_WIDTH + 15, WINDOW_HEIGHT)) {
            scroll = Mth.clamp(scroll - deltaY * SCROLL_SPEED, 0, getMaxScroll());
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
    }

    private void craftItem(int recipeIndex, int amount) {
        if (recipeIndex < 0 || recipeIndex >= visibleRecipes.size()) return;

        RecipeHolder<FurniCraftingRecipe> holder = visibleRecipes.get(recipeIndex);
        int serverIndex = menu.getAvailableRecipes().indexOf(holder);
        if (serverIndex < 0) return;

        CraftRecipeMessage message = new CraftRecipeMessage(menu.containerId, serverIndex, amount);
        NetworkManager.sendToServer(message);
    }

    public void updateRecipeButtons() {
        updateRecipes();
    }
}

