package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.StringType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements IStatement{
    private final IExpression expression;

    public CloseRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        Value value = expression.eval(state.getSymbol_table(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFile_table();
            if (fileTable.existsKey(fileName.getValue())) {
                BufferedReader br = fileTable.findValueForKey(fileName.getValue());
                try {
                    br.close();
                } catch (IOException e) {
                    throw new ExecException(String.format("Error!!! Invalid closing of %s", value));
                }
                fileTable.removeKey(fileName.getValue());
                state.setFile_table(fileTable);
            } else
                throw new ExecException(String.format("Error!!! %s doesn't exist in the FileTable!!!", value));
        } else
            throw new ExecException(String.format("Error!!! %s should evaluate to StringValue!!!", expression));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new ExecException("CError!!! CloseRFileStatement requires a string expression!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("CloseRFile(%s)", expression.toString());
    }
}