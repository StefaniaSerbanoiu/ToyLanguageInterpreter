package Model.ADTs;

import Exceptions.ADTException;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Set;

public interface MyIHeap{
    int getFreeValue();
    HashMap<Integer, Value> getHeapContent();
    void setHeapContent(HashMap<Integer, Value> newMap);
    int add(Value value);
    void update(Integer position, Value value) throws ADTException;
    Value getHeapValueFromPosition(Integer position) throws ADTException;
    boolean containsKey(Integer position);
    void removeKey(Integer key) throws ADTException;
    Set<Integer> allKeys();
}