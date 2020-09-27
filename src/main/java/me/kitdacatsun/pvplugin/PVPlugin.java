package me.kitdacatsun.pvplugin;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static org.bukkit.Bukkit.dispatchCommand;

public final class PVPlugin extends JavaPlugin {

    public static Location lobby;

    public static Team[] teams;
    public static Location[] spawnBarriers;
    public static Material spawnBarrierBlock;

    public static ArrayList<Player> ready = new ArrayList<>();
    public static ArrayList<Player> inGame = new ArrayList<>();

    public static Server server;
    public static Plugin plugin;

    public static World overworld = Bukkit.getWorld("world");

    @Override
    public void onEnable() {
        parseJSON();

        registerCommands();

        server = getServer();
        plugin = this;
    }

    private void parseJSON() {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(getDataFolder().getAbsolutePath() + "\\settings.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONObject json = (JSONObject)obj;

        JSONArray lobbyLocation = (JSONArray) json.get("lobby_location");
        lobby = new Location(overworld, (Long) lobbyLocation.get(0), (Long) lobbyLocation.get(1), (Long) lobbyLocation.get(2));

        JSONArray teamJsonArray = (JSONArray) json.get("teams");
        teams = new Team[teamJsonArray.size()];
        for (int i = 0; i < teams.length; i++) {
            JSONArray team = (JSONArray) teamJsonArray.get(i);
            JSONArray location = (JSONArray) team.get(1);
            teams[i] = new Team((String) team.get(0),
                    new Location(overworld, (Long) location.get(0), (Long) location.get(1), (Long) location.get(2)));
        }

        JSONArray spawnBarrierJsonArray = (JSONArray) json.get("spawn_barriers");
        spawnBarriers = new Location[spawnBarrierJsonArray.size()];
        for (int i = 0; i < teams.length; i++) {
            JSONArray location = (JSONArray) spawnBarrierJsonArray.get(i);
            spawnBarriers[i] = new Location(overworld, (Long) location.get(0), (Long) location.get(1), (Long) location.get(2));
        }

        spawnBarrierBlock = Material.getMaterial((String) json.get("spawn_barrier_block"));
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("join")).setExecutor(new Join());
        Objects.requireNonNull(getCommand("startgame")).setExecutor(new StartGame());
        Objects.requireNonNull(getCommand("endgame")).setExecutor(new EndGame());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new Spawn());
        Objects.requireNonNull(getCommand("ready")).setExecutor(new Ready());
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

        dispatchCommand(Bukkit.getConsoleSender(), "team join NoTeam " + player.getName());
    }

    public static void equip(Player player) {
        Inventory inventory =  player.getInventory();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.SWEEPING_EDGE, 3);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);

        ItemStack axe = new ItemStack(Material.IRON_AXE);
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        ItemStack[] weapons = new ItemStack[] {
                sword,
                axe,
                bow,
        };

        for (ItemStack item : weapons) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        }

        ItemStack[] armour = new ItemStack[] {
                new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.SHIELD),
        };

        for (ItemStack item : armour) {
            item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        }

        inventory.clear();
        inventory.addItem(weapons);
        inventory.addItem(armour);
        inventory.addItem(new ItemStack(Material.COOKED_PORKCHOP, 64));
        inventory.addItem(new ItemStack(Material.ARROW));

        player.updateInventory();
    }
}
