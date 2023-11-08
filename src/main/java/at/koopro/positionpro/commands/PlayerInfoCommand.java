package at.koopro.positionpro.commands;

import at.koopro.positionpro.playerinfo.PlayerInfoManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class PlayerInfoCommand implements CommandExecutor {

    private final PlayerInfoManager playerInfoManager;

    public PlayerInfoCommand(PlayerInfoManager playerInfoManager) {
        this.playerInfoManager = playerInfoManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName|uuid>");
            return true;
        }

        // Fetch the player's data file
        File playerFile = playerInfoManager.getPlayerFile(args[0]);
        if (playerFile == null || !playerFile.exists()) {
            sender.sendMessage(ChatColor.RED + "Player data not found.");
            return true;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        // Format and send the player's data to the sender
        sender.sendMessage(ChatColor.GOLD + "Player Information:");
        sender.sendMessage(ChatColor.GRAY + "UUID: " + ChatColor.WHITE + config.getString("uuid"));
        sender.sendMessage(ChatColor.GRAY + "Play Time: " + ChatColor.WHITE + config.getString("play-time"));
        sender.sendMessage(ChatColor.GRAY + "Last Login: " + ChatColor.WHITE + config.getString("last-login"));
        sender.sendMessage(ChatColor.GRAY + "Coordinates: " + ChatColor.WHITE + config.getString("coordinates"));
        sender.sendMessage(ChatColor.GRAY + "Biome: " + ChatColor.WHITE + config.getString("biome"));
        // Add more data as needed

        return true;
    }
}
