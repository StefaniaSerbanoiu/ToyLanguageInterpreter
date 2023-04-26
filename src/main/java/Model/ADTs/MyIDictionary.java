package Model.ADTs;

import Exceptions.ADTException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<T1,T2> {
    boolean existsKey(T1 key);
    void insertValueAtKey(T1 key, T2 value);// adds a new value to the dictionary, associated to a key
    T2 findValueForKey(T1 key) throws ADTException;// returns the associated value for a given key
    void update(T1 key, T2 value) throws ADTException;
    Collection<T2> allValues(); // returns all the values
    void removeKey(T1 key) throws ADTException;
    Set<T1> allKeys();
    Map<T1, T2> getDictionaryContent();
    MyIDictionary<T1, T2> deepCopy() throws ADTException;
}