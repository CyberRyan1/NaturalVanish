package com.github.cyberryan1.utils;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @deprecated
 */
public class VaultUtils {

    public static Economy economy = null;
    public static Permission permissions = null;
    public static Chat chat = null;



    public VaultUtils() {
        if ( setupPermissions() == false ) {
            Utilities.logError( "Disabled due to no Vault dependency found!" );
            Utilities.getPluginManager().disablePlugin( Utilities.getPlugin() );
            return;
        }
    }



    public static Economy getEconomy() { return economy; }


    public static Permission getPermissions() { return permissions; }


    public static Chat getChat() { return chat; }



    // Check if an online player has permissions
    public static boolean hasPerms( Player player, String perm ) { return hasPerms( ( ( OfflinePlayer ) player), perm ); }


    // Check if a command sender has permissions
    public static boolean hasPerms( CommandSender sender, String perm ) {
        if ( sender instanceof Player ) {
            return hasPerms( ( OfflinePlayer ) sender, perm );
        }
        if ( sender instanceof OfflinePlayer ) {
            return hasPerms( sender, perm );
        }

        return permissions.has( sender, perm );
    }


    // Check if an offline player has permissions
    public static boolean hasPerms( OfflinePlayer player, String perm ) {
        if ( player.isOp() && ConfigUtils.getBool( "vanish.op-override" ) ) { return true; }
        return permissions.playerHas( null, player, perm );
    }



    private boolean setupPermissions() {
        if ( Utilities.getPluginManager().getPlugin( "Vault" ) == null ) {
            return false;
        }

        RegisteredServiceProvider<Permission> rsp = Utilities.getPlugin().getServer().getServicesManager().getRegistration( Permission.class );
        if ( rsp == null ) {
            return false;
        }

        permissions = rsp.getProvider();
        return permissions != null;
    }
}
