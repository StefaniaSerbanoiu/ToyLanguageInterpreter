package Model.Expression;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Model.Type.IntType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIHeap;
import Model.Value.IntValue;
import Model.Value.Value;

public class ArithmeticExpression implements IExpression {
    IExpression expression1;
    IExpression expression2;
    char operation;

    public ArithmeticExpression(char operation, IExpression expression1, IExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws EvalException, ADTException {
        Type type1, type2;
        type1 = expression1.typeCheck(typeEnv);
        type2 = expression2.typeCheck(typeEnv);
        if (type1.equals(new IntType()))
        {
            if (type2.equals(new IntType()))
            {
                return new IntType();
            }
            else throw new EvalException("Error!!! Second operand is not an integer!!!");
        }
        else throw new EvalException("Error!!! First operand is not an integer!!!");
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap heap) throws EvalException, ADTException {
        Value value1, value2;
        value1 = this.expression1.eval(table, heap);
        if (value1.getType().equals(new IntType()))
        {
            value2 = this.expression2.eval(table, heap);
            if (value2.getType().equals(new IntType()))
            {
                IntValue int1 = (IntValue) value1;
                IntValue int2 = (IntValue) value2;
                int n1, n2;
                n1 = int1.getValue();
                n2 = int2.getValue();
                if (this.operation == '+')
                    return new IntValue(n1 + n2);
                else if (this.operation == '-')
                    return new IntValue(n1 - n2);
                else if (this.operation == '*')
                    return new IntValue(n1 * n2);
                else if (this.operation == '/')
                    if (n2 == 0)
                        throw new EvalException("Error!!! Division by zero is not permitted!!!");
                    else
                        return new IntValue(n1 / n2);
            }
            else
                throw new EvalException("Error!!! Second operand is not an integer!!!");
        }
        else
            throw new EvalException("Error!!! First operand is not an integer!!!");
        return null;
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(operation, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        return expression1.toString() + " " + operation + " " + expression2.toString();
    }
}