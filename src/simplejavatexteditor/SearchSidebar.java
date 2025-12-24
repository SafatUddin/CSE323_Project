package simplejavatexteditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchSidebar extends JPanel {
    private JTextComponent textArea;
    private JTextField searchField;
    private JTextField replaceField;
    private JButton closeButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    
    // Custom painter to distinguish search highlights from syntax highlights
    class SearchHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public SearchHighlightPainter(Color color) {
            super(color);
        }
    }
    
    private SearchHighlightPainter painter = new SearchHighlightPainter(new Color(0, 255, 255, 100)); // Cyan with transparency

    public SearchSidebar(JTextComponent textArea) {
        this.textArea = textArea;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0)); // Sidebar width
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(30,30,30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- Header ---
        JLabel searchLabel = new JLabel("Search");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(searchLabel, gbc);
        
        closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(20, 20));
        closeButton.setFocusable(false);
        closeButton.setBackground(Color.DARK_GRAY);
        closeButton.setForeground(Color.WHITE);
        closeButton.setToolTipText("Close Search");
        closeButton.addActionListener(e -> {
            setVisible(false);
            clearHighlights();
            Container parent = getParent();
            if (parent != null) {
                parent.revalidate();
                parent.repaint();
            }
        });
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        topPanel.add(closeButton, gbc);
        
        // --- Search Field ---
        searchField = new JTextField();
        searchField.setBackground(Color.DARK_GRAY);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        topPanel.add(searchField, gbc);

        // --- Replace Header ---
        JLabel replaceLabel = new JLabel("Replace");
        replaceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        replaceLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 5, 5, 5); // Add some top space
        topPanel.add(replaceLabel, gbc);

        // --- Replace Field ---
        replaceField = new JTextField();
        replaceField.setBackground(Color.DARK_GRAY);
        replaceField.setForeground(Color.WHITE);
        replaceField.setCaretColor(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        topPanel.add(replaceField, gbc);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setBackground(new Color(30,30,30));
        replaceButton = new JButton("Replace");
        replaceAllButton = new JButton("All");
        
        replaceButton.setBackground(Color.DARK_GRAY);
        replaceButton.setForeground(Color.WHITE);
        replaceAllButton.setBackground(Color.DARK_GRAY);
        replaceAllButton.setForeground(Color.WHITE);
        
        replaceButton.addActionListener(e -> replaceOne());
        replaceAllButton.addActionListener(e -> replaceAll());
        
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        topPanel.add(buttonPanel, gbc);

        add(topPanel, BorderLayout.NORTH);
        
        // --- Instructions ---
        JTextArea helpText = new JTextArea("Type to search.\nMatches are highlighted.");
        helpText.setEditable(false);
        // helpText.setBackground(getBackground()); // Inherit from parent (which is dark now)
        helpText.setBackground(new Color(30,30,30));
        helpText.setFont(new Font("Arial", Font.PLAIN, 10));
        helpText.setForeground(Color.LIGHT_GRAY);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        add(helpText, BorderLayout.CENTER);

        // --- Listeners ---
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                highlightSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                highlightSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                highlightSearch();
            }
        });
    }

    private void clearHighlights() {
        Highlighter highlighter = textArea.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (Highlighter.Highlight h : highlights) {
            if (h.getPainter() instanceof SearchHighlightPainter) {
                highlighter.removeHighlight(h);
            }
        }
    }

    private void highlightSearch() {
        clearHighlights();
        String term = searchField.getText();
        if (term.isEmpty()) {
            return;
        }

        try {
            Highlighter highlighter = textArea.getHighlighter();
            String text = textArea.getText();
            
            // Case insensitive search
            Pattern pattern = Pattern.compile(Pattern.quote(term), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            
            while (matcher.find()) {
                highlighter.addHighlight(matcher.start(), matcher.end(), painter);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void replaceOne() {
        String term = searchField.getText();
        String replacement = replaceField.getText();
        if (term.isEmpty()) return;

        // Check if current selection matches term (case insensitive)
        String selection = textArea.getSelectedText();
        if (selection != null && selection.equalsIgnoreCase(term)) {
            // Replace current selection
            textArea.replaceSelection(replacement);
            // Move to next?
            findNext(term);
        } else {
            // Find next
            findNext(term);
        }
        highlightSearch(); // update highlights
    }

    private void findNext(String term) {
        String text = textArea.getText();
        int caretPos = textArea.getCaretPosition();
        
        Pattern pattern = Pattern.compile(Pattern.quote(term), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find(caretPos)) {
            textArea.setCaretPosition(matcher.start());
            textArea.moveCaretPosition(matcher.end());
            textArea.requestFocusInWindow();
        } else {
            // Wrap around
            if (matcher.find(0)) {
                textArea.setCaretPosition(matcher.start());
                textArea.moveCaretPosition(matcher.end());
                textArea.requestFocusInWindow();
            }
        }
    }

    private void replaceAll() {
        String term = searchField.getText();
        String replacement = replaceField.getText();
        if (term.isEmpty()) return;

        String text = textArea.getText();
        // Use regex for case insensitive replacement
        // Note: text field acts as literal, so we quote it for the pattern
        // Replacement string: if it contains $ or \, we should quote it too for appendReplacement? 
        // Or simple string replace? java String.replaceAll uses regex for replacement too ($1 etc). 
        // We probably want literal replacement.
        
        Pattern pattern = Pattern.compile(Pattern.quote(term), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        // This is safe for literal text replacement
        String result = matcher.replaceAll(Matcher.quoteReplacement(replacement));
        
        textArea.setText(result);
        
        // Re-highlight if any might remain (none should if replaced with something else, but if replaced with same...)
        highlightSearch();
    }
    
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
             SwingUtilities.invokeLater(() -> searchField.requestFocusInWindow());
             highlightSearch(); // Re-highlight if text exists
        } else {
             clearHighlights();
        }
    }
}
