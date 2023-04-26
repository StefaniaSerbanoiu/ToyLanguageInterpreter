package Model.Expression;
import Exceptions.EvalException;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.Value;

public class ValueExpression implements IExpression {
    Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException {
        return value.getType();
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap heap) {
        return this.value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}