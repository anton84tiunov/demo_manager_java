package com.example.my_companents.base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FolderTabs extends JTabbedPane {

    
    public FolderTabs() {
        super();
        setTabPlacement(JTabbedPane.TOP); // Установка размещения вкладок сверху

        // JLabel lbl1 = new JLabel("Content for Tab 1");
        // JLabel lbl2 = new JLabel("Content for Tab 2");
        // lbl1.setOpaque(true);
        // addTab("Tab 1", lbl1);
        // addTab("Tab 2", lbl2);
    }

    @Override
    public void addTab(String title, final Component component) {
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 0, 0);
        JPanel panel = new JPanel(fl); // Горизонтальный Layout
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        JButton closeButton = new JButton("x");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < getTabCount(); i++) {
                    if (getTitleAt(i).equals(title)) {
                        removeTabAt(i);
                        break;
                    }
                }
            }
        });
        panel.add(label);
        panel.add(closeButton);
    
        // Создаем JScrollPane и добавляем в него компонент
        JScrollPane scrollPane = new JScrollPane(component);
    
        super.addTab(title, scrollPane); // Добавляем вкладку с JScrollPane
        setTabComponentAt(getTabCount() - 1, panel);
    }
}