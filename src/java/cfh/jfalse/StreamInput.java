package cfh.jfalse;

import java.io.IOException;
import java.io.InputStream;

public class StreamInput implements Input {

    private final InputStream stream;
    
    public StreamInput(InputStream stream) {
        if (stream == null) throw new NullPointerException();
        
        this.stream = stream;
    }
    
    @Override
    public void flush() throws IOException {
        int count;
        while ((count = stream.available()) > 0) {
            stream.skip(count);
        }
    }

    @Override
    public int read() throws IOException {
        return stream.read();
    }

}
