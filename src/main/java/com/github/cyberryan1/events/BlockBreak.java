package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.spigot.CyberCore;
import com.github.cyberryan1.cybercore.spigot.utils.CyberMsgUtils;
import com.github.cyberryan1.cybercore.spigot.utils.CyberVaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreak implements Listener {

    private ArrayList<String> cooldown = new ArrayList<>();

    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) ) {
            if ( Settings.VANISH_EVENTS_BUILD_CANCEL.bool() ) {
                if ( Settings.VANISH_EVENTS_BUILD_BYPASS.bool() == false
                        || CyberVaultUtils.hasPerms( event.getPlayer(), Settings.VANISH_EVENTS_BUILD_BYPASS_PERMISSION.string() ) ){
                    event.setCancelled( true );

                    String cancelMsg = Settings.VANISH_EVENTS_BUILD_CANCEL_MSG.coloredString();
                    if ( cancelMsg.isBlank() == false && cooldown.contains( event.getPlayer().getName() ) == false ) {
                        cooldown.add( event.getPlayer().getName() );
                        cancelMsg = cancelMsg.replace( "[PLAYER]", event.getPlayer().getName() );
                        CyberMsgUtils.sendMsg( event.getPlayer(), cancelMsg );

                        Bukkit.getScheduler().runTaskLater( CyberCore.getPlugin(), () -> {
                            cooldown.remove( event.getPlayer().getName() );
                        }, 40L );
                    }
                }
            }
        }
    }
}
