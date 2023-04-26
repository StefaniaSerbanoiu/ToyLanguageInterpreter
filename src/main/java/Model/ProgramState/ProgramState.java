package Model.ProgramState;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ADTs.*;
import Model.Statement.IStatement;
import Model.Value.Value;

import java.io.BufferedReader;
import java.util.List;



public class ProgramState {
    private MyIStack<IStatement> execution_stack;
    private MyIDictionary<String, Value> symbol_table;
    private MyIList<Value> output;
    private MyIDictionary<String, BufferedReader> file_table;
    private MyIHeap heap;

    private MyIDictionary<Integer, Integer> lock_table;
    private IStatement first_program;
    private int id = 1;
    private static int lastId = 0;

    private MyISemaphore semaphore_table;





    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, Value> symTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, IStatement program, MySemaphore semaphore) {
        this.execution_stack = stack;
        this.symbol_table = symTable;
        this.output = out;
        this.file_table = fileTable;
        this.heap = heap;
        this.first_program = program.deepCopy();
        this.execution_stack.push(this.first_program);
        this.id = setId();
        this.semaphore_table = semaphore;
    }

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, Value> symTable, MyIList<Value> out, MyIDictionary<String, BufferedReader> fileTable, MyIHeap heap, MySemaphore semaphore) {
        this.execution_stack = stack;
        this.symbol_table = symTable;
        this.output = out;
        this.file_table = fileTable;
        this.heap = heap;
        this.id = setId();
        this.semaphore_table = semaphore;
    }


    public synchronized int setId() {
        lastId++;
        return lastId;
    }

    public int getId() { return id; }

    public void setExecution_stack(MyIStack<IStatement> newStack) {
        this.execution_stack = newStack;
    }

    public void setSymbol_table(MyIDictionary<String, Value> newSymTable) {
        this.symbol_table = newSymTable;
    }

    public void setOutput(MyIList<Value> newOut) {
        this.output = newOut;
    }

    public void setFile_table(MyIDictionary<String, BufferedReader> newFileTable) {
        this.file_table = newFileTable;
    }

    public void setHeap(MyIHeap newHeap) {
        this.heap = newHeap;
    }

    public MyIStack<IStatement> getExecution_stack() {
        return execution_stack;
    }

    public MyIDictionary<String, Value> getSymbol_table() {
        return symbol_table;
    }

    public MyIList<Value> getOutput() {
        return output;
    }

    public MyIDictionary<String, BufferedReader> getFile_table() {
        return file_table;
    }

    public MyIHeap getHeap() {
        return heap;
    }

    public MyISemaphore getSemaphore_table() { return semaphore_table; }

    public void setSemaphore_table(MyISemaphore semaphore_table) { this.semaphore_table = semaphore_table; }

    public ProgramState oneStep() throws ExecException, ADTException, EvalException {
        if (execution_stack.isEmpty())
            throw new ExecException("Program state stack is empty!");
        IStatement currentStatement = execution_stack.pop();
        return currentStatement.execute(this);
    }


    public boolean isNotCompleted() {
        return execution_stack.isEmpty();
    }




    /*
    methods for transforming the program state into a printable form (=>string)
     */


    @Override
    public String toString() {
        return "Id = " + id + "\nExecution stack: \n" + execution_stack.reverse() + "\nSymbol table: \n" + symbol_table.toString() +
                "\nOutput list: \n" + output.toString() + "\nFile table:\n" + file_table.toString() + "\nHeap memory:\n" +
                heap.toString() + "\n" + "Semaphore Table l:\n" + semaphore_table.toString() + "\n";
    }

    public String programStateString() throws ADTException {
        return "Id = " + id + "\nExecution stack: \n" + executionStackString() + "Symbol table: \n" + symbolTableString() +
                "Output list: \n" + outputString() + "File table:\n" + fileTableString() + "Heap memory:\n" + heapString() +
                "\nSemaphore table e: \n" + semaphoreString() + "\n";
    }

    public String executionStackString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        List<IStatement> stack = execution_stack.reverse();
        for (IStatement statement: stack) {
            exeStackStringBuilder.append(statement.toString()).append("\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symbolTableString() throws ADTException {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key: symbol_table.allKeys()) {
            symTableStringBuilder.append(String.format("%s -> %s\n", key, symbol_table.findValueForKey(key).toString()));
        }
        return symTableStringBuilder.toString();
    }

    public String outputString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (Value elem: output.getList()) {
            outStringBuilder.append(String.format("%s\n", elem.toString()));
        }
        return outStringBuilder.toString();
    }

    public String fileTableString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String key: file_table.allKeys()) {
            fileTableStringBuilder.append(String.format("%s\n", key));
        }
        return fileTableStringBuilder.toString();
    }

    public String heapString() throws ADTException {
        StringBuilder heapStringBuilder = new StringBuilder();
        for (int key: heap.allKeys()) {
            heapStringBuilder.append(String.format("%d -> %s\n", key, heap.getHeapValueFromPosition(key)));
        }
        return heapStringBuilder.toString();
    }

    public String semaphoreString() throws ADTException {
        StringBuilder semaphoreStringBuilder = new StringBuilder();
        for(int key : semaphore_table.getSemaphore_table().keySet())
        {
            semaphoreStringBuilder.append(String.format("%d -> (%d, %s)\n", key, semaphore_table.getFromSemaphore(key).getKey(), semaphore_table.getFromSemaphore(key).getValue()));
        }
        return semaphoreStringBuilder.toString();
    }
}