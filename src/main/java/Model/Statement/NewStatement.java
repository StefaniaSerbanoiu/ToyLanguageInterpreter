package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.RefType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.RefValue;
import Model.Value.Value;

public class NewStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        MyIDictionary<String, Value> symTable = state.getSymbol_table();
        MyIHeap heap = state.getHeap();
        if (symTable.existsKey(varName)) {
            Value varValue = symTable.findValueForKey(varName);
            if ((varValue.getType() instanceof RefType)) {
                Value evaluated = expression.eval(symTable, heap);
                Type locationType = ((RefValue) varValue).getLocationType();
                if (locationType.equals(evaluated.getType())) {
                    // Create a new entry in the Heap table such that a new key (new free address) is generated, and
                    // it is associated to the result of the expression evaluation
                    int newPosition = heap.add(evaluated);
                    //in SymTable update the RefValue associated to the var_name such that the new RefValue
                    //has the same locationType and the address is equal to the new key generated in the Heap at
                    //the previous step
                    symTable.insertValueAtKey(varName, new RefValue(newPosition, locationType));
                    state.setSymbol_table(symTable);
                    state.setHeap(heap);
                } else
                    throw new ExecException(String.format("Error!!! %s != %s!!!", varName, evaluated.getType()));
            } else {
                throw new ExecException(String.format("Error!!! %s should be RefType!!!", varName));
            }
        } else {
            throw new ExecException(String.format("Error!!! %s is not a part of the table of symbols!!!", varName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        Type typeVar = typeEnv.findValueForKey(varName);
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr)))
            return typeEnv;
        else
            throw new ExecException("Error!!! Args of 'new' should be the same type!!!" );
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("new(%s, %s)", varName, expression);
    }
}