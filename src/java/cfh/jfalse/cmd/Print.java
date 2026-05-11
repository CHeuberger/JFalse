package cfh.jfalse.cmd;

import cfh.jfalse.Environment;

public class Print extends Command {

    private final String text;
    
    public Print(String text) {
        this.text = text;
    }
    
    @Override
    public void execute0(Environment environment) {
        environment.getOutput().append(text);
    }

    @Override
    public String toString() {
        return "\"" + text + "\"";
    }
}
