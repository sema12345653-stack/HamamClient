package com.example.ourmod.module;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    static {
        MODULES.add(new Module("Auto GApple", "Eat a golden apple automatically", Category.COMBAT));
        MODULES.add(new Module("Attack Aura", "Targets nearby enemies", Category.COMBAT));
        MODULES.add(new Module("Auto Potion", "Uses selected potions automatically", Category.COMBAT));
        MODULES.add(new Module("Auto Totem", "Keeps a totem in the offhand", Category.COMBAT));
        MODULES.add(new Module("Fast Bow", "Improves bow handling", Category.COMBAT));
        MODULES.add(new Module("Sprint", "Automatically maintains sprint", Category.MOVEMENT));
        MODULES.add(new Module("No Slow", "Reduces item-use movement slowdown", Category.MOVEMENT));
        MODULES.add(new Module("Fullbright", "Brightens dark areas", Category.RENDER));
        MODULES.add(new Module("Player ESP", "Highlights nearby players", Category.RENDER));
        MODULES.add(new Module("Inv Move", "Move while inventory is open", Category.PLAYER));
        MODULES.add(new Module("HUD", "Shows the module HUD", Category.DISPLAY));
    }

    private ModuleManager() {}

    public static List<Module> modules() {
        return MODULES;
    }
}
