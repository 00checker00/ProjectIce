package de.project.ice.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @param <F> the type of the first
 * @param <S> the type of the second
 */
public class Triple<F, S, T>
{

    /**
     * the first
     */
    private F first;

    /**
     * the second
     */
    private S second;

    /**
     * the third
     */
    private T third;

    /**
     * creates an empty triple ({@link #first}, {@link #second} and {@link #third} are {@code null})
     */
    public Triple()
    {
    }

    /**
     * @param first  the {@link #first}
     * @param second the {@link #second}
     * @param third the {@link #third}
     */
    public Triple(F first, S second, T third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * @param triple the {@link Triple} to copy
     */
    public Triple(Triple<F, S, T> triple)
    {
        first = triple.first;
        second = triple.second;
        third = triple.third;
    }

    /**
     * @param triple the Triple which {@link #first}, {@link #second} and {@link #third} to use
     * @return this Triple for chaining
     */
    public Triple<F, S, T> set(Triple<F, S, T> triple)
    {
        first = triple.first;
        second = triple.second;
        third = triple.third;
        return this;
    }

    /**
     * @param first   the {@link #first} to set
     * @param second the {@link #second} to set
     * @param third the {@link #third} to set
     * @return this Pair for chaining
     */
    public Triple<F, S, T> set(F first, S second, T third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
        return this;
    }

    public static <F, S, T> Triple<F, S, T> create(F first, S second, T third)
    {
        return new Triple<F, S, T>(first, second, third);
    }

    /**
     * sets {@link #first} and {@link #second} to null
     */
    public void clear()
    {
        first = null;
        second = null;
        third = null;
    }

    @Override
    public int hashCode()
    {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        result = 31 * result + (third != null ? third.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;

        if (first != null ? !first.equals(triple.first) : triple.first != null)
        {
            return false;
        }
        if (second != null ? !second.equals(triple.second) : triple.second != null)
        {
            return false;
        }
        return !(third != null ? !third.equals(triple.third) : triple.third != null);

    }

    @Override
    public String toString()
    {
        return "Triple{" +
                 first +
                ", " + second +
                ", " + third +
                '}';
    }

    // getters and setters

    /**
     * @return the {@link #first}
     */
    public F getFirst()
    {
        return first;
    }

    /**
     * @param first the {@link #first} to set
     */
    public void setFirst(F first)
    {
        this.first = first;
    }

    /**
     * @return the {@link #second}
     */
    public S getSecond()
    {
        return second;
    }

    /**
     * @param second the {@link #second} to set
     */
    public void setSecond(S second)
    {
        this.second = second;
    }

    /**
     * @return the {@link #third}
     */
    public T getThird()
    {
        return third;
    }

    /**
     * @param third the {@link #third} to set
     */
    public void setThird(T third)
    {
        this.third = third;
    }


}