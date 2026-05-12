package cfh.jfalse.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import cfh.jfalse.Environment;
import cfh.jfalse.Stack;
import cfh.jfalse.stack.StackObject;

public class ListStack implements Stack, PropertyChangeListener {

    private final LinkedList<StackObject> data;
    private final StackListModel model;

    private boolean useHead;
    
    private boolean update = true;
    
    private int lastUpdateSize;
    
    public ListStack() {
        data = new LinkedList<StackObject>();
        model = new StackListModel();
        
        final Settings settings = Settings.getInstance();
        settings.addListener(Settings.STACK_USE_HEAD, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                synchronized (data) {
                    Collections.reverse(data);
                    model.fireContentsChanged(0, data.size()-1);
                }
                useHead = settings.getStackUseHead();
            }
        });
        useHead = settings.getStackUseHead();
    }
    
    public boolean isUseHead() {
        return useHead;
    }
    
    public ListModel<StackObject> getListModel() {
        return model;
    }
    
    @Override
    public void push(StackObject obj) {
        if (obj == null)
            throw new IllegalArgumentException("null");
        if (useHead) {
            synchronized (data) {
                data.addFirst(obj);
            }
            model.fireIntervalAdded(0, 0);
        } else {
            int index;
            synchronized (data) {
                data.addLast(obj);
                index = data.size()-1;
            }
            model.fireIntervalAdded(index, index);
        }
    }

    @Override
    public StackObject pop() {
        if (useHead) {
            StackObject top;
            synchronized (data) {
                top = data.pop();
            }
            model.fireIntervalRemoved(0, 0);
            return top;
        } else {
            StackObject last;
            int index;
            synchronized (data) {
                last = data.removeLast();
                index = data.size();
            }
            model.fireIntervalRemoved(index, index);
            return last;
        }
    }

    @Override
    public StackObject peek() {
        if (useHead) {
            synchronized (data) {
                return data.peek();
            }
        } else {
            synchronized (data) {
                return data.getLast();
            }
        }
    }
    
    @Override
    public StackObject peek(int index) {
        if (useHead) {
            synchronized (data) {
                return data.get(index);
            }
        } else {
            synchronized (data) {
                return data.get(data.size() - index - 1);
            }
        }
    }

    @Override
    public int size() {
        synchronized (data) {
            return data.size();
        }
    }
    
    public void clear() {
        int size;
        synchronized (data) {
            size = data.size();
        }
        if (size > 0) {
            synchronized (data) {
                data.clear();
            }
            model.fireIntervalRemoved(0, size-1);
        }
    }
    
    private void setUpdate(boolean update) {
        if (update != this.update) {
            this.update = update;
            if (update) {
                model.fireContentsChanged(0, lastUpdateSize-1);
                int size;
                synchronized (data) {
                    size = data.size();
                }
                if (size < lastUpdateSize) {
                    model.fireIntervalRemoved(size, lastUpdateSize-1);
                } else if (size > lastUpdateSize) {
                    model.fireIntervalAdded(lastUpdateSize, size-1);
                }
            } else {
                synchronized (data) {
                    lastUpdateSize = data.size();
                }
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String name = evt.getPropertyName();
        if (name.equals(Environment.PROP_STATUS)) {
            setUpdate(evt.getNewValue() != Environment.Status.RUNNING);
        }
    }
    
    @Override
    public String toString() {
        return data.toString();
    }

    //==============================================================================================
    
    private class StackListModel extends AbstractListModel<StackObject> {
        
        private static final long serialVersionUID = 5904219800262686730L;

        private void fireContentsChanged(int index0, int index1) {
            if (update) 
                super.fireContentsChanged(this, index0, index1);
        }

        private void fireIntervalAdded(int index0, int index1) {
            if (update) 
                super.fireIntervalAdded(this, index0, index1);
        }

        private void fireIntervalRemoved(int index0, int index1) {
            if (update) 
                super.fireIntervalRemoved(this, index0, index1);
        }

        @Override
        public StackObject getElementAt(int index) {
            synchronized (data) {
                return data.get(index);
            }
        }

        @Override
        public int getSize() {
            synchronized (data) {
                return data.size();
            }
        }
    }
}
