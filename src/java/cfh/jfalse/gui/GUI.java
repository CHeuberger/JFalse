package cfh.jfalse.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ThreadFactory;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import cfh.jfalse.Environment;
import cfh.jfalse.JFalse;
import cfh.jfalse.Parser;
import cfh.jfalse.Environment.Status;
import cfh.jfalse.cmd.Command;
import cfh.jfalse.stack.Lambda;
import cfh.jfalse.stack.StackObject;
import cfh.jfalse.stack.StatusBar;

public class GUI {

    private static final String WELCOME_MESSAGE = 
        ">>>>>  WELCOME TO JFALSE - v" + JFalse.VERSION + "  <<<<<\n";
    
    private final Environment environment;
    private final Parser parser;

    private final Action executeAction;
    private final JCheckBoxMenuItem stepping;
    
    private final JFrame frame;
    private final JTextArea command;
    private final JTextArea history;
    private final JEditorPane inputView;
    private final JTextArea outputView;
    private final JList stackView;
    private final JTable variableView;
    private final StatusBar statusbar;

    private final ThreadFactory threadFactory;
    private final StepListener stepListener;

    private final DocumentInput input;

    private final DocumentOutput output;

    private final ListStack stack;

    private final TableVariables variables;
    
    private Window helpView = null;
    private ExecutorRunnable executor = null;


