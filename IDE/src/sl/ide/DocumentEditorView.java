/*
 * Copyright (c) 2007, Sun Microsystems, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems, Inc. nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package sl.ide;

//<editor-fold defaultstate="collapsed" desc="Imports">
import sl.ide.tasks.ExecuteTask;
import java.awt.Image;
import javax.swing.text.BadLocationException;
import sl.ide.components.LineViewTextArea;
import sl.ide.io.TextAreaReader;
import sl.ide.io.TextAreaDataWriter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import org.jdesktop.application.Application;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import sl.ide.components.SingleLineSelector;
import sl.parser.AbstractParseException;
import sl.program.Debugger.Mode;
//</editor-fold>

/**
 * This application is a simple text editor. This class displays the main frame
 * of the application and provides much of the logic. This class is called by
 * the main application class, DocumentEditorApp. For an overview of the
 * application see the comments for the DocumentEditorApp class.
 */
public class DocumentEditorView extends FrameView {

    // properties
    private File file;
    private boolean modified = false;
    private boolean canUndo = false;
    private boolean canRedo = false;
    private boolean editMode = true;
    private boolean executeMode = false;
    private boolean debugButtonsEnabled = false;
    // end properties
    private Readable reader;
    private Appendable writer;
    //
    private boolean df_file = true;
    private LineViewTextArea lineView = null;
    private BreakpointsManager breakpoints = null;
    private UndoManager undoManager = null;
    private ExecuteTask executeTask = null;
    private SingleLineSelector lineSelector = null;
    private WatchesManager watchesManager = null;

