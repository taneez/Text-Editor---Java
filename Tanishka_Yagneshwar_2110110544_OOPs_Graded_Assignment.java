
package texteditor;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.plaf.metal.*;
import java.awt.event.*;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextEditor implements ActionListener,KeyListener{

   
    static JTextPane text_box;
    static JFrame window;
    

    public static void changeStyle(JButton b, String str) {
        StyledDocument doc = (StyledDocument) text_box.getDocument();
        int selectionEnd = text_box.getSelectionEnd();
        int selectionStart = text_box.getSelectionStart();
        if (selectionStart == selectionEnd) {
            return;
        }

        if (str == "BOLD"){
            Element element = doc.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();

            MutableAttributeSet asNew = new SimpleAttributeSet();
            StyleConstants.setBold(asNew, !StyleConstants.isBold(as));
            doc.setCharacterAttributes(selectionStart, text_box.getSelectedText().length(), asNew, true);

        }
        if (str == "ITALIC"){
            Element element = doc.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();

            MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
            StyleConstants.setItalic(asNew, !StyleConstants.isItalic(as));
            doc.setCharacterAttributes(selectionStart, text_box.getSelectedText().length(), asNew, true);
        }
        
        if(str == "UNDERLINE"){
            Element element = doc.getCharacterElement(selectionStart);
            AttributeSet as = element.getAttributes();

            MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
            StyleConstants.setUnderline(asNew, !StyleConstants.isUnderline(as));
            doc.setCharacterAttributes(selectionStart, text_box.getSelectedText().length(), asNew, true);
        }
    }
    
    static void Text_Editor (JPanel panel1, JFrame f)
    {
        
        //check box for adding bold, italic, underline
        JButton bold = new JButton("BOLD");
        bold.addActionListener(e ->changeStyle(bold, "BOLD"));
        JButton italic = new JButton("ITALIC");
        italic.addActionListener(e ->changeStyle(italic, "ITALIC"));
        JButton underline = new JButton("UNDERLINE");
        underline.addActionListener(e ->changeStyle(underline, "UNDERLINE"));
        panel1.add(bold);
        panel1.add(italic);
        panel1.add(underline);
        bold.setBounds(10,10,70,20);
        italic.setBounds(90,10,80,20);
        underline.setBounds(180,10,110,20); 
        //
        
        //Menu bar with file and edit
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu reviewMenu = new JMenu("Review");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem file_new = new JMenuItem("New");
        JMenuItem file_save = new JMenuItem("Save");
        JMenuItem file_open = new JMenuItem("Open");
        JMenuItem file_print = new JMenuItem("Print");
        file_save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){     
                JFileChooser j = new JFileChooser("f:");
                int r = j.showSaveDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File fi = new File(j.getSelectedFile().getAbsolutePath());
                    try {
                        FileWriter wr = new FileWriter(fi, false); 
                        BufferedWriter w = new BufferedWriter(wr);
                        w.write(text_box.getText());
                        w.flush();
                        w.close();
                    }
                    catch (Exception evt) {
                        JOptionPane.showMessageDialog(f, evt.getMessage());
                    }
                }
                else
                    JOptionPane.showMessageDialog(f, "the user cancelled the operation");
            }
        });
        
        file_open.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser j = new JFileChooser("f:");

                int r = j.showOpenDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    File fi = new File(j.getSelectedFile().getAbsolutePath());
 
                    try {
                        String s1 = "", sl = "";
                        FileReader fr = new FileReader(fi);
                        BufferedReader br = new BufferedReader(fr);
                        sl = br.readLine();
                        while ((s1 = br.readLine()) != null) {
                            sl = sl + "\n" + s1;
                        }
                        text_box.setText(sl);
                    }
                    catch (Exception evt) {
                        JOptionPane.showMessageDialog(f, evt.getMessage());
                    }
                }
                else
                    JOptionPane.showMessageDialog(f, "the user cancelled the operation");
            }
        });
        
        file_print.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                text_box.print();
                }
                catch (Exception evt) {
                    JOptionPane.showMessageDialog(f, evt.getMessage());
                }
            }
        });
        
        file_new.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                text_box.setText("");
            }
        });
        
        fileMenu.add(file_new);
        fileMenu.add(file_save);
        fileMenu.add(file_open);
        fileMenu.add(file_print);
        JMenuItem edit_cut = new JMenuItem("Cut");
        JMenuItem edit_copy = new JMenuItem("Copy");
        JMenuItem edit_paste = new JMenuItem("Paste");
        edit_cut.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                text_box.cut();
            }
        });
        edit_copy.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                text_box.copy();
            }
        });
        edit_paste.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                text_box.paste();
            }
        });
        editMenu.add(edit_cut);
        editMenu.add(edit_copy);
        editMenu.add(edit_paste);
        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(reviewMenu);
        mb.add(helpMenu);
        f.setJMenuBar(mb);
        //
        
        //Font type drop down
        String font_type_list[] = {"FONT TYPE", "Arial", "Times New Roman", "Comic Sans MS"};
        JComboBox font_type =  new JComboBox(font_type_list);
        font_type.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //text_box.setFont(new Font(""+font_type.getItemAt(font_type.getSelectedIndex()), Font.PLAIN, 20));
                StyledDocument doc = (StyledDocument) text_box.getDocument();
                int selectionEnd = text_box.getSelectionEnd();
                int selectionStart = text_box.getSelectionStart();
                Element element = doc.getCharacterElement(selectionStart);
                AttributeSet as = element.getAttributes();

                MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
                StyleConstants.setFontFamily(asNew,(""+font_type.getItemAt(font_type.getSelectedIndex())));
                doc.setCharacterAttributes(selectionStart, text_box.getSelectedText().length(), asNew, true);  
            }
        });
        panel1.add(font_type);
        font_type.setBounds(300,10,150,20);
        //
        //font size drop down
        String font_size_list[] = {"FONT SIZE","0","5","10","15","20","25","30"};
        JComboBox font_size =  new JComboBox(font_size_list);
        font_size.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //text_box.setFont(new Font(""+font_type.getItemAt(font_type.getSelectedIndex()), Font.PLAIN, 20));
                StyledDocument doc = (StyledDocument) text_box.getDocument();
                int selectionEnd = text_box.getSelectionEnd();
                int selectionStart = text_box.getSelectionStart();
                Element element = doc.getCharacterElement(selectionStart);
                AttributeSet as = element.getAttributes();
                MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
                StyleConstants.setFontSize(asNew,15+Integer.parseInt(""+font_size.getItemAt(font_size.getSelectedIndex())));
                doc.setCharacterAttributes(selectionStart, text_box.getSelectedText().length(), asNew, true);  
            }
        });
        panel1.add(font_size);
        font_size.setBounds(460,10,100,20);
        //
        
        //Find Label
        JLabel find_label = new JLabel("Find:");
        panel1.add(find_label);
        find_label.setBounds(10,40,40,20);
        //find text field
        JTextField find_text = new JTextField();
        panel1.add(find_text);
        find_text.setBounds(10,65,880,20);
        String text_to_find;
        
        //
        
        //replace Label
        JLabel replace_label = new JLabel("Replace:");
        panel1.add(replace_label);
        replace_label.setBounds(10,95,50,20);
        //replace text field
        JTextField replace_text = new JTextField();
        panel1.add(replace_text);
        replace_text.setBounds(10,120,880,20);
        String text_to_replace;
        //
        
        //find all, find next, replace, replace all buttons
        JButton find = new JButton("FIND");
        panel1.add(find);
        find.setBounds(10,160,100,20);
        
        JButton replace_button = new JButton("REPLACE");
        panel1.add(replace_button);
        replace_button.setBounds(120,160,100,20);
        
        replace_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //text_to_find = find_text.getText();
            }
        });
        JButton replace_all = new JButton("REPLACE ALL");
        panel1.add(replace_all);
        replace_all.setBounds(230,160,130,20);
        //
        
        
    }    

    public static void Shape_Editor(JPanel panel2)
    {
        //Heading Label
        JLabel sketchpad = new JLabel("SKETCH PAD", JLabel.CENTER);
        panel2.add(sketchpad);
        sketchpad.setBounds(250,10,80,40);
        //
        //buttons
        JButton rect = new JButton("RECTANGLE");
        panel2.add(rect);
        rect.setBounds(10,70,120,20);
        rect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("hi");
            }
        });
        
        JButton oval = new JButton("OVAL");
        panel2.add(oval);
        oval.setBounds(140,70,80,20);
        
        JButton line = new JButton("LINE");
        panel2.add(line);
        line.setBounds(230,70,80,20);
        
        JButton triangle = new JButton("TRIANGLE");
        panel2.add(triangle);
        triangle.setBounds(320,70,120,20);
        
        JButton pentagon = new JButton("PENTAGON");
        panel2.add(pentagon);
        pentagon.setBounds(450,70,120,20);
        //
        
        //clear button
        JButton clear = new JButton("CLEAR");
        panel2.add(clear);
        clear.setBounds(10,690,80,20);
        //
    }

    public void keyPressed(KeyEvent e) {}  
            public void keyReleased (KeyEvent e) { }
                
        
    public void keyTyped(KeyEvent e){}
    public void actionPerformed(ActionEvent e){}
    
    
    
    public static void main(String[] args) {
        
        try {
  
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        }
        catch (Exception e) {
            System.out.println("Look and Feel not set");
        }
        
        //creating the main frame 
        JFrame window = new JFrame();
        window.setTitle("Text Editor");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //creating the 2 panels to place the text editor screen and another one to place the shape editor screen.
        JPanel panel1 = new JPanel();//text editor
        JPanel panel2 = new JPanel();//shape editor
        
        Text_Editor(panel1, window);
        
        
        
        //Main Text Pane
        text_box = new JTextPane();
        text_box.setFont(new Font("Arial",Font.PLAIN,15));
        
        panel1.add(text_box);
        text_box.setBounds(10,200,880,510);
        //
        
        //adding word count and char count labels to window
        JLabel word_count = new JLabel("Word Count:");
        window.add(word_count);
        word_count.setBounds(20,740,80,20);
        
        JLabel char_count = new JLabel("Character Count:");
        window.add(char_count);
        char_count.setBounds(170,740,120,20);
        
        //
        
        KeyListener listener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                
            }
            @Override
            public void keyReleased(KeyEvent event) {
                String text = text_box.getText();    
                String words[] = text.split ("\\s");
                word_count.setText("Words: "+words.length);  
                char_count.setText("Characters: "+text.length());  
            }
            @Override
            public void keyTyped(KeyEvent event) {
                
            }
            
            
        };
        text_box.addKeyListener(listener);

        
        Color light_gray = new Color(245,245,245);
        
        panel1.setBounds(10,10,900,720);
        panel1.setBackground(light_gray);
        panel1.setLayout(null);
        window.add(panel1);
        
        Shape_Editor(panel2);
        
        panel2.setBounds(920,10,600,720);
        panel2.setBackground(light_gray);
        panel2.setLayout(null);
        window.add(panel2);
        
        window.setSize(1550,830);
        window.setLayout(null);
        window.setVisible(true);
        
    }
    
}


