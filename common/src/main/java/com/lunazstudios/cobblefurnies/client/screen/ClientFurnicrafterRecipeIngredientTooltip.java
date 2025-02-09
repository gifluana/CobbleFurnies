package com.lunazstudios.cobblefurnies.client.screen;

import com.lunazstudios.cobblefurnies.recipe.CountedIngredient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.lunazstudios.cobblefurnies.menu.FurniCrafterMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class ClientFurnicrafterRecipeIngredientTooltip implements ClientTooltipComponent {
    private final FurniCrafterMenu menu;
    private final CountedIngredient material;
    private final Map<Integer, Integer> counted;

    public ClientFurnicrafterRecipeIngredientTooltip(FurniCrafterMenu menu, CountedIngredient material, Map<Integer, Integer> counted) {
        this.menu = menu;
        this.material = material;
        this.counted = counted;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        // 18 pixels for the icon plus the width of the item's name.
        return 18 + font.width(getStack().getDisplayName());
    }

    @Override
    public void renderImage(Font font, int start, int top, GuiGraphics graphics) {
        ItemStack stack = getStack().copy();
        stack.setCount(this.material.count());
        // Render the ingredient icon.
        graphics.renderFakeItem(stack, start, top);
        graphics.renderItemDecorations(font, stack, start, top);
        // Draw the item name in gray next to the icon.
        MutableComponent name = stack.getHoverName().copy().withStyle(ChatFormatting.GRAY);
        graphics.drawString(font, name, start + 18 + 5, top + 4, 0xFFFFFFFF);

        // Draw the check or cross overlay.
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(0, 0, 200);
        boolean hasEnough = this.menu.hasMaterials(this.material, this.counted);
        graphics.blit(FurniCrafterScreen.TEXTURE, start, top, hasEnough ? 246 : 240, 40, 6, 5, 256, 256);
        pose.popPose();
    }

    private ItemStack getStack() {
        ItemStack[] items = this.material.ingredient().getItems();
        int index = (int) ((Util.getMillis() / 1000) % items.length);
        return items[index];
    }
}
