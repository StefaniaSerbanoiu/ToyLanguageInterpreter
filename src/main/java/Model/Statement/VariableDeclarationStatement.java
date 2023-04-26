package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ProgramState.ProgramState;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.Value.Value;

public class VariableDeclarationStatement implements IStatement {
    String name;
    Type type;

    public VariableDeclarationStatement(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException {
        MyIDictionary<String, Value> symTable = state.getSymbol_table();
        if (symTable.existsKey(name)) {
            throw new ExecException("Error!!! Variable " + name + " has been already defined in the symbol table!!!");
        }
        symTable.insertValueAtKey(name, type.defaultValue());
        //returns null instead of the program's state, so we'll update the symbol table
        state.setSymbol_table(symTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        typeEnv.insertValueAtKey(name, type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(name, type);
    }

    @Override
    public String toString() {
        return String.format("%s %s", type.toString(), name);
    }
}