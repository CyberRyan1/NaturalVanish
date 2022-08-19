package com.github.cyberryan1.events;

import com.github.cyberryan1.utils.VanishUtils;
import com.github.cyberryan1.utils.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Hunger implements Listener {

    @EventHandler
    public void onHungerChange( FoodLevelChangeEvent event ) {
        if ( Settings.VANISH_EVENTS_HUNGER_CANCEL.bool() == false ) { return; }
        if ( event.getEntity() instanceof Player ) {
            Player player = ( Player ) event.getEntity();
            if ( VanishUtils.checkVanished( player ) ) {
                event.setFoodLevel( 20 );
            }
        }
    }
}
