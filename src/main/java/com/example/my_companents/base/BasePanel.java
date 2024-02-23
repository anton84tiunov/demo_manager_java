package com.example.my_companents.base;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

// import java.awt.Color;


public class BasePanel extends JPanel{

    RightSubPanel rightSubPanel = new RightSubPanel();

    LeftSubPanel leftSubPanel = new LeftSubPanel(rightSubPanel);
   

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSubPanel, rightSubPanel);

    public BasePanel() {

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        // splitPane.setDividerLocation(0.5);
     }



    
}

