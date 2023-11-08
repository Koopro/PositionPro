package at.koopro.positionpro.playerinfo;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {

    private final PlayerInfoManager infoManager;

    public PlayerEventListener(PlayerInfoManager infoManager) {
        this.infoManager = infoManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // When a player joins, log their login time
        infoManager.playerLoggedIn(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // When a player quits, update their playtime and other data
        infoManager.playerLoggedOut(event.getPlayer());
    }
}
