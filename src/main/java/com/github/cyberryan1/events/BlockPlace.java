package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BlockPlace implements Listener {

    private ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onBlockPlace( BlockPlaceEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) ) {
            if ( Settings.VANISH_EVENTS_BUILD_CANCEL.bool() ) {
                if ( Settings.VANISH_EVENTS_BUILD_BYPASS.bool() == false ||
                        VaultUtils.hasPerms( event.getPlayer(), Settings.VANISH_EVENTS_BUILD_BYPASS_PERMISSION.string() ) ) {
                    event.setCancelled( true );

                    String cancelMsg = Settings.VANISH_EVENTS_BUILD_CANCEL_MSG.coloredString();
                    if ( cancelMsg.isBlank() == false && cooldown.contains( event.getPlayer().getName() ) == false ) {
                        cooldown.add( event.getPlayer().getName() );
                        cancelMsg = cancelMsg.replace( "[PLAYER]", event.getPlayer().getName() );
                        CoreUtils.sendMsg( event.getPlayer(), cancelMsg );

                        Bukkit.getScheduler().runTaskLater( CyberCore.getPlugin(), () -> {
                            cooldown.remove( event.getPlayer().getName() );
                        }, 40L );
                    }
                }
            }
        }
    }
}
