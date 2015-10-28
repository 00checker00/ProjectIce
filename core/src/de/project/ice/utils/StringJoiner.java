package de.project.ice.utils;

import java.util.Iterator;

public abstract class StringJoiner
{
    public static String join(Iterable<String> iterable, String delimiter)
    {
        Iterator<String> iterator = iterable.iterator();
        if (!iterator.hasNext())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder(iterator.next());
        while (iterator.hasNext())
        {
            sb.append(delimiter);
            sb.append(iterator.next());
        }
        return sb.toString();
    }
}
