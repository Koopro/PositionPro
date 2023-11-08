package at.koopro.positionpro.playerinfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class PlayerInfoManager {

    private final Plugin plugin;
    private final File playerFolder;
    private final Map<UUID, Instant> loginTimes = new HashMap<>();

    public PlayerInfoManager(Plugin plugin, File dataFolder) {
        this.plugin = plugin;
        this.playerFolder = new File(dataFolder, "playerdata");
        if (!playerFolder.exists()) {
            playerFolder.mkdirs();
        }
    }

    public void playerLoggedIn(Player player) {
        loginTimes.put(player.getUniqueId(), Instant.now());
    }

    private String formatPlayTime(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02dh%02dmin%02dsec", hours, minutes, seconds);
    }

    private String formatLastLogin(Instant loginTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-hh:mm a")
                .withLocale(Locale.ENGLISH)
                .withZone(ZoneId.systemDefault());
        return formatter.format(loginTime);
    }

    private String formatCoordinates(Location location) {
        return String.format("x: %.2f, y: %.2f, z: %.2f", location.getX(), location.getY(), location.getZ());
    }

    public void playerLoggedOut(Player player) {
        UUID uuid = player.getUniqueId();
        Instant loginTime = loginTimes.getOrDefault(uuid, Instant.now());
        Duration playTime = Duration.between(loginTime, Instant.now());

        String playerName = player.getName();
        File playerFile = new File(playerFolder, playerName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        config.set("name: ", playerName);

        // Calculate total play time
        long totalPlayTime = config.getLong("play-time-seconds", 0) + playTime.getSeconds();
        String formattedPlayTime = formatPlayTime(totalPlayTime);
        config.set("play-time", formattedPlayTime);

        // Format and set the last login time
        String formattedLastLogin = formatLastLogin(loginTime);
        config.set("last-login", formattedLastLogin);

        // Format and set the coordinates
        Location loc = player.getLocation();
        String formattedCoordinates = formatCoordinates(loc);
        config.set("coordinates", formattedCoordinates);

        // Set the UUID in the file
        config.set("uuid", uuid.toString());

        // Set the dimension
        String dimension = player.getWorld().getName();
        config.set("dimension", dimension);

        // Set the biome
        String biome = loc.getBlock().getBiome().name();
        config.set("biome", biome);

        // Set OP status
        boolean isOp = player.isOp();
        config.set("is-op", isOp);

        // Save the file
        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the player from the loginTimes map
        loginTimes.remove(uuid);
    }

    public File getPlayerFile(String playerNameOrUuid) {
        // Check if the argument is a UUID
        try {
            UUID uuid = UUID.fromString(playerNameOrUuid);
            return new File(playerFolder, uuid + ".yml");
        } catch (IllegalArgumentException e) {
            // If not a UUID, assume it's a player name
            return new File(playerFolder, playerNameOrUuid + ".yml");
        }
    }

    public File getPlayerFolder() {
        return playerFolder;
    }
}
