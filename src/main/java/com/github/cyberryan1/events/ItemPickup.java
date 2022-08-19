package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;

public class ItemPickup implements Listener {
    private ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onPlayerPickup( PlayerPickupItemEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) == false ) { return; }

        if ( Settings.VANISH_EVENTS_DROP_CANCEL.bool() ) {
            if ( Settings.VANISH_EVENTS_DROP_BYPASS.bool() == false ||
                    VaultUtils.hasPerms( event.getPlayer(), Settings.VANISH_EVENTS_DROP_BYPASS_PERMISSION.string() ) == false ) {
                event.setCancelled( true );

                String cancelMsg = Settings.VANISH_EVENTS_DROP_CANCEL_MSG.coloredString();
                if ( cancelMsg.isBlank() == false ) {
                    CoreUtils.sendMsg( event.getPlayer(), cancelMsg.replace( "[PLAYER]", event.getPlayer().getName() ) );

                    cooldown.add( event.getPlayer().getName() );
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( CyberCore.getPlugin(), new Runnable() {
                        public void run() {
                            cooldown.remove( event.getPlayer().getName() );
                        }
                    }, 40L );
                }
            }
        }
    }
}
