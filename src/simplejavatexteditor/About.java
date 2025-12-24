package simplejavatexteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JEditorPane textPane;

    public About(UI ui) {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setBackground(MidnightTheme.BACKGROUND);
        
        frame = new JFrame();
        frame.getContentPane().setBackground(MidnightTheme.BACKGROUND);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        frame.setVisible(true);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(ui);
        
        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setOpaque(false); // Transparent to show panel background
        textPane.setBackground(MidnightTheme.BACKGROUND);
        
        // Add Hyperlink Listener
        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(textPane);
        frame.add(panel);
    }

    public void me() {
        frame.setTitle("About Me - " + SimpleJavaTextEditor.NAME);

        // CSS for Dark Theme
        String style = "<style>" +
                "body { font-family: sans-serif; color: #DCDCDC; background-color: #14141E; }" +
                "a { color: #80AAFF; text-decoration: none; }" +
                "</style>";

        contentText = "<html><head>" + style + "</head><body><p>" +
        "Author: Safat Uddin<br /><br />" +
        "Contact me at:<br />" +
        "<a href='mailto:" + SimpleJavaTextEditor.AUTHOR_EMAIL + "?subject=About the NotePad PH Software'>" + SimpleJavaTextEditor.AUTHOR_EMAIL + "</a><br /><br />" +
        "GitHub Profile:<br />" +
        "<a href='https://github.com/safat-tech'>https://github.com/safat-tech</a><br /><br />" +
        "Project Repository:<br />" +
        "<a href='https://github.com/safat-tech/TextEditor'>https://github.com/safat-tech/TextEditor</a>" +
        "</p></body></html>";

        textPane.setText(contentText);
    }

    public void software() {
        frame.setTitle("About Software - " + SimpleJavaTextEditor.NAME);

        String style = "<style>" +
                "body { font-family: sans-serif; color: #DCDCDC; background-color: #14141E; }" +
                "</style>";

        contentText = "<html><head>" + style + "</head><body><p>" +
        "Name: " + SimpleJavaTextEditor.NAME + "<br />" +
        "Version: 1.0<br />" +
        "A simple Java text editor with syntax highlighting and dark theme." +
        "</p></body></html>";

        textPane.setText(contentText);
    }

}