    public GUI() {
        JPopupMenu popup;
        JMenuItem item;
        
        final Settings settings = Settings.getInstance();
        settings.addListener(Settings.FONT_SIZE, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setFont();
            }
        });
        
        ShowComponent.install(this);
        
        input = new DocumentInput();
        output = new DocumentOutput();
        stack = new ListStack();
        variables = new TableVariables();
        parser = new Parser();
        
        environment = new Environment(input, output, stack, variables);
        environment.addPropertyChangeListener(stack);
        environment.addPropertyChangeListener(variables);
        
        threadFactory = new ThreadFactory() {
            private int number = 0;
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Executor-" + number++);
                thread.setPriority(settings.getExecutorPriority());
                thread.setDaemon(true);
                System.out.println("created " + thread);
                return thread;
            }
        };
        stepListener = new StepListener();
        Command.addListener(stepListener);

        popup = newJPopupMenu("Command");
        executeAction = new AbstractAction("Execute") {
            @Override
            public void actionPerformed(ActionEvent e) {
                processLine(command.getText());
                command.requestFocusInWindow();
                command.selectAll();
            }
        };
        popup.add(executeAction);
        popup.addSeparator();
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command.setText(null);
            }
        });
        popup.add(item);
        
        command = newJTextArea(10, 40, "command");
        command.setComponentPopupMenu(popup);
        KeyStroke ctrlEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK);
        String actionKey = "execute";
        command.getActionMap().put(actionKey, executeAction);
        command.getInputMap().put(ctrlEnter, actionKey);
        
        popup = newJPopupMenu("History");
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                history.setText(WELCOME_MESSAGE);
                history.setCaretPosition(WELCOME_MESSAGE.length());
            }
        });
        popup.add(item);
        
        history = newJTextArea(WELCOME_MESSAGE, "history");
        history.setEditable(false);
        history.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int offset = history.getCaretPosition();
                    int line;
                    try {
                        line = history.getLineOfOffset(offset);
                        int start = history.getLineStartOffset(line);
                        int end = history.getLineEndOffset(line);
                        if (end > start + 1) { 
                            String text = history.getText(start, end - start - 1);
                            if (!text.isEmpty()) {
                                command.replaceSelection(text + "\n");
                            }
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    command.setCaretPosition(command.getDocument().getLength());
                    command.requestFocusInWindow();
                    e.consume();
                }
            }
        });
        history.setComponentPopupMenu(popup);
        
        JScrollPane scroll = titledScrollPane("Command", command);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSplitPane cmdhist = newJSplitPane(JSplitPane.VERTICAL_SPLIT, "cmdhist");
        cmdhist.setTopComponent(scroll);
        cmdhist.setBottomComponent(titledScrollPane("History", history));
        
        popup = newJPopupMenu("Input");
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input.clear();
            }
        });
        popup.add(item);
        
        inputView = new JTextPane(input.getDocument()) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                if (getParent() instanceof JViewport) {
                    JViewport port = (JViewport)getParent();
                    int w = port.getWidth();
                    Dimension max = ui.getMaximumSize(this);
                    Dimension pref = ui.getPreferredSize(this);
                    if ((w >= pref.width) && (w <= max.width)) {
                        return true;
                    }
                }
                return false;
            }
        };
        inputView.setName("input");
        inputView.setComponentPopupMenu(popup);
        
        popup = newJPopupMenu("Output");
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.clear();
            }
        });
        popup.add(item);
        
        outputView = newJTextArea(output.getDocument(), "output");
        outputView.setEditable(false);
        outputView.setComponentPopupMenu(popup);
        outputView.setLineWrap(false);
        
        JSplitPane inout = newJSplitPane(JSplitPane.VERTICAL_SPLIT, "inout");
        inout.setTopComponent(titledScrollPane("Input", inputView));
        inout.setBottomComponent(titledScrollPane("Output", outputView));
        inout.setResizeWeight(0.5);
        
        JSplitPane center = newJSplitPane(JSplitPane.HORIZONTAL_SPLIT, "center");
        center.setLeftComponent(inout);
        center.setRightComponent(cmdhist);
        center.setResizeWeight(0.4);

        popup = newJPopupMenu("Stack");
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stack.clear();
            }
        });
        popup.add(item);

        ListModel stackModel = stack.getListModel();
        stackModel.addListDataListener(new ScrollToEnd());
        stackView = newJList(stackModel, "stack");
        stackView.setFocusable(false);
        stackView.setPrototypeCellValue("1234567890123456789012345");
        stackView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    StackObject value = (StackObject) stackView.getSelectedValue();
                    command.replaceSelection(" " + value.toString());
                    command.setCaretPosition(command.getDocument().getLength());
                    command.requestFocusInWindow();
                    e.consume();
                }
            }
        });
        stackView.setComponentPopupMenu(popup);
        
        popup = newJPopupMenu("Variables");
        item = newJMenuItem("Clear");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                variables.clear();
            }
        });
        popup.add(item);
        
        variableView = newJTable(variables.getModel(), "variables");
        variableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        variableView.setPreferredScrollableViewportSize(null);
        variableView.getColumn(TableVariables.ADDR_LABEL).setPreferredWidth(30);
        variableView.getColumn(TableVariables.VALUE_LABEL).setPreferredWidth(140);
        variableView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = variableView.getSelectedRow();
                    StackObject value = (StackObject) variableView.getValueAt(row, 1);
                    command.replaceSelection(" " + value.toString());
                    command.setCaretPosition(command.getDocument().getLength());
                    command.requestFocusInWindow();
                    e.consume();
                }
            }
        });
        variableView.setComponentPopupMenu(popup);

        JSplitPane stavar = newJSplitPane(JSplitPane.VERTICAL_SPLIT, "stavar");
        stavar.setTopComponent(titledScrollPane("Stack", stackView));
        stavar.setBottomComponent(titledScrollPane("Variables", variableView));
        stavar.setResizeWeight(0.15);

        statusbar = new StatusBar();
        statusbar.setName("status");
        JMenuItem stop = newJMenuItem("Stop");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (confirm("Confirm Reset", "really stop?")) {
                    if (executor != null) {
                        executor.cancel();
                        executor = null;
                    }
                    statusbar.showStatus("stopped");
                }
            }
        });

        JMenuItem reset = newJMenuItem("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (confirm("Confirm Reset", "really reset?")) {
                    if (executor != null) {
                        executor.cancel();
                        executor = null;
                    }
                    input.flush();
                    output.clear();
                    stack.clear();
                    variables.clear();
                    statusbar.showStatus("reset");
                }
            }
        });

        JMenuItem clear = newJMenuItem("Clear All");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (confirm("Confirm Clear", "really clear all?")) {
                    input.clear();
                    output.clear();
                    history.setText(WELCOME_MESSAGE);
                    stack.clear();
                    variables.clear();
                    command.setText(null);
                    statusbar.clear();
                }
            }
        });

        JMenuItem quit = newJMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (confirm("Confirm Quit", "really quit?")) {
                    frame.dispose();
                }
            }
        });

        JMenu jfalse = newJMenu("JFalse");
        jfalse.add(stop);
        jfalse.add(reset);
        jfalse.addSeparator();
        jfalse.add(clear);
        jfalse.addSeparator();
        jfalse.add(quit);

        stepping = newJCheckBoxMenuItem("step");
        stepping.setToolTipText("step the program");
        stepping.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepListener.setEnabled(stepping.isSelected());
            }
        });
        
        final JCheckBoxMenuItem useHead = newJCheckBoxMenuItem("Use Head");
        useHead.setToolTipText("Stack operations work on the head of the stack");
        useHead.setSelected(settings.getStackUseHead());
        useHead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings set = Settings.getInstance();
                set.setStackUseHead(useHead.isSelected());
                statusbar.showStatus(set.getStackUseHead() ? "Use Head" : "Use Bottom");
            }
        });
        final JCheckBoxMenuItem extensions = newJCheckBoxMenuItem("Extensions");
        extensions.setToolTipText("Use extensions like '�'");
        extensions.setSelected(settings.getExtensions());
        extensions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings set = Settings.getInstance();
                set.setExtensions(extensions.isSelected());
                statusbar.showStatus(set.getExtensions() ? "Use Extensions" : "No Extensions");
            }
        });
        final JSpinner size = newJSpinner(new SpinnerNumberModel(settings.getFontSize(), 4, 80, 1), "size");
        size.setToolTipText("Font Size");
        size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) size.getValue();
                if (value >= 4) {
                    settings.setFontSize(value);
                }
            }
        });
        final JSpinner prio = newJSpinner(new SpinnerNumberModel(
                settings.getExecutorPriority(), Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1), "priority");
        prio.setToolTipText("Executor Priority");
        prio.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (Integer) prio.getValue();
                settings.setExecutorPriority(value);
            }
        });

        JMenu setmenu = newJMenu("Settings");
        setmenu.add(stepping);
        setmenu.addSeparator();
        setmenu.add(useHead);
        setmenu.add(extensions);
        setmenu.addSeparator();
        setmenu.add(size);
        setmenu.addSeparator();
        setmenu.add(prio);

        JMenuItem commandHelp = newJMenuItem("Commands");
        commandHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (helpView == null)  {
                    helpView = createHelpView();
                }
                helpView.setVisible(true);
            }
        });
        
        JMenu help = newJMenu("Help");
        help.add(commandHelp);
        // TODO about

        JMenuBar bar = new JMenuBar();
        bar.setName("bar");
        bar.add(jfalse);
        bar.add(setmenu);
        bar.add(help);


        setFont();

        frame= new JFrame("JFALSE");
        frame.setName("JFALSE");
        frame.setJMenuBar(bar);
        frame.setLayout(new BorderLayout());
        frame.add(center, BorderLayout.CENTER);
        frame.add(stavar, BorderLayout.LINE_END);
        frame.add(statusbar, BorderLayout.PAGE_END);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
                command.requestFocusInWindow();
            }
        });
    }
    
    private void addHistory(String text) {
        history.append(text + "\n");
        history.setCaretPosition(history.getText().length());
    }
    
    private void processLine(final String line) {
        addHistory(line);
        
        command.setEnabled(false);
        executor = new ExecutorRunnable(line);
        threadFactory.newThread(executor).start();
    }

    private boolean confirm(String title, String message) {
        int option = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    private void setFont() {
        Settings settings = Settings.getInstance();
        final Font monospaced = new Font("monospaced", Font.PLAIN, settings.getFontSize());
        command.setFont(monospaced);
        history.setFont(monospaced);
        inputView.setFont(monospaced);
        outputView.setFont(monospaced);
        stackView.setFont(monospaced);
        variableView.setFont(monospaced);
    }

    private JScrollPane titledScrollPane(String title, JComponent comp) {
        JScrollPane scroll = new JScrollPane(comp);
        scroll.setName(title + " ScrollPane");
        scroll.setBorder(new TitledBorder(title));
        if (comp.getName() == null) {
            comp.setName(title);
        }
        comp.setBorder(new EmptyBorder(2, 2, 2, 2));
        return scroll;
    }

    private JSpinner newJSpinner(SpinnerNumberModel model, String name) {
        JSpinner spinner = new JSpinner(model);
        spinner.setName(name);
        return spinner;
    }

    private JTable newJTable(TableModel model, String name) {
        JTable table = new JTable(model);
        table.setName(name);
        return table;
    }

    private JList newJList(ListModel model, String name) {
        JList list = new JList(model);
        list.setName(name);
        return list;
    }

    private JSplitPane newJSplitPane(int orientation, String name) {
        JSplitPane pane = new JSplitPane(orientation);
        pane.setName(name);
        pane.setOneTouchExpandable(true);
        return pane;
    }

    private JMenu newJMenu(String label) {
        JMenu menu = new JMenu(label);
        menu.setName(label + " Menu");
        return menu;
    }
    
    private JPopupMenu newJPopupMenu(String label) {
        JPopupMenu menu = new JPopupMenu(label);
        menu.setName(label + " Popup");
        return menu;
    }

    private JMenuItem newJMenuItem(String label) {
        JMenuItem item = new JMenuItem(label);
        item.setName(label + " MenuItem");
        return item;
    }
    
    private JCheckBoxMenuItem newJCheckBoxMenuItem(String label) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(label);
        item.setName(label + " MenuItem");
        return item;
    }

    private JTextArea newJTextArea(int rows, int columns, String name) {
        JTextArea comp = new JTextArea(rows, columns);
        comp.setName(name);
        return comp;
    }
    
    private JTextArea newJTextArea(String text, String name) {
        JTextArea comp = new JTextArea(text);
        comp.setName(name);
        return comp;
    }
    
    private JTextArea newJTextArea(Document doc, String name) {
        JTextArea comp = new JTextArea(doc);
        comp.setName(name);
        return comp;
    }
    
    private Window createHelpView() {
        JEditorPane pane = new JEditorPane();
        pane.setName("help");
        pane.setContentType("text/html");
        // TODO read help text
        pane.setText("<html>Just<br> a <br><b>test</b>");
        
        JFrame helpframe = new JFrame("Help");
        helpframe.setName("Help Frame");
        helpframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpframe.add(new JScrollPane(pane));
        helpframe.pack();
        return helpframe;
    }
    
    //==============================================================================================
    
    private final class ExecutorRunnable implements Runnable {
        private final String line;
        private Thread thread;

        private ExecutorRunnable(String line) {
            this.line = line;
        }
        
        private void cancel() {
            if (thread != null) {
                thread.interrupt();
            }
        }

        @Override
        public void run() {
            thread = Thread.currentThread();
            thread.setPriority(Settings.getInstance().getExecutorPriority());
            Lambda lambda;
            try {
                lambda = parser.parse(line);
                if (lambda != null) {
                    stepListener.setEnabled(stepping.isSelected());
                    environment.resetLevel();
                    lambda.execute(environment);
                }
                statusbar.clear();
            } catch (InterruptedException ex) {
                statusbar.showStatus(ex.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                statusbar.showError(ex.toString());
            } finally {
                thread = null;
                executor = null;
                command.setEnabled(true);
                command.selectAll();
            }
        }
    }

    //==============================================================================================
    
    private final class ScrollToEnd implements ListDataListener {

        @Override
        public void intervalRemoved(ListDataEvent e) {
            ensureVisible();
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            ensureVisible();
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            ensureVisible();
        }

        private void ensureVisible() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int index;
                    if (stack.isUseHead()) {
                        index = 0;
                    } else {
                        index = stackView.getModel().getSize()-1;
                    }
                    stackView.ensureIndexIsVisible(index);
                }
            });
        }
    }
    
    //==============================================================================================
    
    private class StepListener implements Command.Listener {

        private boolean enabled = true;
        
        private void setEnabled(boolean enable) {
            this.enabled = enable;
        }
        
        @Override
        public void beforeExecute(Command cmd, Environment env) {
            String text = cmd.toString();
            stepMessage(text, env);
        }

        @Override
        public void trace(String message, Environment env) {
            stepMessage(message, env);
        }
        
        private void stepMessage(String message, Environment env) {
            if (!enabled)
                return;
            Status old = env.setStatus(Status.PAUSE);
            try {
                String text = env.getLevel() + ": " + message;
                int option = JOptionPane.showConfirmDialog(frame, text, "Step", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.CANCEL_OPTION) {
                    enabled = false;
                }
            } finally {
                env.setStatus(old);
            }
        }
    }
}
