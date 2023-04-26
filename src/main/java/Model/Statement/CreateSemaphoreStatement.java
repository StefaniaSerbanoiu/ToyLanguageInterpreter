package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ADTs.*;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.IntType;
import Model.Type.Type;
import Model.Value.IntValue;
import Model.Value.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStatement implements IStatement{
    String var;
    IExpression expression;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphoreStatement(String new_variable, IExpression new_expression)
    {
        this.var = new_variable;
        this.expression = new_expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException
    {
        /*
        - evaluate the expression exp1 using SymTable1 and Heap1 and let be number1 the
        result of this evaluation. If number1 is not an integer then print an error and stop the
        execution.
                SemaphoreTable2 = SemaphoreTable1 synchronizedUnion {newfreelocation -
            >(number1,empty list)}
        if var exists in SymTable1 and has the type int then
        SymTable2 = update(SymTable1,var, newfreelocation)
else print an error and stop the execution.
            */

        lock.lock();

        MyIDictionary<String, Value> symbol_table = state.getSymbol_table();
        MyIHeap heap = state.getHeap();
        MyISemaphore semaphore = state.getSemaphore_table();

        IntValue nr = (IntValue) (expression.eval(symbol_table, heap));
        int number1 = nr.getValue();
        int freeAddress = semaphore.getFree_location();
        semaphore.insertInSemaphore(freeAddress, new Pair<>(number1, new ArrayList<>()));

        if (symbol_table.existsKey(var) && symbol_table.findValueForKey(var).getType().equals(new IntType()))
        {
            symbol_table.update(var, new IntValue(freeAddress));
        }
        else
        {
            throw new ExecException("Error!!! The variable " + var + " hasn't been declared before( doesn't exist in the symbol table) or is not of type int!!!");
        }
        lock.unlock();

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException
    {
        if (typeEnv.findValueForKey(var).equals(new IntType()))
        {
            if (expression.typeCheck(typeEnv).equals(new IntType()))
            {
                return typeEnv;
            }
            else
            {
                throw new EvalException("Error!!! Expression in 'createSemaphoreStatement' should be of type int!!!");
            }
        }
        else
        {
            throw new EvalException(String.format("Error!! %s is not of type int!!!", var));
        }
    }

    @Override
    public IStatement deepCopy()
    {
        return new CreateSemaphoreStatement(var, expression.deepCopy());
    }

    @Override
    public String toString()
    {
        return String.format("CreateSemaphoreStatement(%s, %s) ", var, expression.toString());
    }

    public void setExpression(IExpression expression) {
        this.expression = expression;
    }

     public IExpression getExpression() { return this.expression; }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() { return this.var; }

    public static Lock getLock() { return lock; }
}
