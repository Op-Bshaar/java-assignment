package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class CommandLineInterpeter {
    public void runCommandLine(){
        System.out.println("please enter command :)");
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");

        while (true) {
            String commandIn = scanner.nextLine().trim();
            String[] parts = commandIn.split(" ");
            String cmd = parts[0];
            String[] argsArray = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
            Command command;

            switch (cmd) {
                case "mkdir":
                    command = new MkdirCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "rm":
                    command = new RmCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "ls":
                    command = new LsCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "pwd":
                    String path = pwd();
                    System.out.println(path);
                    break;
                case "exit":
                    System.out.println("Exiting..");
                    scanner.close();
                    return;
                default:
                    System.out.println("Command " + cmd + " not found.");
            }
            System.out.print("> ");
        }
    }
    public String pwd(){
        return System.getProperty("user.dir");
    }
}
