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

public class AcquireStatement implements IStatement{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public AcquireStatement(String new_variable) { this.var = new_variable; }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException
    {
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
                    Pair<Integer, List<Integer>> foundSemaphore = semaphore.getFromSemaphore(foundIndex);
                    int NL = foundSemaphore.getValue().size();
                    int N1 = foundSemaphore.getKey();
                    if (N1 > NL)
                    {
                        if (!foundSemaphore.getValue().contains(state.getId()))
                        {
                            foundSemaphore.getValue().add(state.getId());
                            semaphore.updateSemaphore(foundIndex, new Pair<>(N1, foundSemaphore.getValue()));
                        }
                    }
                    else
                    {
                        state.getExecution_stack().push(this);
                    }
                }
                else
                {
                    throw new ExecException("Error!!! The index isn't recognized as a key in the symbol table!!!");
                }
            }
            else
            {
                throw new ExecException("Error!!! The index must be defined as an int type in the AcquireStatement!!!");
            }
        }
        else
        {
            throw new ExecException(String.format("Error!!! %s wasn't defined before in the symbol table!!!", var));
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
            throw new ADTException(String.format("Error!!! %s should be of type int in the 'AcquireStatement'!!!", var));
        }
    }

    @Override
    public IStatement deepCopy() {
        return new AcquireStatement(var);
    }

    @Override
    public String toString() {
        return String.format("acquire(%s) ", var);
    }
}
