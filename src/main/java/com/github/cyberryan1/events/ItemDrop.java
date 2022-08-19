package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDrop implements Listener {

    @EventHandler
    public void onPlayerDrop( PlayerDropItemEvent event ) {
        if ( VanishUtils.checkVanished( event.getPlayer() ) == false ) { return; }

        if ( Settings.VANISH_EVENTS_DROP_CANCEL.bool() ) {
            if ( Settings.VANISH_EVENTS_DROP_BYPASS.bool() == false ||
                    VaultUtils.hasPerms( event.getPlayer(), Settings.VANISH_EVENTS_DROP_BYPASS_PERMISSION.string() ) == false ) {
                event.setCancelled( true );

                String cancelMsg = Settings.VANISH_EVENTS_DROP_CANCEL_MSG.coloredString();
                if ( cancelMsg.isBlank() == false ) {
                    CoreUtils.sendMsg( event.getPlayer(), cancelMsg.replace( "[PLAYER]", event.getPlayer().getName() ) );
                }
            }
        }
    }
}
