package simplejavatexteditor;

import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SimpleJavaTextEditor extends JTextPane {

    private static final long serialVersionUID = 1L;
    public final static String AUTHOR_EMAIL = "safat.uddin@northsouth.edu";
    public final static String NAME = "323-TextEditor";
    public final static String EDITOR_EMAIL = "safat.uddin@northsouth.edu";

    public static void main(String[] args) {
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
    		e.printStackTrace();
    	}
    	
        new UI().setVisible(true);
    }

}
