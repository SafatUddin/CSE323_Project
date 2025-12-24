package simplejavatexteditor;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * LineNumberComponent displays line numbers in the row header of a JScrollPane
 * Automatically updates when text is added or removed
 */
public class LineNumberComponent extends JPanel implements PropertyChangeListener {
    
    private static final int MARGIN = 5;
    private static final Color LINE_NUMBER_COLOR = new Color(150, 150, 170);
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 45);
    private static final Color BORDER_COLOR = new Color(50, 50, 70);
    
    private JTextPane textPane;
    private FontMetrics fontMetrics;
    
    public LineNumberComponent(JTextPane textPane) {
        this.textPane = textPane;
        
        setBackground(BACKGROUND_COLOR);
        setForeground(LINE_NUMBER_COLOR);
        setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        // Listen for document changes
        textPane.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                repaint();
            }
        });
        
        // Listen for font changes
        textPane.addPropertyChangeListener("font", this);
        
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("font".equals(evt.getPropertyName())) {
            repaint();
        }
    }
    
    /**
     * Calculate the width needed to display line numbers
     */
    private int calculateWidth() {
        int lineCount = getLineCount();
        int digits = Math.max(String.valueOf(lineCount).length(), 2);
        
        if (fontMetrics == null) {
            fontMetrics = getFontMetrics(getFont());
        }
        
        return fontMetrics.stringWidth("0") * digits + MARGIN * 2;
    }
    
    /**
     * Get the total number of lines in the document
     */
    private int getLineCount() {
        try {
            Document doc = textPane.getDocument();
            Element root = doc.getDefaultRootElement();
            return root.getElementCount();
        } catch (Exception e) {
            return 1;
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(calculateWidth(), textPane.getHeight());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Get the clip bounds to optimize rendering
        Rectangle clip = g.getClipBounds();
        
        // Get font metrics
        fontMetrics = g2d.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int fontAscent = fontMetrics.getAscent();
        
        try {
            // Get the visible rectangle of the text pane
            Rectangle visibleRect = textPane.getVisibleRect();
            
            // Calculate the starting line
            int startOffset = textPane.viewToModel2D(new Point(0, visibleRect.y));
            int endOffset = textPane.viewToModel2D(new Point(0, visibleRect.y + visibleRect.height));
            
            Document doc = textPane.getDocument();
            Element root = doc.getDefaultRootElement();
            
            int startLine = root.getElementIndex(startOffset);
            int endLine = root.getElementIndex(endOffset);
            
            // Draw line numbers
            for (int line = startLine; line <= endLine; line++) {
                try {
                    Element lineElement = root.getElement(line);
                    int lineStart = lineElement.getStartOffset();
                    
                    // Get the Y position of this line
                    Rectangle2D rect = textPane.modelToView2D(lineStart);
                    int y = (int) rect.getY();
                    
                    // Draw the line number
                    String lineNumber = String.valueOf(line + 1);
                    int stringWidth = fontMetrics.stringWidth(lineNumber);
                    int x = getWidth() - stringWidth - MARGIN;
                    
                    g2d.setColor(LINE_NUMBER_COLOR);
                    g2d.drawString(lineNumber, x, y + fontAscent);
                    
                } catch (BadLocationException e) {
                    // Skip this line if there's an error
                }
            }
            
        } catch (Exception e) {
            // Fallback: draw simple line numbers
            int lineCount = getLineCount();
            for (int i = 1; i <= Math.min(lineCount, 100); i++) {
                String lineNumber = String.valueOf(i);
                int stringWidth = fontMetrics.stringWidth(lineNumber);
                int x = getWidth() - stringWidth - MARGIN;
                int y = fontHeight * i;
                
                if (y >= clip.y && y <= clip.y + clip.height) {
                    g2d.setColor(LINE_NUMBER_COLOR);
                    g2d.drawString(lineNumber, x, y);
                }
            }
        }
    }
}
