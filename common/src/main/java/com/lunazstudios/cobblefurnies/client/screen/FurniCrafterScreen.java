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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<FurniCraftingRecipe> recipes = new ArrayList<>();
    private double scroll = 0;
    private int hoveredRecipeIndex = -1;
    private int clickedY = -1;

    public FurniCrafterScreen(FurniCrafterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 204;
        this.inventoryLabelY = 110;
        updateRecipes();
    }

    private void updateRecipes() {
        this.recipes = menu.getAvailableRecipes().stream()
                .map(RecipeHolder::value)
                .toList();
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
        RecipeHolder<FurniCraftingRecipe> holder = menu.getAvailableRecipes().get(recipeIndex);
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
        renderScrollbar(graphics, mouseY);
    }

    private void renderRecipes(GuiGraphics graphics, int mouseX, int mouseY) {
        hoveredRecipeIndex = -1;
        int clipX = leftPos + GRID_X_OFFSET;
        int clipY = topPos + GRID_Y_OFFSET;
        graphics.enableScissor(clipX, clipY, clipX + WINDOW_WIDTH, clipY + WINDOW_HEIGHT);

        int totalRecipes = recipes.size();
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
            boolean canCraft = menu.canCraft(recipes.get(i));
            int textureU = 176 + (!canCraft ? BUTTON_SIZE : 0);
            int textureV = 0;
            graphics.blit(TEXTURE, x, y, textureU, textureV, BUTTON_SIZE, BUTTON_SIZE, 256, 256);
            graphics.renderFakeItem(recipes.get(i).getResultItem(null), x + 2, y + 2);

            if (mouseInGrid && mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= y && mouseY < y + BUTTON_SIZE) {
                hoveredRecipeIndex = i;
            }
        }
        graphics.disableScissor();
    }

    private void renderScrollbar(GuiGraphics graphics, int mouseY) {
        int maxScroll = getMaxScroll();
        int scrollbarX = leftPos + GRID_X_OFFSET + WINDOW_WIDTH + 3;
        int scrollbarY = topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION;
        int scrollbarPos = (int) ((getScrollAmount(mouseY) / (double) maxScroll) * (SCROLLBAR_AREA - SCROLLBAR_HEIGHT));
        int textureX = maxScroll > 0 ? SCROLLBAR_TEXTURE_ENABLED_X : SCROLLBAR_TEXTURE_DISABLED_X;
        graphics.blit(TEXTURE, scrollbarX, scrollbarY + scrollbarPos, textureX, SCROLLBAR_TEXTURE_Y, 12, SCROLLBAR_HEIGHT, 256, 256);
    }

    private int getMaxScroll() {
        int totalRows = (int) Math.ceil(recipes.size() / (double) RECIPES_PER_ROW);
        return Math.max(0, totalRows * BUTTON_SIZE - WINDOW_HEIGHT);
    }

    private double getScrollAmount(int mouseY) {
        return Mth.clamp(scroll + getScrollbarDelta(mouseY), 0, getMaxScroll());
    }

    private int getScrollbarDelta(int mouseY) {
        double scrollPerUnit = getMaxScroll() / (double) (SCROLLBAR_AREA - SCROLLBAR_HEIGHT);
        return clickedY != -1 ? (int) ((mouseY - clickedY) * scrollPerUnit) : 0;
    }

    private boolean isMouseWithinBounds(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            if (hoveredRecipeIndex != -1) {
                craftItem(hoveredRecipeIndex);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
            int scrollbarX = leftPos + GRID_X_OFFSET + WINDOW_WIDTH + 3;
            int scrollbarPos = (int) ((getScrollAmount((int) mouseY) / (double) getMaxScroll()) * (SCROLLBAR_AREA - SCROLLBAR_HEIGHT));
            if (isMouseWithinBounds(mouseX, mouseY, scrollbarX, topPos + GRID_Y_OFFSET - Y_OFFSET_CORRECTION + scrollbarPos, 12, SCROLLBAR_HEIGHT)) {
                clickedY = (int) mouseY;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && clickedY != -1) {
            scroll = getScrollAmount((int) mouseY);
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

    private void craftItem(int recipeIndex) {
        var registryAccess = Minecraft.getInstance().player.registryAccess();
        RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), registryAccess);
        CraftRecipeMessage message = new CraftRecipeMessage(menu.containerId, recipeIndex);
        CraftRecipeMessage.CODEC.encode(buf, message);
        NetworkManager.sendToServer(CFNetwork.CRAFT_RECIPE, buf);
    }

    public void updateRecipeButtons() {
        updateRecipes();
    }
}
