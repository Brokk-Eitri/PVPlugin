package me.kitdacatsun.pvplugin;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

import static org.bukkit.Bukkit.dispatchCommand;

public final class PVPlugin extends JavaPlugin {

    public static Location lobby;

    public static Team[] teams;
    public static Location[] spawnBarriers;
    public static Material spawnBarrierBlock;

    public static ArrayList<Player> ready;
    public static ArrayList<Player> inGame;

    @Override
    public void onEnable() {
        System.out.println("Started Up");

        lobby = new Location(Bukkit.getWorld("world_nether"), 0.5, 125, 0.5, 0, 0);

        World nether = Bukkit.getWorld("world_nether");

        teams = new Team[] {
                new Team("Red", new Location(nether, -12.5f, 130, 0.5f,-90,0)),
                new Team("Blue", new Location(nether, 12.5f, 130, 0.5f,90,0)),
                new Team("None", new Location[]{
                        new Location(nether, -40, 130, 4,-90,0),
                        new Location(nether, -25, 130, -27,0,0),
                        new Location(nether, 40, 132, -19,90,0),
                        new Location(nether, 34, 130, 23,0,0),
                })
        };

        teams[2].spawnReady = true;

        spawnBarriers = new Location[]{
                new Location(nether, 10, 129, 0,0,0),
                new Location(nether, 10, 130, 0,0,0),
                new Location(nether, -10, 129, 0,0,0),
                new Location(nether, -10, 130, 0,0,0),
        };

        spawnBarrierBlock = Material.BLACK_STAINED_GLASS;

        inGame = new ArrayList<>();
        ready = new ArrayList<>();

        this.getCommand("join").setExecutor(new Join());
        this.getCommand("startgame").setExecutor(new StartGame());
        this.getCommand("endgame").setExecutor(new EndGame());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("ready").setExecutor(new Ready());

        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    public static void resetPlayer(Player player) {
        player.teleport(lobby);
        player.setGameMode(GameMode.ADVENTURE);

        player.getInventory().clear();
        player.updateInventory();

        player.setHealth(20);
        player.setExp(0);
        player.setLevel(0);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        dispatchCommand(Bukkit.getConsoleSender(), "team join NoTeam");
    }

    public static void equip(Player player) {
        Inventory inventory =  player.getInventory();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
        sword.addEnchantment(Enchantment.SWEEPING_EDGE, 2);
        sword.addEnchantment(Enchantment.KNOCKBACK, 1);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        ItemStack[] weapons = new ItemStack[] {
                sword,
                new ItemStack(Material.IRON_AXE),
                bow,
        };

        for (ItemStack item : weapons) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
            item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        }

        ItemStack[] armour = new ItemStack[] {
                new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.SHIELD),
        };

        for (ItemStack item : armour) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
            item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        }


        inventory.addItem(weapons);
        inventory.addItem(armour);
        inventory.addItem(new ItemStack(Material.COOKED_PORKCHOP, 64));
        inventory.addItem(new ItemStack(Material.ARROW));

        player.updateInventory();
    }
}


