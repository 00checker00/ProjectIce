package de.project.ice.utils;

/**
 * @param <F> the type of the first
 * @param <S> the type of the second
 * @author dermetfan
 */
public class Pair<F, S> {

    /**
     * the first
     */
    private F first;

    /**
     * the second
     */
    private S second;

    /**
     * creates an empty pair ({@link #first} and {@link #second} are {@code null})
     */
    public Pair () {
    }

    /**
     * @param first  the {@link #first}
     * @param second the {@link #second}
     */
    public Pair (F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @param pair the {@link Pair} to copy
     */
    public Pair (Pair<F, S> pair) {
        first = pair.first;
        second = pair.second;
    }

    /**
     * @param pair the Pair which {@link #first} and {@link #second} to use
     * @return this Pair for chaining
     */
    public Pair<F, S> set (Pair<F, S> pair) {
        first = pair.first;
        second = pair.second;
        return this;
    }

    /**
     * @param key   the {@link #first} to set
     * @param value the {@link #second} to set
     * @return this Pair for chaining
     */
    public Pair<F, S> set (F key, S value) {
        this.first = key;
        this.second = value;
        return this;
    }

    /**
     * sets {@link #first} and {@link #second} to null
     */
    public void clear () {
        first = null;
        second = null;
    }

    /**
     * swaps first and second
     *
     * @throws IllegalStateException if the classes of {@link #first} and {@link #second} are not {@link Class#isAssignableFrom(Class) assignable} from each other
     */
    @SuppressWarnings("unchecked")
    public void swap () throws IllegalStateException {
        if (first.getClass() != second.getClass())
            throw new IllegalStateException("first and second are not of the same type: " + first.getClass() + " & " + second.getClass());
        S oldValue = second;
        second = (S) first;
        first = (F) oldValue;
    }

    @Override
    public int hashCode () {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    /**
     * if the given object is a {@link Pair} instance, {@link Object#equals(Object) equals} comparison will be used on first and second
     */
    @Override
    public boolean equals (Object obj) {
        if (obj instanceof Pair) {
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return first.equals(pair.first) && second.equals(pair.second);
        }
        return super.equals(obj);
    }

    /**
     * @return [{@link #first} &amp; {@link #second}]
     */
    @Override
    public String toString () {
        return "[" + first + " & " + second + ']';
    }

    // getters and setters

    /**
     * @return the {@link #first}
     */
    public F getFirst () {
        return first;
    }

    /**
     * @param first the {@link #first} to set
     */
    public void setFirst (F first) {
        this.first = first;
    }

    /**
     * @return the {@link #second}
     */
    public S getSecond () {
        return second;
    }

    /**
     * @param second the {@link #second} to set
     */
    public void setSecond (S second) {
        this.second = second;
    }

}