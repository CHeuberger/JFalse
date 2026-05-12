package cfh.jfalse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.SwingUtilities;

import cfh.jfalse.gui.GUI;
import cfh.jfalse.stack.Lambda;

/*
 * { factorial program in false! }
 *   [$1=~[$1-f;!*]?]f:          { fac() in FALSE }
 *   "calculate the factorial of [1..8]: "
 *   �^�'0-$$0>~\8>|$
 *   "result: "
 *   ~[\f;!.]?
 *   ["illegal input!"]?"
 *   "
 * 
 * { writes all prime numbers between 0 and 100 }
 *   99 9[1-$][\$@$@$@$@\/*=[1-$$[%\1-$@]?0=[\$.' ,\]?]?]#
 */
public class JFalse {

    public static final String VERSION = "1.1";
    
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
        } else {
            if (args[0].equalsIgnoreCase("-gui")) {
                SwingUtilities.invokeLater(GUI::new);
            } else if (args[0].equalsIgnoreCase("-f")) {
                if (args.length < 2) {
                    usage();
                } else {
                    File file = new File(args[1]);
                    executeFile(file);
                }
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        builder.append(' ');
                    }
                    builder.append(args[i]);
                }
                executeString(builder.toString());
            }
        }
    }
    
    private static void executeString(String string) {
        StreamInput input = new StreamInput(System.in);
        PrintOutput ouput = new PrintOutput(System.out);
        SingleStack stack = new SingleStack();
        SingleVariables var = new SingleVariables();
        Environment environment = new Environment(input, ouput, stack, var);
        try {
            Lambda lambda = new Parser().parse(string);
            lambda.execute(environment);
            if (environment.getStack().size() > 0) {
                System.err.println("\nWarning: stack not empty " + environment.getStack());
            }
        } catch (Exception ex) {
            System.err.println("trying to execute \"" + string + "\"");
            ex.printStackTrace();
        }
    }

    private static void executeFile(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    builder.append(line).append('\n');
                }
                in.close();
                executeString(builder.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("java " + JFalse.class.getName() + " -f <file> | -gui | <script>");
    }
}
