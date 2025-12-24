package simplejavatexteditor;

import java.lang.reflect.Method;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

public class UI extends JFrame implements ActionListener {

    private final String[] dragDropExtensionFilter = {".txt", ".dat", ".log", ".xml", ".mf", ".html", ".java", ".c", ".cpp", ".py", ".css", ".js", ".json", ".sql", ".php"};
    private static long serialVersionUID = 1L;
    private final JTextPane textArea;
    private final JMenuBar menuBar;
    private final JComboBox<String> fontType;
    private final JComboBox<Integer> fontSize;
    private final JMenu menuFile, menuEdit, menuFind, menuAbout;
    private final JMenuItem newFile, openFile, saveFile, close, cut, copy, paste, clearFile, selectAll, quickFind,
            aboutMe, aboutSoftware, wordWrap;
    private final JToolBar mainToolbar;
    JButton newButton, openButton, saveButton, clearButton, quickButton, aboutMeButton, aboutButton, closeButton, boldButton;
    private final Action selectAllAction;

    private final ImageIcon boldIcon = new ImageIcon(UI.class.getResource("icons/bold.png"));
    private final ImageIcon newIcon = new ImageIcon(UI.class.getResource("icons/new.png"));
    private final ImageIcon openIcon = new ImageIcon(UI.class.getResource("icons/open.png"));
    private final ImageIcon saveIcon = new ImageIcon(UI.class.getResource("icons/save.png"));
    private final ImageIcon closeIcon = new ImageIcon(UI.class.getResource("icons/close.png"));
    private final ImageIcon clearIcon = new ImageIcon(UI.class.getResource("icons/clear.png"));
    private final ImageIcon cutIcon = new ImageIcon(UI.class.getResource("icons/cut.png"));
    private final ImageIcon copyIcon = new ImageIcon(UI.class.getResource("icons/copy.png"));
    private final ImageIcon pasteIcon = new ImageIcon(UI.class.getResource("icons/paste.png"));
    private final ImageIcon selectAllIcon = new ImageIcon(UI.class.getResource("icons/selectall.png"));
    private final ImageIcon wordwrapIcon = new ImageIcon(UI.class.getResource("icons/wordwrap.png"));
    private final ImageIcon searchIcon = new ImageIcon(UI.class.getResource("icons/search.png"));
    private final ImageIcon aboutMeIcon = new ImageIcon(UI.class.getResource("icons/about_me.png"));
    private final ImageIcon aboutIcon = new ImageIcon(UI.class.getResource("icons/about.png"));

    private SupportedKeywords kw = new SupportedKeywords();
    private HighlightText languageHighlighter = new HighlightText(new Color(255, 255, 150));
    AutoComplete autocomplete;
    private boolean hasListener = false;
    private boolean edit = false;
    private String currentFileType = "";

    private Timer highlightTimer;
    private SearchSidebar searchSidebar;

