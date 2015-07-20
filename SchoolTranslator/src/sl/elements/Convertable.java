package sl.elements;

public interface Convertable<T extends Convertable<T, E>, E extends Exception> {

    public T convert() throws E;
}
