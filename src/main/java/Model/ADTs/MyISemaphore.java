package Model.ADTs;

import Exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface MyISemaphore {
    void setSemaphoreTable(HashMap<Integer, Pair<Integer, List<Integer>>> new_semaphore_table);
    HashMap<Integer, Pair<Integer, List<Integer>>> getSemaphore_table();
    void setFree_location(int free);
    int getFree_location();
    boolean existsKey(int key);
    Pair<Integer, List<Integer>> getFromSemaphore(int key) throws ADTException;
    void insertInSemaphore(int key, Pair<Integer, List<Integer>> new_element) throws ADTException;
    void updateSemaphore(int key, Pair<Integer, List<Integer>> new_element) throws ADTException;
}
