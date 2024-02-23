package com.example.my_companents.base;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class RightSubPanel extends JPanel {
    private FolderTabs folderTabs;

    public RightSubPanel() {
        setLayout(new BorderLayout());
        folderTabs = new FolderTabs();
        add(folderTabs, BorderLayout.CENTER);
    }

    // Добавляем метод для получения ссылки на FolderTabs
    public FolderTabs getFolderTabs() {
        return folderTabs;
    }
}