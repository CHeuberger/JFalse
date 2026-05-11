package cfh.jfalse.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;


public class ShowComponent implements AWTEventListener {

    private static ShowComponent instance = null;
    
    public static synchronized void install(Object parent) {
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (instance != null) {
            toolkit.removeAWTEventListener(instance);
        } else {
        }
        instance = new ShowComponent(parent);
        toolkit.addAWTEventListener(instance, AWTEvent.MOUSE_EVENT_MASK);
    }

    private final Object parent;
    
    private ShowComponent(Object parent) {
        this.parent = parent;
    }

    public void eventDispatched(AWTEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent evt = (MouseEvent) event;
            final int mask = MouseEvent.ALT_DOWN_MASK | MouseEvent.CTRL_DOWN_MASK;
            if (evt.getID() == MouseEvent.MOUSE_PRESSED && 
                (evt.getModifiersEx() & mask) == mask) 
            {
                printEvent(evt);
                evt.consume();
            }
        }
    }

    private void printEvent(MouseEvent evt) {
        System.out.println();
        Object source = evt.getSource();
        if (source instanceof Container) {
            Component comp = ((Container)source).findComponentAt(evt.getPoint());
            if (comp != null) {
                source = comp;
            }
        }
        if (source instanceof Component) {
            Point screen = ((Component)source).getLocationOnScreen();
            screen.translate(evt.getX(), evt.getY());
            System.out.println("relative: " + pointToString(evt.getPoint()) + 
                    "  screen: " + pointToString(screen));
        } else {
            System.out.println("relative: " + pointToString(evt.getPoint()));
        }
        if (source instanceof Container) {
            printHierarchie((Container) source);
        } else {
            System.out.println(source);
        }
    }

    private void printHierarchie(Container source) {
        String indent = "";
        String found = null;
        for (Container comp = source; comp != null; comp = comp.getParent()) {
            if (found == null) {
                found = searchField(source, comp);
                if (found == null && parent != null) {
                    found = searchField(source, parent);
                }
            }
            System.out.println(indent + comp);
            indent += (found != null) ? "~~" : "  ";
        }
        if (found != null) {
            System.out.println(found);
        }
    }

    private String searchField(Object source, Object comp) {
        final Class<?> clazz = comp.getClass();
        if (clazz.getPackage().getName().startsWith("cfh.jfalse")) {
            final Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    Object var = fields[i].get(comp);
                    if (var == source) {
                        return "found: " + fields[i];
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }
    
    private static final Object pointToString(Point point) {
        return "(" + point.x + "," + point.y + ")";
    }
}
