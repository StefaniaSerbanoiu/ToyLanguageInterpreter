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

public class HeapWritingStatement implements IStatement{
    private final String varName;
    private final IExpression expression;

    public HeapWritingStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        MyIDictionary<String, Value> symTable = state.getSymbol_table();
        MyIHeap heap = state.getHeap();
        if (symTable.existsKey(varName)) {
            Value value = symTable.findValueForKey(varName);
            if (value.getType() instanceof RefType) {
                RefValue refValue = (RefValue) value;
                if (heap.containsKey(refValue.getAddress())) {
                    Value evaluated = expression.eval(symTable, heap);
                    if (evaluated.getType().equals(refValue.getLocationType())) {
                        heap.update(refValue.getAddress(), evaluated);
                        state.setHeap(heap);
                    } else
                        throw new ExecException(String.format("Error!!! %s != %s!!!", evaluated, refValue.getLocationType()));
                } else
                    throw new ExecException(String.format("Error!!! %s is not defined in the heap!!!", value));
            } else
                throw new ExecException(String.format("Error!!! %s should be a reference!!!", value));
        } else
            throw new ExecException(String.format("Error!!! %s doesn't exist in the symTable", varName));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        if (typeEnv.findValueForKey(varName).equals(new RefType(expression.typeCheck(typeEnv))))
            return typeEnv;
        else
            throw new ExecException("Error!!! In the heap: the arguments should be of the same type!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWritingStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("wH(%s, %s)", varName, expression);
    }
}