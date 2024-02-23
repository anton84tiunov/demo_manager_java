package com.example.my_companents.base;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class FileSystemTree extends JTree {

    RightSubPanel rightSubPanel;
    Icon folderIcon = new ImageIcon("src/main/resources/icons/folder_icon.png");
    Icon fileIcon = new ImageIcon("src/main/resources/icons/file_icon.png");
    
    public FileSystemTree(RightSubPanel rightSubPanel) {
        super(new DefaultMutableTreeNode("/")); // Создаем корневой узел
        setCellRenderer(new FileTreeCellRenderer(folderIcon, fileIcon));
        
        this.rightSubPanel = rightSubPanel; // Сохраняем экземпляр класса RightSubPanel
        setShowsRootHandles(true); // Показывать иконки у корневых узлов
        setRootVisible(true); // Показывать корневой узел
        Font BigFontTR = new Font("TimesRoman", Font.BOLD, 20);// Тут все про шрифт)

        this.setFont(BigFontTR);// применяем шрифт к кнопке

        // Загрузка содержимого корневой папки
        try {
            File rootFolder = new File("/");
            // File rootFolder = new File(System.getProperty("/"));
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getModel().getRoot();
            addFilesAndSubdirectories(rootFolder, rootNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                if (parentNode.getChildCount() == 1 && parentNode.getChildAt(0).toString().equals("Loading...")) {
                    try {
                        StringBuilder fullPath = new StringBuilder();
                        TreePath path = event.getPath();
                        Object[] nodes = path.getPath();
                        for (Object node : nodes) {
                            fullPath.append(node.toString());
                            fullPath.append(File.separator); // Разделитель для файловой системы
                        }
                        File currentFile = new File(fullPath.toString());
                        addFilesAndSubdirectories(currentFile, parentNode);
                        ((DefaultTreeModel) getModel()).reload(parentNode);

                        // Удаление узла "Loading..."
                        parentNode.remove(0);
                        ((DefaultTreeModel) getModel()).reload(parentNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                // Ничего не делаем при сворачивании узла
            }
        });

        // ваш код инициализации
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        // Действия при двойном щелчке мыши
                        System.out.println("Double-clicked on: " + node.getUserObject());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        Object userObject = node.getUserObject();
                        System.out.println("Right-clicked userObject: " + userObject);
                        // Проверяем, был ли клик при множественном выборе

                        if (userObject instanceof FileNode) {
                            FileNode fileNode = (FileNode) userObject;
                            String filePath = fileNode.getPath(); // Получение полного пути к файлу
                            File file = new File(filePath); // Создание объекта File
                            System.out.println("Right-clicked fileName: " + filePath);
                            if (file.exists()) {
                                if (file.isDirectory()) {
                                    // Обработка события для каталога
                                    showPopupMenuFolder(fileNode, e.getComponent(), e.getX(), e.getY());
                                    System.out.println("Right-clicked on directory: " + filePath);
                                } else {
                                    // Обработка события для файла
                                    showPopupMenuFile(e.getComponent(), e.getX(), e.getY());
                                    System.out.println("Right-clicked on file: " + filePath);
                                }
                            }
                        }
                    }
                }
            }

        });
        // Обновляем дерево
        ((DefaultTreeModel) getModel()).reload();
    }

    private void showPopupMenuFile(Component component, int x, int y) {
        // Создание и отображение выпадающего меню
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("Menu Item FILE 1");

        JMenuItem menuItem2 = new JMenuItem("Menu Item FILE 2");

        // Добавление слушателя события для пункта меню 1
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта меню 1
                System.out.println("Menu Item 1 selected");
                // Здесь можно выполнить нужные действия
            }
        });

        // Добавление слушателя события для пункта меню 2
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта меню 2
                System.out.println("Menu Item 2 selected");
                // Здесь можно выполнить нужные действия
            }
        });

        // Добавление пунктов меню в выпадающее меню
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

        // Отображение контекстного меню
        popupMenu.show(component, x, y);
    }

    private void showPopupMenuFolder(FileNode fileNode, Component component, int x, int y) {
        // Создание и отображение выпадающего меню
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("add folder to tabs");
        JMenuItem menuItem2 = new JMenuItem("Menu Item FOLDER 2");

        // Добавление слушателя события для пункта меню 1
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта меню 1
                System.out.println("Menu Item 1 selected");
                // Получаем ссылку на FolderTabs из RightSubPanel
                String folderName = fileNode.getName();
                String folderPatch = fileNode.getPath();
                FolderTabs folderTabs = rightSubPanel.getFolderTabs();
                // Добавляем новую вкладку

                folderTabs.addTab(folderName, new FileSystemTreeTab(folderPatch));
                folderTabs.setToolTipTextAt(folderTabs.getTabCount() - 1, folderPatch);

            }
        });

        // Добавление слушателя события для пункта меню 2
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе пункта меню 2
                System.out.println("Menu Item 2 selected");
                // Здесь можно выполнить нужные действия
            }
        });

        // Добавление пунктов меню в выпадающее меню
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);

        // Отображение контекстного меню
        popupMenu.show(component, x, y);
    }

    private void addFilesAndSubdirectories(File directory, DefaultMutableTreeNode parentNode) {
        // Получаем список файлов и подкаталогов текущей папки
        File[] files = directory.listFiles();
        System.out.println(directory); // snap
        System.out.println(files); // nul
        if (files == null) {
            return; // Если список файлов пуст, выходим из метода
        }

        // Проходим по каждому файлу или подкаталогу и добавляем его в качестве
        // дочернего узла
        for (File file : files) {
            FileNode fileNode = new FileNode(file.getName(), file.getAbsolutePath(), file); // Создаем объект FileNode
            DefaultMutableTreeNode fileTreeNode = new DefaultMutableTreeNode(fileNode); // Используем FileNode для
                                                                                        // создания узла дерева
            parentNode.add(fileTreeNode);

            if (file.isDirectory()) {
                // Добавляем заглушку для папки, чтобы можно было раскрывать подкаталоги
                fileTreeNode.add(new DefaultMutableTreeNode("Loading..."));
            }
        }
    }
}