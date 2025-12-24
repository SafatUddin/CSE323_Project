package simplejavatexteditor;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import javax.swing.*;

public class Find extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    int startIndex=0;
        int select_start=-1;
    JLabel lab1, lab2;
    JTextField textF, textR;
    JButton findBtn, findNext, replace, replaceAll, cancel;
    private JTextComponent txt;

    public Find(JTextComponent text) {
        super((JFrame)SwingUtilities.getWindowAncestor(text), "Find and Replace", Dialog.ModalityType.MODELESS);
        this.txt = text;
        
        txt.setSelectionColor(new Color(184, 207, 229));
        txt.setSelectedTextColor(Color.BLACK);

        lab1 = new JLabel("Find:");
        lab2 = new JLabel("Replace:");
        textF = new JTextField(30);
        textR = new JTextField(30);
        findBtn = new JButton("Find");
        findNext = new JButton("Find Next");
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace All");
        cancel = new JButton("Cancel");

        setLayout(null);

        int labWidth = 80;
        int labHeight = 20;

        lab1.setBounds(10,10, labWidth, labHeight);
        add(lab1);
        textF.setBounds(10+labWidth, 10, 120, 20);
        add(textF);
        lab2.setBounds(10, 10+labHeight+10, labWidth, labHeight);
        add(lab2);
        textR.setBounds(10+labWidth, 10+labHeight+10, 120, 20);
        add(textR);

        findBtn.setBounds(225, 6, 115, 20);
        add(findBtn);
        findBtn.addActionListener(this);

        findNext.setBounds(225, 28, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        replace.setBounds(225, 50, 115, 20);
        add(replace);
        replace.addActionListener(this);

        replaceAll.setBounds(225, 72, 115, 20);
        add(replaceAll);
        replaceAll.addActionListener(this);

        cancel.setBounds(225, 94, 115, 20);
        add(cancel);
        cancel.addActionListener(this);

        int width = 360;
        int height = 160;

        setSize(width,height);

        setLocationRelativeTo(txt);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void find() {
        String searchText = textF.getText();
        if (searchText == null || searchText.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please enter text to find!");
            return;
        }
        
        String content = txt.getText();
        select_start = content.toLowerCase().indexOf(searchText.toLowerCase());
        
        if(select_start == -1)
        {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Could not find \"" + searchText + "\"!");
            return;
        }
        
        int select_end = select_start + searchText.length();
        txt.requestFocusInWindow();
        txt.setCaretPosition(select_start);
        txt.moveCaretPosition(select_end);
        startIndex = select_end;
    }

    public void findNext() {
        String selection = txt.getSelectedText();
        String searchText = textF.getText();
        
        if (selection == null || selection.isEmpty())
        {
            if (searchText == null || searchText.isEmpty())
            {
                searchText = JOptionPane.showInputDialog("Find:");
                if (searchText == null) return;
                textF.setText(searchText);
            }
            selection = searchText;
        }
        
        String content = txt.getText();
        int select_start = content.toLowerCase().indexOf(selection.toLowerCase(), startIndex);
        
        if (select_start == -1)
        {
            startIndex = 0;
            select_start = content.toLowerCase().indexOf(selection.toLowerCase(), startIndex);
            if (select_start == -1)
            {
                JOptionPane.showMessageDialog(null, "Could not find \"" + selection + "\"!");
                return;
            }
        }
        
        int select_end = select_start + selection.length();
        txt.requestFocusInWindow();
        txt.setCaretPosition(select_start);
        txt.moveCaretPosition(select_end);
        startIndex = select_end;
    }

    public void replace() {
        String selection = txt.getSelectedText();
        String searchText = textF.getText();
        String replaceText = textR.getText();
        
        if (selection != null && !selection.isEmpty() && 
            selection.equalsIgnoreCase(searchText))
        {
            int start = txt.getSelectionStart();
            txt.replaceSelection(replaceText);
            txt.requestFocusInWindow();
            txt.setCaretPosition(start);
            txt.moveCaretPosition(start + replaceText.length());
        }
        else
        {
            find();
        }
    }

    public void replaceAll() {
        txt.setText(txt.getText().replaceAll(textF.getText() , textR.getText()));
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == findBtn)
        {
           find();
        }
        else if(e.getSource() == findNext)
        {
           findNext();
        }
        else if(e.getSource() == replace)
        {
            replace();
        }
        else if(e.getSource() == replaceAll)
        {
           replaceAll();
        }
        else if(e.getSource() == cancel)
        {
           this.setVisible(false);
        }
   }

}