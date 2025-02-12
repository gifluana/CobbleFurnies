package com.lunazstudios.cobblefurnies.fabric.compat;

import com.lunazstudios.cobblefurnies.CobbleFurnies;
import com.lunazstudios.cobblefurnies.registry.CFBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FurniCrafterCategory implements DisplayCategory<BasicDisplay> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            CobbleFurnies.MOD_ID, "textures/gui/rei/furnicrafter.png");
    public static final CategoryIdentifier<FurniCrafterDisplay> FURNICRAFTER =
            CategoryIdentifier.of(CobbleFurnies.MOD_ID, "furnicrafter");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return FURNICRAFTER;
    }

    @Override
    public Component getTitle() {
        return Component.literal("FurniCrafter");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(CFBlocks.FURNI_CRAFTER.get());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();

        int bgWidth = 150;
        int bgHeight = 49;
        int bgX = bounds.getCenterX() - bgWidth / 2;
        int bgY = bounds.getCenterY() - bgHeight / 2;
        Point bgPos = new Point(bgX, bgY);

        widgets.add(Widgets.createTexturedWidget(
                TEXTURE,
                new Rectangle(bgPos.x, bgPos.y, bgWidth, bgHeight),
                0.0F, 0.0F,
                bgWidth, bgHeight
        ));

        int inputStartX = bgPos.x + 17;
        int inputStartY = bgPos.y + 17;
        List<EntryIngredient> inputs = display.getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            int x = inputStartX + i * 20;
            int y = inputStartY;
            widgets.add(Widgets.createSlot(new Point(x, y))
                    .entries(inputs.get(i))
                    .markInput());
        }

        List<EntryIngredient> outputs = display.getOutputEntries();
        int outputX = bgPos.x + 115;
        int outputY = bgPos.y + 17;
        for (int i = 0; i < outputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(outputX, outputY + i * 20))
                    .entries(outputs.get(i))
                    .markOutput());
        }

        return widgets;
    }


    @Override
    public int getDisplayHeight() {
        return 50;
    }
}
