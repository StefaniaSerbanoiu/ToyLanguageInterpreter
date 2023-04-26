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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFileStatement implements IStatement{
    private final IExpression expression;

    public OpenRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        Value value = expression.eval(state.getSymbol_table(), state.getHeap());
        if (value.getType().equals(new StringType())) {
            StringValue fileName = (StringValue) value;
            MyIDictionary<String, BufferedReader> fileTable = state.getFile_table();
            if (!fileTable.existsKey(fileName.getValue())) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(fileName.getValue()));
                } catch (FileNotFoundException e) {
                    throw new ExecException(String.format("Error!!! %s could not be opened!!!", fileName.getValue()));
                }
                fileTable.insertValueAtKey(fileName.getValue(), br);
                state.setFile_table(fileTable);
            } else {
                throw new ExecException(String.format("Error!!! %s already opened!!!", fileName.getValue()));
            }
        } else {
            throw new ExecException(String.format("Error!!! %s does not evaluate to StringType!!!", expression.toString()));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new ExecException("Error!!! OpenRFile uses a string!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(expression.deepCopy());
    }

    @Override
    public String toString() {
        return String.format("OpenRFile(%s)", expression.toString());
    }
}