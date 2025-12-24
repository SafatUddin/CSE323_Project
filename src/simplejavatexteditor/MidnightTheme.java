package simplejavatexteditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;

/**
 * Helper class to apply Midnight Theme styles consistently
 */
public class MidnightTheme {
    
    // Theme Colors
    public static final Color BACKGROUND = new Color(20, 20, 30);      // Deep Midnight
    public static final Color COMPONENT_BG = new Color(30, 30, 45);    // Slightly lighter for toolbars/menus
    public static final Color FOREGROUND = new Color(220, 220, 220);   // White/Light Gray text
    public static final Color SELECTION = new Color(60, 60, 90);       // Selection Blue
    public static final Color HOVER = new Color(45, 45, 70);           // Hover state
    public static final Color BORDER = new Color(10, 10, 15);          // Dark Border
    
    // Apply theme to main UI
    public static void applyTheme(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND);
    }
    
    public static void styleMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(COMPONENT_BG);
        menuBar.setForeground(FOREGROUND);
        menuBar.setBorderPainted(false);
    }
    
    public static void styleMenu(JMenu menu) {
        menu.setBackground(COMPONENT_BG);
        menu.setForeground(FOREGROUND);
        menu.setOpaque(true);
    }
    
    public static void styleMenuItem(JMenuItem item) {
        item.setBackground(COMPONENT_BG);
        item.setForeground(FOREGROUND);
        item.setOpaque(true);
        if (item.getIcon() != null && item.getIcon() instanceof ImageIcon) {
             item.setIcon(recolorIcon((ImageIcon)item.getIcon(), FOREGROUND));
        }
    }
    
    public static void styleButton(JButton btn) {
        btn.setBackground(COMPONENT_BG);
        btn.setForeground(FOREGROUND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        // Make transparent so toolbar background shows through
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        
        if (btn.getIcon() != null && btn.getIcon() instanceof ImageIcon) {
             btn.setIcon(recolorIcon((ImageIcon)btn.getIcon(), FOREGROUND));
        }
    }
    
    public static void styleTextArea(JTextPane textArea) {
        textArea.setBackground(BACKGROUND);
        textArea.setForeground(FOREGROUND);
        textArea.setCaretColor(FOREGROUND);
        textArea.setSelectionColor(SELECTION);
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(BACKGROUND);
        textArea.setForeground(FOREGROUND);
        textArea.setCaretColor(FOREGROUND);
        textArea.setSelectionColor(SELECTION);
    }
    
    public static void styleComboBox(JComboBox<?> box) {
        box.setBackground(COMPONENT_BG);
        box.setForeground(FOREGROUND);
        // Attempt to style the internal components (editor/renderer) often needed for full dark mode
         Object child = box.getEditor().getEditorComponent();
         if (child instanceof javax.swing.JComponent) {
             ((javax.swing.JComponent)child).setBackground(COMPONENT_BG);
             ((javax.swing.JComponent)child).setForeground(FOREGROUND);
         }
        // This is a naive way to style standard combos, works for some LAFs
    }
    
    public static void styleScrollPane(javax.swing.JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = COMPONENT_BG;
                this.trackColor = BACKGROUND;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new java.awt.Dimension(0, 0));
                jbutton.setMinimumSize(new java.awt.Dimension(0, 0));
                jbutton.setMaximumSize(new java.awt.Dimension(0, 0));
                return jbutton;
            }
        });
    }
    
    public static ImageIcon recolorIcon(ImageIcon icon, Color color) {
        Image image = icon.getImage();
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
               // Luma to Alpha conversion
                // White (255) -> 0 Alpha
                // Black (0) -> 255 Alpha
                
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                // Calculate brightness (Luma)
                double brightness = (r * 0.299 + g * 0.587 + b * 0.114);
                
                // Invert brightness for alpha
                int alpha = 255 - (int)brightness;
                
                // Clamp alpha
                if (alpha < 0) alpha = 0;
                if (alpha > 255) alpha = 255;
                
                // If it was already transparent, keep it transparent
                int origAlpha = (rgb >> 24) & 0xFF;
                if (origAlpha == 0) return 0;
                
                // Combine: Target Color with calculated Alpha
                // We use the calculated alpha * original alpha (normalized)
                int finalAlpha = (alpha * origAlpha) / 255;
                
                // Enhance: Boost alpha for visibility?
                // if (finalAlpha < 50) return 0; 
                
                return (finalAlpha << 24) | (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
            }
        };
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return new ImageIcon(java.awt.Toolkit.getDefaultToolkit().createImage(ip));
    }
}
