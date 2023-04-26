package Model.Expression;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Model.Type.RefType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.RefValue;
import Model.Value.Value;

public class ReadHeapExpression implements IExpression{
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException, ADTException {
        Type type = expression.typeCheck(typeEnv);
        if (type instanceof RefType)
        {
            RefType refType = (RefType) type;
            return refType.getInner();
        }
        else throw new EvalException("Error!!! The rH argument is not a reference (RefType)!!!");
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyIHeap heap) throws EvalException, ADTException {
        Value value = expression.eval(symTable, heap);
        if (value instanceof RefValue)
        {
            RefValue refValue = (RefValue) value;
            if (heap.containsKey(refValue.getAddress()))
                return heap.getHeapValueFromPosition(refValue.getAddress());
            else
                throw new EvalException("Error!!! The address is not defined on the heap!");
        }
        else throw new EvalException(String.format("Error!!! %s not a reference(RefType)!!!", value));
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("rH(%s)", expression);
    }
}