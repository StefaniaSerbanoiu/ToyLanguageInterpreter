package view;

import Exceptions.ADTException;
import Model.ADTs.MyDictionary;
import Model.ADTs.MyIDictionary;

import java.util.Scanner;

public class TextMenu {
    private MyIDictionary<String, Command> commands;

    public TextMenu() {
        this.commands = new MyDictionary<>();
    }

    public void addCommand(Command command) {
        this.commands.insertValueAtKey(command.getKey(), command);
    }

    private void printMenu() {
        System.out.println("############################# COMMAND MENU ############################");
        for (Command command: commands.allValues()) {
            String line = String.format("%4s: %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.println("Introduce your option: ");
            String key = scanner.nextLine();
            try {
                Command command = commands.findValueForKey(key);
                command.execute();
            } catch (ADTException exception){
                System.out.println("Error!!! Invalid option!!!");
            }
        }
    }
}