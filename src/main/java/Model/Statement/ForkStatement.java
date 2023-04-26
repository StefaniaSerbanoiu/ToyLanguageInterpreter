package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ADTs.*;
import Model.ProgramState.ProgramState;
import Model.Type.Type;
import Model.Value.Value;

import java.util.Map;

public class ForkStatement implements IStatement{
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        MyIStack<IStatement> newStack = new MyStack<>();
        newStack.push(statement);
        MyIDictionary<String, Value> newSymTable = new MyDictionary<>();
        //The symbol_table of the new thread is a clone (or a new deep copy) and is not shared with the parent thread.
        //the symbol table remains unchanged while creating a new thread
        //the contents of the old symbol table are copied into a new one
        for (Map.Entry<String, Value> entry: state.getSymbol_table().getDictionaryContent().entrySet()) {
            newSymTable.insertValueAtKey(entry.getKey(), entry.getValue().deepCopy());
        }

        return new ProgramState(newStack, newSymTable, state.getOutput(), state.getFile_table(), state.getHeap(), (MySemaphore) state.getSemaphore_table());
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("Fork(%s)", statement.toString());
    }
}