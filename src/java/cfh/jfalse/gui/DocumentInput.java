package cfh.jfalse.gui;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import cfh.jfalse.Input;

public class DocumentInput implements Input {

    private final DefaultStyledDocument document;
    private final Style normal;
    private final Style mark;
    
    private int readIndex = 0;
    
    public DocumentInput() {
        document = new DefaultStyledDocument();
        document.setDocumentFilter(new Filter());
        
        normal = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        mark = document.addStyle("mark", normal);
        StyleConstants.setForeground(mark, Color.RED.darker());
        StyleConstants.setBackground(mark, new Color(0, 0, 255, 32));
    }
    
    StyledDocument getDocument() {
        return document;
    }

    public int read() {
        if (readIndex > document.getLength()-1)    // extra linefeed
            return -1;
        try {
            char ch = document.getText(readIndex, 1).charAt(0);
            moveReadIndex(readIndex + 1);
            return ch;
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void flush() {
        moveReadIndex(0);
    }
    
    public void clear() {
        moveReadIndex(0);
        try {
            document.remove(0, document.getLength());
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void moveReadIndex(int index) {
        readIndex = index;
        document.setCharacterAttributes(0, readIndex, mark, true);
        document.setCharacterAttributes(readIndex, document.getLength()-readIndex, normal, true);
    }
    
    //----------------------------------------------------------------------------------------------
    
    private class Filter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
        throws BadLocationException {
            if (offset >= readIndex) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= readIndex) {
                super.remove(fb, offset, length);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
        throws BadLocationException {
            if (offset >= readIndex) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
