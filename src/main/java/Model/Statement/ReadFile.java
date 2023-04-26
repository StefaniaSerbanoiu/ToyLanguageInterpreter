package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.Expression.IExpression;
import Model.ProgramState.ProgramState;
import Model.Type.IntType;
import Model.Type.StringType;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements IStatement{
    private final IExpression expression;
    private final String varName;

    public ReadFile(IExpression expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }
    @Override
    public ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException {
        MyIDictionary<String, Value> symTable = state.getSymbol_table();
        MyIDictionary<String, BufferedReader> fileTable = state.getFile_table();

        if (symTable.existsKey(varName)) {
            Value value = symTable.findValueForKey(varName);
            if (value.getType().equals(new IntType())) {
                Value fileNameValue = expression.eval(symTable, state.getHeap());
                if (fileNameValue.getType().equals(new StringType())) {
                    StringValue castValue = (StringValue)fileNameValue;
                    if (fileTable.existsKey(castValue.getValue())) {
                        BufferedReader br = fileTable.findValueForKey(castValue.getValue());
                        try {
                            String line = br.readLine();
                            if (line == null)
                                line = "0";
                            symTable.insertValueAtKey(varName, new IntValue(Integer.parseInt(line)));
                        } catch (IOException e) {
                            throw new ExecException(String.format("Error!!! Could not read from file %s", castValue));
                        }
                    } else {
                        throw new ExecException(String.format("Error!!! The file table does not contain %s", castValue));
                    }
                } else {
                    throw new ExecException(String.format("Error!!! %s should have as a result of evaluation a StringType", value));
                }
            } else {
                throw new ExecException(String.format("Error!!! %s != int (IntType)!!!", value));
            }
        } else {
            throw new ExecException(String.format("Error!!! %s wasn't defined in the symbol table!!!", varName));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            if (typeEnv.findValueForKey(varName).equals(new IntType()))
                return typeEnv;
            else
                throw new ExecException("Error!!! Reading a file requires an int as a function param!!!");
        else
            throw new ExecException("Error!!! Reading a file requires a string as es expression param!!!");
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFile(expression.deepCopy(), varName);
    }

    @Override
    public String toString() {
        return String.format("ReadFile(%s, %s)", expression.toString(), varName);
    }
}