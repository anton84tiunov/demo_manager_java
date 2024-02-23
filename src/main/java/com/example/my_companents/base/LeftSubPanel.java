package com.example.my_companents.base;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LeftSubPanel  extends JPanel{
    FileSystemTree tree;
      public LeftSubPanel(RightSubPanel rightSubPanel) {
        setLayout(new BorderLayout()); // Устанавливаем BorderLayout для родительской панели
        tree = new FileSystemTree(rightSubPanel);
        JScrollPane scrollPane = new JScrollPane(tree); // Передаем экземпляр FileSystemTree в конструктор JScrollPane
        add(scrollPane, BorderLayout.CENTER); // Добавляем JScrollPane в центр родительской панели
    }

}
