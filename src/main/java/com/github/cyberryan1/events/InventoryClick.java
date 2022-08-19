package com.github.cyberryan1.events;

import com.github.cyberryan1.cybercore.utils.CoreItemUtils;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import com.github.cyberryan1.utils.settings.Settings;
import com.github.cyberryan1.utils.yml.YMLUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick( InventoryClickEvent event ) {
        if ( Settings.VANISH_SKULL_HELMET.bool() == false ) { return; }

        if ( YMLUtils.getData().getBool( "vanish." + event.getWhoClicked().getUniqueId() + ".enabled" ) ) {
            if ( event.getCurrentItem() == null ) { return; }
            if ( event.getWhoClicked().getInventory().getHelmet() == null ) { return; }

            if ( event.getCurrentItem().equals( event.getWhoClicked().getInventory().getHelmet() ) ) {
                if ( event.getCurrentItem().equals( CoreItemUtils.getPlayerSkull( ( Player ) event.getWhoClicked() ) ) ) {
                    event.setCancelled( true );
                    Player player = ( Player ) event.getWhoClicked();

                    CoreUtils.sendMsg( player, Settings.VANISH_CANCEL_SKULL_MSG.coloredString() );
                    player.closeInventory();
                }
            }
        }
    }
}
