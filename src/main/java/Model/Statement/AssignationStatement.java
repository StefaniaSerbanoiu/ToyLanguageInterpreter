package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.Value.Value;

public class AssignationStatement implements IStatement {
    private final String key;
    private final IExpression expression;


    public AssignationStatement(String key, IExpression expression) {
        this.key = key;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        MyIDictionary<String, Value> symbolTable = state.getSymbol_table();

        if (symbolTable.existsKey(key)) {
            Value value = expression.eval(symbolTable, state.getHeap());
            Type typeId = (symbolTable.findValueForKey(key)).getType();
            if (value.getType().equals(typeId)) {
                symbolTable.update(key, value);
            } else {
                throw new ExecException("Error!!! The type of the variable " + key + " and the type of the assigned expression are different, but they should be the same!!!");
            }
        } else {
            throw new ExecException("Error!!! The variable " + key + " hasn't been declared before!!!");
        }
        //returns null instead of the program's state, so we'll update the symbol table
        state.setSymbol_table(symbolTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        Type typeVar = typeEnv.findValueForKey(key);
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExpr))
            return typeEnv;
        else
            throw new ExecException("Error!!! The left and right hand side should have the same type!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new AssignationStatement(key, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", key, expression.toString());
    }
}