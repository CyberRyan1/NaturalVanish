package skriptelements.conditions.types;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

public abstract class RegularCondition extends Condition {

    @SuppressWarnings( "unchecked" )
    @Override
    public abstract boolean init( Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult );

    @Override
    public abstract boolean check( Event event );

    @Override
    public abstract String toString( @Nullable Event event, boolean debug );
}
