package view;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class GUI extends JFrame {
    private JTextArea textArea;
     
    private JButton buttonStart = new JButton("Execute");
    private JTextArea txt;
    private PrintStream standardOut;
    private String query;
     
    public GUI() {
        super("SearchEngine");
        
        txt = new JTextArea("------------Enter query here------------", 400, 60);  
        txt.setBounds(10,30,400,50);  
        txt.setBackground(Color.DARK_GRAY);  
        txt.setForeground(Color.white); 
        
        buttonStart.setBackground(Color.yellow);
        
        this.getContentPane().setBackground(Color.black);
        textArea = new JTextArea(50, 10);
        textArea.setEditable(false);
        textArea.setBackground(Color.DARK_GRAY);  
        textArea.setForeground(Color.white);
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
         
        // keeps reference of standard output stream
        standardOut = System.out;
         
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
 
        // creates the GUI
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
         
        add(buttonStart, constraints);
         
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        add(txt, constraints);
         
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
         
        add(new JScrollPane(textArea), constraints);
         
        // adds event handler for button Execute
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            	query = txt.getText();
            	txt.setText("------------Enter query here------------");
            	String result = getQuery();
            }
        });
         
         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);    // centers on screen
        setVisible(true);
    }
     
    public boolean checkQuery()
    {
    	if(query == null)
    		return false;
    	return true;
    }
    public String getQuery()
    {
    	String q = query;
    	query = null;
    	return q;
    }
 
}
