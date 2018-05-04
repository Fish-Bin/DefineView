package com.liubin.fenghui.lib;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class myClass {
    public static void main(String[] args){
        JFrame frame=new JFrame();
        JTextPane editor=new JTextPane();
        editor.getDocument().addDocumentListener(new SyntaxHighlighter(editor));
        frame.getContentPane().add(editor);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

    }
}
