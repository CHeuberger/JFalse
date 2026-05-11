package cfh.jfalse;

public class Tokenizer {

    private final char[] text;
    
    private int index;
    
    public Tokenizer(String string) {
        text = string.toCharArray();
        index = 0;
        skipWhitespace();
    }

    public void skipWhitespace() {
        while (index < text.length && isWhitespace(index)) {
            index += 1;
        }
    }
    
    public int getIndex() {
        return index;
    }
    
    public boolean hasNext() {
        return index < text.length;
    }
    
    public char next() {
        return text[index++];
    }
    
    public void back() {
        index -= 1;
        if (index < 0)
            throw new IndexOutOfBoundsException();
    }
    
    private boolean isWhitespace(int i) {
        char ch = text[i];
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        if (0 <= index && index < text.length) {
            builder.insert(index, '|');
        }
        return builder.toString();
    }
}
