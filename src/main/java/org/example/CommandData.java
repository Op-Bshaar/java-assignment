package org.example;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandData {
    private String command;
    private String[] parameters;
    public String[] getParameters() {
        return parameters;
    }

    private String redirectFile = null;
    public String getRedirectFile() {
        return redirectFile;
    }

    private boolean append = false;
    public boolean isAppend() {
        return append;
    }
    public String getCommand(){
        return command;
    }
    public String getFirstParameter() {
        return parameters.length > 0 ? parameters[0] : null;
    }
    
    public CommandData(String commandInput) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\S+");
        Matcher matcher = pattern.matcher(commandInput);
        ArrayList<String> partsList = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                partsList.add(matcher.group(1));
            } else {
                partsList.add(matcher.group());
            }
        }

        for (int i = partsList.size() - 1; i >= 0; i--) {
            String part = partsList.get(i);
            if (part.equals(">") || part.equals(">>")) {
                if (i + 1 < partsList.size()) { // Ensure there is a filename after `>` or `>>`
                    this.redirectFile = partsList.get(i + 1);
                    this.append = part.equals(">>");
                    partsList = new ArrayList<>(partsList.subList(0, i));
                } else {
                    System.out.println("Usage <command> " + part + " <file path>");
                }
                break;
            }
        }

        this.command = partsList.get(0);
        this.parameters = partsList.size() > 1 ? partsList.subList(1, partsList.size()).toArray(new String[0])
                : new String[0];
    }

}
