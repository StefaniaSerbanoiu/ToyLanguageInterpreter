package Model.Expression;


import Exceptions.ADTException;
import Exceptions.EvalException;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.BoolValue;
import Model.Value.Value;

import java.util.Objects;

public class LogicExpression implements IExpression {
    IExpression expression1;
    IExpression expression2;
    String operation;


    public LogicExpression(String operation, IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException, ADTException {
        Type type1, type2;
        type1 = expression1.typeCheck(typeEnv);
        type2 = expression2.typeCheck(typeEnv);
        if (type1.equals(new BoolType()))
        {
            if (type2.equals(new BoolType()))
                return new BoolType();
            else
                throw new EvalException("Second operand is not a boolean.");
        }
        else
            throw new EvalException("First operand is not a boolean.");

    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap heap) throws EvalException, ADTException {
        Value value1, value2;
        value1 = this.expression1.eval(table, heap);
        if (value1.getType().equals(new BoolType()))
        {
            value2 = this.expression2.eval(table, heap);
            if (value2.getType().equals(new BoolType()))
            {
                BoolValue bool1 = (BoolValue) value1;
                BoolValue bool2 = (BoolValue) value2;
                boolean b1, b2;
                b1 = bool1.getValue();
                b2 = bool2.getValue();
                if (Objects.equals(this.operation, "and"))
                    return new BoolValue(b1 && b2);
                else if (Objects.equals(this.operation, "or"))
                    return new BoolValue(b1 || b2);
            }
            else throw new EvalException("Error!!! Second operand is not a boolean!!!");

        }
        else throw new EvalException("Error!!! First operand is not a boolean!!!");

        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(operation, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return expression1.toString() + " " + operation + " " + expression2.toString();
    }
}