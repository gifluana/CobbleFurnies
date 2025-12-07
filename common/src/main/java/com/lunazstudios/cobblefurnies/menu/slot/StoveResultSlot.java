package com.lunazstudios.cobblefurnies.menu.slot;

import com.cobblemon.mod.common.CobblemonItemComponents;
import com.cobblemon.mod.common.CobblemonSounds;
import com.lunazstudios.cobblefurnies.menu.StoveMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StoveResultSlot extends Slot {

    public StoveResultSlot(CraftingContainer container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        if (stack.has(CobblemonItemComponents.FLAVOUR)) {
            stack.set(CobblemonItemComponents.CRAFTED, true);
        }

        if (player.containerMenu instanceof StoveMenu menu) {
            menu.broadcastChanges();
            player.playNotifySound(CobblemonSounds.CAMPFIRE_POT_TAKE_ITEM, SoundSource.MASTER, 1.0f, 1.0f);
        }

        super.onTake(player, stack);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}