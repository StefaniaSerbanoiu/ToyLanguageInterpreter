package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ProgramState.ProgramState;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.ADTs.MyIStack;

public class CompoundStatement implements IStatement {
    private final IStatement firstStatement;
    private final IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public ProgramState execute(ProgramState state){
        MyIStack<IStatement> stack = state.getExecution_stack();
        stack.push(secondStatement);
        stack.push(firstStatement);
        //returns null instead of the program's state, so we'll update the execution stack
        state.setExecution_stack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        return secondStatement.typeCheck(firstStatement.typeCheck(typeEnv));
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(firstStatement.deepCopy(), secondStatement.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("(%s ; %s)", firstStatement.toString(), secondStatement.toString());
    }
}