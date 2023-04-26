package Model.ADTs;

import Exceptions.ADTException;
import Model.Value.Value;

import java.util.HashMap;
import java.util.Set;

public class MyHeap implements MyIHeap{
    HashMap<Integer, Value> heap;
    Integer freeLocationValue;


    public int newValue() {
        freeLocationValue += 1;
        while (freeLocationValue == 0 || heap.containsKey(freeLocationValue))
            freeLocationValue += 1;
        return freeLocationValue;
    }

    public MyHeap() {
        this.heap = new HashMap<>();
        freeLocationValue = 1;
    }

    @Override
    public int getFreeValue() {
        return freeLocationValue;
    }

    @Override
    public HashMap<Integer, Value> getHeapContent() {
        return heap;
    }

    @Override
    public void setHeapContent(HashMap<Integer, Value> newMap) {
        this.heap = newMap;
    }

    @Override
    public int add(Value value) {
        heap.put(freeLocationValue, value);
        Integer toReturn = freeLocationValue;
        freeLocationValue = newValue();
        return toReturn;
    }

    @Override
    public void update(Integer position, Value value) throws ADTException {
        if (!heap.containsKey(position))
            throw new ADTException(String.format("Error!! %d is not a key of the heap!!!", position));
        heap.put(position, value);
    }

    @Override
    public Value getHeapValueFromPosition(Integer position) throws ADTException {
        if (!heap.containsKey(position))
            throw new ADTException(String.format("Error!! %d is not a key of the heap!!!", position));
        return heap.get(position);
    }

    @Override
    public boolean containsKey(Integer position) {
        return this.heap.containsKey(position);
    }

    @Override
    public void removeKey(Integer key) throws ADTException {
        if (!containsKey(key))
            throw new ADTException(String.format("Error!! %d is not a key of the heap!!!", key));
        freeLocationValue = key;
        this.heap.remove(key);
    }

    @Override
    public Set<Integer> allKeys() {
        return heap.keySet();
    }
    @Override
    public String toString() {
        return this.heap.toString();
    }

}