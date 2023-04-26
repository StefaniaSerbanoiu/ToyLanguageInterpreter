package Model.Expression;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.Value;

public class VariableExpression implements IExpression {
    String key;

    public VariableExpression(String key) {
        this.key = key;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException, ADTException {
        return typeEnv.findValueForKey(key);
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap heap) throws ADTException {
        return table.findValueForKey(key);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(key);
    }

    @Override
    public String toString() {
        return key;
    }
}