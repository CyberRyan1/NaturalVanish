package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.VanishUtils;
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
                if ( ConfigUtils.getBool( "vanish.other-events.damage.cancel" ) ) {
                    event.setCancelled( true );
                }
            }
        }
    }
}