    public UI() {
        try {
            // Optional: try to set metal to allow better coloring
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        try {
            ImageIcon image = new ImageIcon(UI.class.getResource("icons/ste.png"));
            super.setIconImage(image.getImage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setSize(800, 500);
        setTitle("Untitled | " + SimpleJavaTextEditor.NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextPane();
        textArea.setFont(new Font("Arial", Font.PLAIN, 15));
        textArea.setBackground(new Color(20, 20, 30)); // Midnight background
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        
        DropTarget dropTarget = new DropTarget(textArea, dropTargetListener);

        highlightTimer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performHighlighting();
            }
        });
        highlightTimer.setRepeats(false);

        textArea.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                setTitle("Untitled | " + SimpleJavaTextEditor.NAME + "     [ Length: " + textArea.getText().length()
                        + "    Lines: " + (textArea.getText() + "|").split("\n").length
                        + "    Words: " + textArea.getText().trim().split("\\s+").length + " ]");
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                edit = true;
                if (!currentFileType.isEmpty()) {
                    highlightTimer.restart();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        // textArea.setWrapStyleWord(true); // JTextPane doesn't have this, does it by default in ScrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().setLayout(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        getContentPane().add(panel, BorderLayout.CENTER);
        
        searchSidebar = new SearchSidebar(textArea);
        searchSidebar.setBackground(new Color(25, 25, 45));
        searchSidebar.setVisible(false);
        getContentPane().add(searchSidebar, BorderLayout.EAST);

        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuFind = new JMenu("Search");
        menuAbout = new JMenu("About"); 
        
        MidnightTheme.styleMenu(menuFile);
        MidnightTheme.styleMenu(menuEdit);
        MidnightTheme.styleMenu(menuFind);
        MidnightTheme.styleMenu(menuAbout);

        newFile = new JMenuItem("New", newIcon);
        openFile = new JMenuItem("Open", openIcon);
        saveFile = new JMenuItem("Save", saveIcon);
        close = new JMenuItem("Quit", closeIcon);
        clearFile = new JMenuItem("Clear", clearIcon);
        quickFind = new JMenuItem("Quick", searchIcon);
        aboutMe = new JMenuItem("About Me", aboutMeIcon);
        aboutSoftware = new JMenuItem("About Software", aboutIcon);

        menuBar = new JMenuBar();
        MidnightTheme.styleMenuBar(menuBar);
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuFind);

        menuBar.add(menuAbout);

        this.setJMenuBar(menuBar);

        selectAllAction = new SelectAllAction("Select All", clearIcon, "Select all text", new Integer(KeyEvent.VK_A),
                textArea);

        this.setJMenuBar(menuBar);

        newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)); // Set a keyboard shortcut
        menuFile.add(newFile); // Adding the file menu

        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        menuFile.add(openFile);
        


        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(saveFile);

        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        menuFile.add(close);

