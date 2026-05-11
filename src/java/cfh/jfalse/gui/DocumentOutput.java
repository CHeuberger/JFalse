package cfh.jfalse.gui;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import cfh.jfalse.Output;

public class DocumentOutput implements Output {

    private final PlainDocument document;
    
    public DocumentOutput() {
        document = new PlainDocument();
    }
    
    Document getDocument() {
        return document;
    }
    
    public void append(String text) {
        try {
            document.insertString(document.getLength(), text, null);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void append(char ch) {
        append(Character.toString(ch));
    }
    
    public void append(int value) {
        append(Integer.toString(value));
    }

    public void flush() {
        // TODO Auto-generated method stub
    }
    
    public void clear() {
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
