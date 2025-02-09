package com.lunazstudios.cobblefurnies.registry;

import com.lunazstudios.cobblefurnies.client.screen.FurniCrafterScreen;

public class CFScreens {

    public static void init() {
        CFRegistry.registerScreen(() -> CFMenus.FURNI_CRAFTER_MENU, FurniCrafterScreen::new);
    }
}
