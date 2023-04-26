package com.example.a7gui;

import Exceptions.ADTException;
import Exceptions.EvalException;
import Exceptions.ExecException;
import Model.ADTs.*;
import Model.Expression.*;
import Model.ProgramState.ProgramState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Repository.IRepository;
import Repository.Repository;
import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chooseProgram {

    private ProgramExecutorController exec;

    @FXML
    private ListView<IStatement> programsListView;

    public void setExec(ProgramExecutorController exec) { this.exec = exec; }

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() throws ExecException, EvalException, ADTException {
        ObservableList<IStatement> programs = getPrograms();

        programsListView.setItems(programs);
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent action) {
        IStatement chosen_statement = programsListView.getSelectionModel().getSelectedItem();
        if(chosen_statement == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("No program selected!!!");
            alert.showAndWait();
        }
        else
        {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try
            {
                chosen_statement.typeCheck(new MyDictionary<>());
                ProgramState state = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap(), chosen_statement, new MySemaphore());
                IRepository repository = new Repository(state, "log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                exec.setController(controller);
            }
            catch (ExecException | EvalException | ADTException | IOException exception)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStatement> getPrograms() throws ExecException, EvalException, ADTException {
        List<IStatement> programs = new ArrayList<>();

        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignationStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        programs.add(ex1);
        IStatement ex2 = new CompoundStatement(new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignationStatement("a", new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new
                                ArithmeticExpression('*',new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignationStatement("b",new ArithmeticExpression('+',new VariableExpression("a"), new ValueExpression(new
                                        IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));
        programs.add(ex2);
        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignationStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignationStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignationStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
       programs.add(ex3);
        IStatement ex4 = new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(new AssignationStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf"))))))))));
        programs.add(ex4);
        IStatement ex5 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
                        new CompoundStatement(new AssignationStatement("a", new ValueExpression(new IntValue(5))),
                                new CompoundStatement(new AssignationStatement("b", new ValueExpression(new IntValue(7))),
                                        new IfStatement(new RelationalExpression(">", new VariableExpression("a"),
                                                new VariableExpression("b")),new PrintStatement(new VariableExpression("a")),
                                                new PrintStatement(new VariableExpression("b")))))));
      programs.add(ex5);
        IStatement ex6 = new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(new AssignationStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFile(new VariableExpression("varf"),
                                                                "varc"),
                                                                new CompoundStatement(new PrintStatement
                                                                        (new VariableExpression("varc")),
                                                                        new CompoundStatement(new CloseRFileStatement
                                                                                (new VariableExpression("varf")),
                                                                                new CloseRFileStatement(
                                                                                        new VariableExpression("varf")))))))))));
       programs.add(ex6);
        IStatement ex7 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignationStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(">",
                                new VariableExpression("v"), new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignationStatement("v",new ArithmeticExpression('-',
                                                new VariableExpression("v"), new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));
        programs.add(ex7);
        /*
        int v; Ref int a; v=10;new(a,22);
        fork(wH(a,30);v=32;print(v);print(rH(a)));
        print(v);print(rH(a))
         */
        IStatement ex8 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(new AssignationStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))),
                                                new CompoundStatement(new AssignationStatement("v", new ValueExpression
                                                        (new IntValue(32))),
                                                        new CompoundStatement(new PrintStatement(
                                                                new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(
                                                                        new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(
                                                                new VariableExpression("a")))))))));
       programs.add(ex8);


        IStatement ex109 = new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new PrintStatement(
                                new ReadHeapExpression(new VariableExpression("a"))))));
        programs.add(ex109);


        IStatement ex12 = new CompoundStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())), new CompoundStatement(
                        new VariableDeclarationStatement("cnt", new IntType()), new CompoundStatement(
                                new NewStatement("v1", new ValueExpression(new IntValue(1))),
                                new CompoundStatement(new CreateSemaphoreStatement
                                        ("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(
                                                new AcquireStatement("cnt"), new CompoundStatement(
                                                        new HeapWritingStatement("v1", new ArithmeticExpression('*', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(
                                                        new VariableExpression("v1"))), new ReleaseStatement("cnt"))))),
                                                new CompoundStatement(new ForkStatement(new CompoundStatement(
                                                        new AcquireStatement("cnt"), new CompoundStatement(
                                                                new HeapWritingStatement("v1", new ArithmeticExpression('*',  new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                        new CompoundStatement(new HeapWritingStatement("v1",
                                                                new ArithmeticExpression('*',  new ReadHeapExpression
                                                                        (new VariableExpression("v1")), new ValueExpression(new IntValue(2)))),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new ReleaseStatement("cnt")))))),
                                                        new CompoundStatement(new AcquireStatement("cnt"), new CompoundStatement(
                                                                new PrintStatement(new ArithmeticExpression('-', new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                        new ReleaseStatement("cnt")))))))));
        programs.add(ex12);
        ex12.typeCheck(new MyDictionary<>());


       return FXCollections.observableArrayList(programs);
    }
}