package at.koopro.positionpro.commands;

import at.koopro.positionpro.playerinfo.PlayerInfoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerInfoTabCompleter implements TabCompleter {

    private final PlayerInfoManager playerInfoManager;

    public PlayerInfoTabCompleter(PlayerInfoManager playerInfoManager) {
        this.playerInfoManager = playerInfoManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Provide a list of player names and UUIDs for tab completion
            File[] files = playerInfoManager.getPlayerFolder().listFiles();
            List<String> suggestions = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        // Add the filename without the .yml extension to the suggestions
                        suggestions.add(file.getName().replace(".yml", ""));
                    }
                }
            }
            return suggestions;
        }
        return null; // No suggestions for other arguments
    }
}
