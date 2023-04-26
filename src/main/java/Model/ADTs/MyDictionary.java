package Model.ADTs;

import Exceptions.ADTException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<T1, T2> implements MyIDictionary<T1, T2>{
    // implementation of a dictionary using HashMap from java.util
    // the dictionary is used later for storing the symbol_table
    // variable(key) -> value(the actual associated value)
    HashMap<T1, T2> dictionary;

    public MyDictionary() {
        this.dictionary = new HashMap<>();
    }

    @Override
    public boolean existsKey(T1 key) {
        return this.dictionary.containsKey(key);
    }

    @Override
    public T2 findValueForKey(T1 key) throws ADTException {
        if (!existsKey(key))
            throw new ADTException( key + " is not defined.");
        return this.dictionary.get(key);
    }

    @Override
    public void update(T1 key, T2 value) throws ADTException {
        if (!existsKey(key))
            throw new ADTException(key + " is not defined.");
        this.dictionary.put(key, value);
    }

    @Override
    public Collection<T2> allValues() {
        return this.dictionary.values();
    }

    @Override
    public void removeKey(T1 key) throws ADTException {
        if (!existsKey(key))
            throw new ADTException("Error!!! Key + " + key +" is not defined!!!");
        this.dictionary.remove(key);
    }

    @Override
    public Set<T1> allKeys() {
        return dictionary.keySet();
    }

    @Override
    public Map<T1, T2> getDictionaryContent() {
        return dictionary;
    }

    @Override
    public MyIDictionary<T1, T2> deepCopy() throws ADTException {
        MyIDictionary<T1, T2> toReturn = new MyDictionary<>();
        for (T1 key: allKeys())
        {
            toReturn.insertValueAtKey(key, findValueForKey(key));
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return this.dictionary.toString();
    }

    @Override
    public void insertValueAtKey(T1 key, T2 value) {
        this.dictionary.put(key, value);
    }

}