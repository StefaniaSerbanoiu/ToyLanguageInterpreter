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

public class IfStatement implements IStatement {
    IExpression expression;
    IStatement thenStatement;
    IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        Value result = this.expression.eval(state.getSymbol_table(), state.getHeap());
        if (result instanceof BoolValue boolResult) {
            IStatement statement;
            if (boolResult.getValue()) {
                statement = thenStatement;
            } else {
                statement = elseStatement;
            }

            MyIStack<IStatement> stack = state.getExecution_stack();
            stack.push(statement);

            //returns null instead of the program's state, so we'll update the execution stack
            state.setExecution_stack(stack);
            return null;
        } else {
            throw new ExecException("Error!!! 'If' requires a boolean!!!");
        }
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        Type typeExpr = expression.typeCheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            thenStatement.typeCheck(typeEnv.deepCopy());
            elseStatement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new ExecException("Error!!! The condition of 'if' requires a boolean!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(expression.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("if(%s){%s}else{%s}", expression.toString(), thenStatement.toString(), elseStatement.toString());
    }
}