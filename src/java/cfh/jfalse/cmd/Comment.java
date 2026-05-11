package cfh.jfalse.cmd;

import cfh.jfalse.Environment;

public class Comment extends Command {

    private final String comment;
    
    public Comment(String comment) {
        this.comment = comment;
    }
    
    @Override
    public void execute0(Environment environment) {
        // do nothing
    }

    @Override
    public String toString() {
        return "{" + comment + "}";
    }

}
