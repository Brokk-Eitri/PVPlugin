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
import java.util.*;

import static org.bukkit.Bukkit.*;

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
        server = getServer();
        plugin = this;

        parseJSON(getDataFolder().getAbsolutePath() + "\\settings.json");

        registerCommands();
    }

    private void parseJSON(String fileName) {
        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(new FileReader(fileName));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        assert json != null;
        
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
        inventory.clear();

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemStack bow = new ItemStack(Material.BOW);

        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.SWEEPING_EDGE, 3);

        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);

        axe.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        ItemStack[] weapons = new ItemStack[] {
                sword,
                axe,
                bow,
        };

        ItemStack[] armour = new ItemStack[] {
                new ItemStack(Material.DIAMOND_BOOTS),
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.DIAMOND_HELMET),
                new ItemStack(Material.SHIELD),
        };

        for (ItemStack item : armour) {
            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        }

        ArrayList<ItemStack> tools = new ArrayList<>();

        tools.addAll(Arrays.asList(weapons));
        tools.addAll(Arrays.asList(armour));

        for (ItemStack t : tools) {
            t.addEnchantment(Enchantment.BINDING_CURSE, 1);
            t.addEnchantment(Enchantment.VANISHING_CURSE, 1);
            t.addEnchantment(Enchantment.DURABILITY, 3);
        }

        inventory.addItem(weapons);
        inventory.addItem(armour);

        inventory.addItem(new ItemStack(Material.COOKED_PORKCHOP, 64));
        inventory.addItem(new ItemStack(Material.ARROW));

        player.updateInventory();
    }

    public static void addToTeam(Player player, Team team) {
        String commandLine = "team join " + team.name + " " + player.getName();
        team.inPlay = true;
        server.dispatchCommand(getConsoleSender(), commandLine);
        for (Player p : inGame) {
            if (p != player) {
                p.sendMessage(player.getName() + " has joined " + team.name + " team");
            }
        }

        player.teleport(team.spawnPoints[new Random().nextInt(team.spawnPoints.length)]);

        team.players.add(player);
    }
}
