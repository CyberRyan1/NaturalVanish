package com.github.cyberryan1.skriptelements.conditions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.github.cyberryan1.skriptelements.conditions.types.RegularCondition;
import com.github.cyberryan1.utils.VanishUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public class CondPlayerVanished extends RegularCondition {

    Expression<Player> player;

    @SuppressWarnings( "unchecked" )
    @Override
    public boolean init( Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult ) {
        player = ( Expression<Player> ) exprs[0];
        setNegated( parseResult.mark == 1 );
        return true;
    }

    @Override
    public boolean check( Event event ) {
        Player p = player.getSingle( event );
        if ( p == null ) { return isNegated(); }

        return VanishUtils.checkVanished( p ) ? isNegated() : !isNegated();
    }

    @Override
    public String toString( @Nullable Event event, boolean debug ) {
        return "%player% (1¦is|2¦is(n't¦ not)) vanished " + player.toString( event, debug );
    }
}