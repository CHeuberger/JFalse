package cfh.jfalse.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;

public class Settings {
    
    public static final String STACK_USE_HEAD = "stackUseHead";
    public static final String EXEC_PRIO = "executorPriority";
    public static final String FONT_SIZE = "fontSize";
    public static final String EXTENSIONS = "extensions";
    
    private static final Settings instance = new Settings();
    
    public static Settings getInstance() {
        return instance;
    }
    
    //----------------------------------------------------------------------------------------------
    
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());
    
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
    private boolean stackUseHead;
    private int executorPriority;
    private int fontSize;
    private boolean extensions;
    
    private Settings() {
        loadPreferences();
    }

    private void loadPreferences() {
        stackUseHead = prefs.getBoolean(STACK_USE_HEAD, false);
        executorPriority = prefs.getInt(EXEC_PRIO, (Thread.NORM_PRIORITY+Thread.MIN_PRIORITY)/2);
        fontSize = prefs.getInt(FONT_SIZE, 12);
        extensions = prefs.getBoolean(EXTENSIONS, true);
    }
    
    public void addListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public void addListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    public void removeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public void removeListener(String propertyName, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    public void setStackUseHead(boolean newValue) {
        boolean oldValue = stackUseHead;
        stackUseHead = newValue;
        prefs.putBoolean(STACK_USE_HEAD, stackUseHead);
        changeSupport.firePropertyChange(STACK_USE_HEAD, oldValue, newValue);
    }
    
    public boolean getStackUseHead() {
        return stackUseHead;
    }
    
    public void setExecutorPriority(int newPrio) {
        int oldPrio = executorPriority;
        executorPriority = newPrio;
        prefs.putInt(EXEC_PRIO, newPrio);
        changeSupport.firePropertyChange(EXEC_PRIO, oldPrio, newPrio);
    }
    
    public int getExecutorPriority() {
        return executorPriority;
    }
    
    public void setFontSize(int newSize) {
        int oldSize = fontSize;
        fontSize = newSize;
        prefs.putInt(FONT_SIZE, newSize);
        changeSupport.firePropertyChange(FONT_SIZE, oldSize, newSize);
    }

    public int getFontSize() {
        return fontSize;
    }
    
    public void setExtensions(boolean newValue) {
        boolean oldValue = extensions;
        extensions = newValue;
        prefs.putBoolean(EXTENSIONS, newValue);
        changeSupport.firePropertyChange(EXTENSIONS, oldValue, newValue);
    }
    
    public boolean getExtensions() {
        return extensions;
    }
}
