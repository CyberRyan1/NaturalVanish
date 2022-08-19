package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Damage implements Listener {

    @EventHandler
    public void onEntityDamage( EntityDamageEvent event ) {
        if ( event.getEntity() instanceof Player ) {
            Player player = ( Player ) event.getEntity();

            if ( VanishUtils.checkVanished( player ) ) {
                if ( Settings.VANISH_EVENTS_DAMAGE_CANCEL.bool() ) {
                    event.setCancelled( true );
                }
            }
        }
    }
}
