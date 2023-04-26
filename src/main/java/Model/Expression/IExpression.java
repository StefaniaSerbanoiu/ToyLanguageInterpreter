package Model.Expression;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.Value;

public interface IExpression {
    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException, ADTException;
    Value eval(MyIDictionary<String, Value> table, MyIHeap heap) throws EvalException, ADTException;// evaluates a given expression
    // returns the result as a Value
    IExpression deepCopy();
}