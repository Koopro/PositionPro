package at.koopro.positionpro;

import at.koopro.positionpro.commands.PlayerInfoCommand;
import at.koopro.positionpro.commands.PlayerInfoTabCompleter;
import at.koopro.positionpro.playerinfo.PlayerEventListener;
import at.koopro.positionpro.playerinfo.PlayerInfoManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PositionPro extends JavaPlugin {

    // Define the plugin prefix
    public static final String PLUGIN_PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "PositionPro" + ChatColor.GRAY + "] " + ChatColor.RESET;


    @Override
    public void onEnable() {
        // Use the plugin prefix in a startup message
        getServer().getConsoleSender().sendMessage(PLUGIN_PREFIX + "Enabling PositionPro...");

        // Ensure the data folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize PlayerInfoManager with RoleManager
        PlayerInfoManager playerInfoManager = new PlayerInfoManager(this, getDataFolder());

        // Set command executors and tab completers
        this.getCommand("playerinfo").setExecutor(new PlayerInfoCommand(playerInfoManager));
        this.getCommand("playerinfo").setTabCompleter(new PlayerInfoTabCompleter(playerInfoManager));
        // If you have a TabCompleter for the role command, set it here as well

        // Register PlayerEventListener
        getServer().getPluginManager().registerEvents(new PlayerEventListener(playerInfoManager), this);

        // Other plugin setup...
    }

    @Override
    public void onDisable() {
        // Use the plugin prefix in a shutdown message
        getServer().getConsoleSender().sendMessage(PLUGIN_PREFIX + "Disabling PositionPro...");

        // Plugin shutdown logic goes here
    }

    // Other methods...
}
