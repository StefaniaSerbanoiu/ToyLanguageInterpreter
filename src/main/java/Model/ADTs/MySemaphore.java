package Model.ADTs;

import Exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class MySemaphore implements MyISemaphore{
    HashMap<Integer, Pair<Integer, List<Integer>>> semaphore_table;
    private int free_location = 0;


    public MySemaphore(){ this.semaphore_table = new HashMap<>();}

    public void setSemaphoreTable(HashMap<Integer, Pair<Integer, List<Integer>>> new_semaphore_table)
    {
        this.semaphore_table = new_semaphore_table;
    }

    public HashMap<Integer, Pair<Integer, List<Integer>>> getSemaphore_table() { return semaphore_table; }

    public void setFree_location(int free) { this.free_location = free; }

    public int getFree_location()
    {
        synchronized (this)
        {
            free_location++;
            return free_location;
        }
    }

    @Override
    public boolean existsKey(int key)
    {
        synchronized (this)
        {
            return this.semaphore_table.containsKey(key);
        }
    }

    @Override
    public Pair<Integer, List<Integer>> getFromSemaphore(int key) throws ADTException
    {
        synchronized (this)
        {
            if(semaphore_table.containsKey(key))
            {
                return semaphore_table.get(key);
            }
            else
            {
                throw new ADTException(String.format("Error!!! The semaphore table doesn't contain the specified key %s!!!", key));
            }
        }
    }

    @Override
    public void insertInSemaphore(int key, Pair<Integer, List<Integer>> new_element) throws ADTException
    {
        synchronized (this)
        {
            if(!semaphore_table.containsKey(key))
            {
                semaphore_table.put(key, new_element);
            }
            else
            {
                throw new ADTException(String.format("Error!!! The semaphore table already contains key %s, so another value can't be associated with this key!!!", key));
            }
        }
    }

    @Override
    public void updateSemaphore(int key, Pair<Integer, List<Integer>> new_element) throws ADTException
    {
        synchronized (this)
        {
            if(semaphore_table.containsKey(key))
            {
                semaphore_table.replace(key, new_element);
            }
            else
            {
                throw new ADTException(String.format("Error!!! The key %s doesn't exist, so it can't be updated!!!", key));
            }
        }
    }
    @Override
    public String toString() { return this.semaphore_table.toString(); }
}
