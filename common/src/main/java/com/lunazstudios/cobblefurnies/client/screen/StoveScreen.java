package com.lunazstudios.cobblefurnies.client.screen;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.block.campfirepot.CampfirePotColor;
import com.cobblemon.mod.common.block.entity.CampfireBlockEntity;
import com.cobblemon.mod.common.client.gui.cookingpot.CookButton;
import com.cobblemon.mod.common.integration.jei.CobblemonJeiPlugin;
import com.cobblemon.mod.common.integration.jei.cooking.CampfirePotRecipeCategory;
import com.cobblemon.mod.common.mixin.accessor.RecipeBookComponentAccessor;
import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import com.lunazstudios.cobblefurnies.menu.slot.SeasoningSlot;
import com.lunazstudios.cobblefurnies.network.message.ToggleStoveLidMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class StoveScreen extends AbstractContainerScreen<StoveMenu> implements RecipeUpdateListener {

    private static final int BACKGROUND_HEIGHT = 166;
    private static final int BACKGROUND_WIDTH = 176;

    public static final int COOK_PROGRESS_HEIGHT = 12;
    public static final int COOK_PROGRESS_WIDTH = 22;

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("cobblemon", "textures/gui/campfirepot/campfire_pot.png");
    public static final ResourceLocation COOK_PROGRESS_SPRITE =
            ResourceLocation.fromNamespaceAndPath("cobblemon", "textures/gui/campfirepot/cook_progress.png");
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("recipe_book/campfire_pot_filter_enabled"),
            ResourceLocation.withDefaultNamespace("recipe_book/campfire_pot_filter_disabled"),
            ResourceLocation.withDefaultNamespace("recipe_book/campfire_pot_filter_enabled_highlighted"),
            ResourceLocation.withDefaultNamespace("recipe_book/campfire_pot_filter_disabled_highlighted")
    );

    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();
    private boolean widthTooNarrow = false;

    private CookButton cookButton;

    public StoveScreen(StoveMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable("container.cobblefurnies.stove"));
        this.imageWidth = BACKGROUND_WIDTH;
        this.imageHeight = BACKGROUND_HEIGHT;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;

        this.recipeBookComponent.init(
                this.width,
                this.height,
                this.minecraft,
                this.widthTooNarrow,
                this.menu
        );

        if (this.recipeBookComponent.isVisible()) {
            StateSwitchingButton recipeBookFilterButton =
                    ((RecipeBookComponentAccessor) this.recipeBookComponent).getFilterButton();
            overrideRecipeBookFilterButton(recipeBookFilterButton);
        }

        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        int topPos = (this.height - BACKGROUND_HEIGHT) / 2;
        this.topPos = topPos;

        ImageButton recipeBookButton = new ImageButton(
                this.leftPos + 5,
                topPos + 35,
                20,
                18,
                RecipeBookComponent.RECIPE_BUTTON_SPRITES,
                button -> {
                    this.recipeBookComponent.toggleVisibility();
                    if (this.recipeBookComponent.isVisible()) {
                        StateSwitchingButton filterButton =
                                ((RecipeBookComponentAccessor) this.recipeBookComponent).getFilterButton();
                        overrideRecipeBookFilterButton(filterButton);
                    }
                    this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
                    button.setPosition(this.leftPos + 5, topPos + 35);
                }
        );

        this.addWidget(this.recipeBookComponent);
        this.addRenderableWidget(recipeBookButton);

        this.titleLabelX = this.imageWidth - (BACKGROUND_WIDTH / 2) - (this.font.width(this.title) / 2);
        this.titleLabelY = 6;

        if (this.cookButton != null) {
            removeWidget(cookButton);
        }

        int colorIndex = this.menu.containerData.get(CampfireBlockEntity.COOKING_POT_COLOR_INDEX);
        colorIndex = Math.max(0, Math.min(colorIndex, CampfirePotColor.values().length - 1));
        CampfirePotColor color = CampfirePotColor.values()[colorIndex];

        boolean isLidClosed = this.menu.containerData.get(CampfireBlockEntity.IS_LID_OPEN_INDEX) == 0;

        this.cookButton = new CookButton(
                this.leftPos + 97,
                topPos + 56,
                isLidClosed,
                color,
                button -> NetworkManager.sendToServer(new ToggleStoveLidMessage(this.menu.containerId))
        );

        addRenderableWidget(this.cookButton);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics context, float partialTick, int mouseX, int mouseY) {
        // Fundo padrão, igual ao do Cobblemon visualmente
        int x = this.leftPos;
        int y = (this.height - BACKGROUND_HEIGHT) / 2;

        GuiUtilsKt.blitk(
                context.pose(),
                BACKGROUND,
                x,
                y,
                BACKGROUND_HEIGHT,
                BACKGROUND_WIDTH
        );

        int cookProgress = (int) Math.ceil(this.menu.getBurnProgress() * COOK_PROGRESS_WIDTH);

        GuiUtilsKt.blitk(
                context.pose(),
                COOK_PROGRESS_SPRITE,
                x + 96,
                y + 39,
                COOK_PROGRESS_HEIGHT,
                cookProgress,
                0,
                0,
                COOK_PROGRESS_WIDTH
        );
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        int colorIndex = this.menu.containerData.get(CampfireBlockEntity.COOKING_POT_COLOR_INDEX);
        colorIndex = Math.max(0, Math.min(colorIndex, CampfirePotColor.values().length - 1));
        this.cookButton.setColor(CampfirePotColor.values()[colorIndex]);
        this.cookButton.setSelected(this.menu.containerData.get(CampfireBlockEntity.IS_LID_OPEN_INDEX) == 0);
        this.cookButton.setPosition(this.leftPos + 97, this.topPos + 56);

        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBackground(context, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.render(context, mouseX, mouseY, partialTicks);
        } else {
            super.render(context, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.render(context, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(context, this.leftPos, this.topPos, true, partialTicks);
        }

        Slot resultSlot = this.menu.slots.get(this.menu.getResultSlotIndex());
        RecipeHolder<?> optionalRecipe = this.menu.currentActiveRecipe;

        if (optionalRecipe != null
                && !this.menu.previewItem.isEmpty()
                && !resultSlot.hasItem()) {

            ItemStack resultItem = this.menu.previewItem;

            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1F, 1F, 1F, 0.5F);

            context.renderFakeItem(resultItem, this.leftPos + resultSlot.x, this.topPos + resultSlot.y);
            context.renderItemDecorations(this.font, resultItem, this.leftPos + resultSlot.x, this.topPos + resultSlot.y);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();

            if (isHovering(resultSlot.x, resultSlot.y, 16, 16, mouseX, mouseY)
                    && this.menu.getCarried().isEmpty()) {
                context.renderTooltip(
                        this.font,
                        getTooltipFromContainerItem(resultItem),
                        resultItem.getTooltipImage(),
                        mouseX,
                        mouseY
                );
            }
        }

        this.renderTooltip(context, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(context, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, @NotNull Slot slot) {

        if (slot instanceof SeasoningSlot) {

            boolean shouldDarken =
                    (!this.menu.getCarried().isEmpty() && !slot.mayPlace(this.menu.getCarried())) ||
                            (slot.hasItem() && !slot.mayPlace(slot.getItem()));

            if (shouldDarken) {
                int x = slot.x;
                int y = slot.y;
                guiGraphics.fill(x, y, x + 16, y + 16, 822018048);
            }
        }

        super.renderSlot(guiGraphics, slot);
    }


    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public @NotNull RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int progressX = this.leftPos + 96;
        int progressY = this.topPos + 39;

        if (Cobblemon.INSTANCE.getImplementation().isModInstalled("jei") &&
                mouseX >= progressX && mouseX < progressX + COOK_PROGRESS_WIDTH &&
                mouseY >= progressY && mouseY < progressY + COOK_PROGRESS_HEIGHT) {

            if (CobblemonJeiPlugin.Companion.getJeiRuntime() != null) {
                CobblemonJeiPlugin.Companion.getJeiRuntime()
                        .getRecipesGui()
                        .showTypes(java.util.List.of(CampfirePotRecipeCategory.Companion.getRECIPE_TYPE()));
            }
            return true;
        }

        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBookComponent);

            if (this.recipeBookComponent.isVisible()) {
                StateSwitchingButton recipeBookFilterButton =
                        ((RecipeBookComponentAccessor) this.recipeBookComponent).getFilterButton();
                if (recipeBookFilterButton.isMouseOver(mouseX, mouseY)) {
                    overrideRecipeBookFilterButton(recipeBookFilterButton);
                }
            }

            return true;
        } else {
            return (this.widthTooNarrow && this.recipeBookComponent.isVisible()) ||
                    super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int progressX = this.leftPos + 96;
        int progressY = this.topPos + 39;

        if (Cobblemon.INSTANCE.getImplementation().isModInstalled("jei") &&
                mouseX >= progressX && mouseX < progressX + COOK_PROGRESS_WIDTH &&
                mouseY >= progressY && mouseY < progressY + COOK_PROGRESS_HEIGHT) {

            guiGraphics.renderTooltip(
                    this.font,
                    Component.translatable("jei.tooltip.show.recipes"),
                    mouseX,
                    mouseY
            );
            return;
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void overrideRecipeBookFilterButton(StateSwitchingButton filterButton) {
        filterButton.initTextureValues(FILTER_BUTTON_SPRITES);
        filterButton.setTooltip(
                filterButton.isStateTriggered()
                        ? Tooltip.create(Component.translatable("cobblemon.container.campfire_pot.recipe_book.toggle_recipes"))
                        : Tooltip.create(Component.translatable("gui.recipebook.toggleRecipes.all"))
        );
    }

    @Override
    protected void slotClicked(Slot slot, int slotId, int mouseButton, @NotNull ClickType type) {
        super.slotClicked(slot, slotId, mouseButton, type);
        this.recipeBookComponent.slotClicked(slot);
    }
}
