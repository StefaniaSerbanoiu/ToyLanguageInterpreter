package Model.Statement;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ProgramState.ProgramState;
import Model.Type.Type;
import Model.ADTs.MyIDictionary;

public interface IStatement {
    ProgramState execute(ProgramState state) throws ExecException, EvalException, ADTException;
    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws ExecException, EvalException, ADTException;
    IStatement deepCopy();
}