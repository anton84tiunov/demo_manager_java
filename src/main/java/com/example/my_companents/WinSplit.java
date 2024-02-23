package com.example.my_companents;

import java.awt.Graphics;

import javax.swing.JSplitPane;

import com.example.my_companents.left.LeftPanel;
import com.example.my_companents.right.RightPanel;

public class WinSplit extends JSplitPane{
    

        // JPanel jPanel1 = new JPanel();
        // JPanel jPanel2 = new JPanel();

        LeftPanel LeftJPanel = new LeftPanel();
        RightPanel RightJPanel = new RightPanel();

        private boolean painted;

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        
            if (!painted) {
                painted = true;
                this.setDividerLocation(0.5);
            }
        }
public WinSplit() {
    // this(JSplitPane.HORIZONTAL_SPLIT, jPanel1,jPanel2);
    this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    this.setLeftComponent(LeftJPanel);
    this.setRightComponent(RightJPanel);
    this.setOneTouchExpandable(true);
    // this.setDividerLocation(50);
    // this.setDividerLocation(150);

}
   

     
     
}
