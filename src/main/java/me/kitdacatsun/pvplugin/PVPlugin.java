package me.kitdacatsun.pvplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class PVPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Started Up");
    }

    @Override
    public void onDisable() {
        System.out.println("Shut Down");
    }
}
