package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.BoolType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIStack;
import Model.Value.BoolValue;
import Model.Value.Value;

public class WhileStatement implements IStatement{
    private final IExpression expression;
    private final IStatement statement;


    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        Value value = expression.eval(state.getSymbol_table(), state.getHeap());
        MyIStack<IStatement> stack = state.getExecution_stack();
        if (!value.getType().equals(new BoolType())) throw new ExecException(String.format("Error!!! %s is not of BoolType!!!", value));
        if (!(value instanceof BoolValue boolValue)) throw new ExecException(String.format("Error!!! %s is not a BoolValue!!!", value));
        if (boolValue.getValue())
        {
            stack.push(this.deepCopy());
            stack.push(statement);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType()))
        {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else throw new ExecException("Error!!! The condition in the while loop should result in a boolean!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("while(%s){%s}", expression, statement);
    }
}