package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyISemaphore;
import Model.ProgramState.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStatement implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public ReleaseStatement(String var) { this.var = var; }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        lock.lock();
        MyIDictionary<String, Value> symbol_table = state.getSymbol_table();
        MyISemaphore semaphore = state.getSemaphore_table();
        if (symbol_table.existsKey(var))
        {
            if (symbol_table.findValueForKey(var).getType().equals(new IntType()))
            {
                IntValue fi = (IntValue) symbol_table.findValueForKey(var);
                int foundIndex = fi.getValue();
                if (semaphore.existsKey(foundIndex))
                {
                    Pair<Integer, List<Integer>> found_values = semaphore.getFromSemaphore(foundIndex);
                    if (found_values.getValue().contains(state.getId()))
                    {
                        found_values.getValue().remove((Integer) state.getId());
                    }
                    semaphore.updateSemaphore(foundIndex, new Pair<>(found_values.getKey(), found_values.getValue()));
                }
                else
                {
                    throw new ExecException("Error!! Index not in the semaphore table for release statement!!!");
                }
            }
            else
            {
                throw new ExecException("Error!!! Index is not defined as int type for release statement!!!!");
            }
        }
        else
        {
            throw new ExecException("Error!!! Index hasn't been defined in the symbol table regarding the release statement!!!");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ADTException {
        if (typeEnv.findValueForKey(var).equals(new IntType()))
        {
            return typeEnv;
        }
        else
        {
            throw new ADTException(String.format("Error!! %s is not int in release statement!!!", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseStatement(var);
    }

    @Override
    public String toString()
    {
        return String.format("release(%s)" , var);
    }
}
