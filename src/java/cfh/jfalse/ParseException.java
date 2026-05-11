package cfh.jfalse;

public class ParseException extends Exception {

    private final String text;
    private final int offset;
    
    public ParseException(String message, String text, int offset) {
        super(message);
        this.text = text;
        this.offset = offset;
    }
    
    public ParseException(String message, Tokenizer tokenizer) {
        this(message, tokenizer.toString(), tokenizer.getIndex()-1);
    }

    public int getOffset() {
        return offset;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(", offset: ").append(offset);
        if (text != null) {
            builder.append(", text: ");
            if (offset >= 0 && offset < text.length()) {
                int len = builder.length();
                builder.append(text);
                builder.insert(len+offset+1, "<<");
                builder.insert(len+offset, ">>");
                builder.insert(len, '"').append('"');
            }
        }
        return builder.toString();
    }
}