    public DocumentEditorView(SingleFrameApplication app) {
        super(app);

        // generated GUI builder code
        initComponents();

        Image icons[] = {
            getResourceMap().getImageIcon("mainFrame.icon16").getImage(),
            getResourceMap().getImageIcon("mainFrame.icon32").getImage()
        };
        getFrame().setIconImages(Arrays.asList(icons));

        tabbedPane.setEnabledAt(1, false);
        tabbedPane.setSelectedIndex(0);

        reader = new TextAreaReader(outputTextArea);
        writer = new TextAreaDataWriter(outputTextArea);

        outputTextArea.setDataReader(reader);

        lineView = new LineViewTextArea(textArea);
        editorTextAreaScrollPane.setRowHeaderView(lineView);
        breakpoints = new BreakpointsManager(lineView);

        lineSelector = new SingleLineSelector(textArea);

        watchesManager = new WatchesManager(watchesStackTable, watchesVariablesTable);

        initUndoManager();

        newfile();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connect action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // if the document is ever edited, assume that it needs to be saved
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }

            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }
        });

        // ask for confirmation on exit
        getApplication().addExitListener(new ConfirmExit());
    }

    private void initUndoManager() {
        undoManager = new UndoManager();
        undoManager.setLimit(100);
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                updateUndoState();
            }
        });
        updateUndoState();
    }

    private void updateUndoState() {
        setCanUndo(undoManager.canUndo());
        setCanRedo(undoManager.canRedo());
    }

    //<editor-fold defaultstate="collapsed" desc="Class properties">
    public File getFile() {
        return file;
    }

    private void setFile(File file) {
        File oldValue = this.file;
        this.file = file;
        String appId = getResourceMap().getString("Application.id");
        getFrame().setTitle(file.getName() + " - " + appId);
        firePropertyChange("file", oldValue, this.file);
    }

    public boolean isModified() {
        return modified;
    }

    private void setModified(boolean modified) {
        boolean oldValue = this.modified;
        this.modified = modified;
        firePropertyChange("modified", oldValue, this.modified);
    }

    public boolean isCanUndo() {
        return canUndo;
    }

    public void setCanUndo(boolean canUndo) {
        boolean oldValue = this.canUndo;
        this.canUndo = canUndo;
        firePropertyChange("canUndo", oldValue, this.canUndo);
    }

    public boolean isCanRedo() {
        return canRedo;
    }

    public void setCanRedo(boolean canRedo) {
        boolean oldValue = this.canRedo;
        this.canRedo = canRedo;
        firePropertyChange("canRedo", oldValue, this.canRedo);
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean edit) {
        boolean oldValue = this.editMode;
        this.editMode = edit;
        textArea.setEditable(editMode);
        firePropertyChange("editMode", oldValue, this.editMode);
    }

    public boolean isExecuteMode() {
        return executeMode;
    }

    public void setExecuteMode(boolean executeMode) {
        boolean oldValue = this.executeMode;
        this.executeMode = executeMode;
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setEnabledAt(1, executeMode);
        firePropertyChange("executeMode", oldValue, this.executeMode);
    }

    public boolean isDebugButtonsEnabled() {
        return debugButtonsEnabled;
    }

    public void setDebugButtonsEnabled(boolean enabled) {
        boolean oldDebugButtonsEnabled = this.debugButtonsEnabled;
        this.debugButtonsEnabled = enabled;
        firePropertyChange("debugButtonsEnabled",
                oldDebugButtonsEnabled, enabled);
    }
    //</editor-fold>

    @Action(enabledProperty = "editMode")
    public void newfile() {
        if (isModified() && askToSave() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        setFile(new File("Безымянный.sl"));
        textArea.setText(getResourceMap().getString("newfile.text"));
        outputTextArea.setText("");
        setModified(false);
        df_file = true;
        breakpoints.clear();
        undoManager.die();
        updateUndoState();
    }

    @Action(enabledProperty = "editMode")
    public Task open() {
        if (isModified() && askToSave() == JOptionPane.CANCEL_OPTION) {
            return null;
        }
        JFileChooser fc = createFileChooser("openFileChooser");
        int option = fc.showOpenDialog(getFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new LoadFileTask(fc.getSelectedFile());
        }
        return task;
    }

    @Action(enabledProperty = "modified")
    public Task save() {
        return df_file ? saveAs() : new SaveFileTask(getFile());
    }

    @Action
    public Task saveAs() {
        JFileChooser fc = createFileChooser("saveAsFileChooser");
        int option = fc.showSaveDialog(getFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new SaveFileTask(fc.getSelectedFile());
        }
        return task;
    }

    @Action(enabledProperty = "canUndo")
    public void undo() {
        try {
            undoManager.undo();
        } catch (CannotUndoException ex) {
        }
        updateUndoState();
    }

    @Action(enabledProperty = "canRedo")
    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotRedoException ex) {
        }
        updateUndoState();
    }

    @Action(enabledProperty = "editMode")
    public Task run() {
        setEditMode(false);
        setExecuteMode(true);
        outputTextArea.setText("");
        return (executeTask = new RunTask());
    }

    @Action(enabledProperty = "editMode")
    public Task debug() {
        setEditMode(false);
        setExecuteMode(true);
        outputTextArea.setText("");
        return (executeTask = new DebugTask());
    }

    @Action(enabledProperty = "executeMode")
    public void stop() {
        executeTask.stopExecution();
        synchronized (executeTask) {
            if (isDebugButtonsEnabled()) {
                executeTask.notify();
            }
        }
    }

    @Action(enabledProperty = "debugButtonsEnabled")
    public void nextBreakpoint() {
        breakAction(Mode.NEXT_BREAKPOINT);
    }

    @Action(enabledProperty = "debugButtonsEnabled")
    public void stepInto() {
        breakAction(Mode.STEP_INTO);
    }

    @Action(enabledProperty = "debugButtonsEnabled")
    public void stepOver() {
        breakAction(Mode.STEP_OVER);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = DocumentEditorApp.getApplication().getMainFrame();
            aboutBox = new DocumentEditorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        DocumentEditorApp.getApplication().show(aboutBox);
    }

    private class LoadFileTask extends DocumentEditorApp.LoadTextFileTask {

        LoadFileTask(File file) {
            super(DocumentEditorView.this.getApplication(), file);
        }

        @Override
        protected void succeeded(String fileContents) {
            setFile(getFile());
            textArea.setText(fileContents);
            outputTextArea.setText("");
            setModified(false);
            df_file = false;
            breakpoints.clear();
            undoManager.die();
            updateUndoState();
        }

        @Override
        protected void failed(Throwable t) {
            logger.log(Level.WARNING, "couldn't load " + getFile(), t);
            String msg = getResourceMap().getString("loadFailedMessage", getFile());
            String title = getResourceMap().getString("loadFailedTitle");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(getFrame(), msg, title, type);
        }
    }

    private class SaveFileTask extends DocumentEditorApp.SaveTextFileTask {

        SaveFileTask(File file) {
            super(DocumentEditorView.this.getApplication(), file, textArea.getText());
        }

        @Override
        protected void succeeded(Void ignored) {
            setFile(getFile());
            setModified(false);
            df_file = false;
        }

        @Override
        protected void failed(Throwable t) {
            logger.log(Level.WARNING, "couldn't save " + getFile(), t);
            String msg = getResourceMap().getString("saveFailedMessage", getFile());
            String title = getResourceMap().getString("saveFailedTitle");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(getFrame(), msg, title, type);
        }
    }

    private class RunTask extends sl.ide.tasks.RunTask {

        public RunTask() {
            super(DocumentEditorView.this.getApplication(), textArea.getText(),
                    reader, writer);
        }

        @Override
        protected void succeeded(Void ignored) {
            setEditMode(true);
            setExecuteMode(false);
        }

        @Override
        protected void failed(Throwable t) {
            formatException(t);
            setEditMode(true);
            setExecuteMode(false);
        }
    }

    private class DebugTask extends sl.ide.tasks.DebugTask {

        public DebugTask() {
            super(DocumentEditorView.this.getApplication(), textArea.getText(),
                    reader, writer, breakpoints);
        }

        @Override
        protected void beforeWait() {
            setDebugButtonsEnabled(true);
            lineSelector.selectLine(getCurrentRow());
            try {
                textArea.setCaretPosition(textArea.getLineStartOffset(getCurrentRow()));
            } catch (BadLocationException ex) {
            }
            watchesManager.setInfo(getProgramsStack(), getDebugInformation());
        }

        private void commonResult() {
            setEditMode(true);
            setExecuteMode(false);
            setDebugButtonsEnabled(false);
            lineSelector.unselectLine();
        }

        @Override
        protected void succeeded(Void ignored) {
            commonResult();
        }

        @Override
        protected void failed(Throwable t) {
            formatException(t);
            commonResult();
        }
    }

    private class ConfirmExit implements Application.ExitListener {

        public boolean canExit(EventObject e) {
            return !isModified() || askToSave() != JOptionPane.CANCEL_OPTION;
        }

        public void willExit(EventObject e) {
        }
    }

    private JFileChooser createFileChooser(String name) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(getResourceMap().getString(name + ".dialogTitle"));
        String textFilesDesc = getResourceMap().getString("fileExtensionDescription");
        fc.setFileFilter(new FileNameExtensionFilter(textFilesDesc, "sl"));
        return fc;
    }

    private int askToSave() {
        String confirmSaveText = getResourceMap().getString("confirmSaveText", getFile());
        String confirmSaveTitle = getResourceMap().getString("confirmSaveTitle");
        int option = JOptionPane.showConfirmDialog(getFrame(), confirmSaveText, confirmSaveTitle, JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Task saveTask = save();
            saveTask.execute();
        }
        return option;
    }

    private void formatException(Throwable t) {
        JOptionPane.showMessageDialog(getFrame(), t.getMessage(),
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        if (t instanceof AbstractParseException) {
            AbstractParseException ape = (AbstractParseException) t;
            textArea.setCaretPosition(ape.getPosition().getBeginPosition());
        }
    }

    private void breakAction(Mode mode) {
        setDebugButtonsEnabled(false);
        final DebugTask localDebugTask = (DebugTask) this.executeTask;
        //cast is correct because of debugButtonsEnabled property
        localDebugTask.setMode(mode);
        synchronized (localDebugTask) {
            localDebugTask.notify();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel mainPanel = new javax.swing.JPanel();
        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane();
        editorTextAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        tabbedPane = new javax.swing.JTabbedPane();
        javax.swing.JScrollPane outputScrollPane = new javax.swing.JScrollPane();
        outputTextArea = new sl.ide.components.OutputTextArea();
        watchesSplitPane = new javax.swing.JSplitPane();
        javax.swing.JScrollPane watchesVariablesScrollPane = new javax.swing.JScrollPane();
        watchesVariablesTable = new javax.swing.JTable();
        javax.swing.JScrollPane watchesStackScrollPane = new javax.swing.JScrollPane();
        watchesStackTable = new javax.swing.JTable();
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem newfileMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem openMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem saveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem saveAsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator fileMunuSeparator = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu editMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem undoMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem redoMenuItem = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator editMenuSeparator = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem cutMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem copyMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem pasteMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem deleteMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu runMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem runMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem stopMenuItem = new javax.swing.JMenuItem();
        javax.swing.JPopupMenu.Separator runMenuSeparator = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem debugMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem nextBreakpointMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem stepIntoMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem stepOverMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        toolBar = new javax.swing.JToolBar();
        newfileToolBarButton = new javax.swing.JButton();
        openToolBarButton = new javax.swing.JButton();
        saveToolBarButton = new javax.swing.JButton();
        javax.swing.JToolBar.Separator toolbarFirstSeparator = new javax.swing.JToolBar.Separator();
        undoToolBarButton = new javax.swing.JButton();
        redoToolBarButton = new javax.swing.JButton();
        javax.swing.JToolBar.Separator toolbarSecondSeparator = new javax.swing.JToolBar.Separator();
        cutToolBarButton = new javax.swing.JButton();
        copyToolBarButton = new javax.swing.JButton();
        pasteToolBarButton = new javax.swing.JButton();
        deleteToolBarButton = new javax.swing.JButton();
        javax.swing.JToolBar.Separator toolbarThirdSeparator = new javax.swing.JToolBar.Separator();
        runToolBarButton = new javax.swing.JButton();
        javax.swing.JToolBar.Separator toolbarFourthSeparator = new javax.swing.JToolBar.Separator();
        debugToolBarButton = new javax.swing.JButton();
        nextBreakpointToolBarButton = new javax.swing.JButton();
        javax.swing.JButton stepIntoToolBarButton = new javax.swing.JButton();
        javax.swing.JButton stepOverToolBarButton = new javax.swing.JButton();
        javax.swing.JToolBar.Separator toolbarFifthSeparator = new javax.swing.JToolBar.Separator();
        stopToolBarButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(1.0);
        splitPane.setName("splitPane"); // NOI18N

        editorTextAreaScrollPane.setName("editorTextAreaScrollPane"); // NOI18N

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setTabSize(4);
        textArea.setName("textArea"); // NOI18N
        editorTextAreaScrollPane.setViewportView(textArea);

        splitPane.setLeftComponent(editorTextAreaScrollPane);

        tabbedPane.setName("tabbedPane"); // NOI18N

        outputScrollPane.setBorder(null);
        outputScrollPane.setName("outputScrollPane"); // NOI18N

        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);
        outputTextArea.setTabSize(4);
        outputTextArea.setName("outputTextArea"); // NOI18N
        outputScrollPane.setViewportView(outputTextArea);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sl.ide.DocumentEditorApp.class).getContext().getResourceMap(DocumentEditorView.class);
        tabbedPane.addTab(resourceMap.getString("outputScrollPane.TabConstraints.tabTitle"), outputScrollPane); // NOI18N

        watchesSplitPane.setResizeWeight(1.0);
        watchesSplitPane.setName("watchesSplitPane"); // NOI18N

        watchesVariablesScrollPane.setName("watchesVariablesScrollPane"); // NOI18N

        watchesVariablesTable.setModel(new WatchesVariablesTableModel(new Object[][]{}));
        watchesVariablesTable.setName("watchesVariablesTable"); // NOI18N
        watchesVariablesScrollPane.setViewportView(watchesVariablesTable);
        watchesVariablesTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("watchesVariablesTable.columnModel.title0")); // NOI18N
        watchesVariablesTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("watchesVariablesTable.columnModel.title1")); // NOI18N
        watchesVariablesTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("watchesVariablesTable.columnModel.title2")); // NOI18N

        watchesSplitPane.setLeftComponent(watchesVariablesScrollPane);

        watchesStackScrollPane.setName("watchesStackScrollPane"); // NOI18N

        watchesStackTable.setModel(new sl.ide.WatchesCallstackTableModel(new Object[][]{}));
        watchesStackTable.setName("watchesStackTable"); // NOI18N
        watchesStackScrollPane.setViewportView(watchesStackTable);

        watchesSplitPane.setRightComponent(watchesStackScrollPane);

        tabbedPane.addTab(resourceMap.getString("watchesSplitPane.TabConstraints.tabTitle"), watchesSplitPane); // NOI18N

        splitPane.setBottomComponent(tabbedPane);

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );
        resourceMap.injectComponents(mainPanel);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(sl.ide.DocumentEditorApp.class).getContext().getActionMap(DocumentEditorView.class, this);
        newfileMenuItem.setAction(actionMap.get("newfile")); // NOI18N
        newfileMenuItem.setName("newfileMenuItem"); // NOI18N
        fileMenu.add(newfileMenuItem);

        openMenuItem.setAction(actionMap.get("open")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        fileMenu.add(openMenuItem);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        fileMenu.add(saveAsMenuItem);

        fileMunuSeparator.setName("fileMunuSeparator"); // NOI18N
        fileMenu.add(fileMunuSeparator);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setName("editMenu"); // NOI18N

        undoMenuItem.setAction(actionMap.get("undo")); // NOI18N
        undoMenuItem.setName("undoMenuItem"); // NOI18N
        editMenu.add(undoMenuItem);

        redoMenuItem.setAction(actionMap.get("redo")); // NOI18N
        redoMenuItem.setName("redoMenuItem"); // NOI18N
        editMenu.add(redoMenuItem);

        editMenuSeparator.setName("editMenuSeparator"); // NOI18N
        editMenu.add(editMenuSeparator);

        cutMenuItem.setAction(actionMap.get("cut"));
        cutMenuItem.setName("cutMenuItem"); // NOI18N
        editMenu.add(cutMenuItem);

        copyMenuItem.setAction(actionMap.get("copy"));
        copyMenuItem.setName("copyMenuItem"); // NOI18N
        editMenu.add(copyMenuItem);

        pasteMenuItem.setAction(actionMap.get("paste"));
        pasteMenuItem.setName("pasteMenuItem"); // NOI18N
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setAction(actionMap.get("delete"));
        deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem.setName("deleteMenuItem"); // NOI18N
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        runMenu.setName("runMenu"); // NOI18N

        runMenuItem.setAction(actionMap.get("run")); // NOI18N
        runMenuItem.setName("runMenuItem"); // NOI18N
        runMenu.add(runMenuItem);

        stopMenuItem.setAction(actionMap.get("stop")); // NOI18N
        stopMenuItem.setName("stopMenuItem"); // NOI18N
        runMenu.add(stopMenuItem);

        runMenuSeparator.setName("runMenuSeparator"); // NOI18N
        runMenu.add(runMenuSeparator);

        debugMenuItem.setAction(actionMap.get("debug")); // NOI18N
        debugMenuItem.setName("debugMenuItem"); // NOI18N
        runMenu.add(debugMenuItem);

        nextBreakpointMenuItem.setAction(actionMap.get("nextBreakpoint")); // NOI18N
        nextBreakpointMenuItem.setName("nextBreakpointMenuItem"); // NOI18N
        runMenu.add(nextBreakpointMenuItem);

        stepIntoMenuItem.setAction(actionMap.get("stepInto")); // NOI18N
        stepIntoMenuItem.setName("stepIntoMenuItem"); // NOI18N
        runMenu.add(stepIntoMenuItem);

        stepOverMenuItem.setAction(actionMap.get("stepOver")); // NOI18N
        stepOverMenuItem.setName("stepOverMenuItem"); // NOI18N
        runMenu.add(stepOverMenuItem);

        menuBar.add(runMenu);

        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);
        resourceMap.injectComponents(menuBar);

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        newfileToolBarButton.setAction(actionMap.get("newfile")); // NOI18N
        newfileToolBarButton.setFocusable(false);
        newfileToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newfileToolBarButton.setName("newfileToolBarButton"); // NOI18N
        newfileToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(newfileToolBarButton);

        openToolBarButton.setAction(actionMap.get("open")); // NOI18N
        openToolBarButton.setFocusable(false);
        openToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openToolBarButton.setName("openToolBarButton"); // NOI18N
        openToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(openToolBarButton);

        saveToolBarButton.setAction(actionMap.get("save")); // NOI18N
        saveToolBarButton.setFocusable(false);
        saveToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveToolBarButton.setName("saveToolBarButton"); // NOI18N
        saveToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(saveToolBarButton);

        toolbarFirstSeparator.setName("toolbarFirstSeparator"); // NOI18N
        toolBar.add(toolbarFirstSeparator);

        undoToolBarButton.setAction(actionMap.get("undo")); // NOI18N
        undoToolBarButton.setFocusable(false);
        undoToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        undoToolBarButton.setName("undoToolBarButton"); // NOI18N
        undoToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(undoToolBarButton);

        redoToolBarButton.setAction(actionMap.get("redo")); // NOI18N
        redoToolBarButton.setFocusable(false);
        redoToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        redoToolBarButton.setName("redoToolBarButton"); // NOI18N
        redoToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(redoToolBarButton);

        toolbarSecondSeparator.setName("toolbarSecondSeparator"); // NOI18N
        toolBar.add(toolbarSecondSeparator);

        cutToolBarButton.setAction(actionMap.get("cut"));
        cutToolBarButton.setFocusable(false);
        cutToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cutToolBarButton.setName("cutToolBarButton"); // NOI18N
        cutToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(cutToolBarButton);

        copyToolBarButton.setAction(actionMap.get("copy"));
        copyToolBarButton.setFocusable(false);
        copyToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        copyToolBarButton.setName("copyToolBarButton"); // NOI18N
        copyToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(copyToolBarButton);

        pasteToolBarButton.setAction(actionMap.get("paste"));
        pasteToolBarButton.setFocusable(false);
        pasteToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pasteToolBarButton.setName("pasteToolBarButton"); // NOI18N
        pasteToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(pasteToolBarButton);

        deleteToolBarButton.setAction(actionMap.get("delete"));
        deleteToolBarButton.setFocusable(false);
        deleteToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteToolBarButton.setName("deleteToolBarButton"); // NOI18N
        deleteToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(deleteToolBarButton);

        toolbarThirdSeparator.setName("toolbarThirdSeparator"); // NOI18N
        toolBar.add(toolbarThirdSeparator);

        runToolBarButton.setAction(actionMap.get("run")); // NOI18N
        runToolBarButton.setFocusable(false);
        runToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runToolBarButton.setName("runToolBarButton"); // NOI18N
        runToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(runToolBarButton);

        toolbarFourthSeparator.setName("toolbarFourthSeparator"); // NOI18N
        toolBar.add(toolbarFourthSeparator);

        debugToolBarButton.setAction(actionMap.get("debug")); // NOI18N
        debugToolBarButton.setFocusable(false);
        debugToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        debugToolBarButton.setName("debugToolBarButton"); // NOI18N
        debugToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(debugToolBarButton);

        nextBreakpointToolBarButton.setAction(actionMap.get("nextBreakpoint")); // NOI18N
        nextBreakpointToolBarButton.setFocusable(false);
        nextBreakpointToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nextBreakpointToolBarButton.setName("nextBreakpointToolBarButton"); // NOI18N
        nextBreakpointToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(nextBreakpointToolBarButton);

        stepIntoToolBarButton.setAction(actionMap.get("stepInto")); // NOI18N
        stepIntoToolBarButton.setFocusable(false);
        stepIntoToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stepIntoToolBarButton.setName("stepIntoToolBarButton"); // NOI18N
        stepIntoToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(stepIntoToolBarButton);

        stepOverToolBarButton.setAction(actionMap.get("stepOver")); // NOI18N
        stepOverToolBarButton.setFocusable(false);
        stepOverToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stepOverToolBarButton.setName("stepOverToolBarButton"); // NOI18N
        stepOverToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(stepOverToolBarButton);

        toolbarFifthSeparator.setName("toolbarFifthSeparator"); // NOI18N
        toolBar.add(toolbarFifthSeparator);

        stopToolBarButton.setAction(actionMap.get("stop")); // NOI18N
        stopToolBarButton.setFocusable(false);
        stopToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopToolBarButton.setName("stopToolBarButton"); // NOI18N
        stopToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(stopToolBarButton);
        resourceMap.injectComponents(toolBar);

        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 330, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );
        resourceMap.injectComponents(statusPanel);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyToolBarButton;
    private javax.swing.JButton cutToolBarButton;
    private javax.swing.JButton debugToolBarButton;
    private javax.swing.JButton deleteToolBarButton;
    private javax.swing.JScrollPane editorTextAreaScrollPane;
    private javax.swing.JButton newfileToolBarButton;
    private javax.swing.JButton nextBreakpointToolBarButton;
    private javax.swing.JButton openToolBarButton;
    private sl.ide.components.OutputTextArea outputTextArea;
    private javax.swing.JButton pasteToolBarButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton redoToolBarButton;
    private javax.swing.JButton runToolBarButton;
    private javax.swing.JButton saveToolBarButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopToolBarButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextArea textArea;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JButton undoToolBarButton;
    private javax.swing.JSplitPane watchesSplitPane;
    private javax.swing.JTable watchesStackTable;
    private javax.swing.JTable watchesVariablesTable;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private static final Logger logger = Logger.getLogger(DocumentEditorView.class.getName());
}