        selectAll = new JMenuItem(selectAllAction);
        selectAll.setText("Select All");
        selectAll.setIcon(selectAllIcon);
        selectAll.setToolTipText("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        menuEdit.add(selectAll);

        clearFile.addActionListener(this);
        clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        menuEdit.add(clearFile);

        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        cut.setIcon(cutIcon);
        cut.setToolTipText("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        menuEdit.add(cut);

        wordWrap = new JMenuItem();
        wordWrap.setText("Word Wrap");
        wordWrap.setIcon(wordwrapIcon);
        wordWrap.setToolTipText("Word Wrap");

        wordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        menuEdit.add(wordWrap);

        wordWrap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // textArea.setLineWrap(!textArea.getLineWrap()); // Not supported easily in JTextPane
            }
        });

        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        copy.setIcon(copyIcon);
        copy.setToolTipText("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        menuEdit.add(copy);

        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        paste.setIcon(pasteIcon);
        paste.setToolTipText("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        menuEdit.add(paste);

        quickFind.addActionListener(this);
        quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        menuFind.add(quickFind);

        aboutMe.addActionListener(this);
        aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuAbout.add(aboutMe);

        aboutSoftware.addActionListener(this);
        aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        menuAbout.add(aboutSoftware);

        // Apply styles after all items are created
        MidnightTheme.styleMenuItem(newFile);
        MidnightTheme.styleMenuItem(openFile);
        MidnightTheme.styleMenuItem(saveFile);
        MidnightTheme.styleMenuItem(close);
        MidnightTheme.styleMenuItem(selectAll);
        MidnightTheme.styleMenuItem(clearFile);
        MidnightTheme.styleMenuItem(cut);
        MidnightTheme.styleMenuItem(copy);
        MidnightTheme.styleMenuItem(paste);
        MidnightTheme.styleMenuItem(quickFind);
        MidnightTheme.styleMenuItem(aboutMe);
        MidnightTheme.styleMenuItem(aboutSoftware);
        MidnightTheme.styleMenuItem(wordWrap);

        mainToolbar = new JToolBar();
        mainToolbar.setBackground(new Color(25, 25, 45));
        mainToolbar.setFloatable(false);
        this.add(mainToolbar, BorderLayout.NORTH);

        newButton = new JButton("New", newIcon);
        newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setToolTipText("New");
        newButton.addActionListener(this);
        mainToolbar.add(newButton);
        mainToolbar.addSeparator();

        openButton = new JButton("Open", openIcon);
        openButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        openButton.setHorizontalTextPosition(SwingConstants.CENTER);
        openButton.setToolTipText("Open");
        openButton.addActionListener(this);
        mainToolbar.add(openButton);
        mainToolbar.addSeparator();

        saveButton = new JButton("Save", saveIcon);
        saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(this);
        mainToolbar.add(saveButton);
        mainToolbar.addSeparator();

        clearButton = new JButton("Clear", clearIcon);
        clearButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        clearButton.setHorizontalTextPosition(SwingConstants.CENTER);
        clearButton.setToolTipText("Clear All");
        clearButton.addActionListener(this);
        mainToolbar.add(clearButton);
        mainToolbar.addSeparator();

        quickButton = new JButton("Search", searchIcon);
        quickButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        quickButton.setHorizontalTextPosition(SwingConstants.CENTER);
        quickButton.setToolTipText("Quick Search");
        quickButton.addActionListener(this);
        mainToolbar.add(quickButton);
        mainToolbar.addSeparator();

        aboutMeButton = new JButton("About Me", aboutMeIcon);
        aboutMeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        aboutMeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        aboutMeButton.setToolTipText("About Me");
        aboutMeButton.addActionListener(this);
        mainToolbar.add(aboutMeButton);
        mainToolbar.addSeparator();

        aboutButton = new JButton("About", aboutIcon);
        aboutButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        aboutButton.setHorizontalTextPosition(SwingConstants.CENTER);
        aboutButton.setToolTipText("About NotePad PH");
        aboutButton.addActionListener(this);
        mainToolbar.add(aboutButton);
        mainToolbar.addSeparator();

        closeButton = new JButton("Quit", closeIcon);
        closeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        closeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        closeButton.setToolTipText("Quit");
        closeButton.addActionListener(this);
        mainToolbar.add(closeButton);
        mainToolbar.addSeparator();

        boldButton = new JButton("Bold", boldIcon);
        boldButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        boldButton.setHorizontalTextPosition(SwingConstants.CENTER);
        boldButton.setToolTipText("Bold");
        boldButton.addActionListener(this);
        mainToolbar.add(boldButton);
        mainToolbar.addSeparator();

        fontType = new JComboBox<String>();

        String[] standardFonts = {
            "Arial",
            "Calibri",
            "Times New Roman",
            "Georgia",
            "Verdana",
            "Helvetica",
            "Tahoma",
            "Trebuchet MS",
            "Comic Sans MS",
            "Courier New",
            "Impact",
            "Palatino",
            "Garamond"
        };

        for (String font : standardFonts) {

            fontType.addItem(font);
        }
        fontType.setSelectedItem("Arial"); // Set Arial as default

        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        mainToolbar.add(fontType);
        mainToolbar.addSeparator();

        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                String p = fontType.getSelectedItem().toString();

                int s = textArea.getFont().getSize();
                textArea.setFont(new Font(p, Font.PLAIN, s));
            }
        });

        fontSize = new JComboBox<Integer>();

        for (int i = 15; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setSelectedItem(15); // Set 15 as default
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        mainToolbar.add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = textArea.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                textArea.setFont(font1);
            }
        });

        MidnightTheme.styleButton(newButton);
        MidnightTheme.styleButton(openButton);
        MidnightTheme.styleButton(saveButton);
        MidnightTheme.styleButton(clearButton);
        MidnightTheme.styleButton(quickButton);
        MidnightTheme.styleButton(aboutMeButton);
        MidnightTheme.styleButton(aboutButton);
        MidnightTheme.styleButton(closeButton);
        MidnightTheme.styleButton(boldButton);
        
        MidnightTheme.styleComboBox(fontType);
        MidnightTheme.styleComboBox(fontSize);

    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                System.exit(99);
            }
        }
    }

    private void performHighlighting() {
        if (!currentFileType.isEmpty()) {
            String[] keywords = kw.getKeywordsFor(currentFileType);
            languageHighlighter.highLight(textArea, keywords);
        }
    }

    protected JTextPane getEditor() {
        return textArea;
    }

    public void enableAutoComplete(File file) {
        if (hasListener) {
            textArea.getDocument().removeDocumentListener(autocomplete);
            hasListener = false;
        }

        String[] list = kw.getSupportedLanguages();
        currentFileType = ""; 

        // 1. Determine File Type
        for (String lang : list) {
            if (file.getName().endsWith(lang)) {
                currentFileType = lang;
                break;
            }
        }
        
        // 2. Setup AutoComplete and Highlighting if type is supported
        if (!currentFileType.isEmpty()) {
            String[] keywords = kw.getKeywordsFor(currentFileType);
            ArrayList<String> arrayList = kw.setKeywords(keywords);
            
            autocomplete = new AutoComplete(this, arrayList);
            textArea.getDocument().addDocumentListener(autocomplete);
            hasListener = true;
            
            languageHighlighter.highLight(textArea, keywords);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == close || e.getSource() == closeButton) {
            if (edit) {
                Object[] options = {"Save and exit", "No Save and exit", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save and exit
                    saveFile();
                    this.dispose();// dispose all resources and close the application
                } else if (n == 1) {// no save and exit
                    this.dispose();// dispose all resources and close the application
                }
            } else {
                this.dispose();// dispose all resources and close the application
            }
        } // If the source was the "new" file option
        else if (e.getSource() == newFile || e.getSource() == newButton) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Return"};
                int n = JOptionPane.showOptionDialog(this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    FEdit.clear(textArea);
                }
            } else {
                FEdit.clear(textArea);
            }

        } // If the source was the "open" option
        else if (e.getSource() == openFile || e.getSource() == openButton) {
            JFileChooser open = new JFileChooser();
            

            open.setAcceptAllFileFilterUsed(true);
            javax.swing.filechooser.FileNameExtensionFilter allSupportedFilter = 
                new javax.swing.filechooser.FileNameExtensionFilter("All Supported Code Files", 
                    "txt", "java", "c", "cpp", "py", "html", "css", "js", "xml", "json", "sql", "php");
            
            open.addChoosableFileFilter(allSupportedFilter);
            open.setFileFilter(allSupportedFilter); 

            
            int option = open.showOpenDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = open.getSelectedFile();
                    System.out.println("Opening file: " + selectedFile.getAbsolutePath());
                    
                    textArea.setText("");
                    
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    
                    textArea.setText(content.toString());
                    System.out.println("File loaded successfully. Content length: " + content.length());
                    
                    setTitle(selectedFile.getName() + " | " + SimpleJavaTextEditor.NAME);
                    edit = false;
                    enableAutoComplete(selectedFile);
                } catch (Exception ex) {
                    System.err.println("Error opening file: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "Cannot open file: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }

        } // If the source of the event was the "save" option
        else if (e.getSource() == saveFile || e.getSource() == saveButton) {
            saveFile();
        }// If the source of the event was the "Bold" button
        else if (e.getSource() == boldButton) {
            if (textArea.getFont().getStyle() == Font.BOLD) {
                textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            } else {
                textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
            }
        }

        if (e.getSource() == clearFile || e.getSource() == clearButton) {

            Object[] options = {"Yes", "No"};
            int n = JOptionPane.showOptionDialog(this, "Are you sure to clear the text Area ?", "Question",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            if (n == 0) {// clear
                FEdit.clear(textArea);
            }
        }


        if (e.getSource() == quickFind || e.getSource() == quickButton) {
            searchSidebar.setVisible(true);
            this.revalidate();
        } // About Me
        else if (e.getSource() == aboutMe || e.getSource() == aboutMeButton) {
            new About(this).me();
        } // About Software
        else if (e.getSource() == aboutSoftware || e.getSource() == aboutButton) {
            new About(this).software();
        }
    }

    class SelectAllAction extends AbstractAction {

        /**
         * Used for Select All function
         */
        private static final long serialVersionUID = 1L;
        
        public SelectAllAction(String text, ImageIcon icon, String desc, Integer mnemonic, final JTextComponent textArea) {
            super(text, icon);
            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }

    private void saveFile() {

        JFileChooser fileChoose = new JFileChooser();
        

        javax.swing.filechooser.FileNameExtensionFilter txtFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Text Files (*.txt)", "txt");
        javax.swing.filechooser.FileNameExtensionFilter javaFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Java Files (*.java)", "java");
        javax.swing.filechooser.FileNameExtensionFilter cFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("C Files (*.c)", "c");
        javax.swing.filechooser.FileNameExtensionFilter cppFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("C++ Files (*.cpp)", "cpp");
        javax.swing.filechooser.FileNameExtensionFilter pyFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Python Files (*.py)", "py");
        javax.swing.filechooser.FileNameExtensionFilter docFilter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Document Files (*.doc, *.docx)", "doc", "docx");
        
        fileChoose.addChoosableFileFilter(txtFilter);
        fileChoose.addChoosableFileFilter(javaFilter);
        fileChoose.addChoosableFileFilter(cFilter);
        fileChoose.addChoosableFileFilter(cppFilter);
        fileChoose.addChoosableFileFilter(pyFilter);
        fileChoose.addChoosableFileFilter(docFilter);
        fileChoose.setFileFilter(txtFilter); // Set .txt as default
        

        int option = fileChoose.showSaveDialog(this);

        /*
             * ShowSaveDialog instead of showOpenDialog if the user clicked OK
             * (and not cancel)
         */
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChoose.getSelectedFile();
                setTitle(selectedFile.getName() + " | " + SimpleJavaTextEditor.NAME);

                BufferedWriter out = new BufferedWriter(new FileWriter(selectedFile.getPath()));
                out.write(textArea.getText());
                out.close();

                edit = false;
                enableAutoComplete(selectedFile);
            } catch (Exception ex) { // again, catch any exceptions and...

                System.err.println(ex.getMessage());
            }
        }
    }
    DropTargetListener dropTargetListener = new DropTargetListener() {

        @Override
        public void dragEnter(DropTargetDragEvent e) {
        }

        @Override
        public void dragExit(DropTargetEvent e) {
        }

        @Override
        public void dragOver(DropTargetDragEvent e) {
        }

        @Override
        public void drop(DropTargetDropEvent e) {
            if (edit) {
                Object[] options = {"Save", "No Save", "Return"};
                int n = JOptionPane.showOptionDialog(UI.this, "Do you want to save the file at first ?", "Question",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
                if (n == 0) {// save
                    UI.this.saveFile();
                    edit = false;
                } else if (n == 1) {
                    edit = false;
                    FEdit.clear(textArea);
                } else if (n == 2) {
                    e.rejectDrop();
                    return;
                }
            }
            try {
                Transferable tr = e.getTransferable();
                DataFlavor[] flavors = tr.getTransferDataFlavors();
                for (DataFlavor flavor : flavors) {
                    if (flavor.isFlavorJavaFileListType()) {
                        e.acceptDrop(e.getDropAction());

                        try {
                            String fileName = tr.getTransferData(flavor).toString().replace("[", "").replace("]", "");

                            boolean extensionAllowed = false;
                            for (String s : dragDropExtensionFilter) {
                                if (fileName.endsWith(s)) {
                                    extensionAllowed = true;
                                    break;
                                }
                            }
                            if (!extensionAllowed) {
                                JOptionPane.showMessageDialog(UI.this, "This file is not allowed for drag & drop", "Error", JOptionPane.ERROR_MESSAGE);

                            } else {
                                FileInputStream fis = new FileInputStream(new File(fileName));
                                byte[] ba = new byte[fis.available()];
                                fis.read(ba);
                                textArea.setText(new String(ba));
                                fis.close();
                                File droppedFile = new File(fileName);
                                setTitle(droppedFile.getName() + " | " + SimpleJavaTextEditor.NAME);
                                enableAutoComplete(droppedFile);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        e.dropComplete(true);
                        return;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            e.rejectDrop();
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent e) {
        }
    };

}
