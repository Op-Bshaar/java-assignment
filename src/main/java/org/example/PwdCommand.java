package org.example;

public class PwdCommand implements Command {
    @Override
    public String run(String[] args) {
        return System.getProperty("user.dir");
    }
}
