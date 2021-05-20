package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.ConfigUtils;
import com.github.cyberryan1.utils.VanishUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener {

    @EventHandler
    public void onHungerChange( FoodLevelChangeEvent event ) {
        if ( event.getEntity() instanceof Player ) {
            Player player = ( Player ) event.getEntity();
            if ( VanishUtils.checkVanished( player ) ) {
                if ( ConfigUtils.getBool( "vanish.other-events.hunger.cancel" ) == true ) {
                    event.setFoodLevel( 20 );
                }
            }
        }
    }
}
