package me.kitdacatsun.pvplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class PVPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Started Up");

        this.getCommand("join").setExecutor(new Join());
    }
}
