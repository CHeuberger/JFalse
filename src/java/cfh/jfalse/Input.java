package cfh.jfalse;

import java.io.IOException;

public interface Input {

    public int read() throws IOException;
    
    public void flush() throws IOException;
}
