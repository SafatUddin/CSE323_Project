package simplejavatexteditor;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.JTextComponent;

public class HighlightText {

    private SimpleAttributeSet keywordStyle;
    private SimpleAttributeSet stringStyle;
    private SimpleAttributeSet commentStyle;
    private SimpleAttributeSet typeStyle;
    private SimpleAttributeSet normalStyle;
    private SimpleAttributeSet includeStyle;
    private SimpleAttributeSet headerStyle; // For Markdown headers
    private SimpleAttributeSet boldStyle;   // For Markdown bold

    public HighlightText(Color color) {
        // We ignore the color arg now as we define specific syntax colors

        // Initialize Styles
        keywordStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(keywordStyle, new Color(100, 150, 255)); // Light Blue
        StyleConstants.setBold(keywordStyle, true);

        // Types (int, double, etc)
        typeStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(typeStyle, new Color(200, 100, 200)); // Light Purple
        StyleConstants.setBold(typeStyle, true);

        stringStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(stringStyle, new Color(100, 200, 100)); // Light Green

        commentStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(commentStyle, new Color(150, 150, 150)); // Light Gray
        StyleConstants.setItalic(commentStyle, true);

        includeStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(includeStyle, new Color(220, 150, 60)); // Orange-ish
        
        // Define Header Style (Cyan for Markdown headers)
        headerStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(headerStyle, new Color(0, 255, 255)); // Cyan
        StyleConstants.setBold(headerStyle, true);

        // Define Bold Style (for Markdown **text**)
        boldStyle = new SimpleAttributeSet();
        StyleConstants.setBold(boldStyle, true);
        
        normalStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(normalStyle, Color.WHITE);
        StyleConstants.setBold(normalStyle, false);
        StyleConstants.setItalic(normalStyle, false);
    }

    public void highLight(JTextComponent textComp, String[] keywords, String language) {
        if (!(textComp instanceof JTextPane)) {
             return;
        }
        
        JTextPane pane = (JTextPane) textComp;
        StyledDocument doc = pane.getStyledDocument();
        String text = pane.getText().replace("\r", ""); // Normalize line endings

        SwingUtilities.invokeLater(() -> {
             // Reset all
             doc.setCharacterAttributes(0, text.length(), normalStyle, true);
             
             // Define Regex Patterns based on language
             
             if (language.equals(".md")) {
                 // MARKDOWN SPECIFIC RULES
                 
                 // 1. Headers: # Header (Cyan)
                 // Match #, ##, etc. at start of line
                 String headerPattern = "^#+ .*";
                 applyRegexLineBased(doc, text, headerPattern, headerStyle);
                 
                 // 2. Bold: **text** 
                 String boldPattern = "\\*\\*.*?\\*\\*";
                 applyRegex(doc, text, boldPattern, boldStyle);
                 
                 // 3. List Bullets: - or * at start
                 String listPattern = "^\\s*[-*] ";
                 applyRegex(doc, text, listPattern, includeStyle); // Reuse Orange Style for bullets
                 
                 // 4. Code Blocks: ``` ... ``` (Use String style for now - Green)
                 String codeBlockPattern = "(?s)```.*?```";
                 applyRegex(doc, text, codeBlockPattern, stringStyle);

             } else {
                 // GENERIC CODE RULES
                 
                 // 1. Comments: // ... or /* ... */ or # ... or <!-- ... --> or -- ...
                 // Note: Python uses #, but Markdown uses # for headers.
                 // We apply python comments only if typically safe or specifically handling it.
                 // For now, we exclude basic # comment if it looks like a header (space after), 
                 // but since we are in "else" block (not md), standard # is fine.
                 String commentPattern = "(?s)/\\*.*?\\*/|//.*|#.*|<!--.*?-->|--.*";
                 applyRegex(doc, text, commentPattern, commentStyle);
    
                 // 2. Strings: "..." or '...'
                 String stringPattern = "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'";
                 applyRegex(doc, text, stringPattern, stringStyle);
    
                 // 3. Preprocessor: #include ...
                 String preprocessorPattern = "^\\s*#.*";
                 applyRegexLineBased(doc, text, preprocessorPattern, includeStyle);
    
                 // 4. Keywords
                 if (keywords.length > 0) {
                     StringBuilder sb = new StringBuilder();
                     sb.append("\\b(");
                     for (String keyword : keywords) {
                         sb.append(Pattern.quote(keyword)).append("|");
                     }
                     if (sb.length() > 0) sb.setLength(sb.length() - 1);
                     sb.append(")\\b");
                     
                     String keywordRegex = sb.toString();
                     applyRegex(doc, text, keywordRegex, keywordStyle);
                 }
             }
        });
    }

    // Overload for backward compatibility if needed, but we will update call site
    public void highLight(JTextComponent textComp, String[] keywords) {
        highLight(textComp, keywords, "");
    }

    private void applyRegex(StyledDocument doc, String text, String regex, SimpleAttributeSet style) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(m.start(), m.end() - m.start(), style, false);
        }
    }
    
    private void applyRegexLineBased(StyledDocument doc, String text, String regex, SimpleAttributeSet style) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            doc.setCharacterAttributes(m.start(), m.end() - m.start(), style, false);
        }
    }
    
    // Legacy method needed? 
    public void removeHighlights(JTextComponent textComp) {
         if (!(textComp instanceof JTextPane)) return;
         JTextPane pane = (JTextPane) textComp;
         StyledDocument doc = pane.getStyledDocument();
         doc.setCharacterAttributes(0, doc.getLength(), normalStyle, true);
    }
}
