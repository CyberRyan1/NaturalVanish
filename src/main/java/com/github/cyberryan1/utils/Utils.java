package com.github.cyberryan1.utils;

import java.util.ArrayList;

public class Utils {

    // formats a string list into a nice string
    public static String getStringFromList( String list[] ) {
        if ( list.length == 0 ) {
            return "N/A";
        }
        else if ( list.length == 1 ) {
            return list[0];
        }
        else if ( list.length == 2 ) {
            return list[0] + " and " + list[1];
        }

        String toReturn = "";
        String listWithoutLast[] = new String[ list.length - 1 ];

        for ( int index = 0; index < listWithoutLast.length; index++ ) {
            listWithoutLast[index] = list[index];
        }

        for ( String s : listWithoutLast ) {
            toReturn += s + ", ";
        }

        toReturn += "and " + list[list.length - 1];
        return toReturn;
    }

    // formats a string ArrayList into a nice string
    public static String getStringFromList( ArrayList<String> list ) {
        if ( list.size() == 0 ) {
            return "N/A";
        }

        return getStringFromList( list.toArray( new String[ list.size() - 1 ] ) );
    }
}