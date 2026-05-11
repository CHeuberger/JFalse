package cfh.jfalse.stack;

import static javax.swing.BorderFactory.*;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import cfh.jfalse.JFalse;

public class StatusBar extends JPanel {

    private static final Border border = 
        createCompoundBorder(createLoweredBevelBorder(), createEmptyBorder(2, 4, 2, 2));
    
    private final JLabel version;
    private final JLabel status;
    
    public StatusBar() {
        status = new JLabel();
        status.setBorder(border);
        status.setToolTipText("status message");
        
        version = new JLabel(JFalse.VERSION);
        version.setBorder(border);
        version.setToolTipText("version");
        
        setLayout(new BorderLayout());
        add(status, BorderLayout.CENTER);
        add(version, BorderLayout.LINE_END);
    }
    
    public void clear() {
        status.setForeground(null);
        status.setText(null);
    }
    
    public void showStatus(String text) {
        status.setForeground(null);
        status.setText(text);
    }
    
    public void showError(String text) {
        status.setForeground(Color.RED.darker());
        status.setText(text);
    }
}
