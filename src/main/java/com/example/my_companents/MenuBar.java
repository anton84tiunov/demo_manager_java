package com.example.my_companents;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import com.example.database.SqlHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public class MenuBar extends JMenuBar{

    JMenuItem fileCreate = new JMenuItem("create");
    JMenuItem fileUpdate = new JMenuItem("update");

    JMenu fileMenu = new JMenu("File");

    JMenuItem editCopy = new JMenuItem("copy");
    JMenuItem editCut = new JMenuItem("cut");
    JMenuItem editPaste = new JMenuItem("paste");
  
    JMenu editMenu = new JMenu("Edit");

    JMenu winStyleMenu = new JMenu("Style");
  
    JMenu winMenu = new JMenu("Window");
    // JMenuBar menuBar = new JMenuBar();
    

      public MenuBar() {
        fileMenu.setMnemonic(KeyEvent.VK_H);
        fileMenu.getAccessibleContext().setAccessibleDescription("File Menu Items");
        fileMenu.add(fileCreate);
        fileMenu.add(fileUpdate);

        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.getAccessibleContext().setAccessibleDescription("Edit Menu Items");
        editMenu.add(editCopy);
        editMenu.add(editCut);
        editMenu.add(editPaste);
    
        this.add(fileMenu);
        this.add(editMenu);
        


        UIManager.LookAndFeelInfo[] mLookAndFeels = UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo lookAndFeel: mLookAndFeels){
            // System.out.println(lookAndFeel.getName());
            // System.out.println(lookAndFeel.getClassName());
            JMenuItem jmi = new JMenuItem(lookAndFeel.getName());
            jmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    System.out.println(lookAndFeel.getName());
                    UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    SqlHelper myHelper = new SqlHelper();

                    myHelper.Conn(); 
                    myHelper.UpdateDB("style", lookAndFeel.getClassName());
                    myHelper.CloseDB();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            });;
            winStyleMenu.add(jmi);
            winMenu.add(winStyleMenu);

        }
        this.add(winMenu);

    }
    
}
