package com.github.cyberryan1;

import com.github.cyberryan1.events.*;
import com.github.cyberryan1.managers.ConfigManager;
import com.github.cyberryan1.managers.DataManager;
import com.github.cyberryan1.utils.*;

import org.bukkit.plugin.java.JavaPlugin;

public final class NaturalVanish extends JavaPlugin {

    // config/data managers
    public ConfigManager config;
    public DataManager data;

    // utilities that need to be initialized
    public Utilities util;
    public ConfigUtils configUtils;
    public DataUtils dataUtils;
    public VaultUtils vaultUtils;



    // TODO add the following events as cancellable: PvP (not started), damage (working on), block break/place (done), interact (done), hunger (not started)
    // TODO allow messages from events to not be sent if they put "" in the config file
    @Override
    public void onEnable() {
        config = new ConfigManager( this );
        data = new DataManager( this );

        util = new Utilities( this, config, data );
        configUtils = new ConfigUtils( config );
        dataUtils = new DataUtils( data );
        vaultUtils = new VaultUtils();

        this.getCommand( "vanish" ).setExecutor( new Vanish() );
        this.getCommand( "vanish" ).setTabCompleter( new TabComplete() );

        this.getServer().getPluginManager().registerEvents( new InventoryClick(), this );
        this.getServer().getPluginManager().registerEvents( new Join(), this );
        this.getServer().getPluginManager().registerEvents( new Leave(), this );
        this.getServer().getPluginManager().registerEvents( new ItemDrop(), this );
        this.getServer().getPluginManager().registerEvents( new ItemPickup(), this );
        this.getServer().getPluginManager().registerEvents( new Interact(), this );
        this.getServer().getPluginManager().registerEvents( new BlockPlace(), this );
        this.getServer().getPluginManager().registerEvents( new BlockBreak(), this );
        this.getServer().getPluginManager().registerEvents( new Damage(), this );

        ConfigUtils.getConfigManager().reloadConfig();

        // Remove all bossbars
        BossbarUtils.removeAllBossbars();
    }
}
