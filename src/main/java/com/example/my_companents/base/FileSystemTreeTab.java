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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.ArrayList;


public class FileSystemTreeTab extends JTree {

    File folderCopy;
    Icon folderIcon = new ImageIcon("src/main/resources/icons/folder_icon.png");
    Icon fileIcon = new ImageIcon("src/main/resources/icons/file_icon.png");

    public FileSystemTreeTab(String path) {
        super(new DefaultMutableTreeNode(path)); // Создаем корневой узел
        setCellRenderer(new FileTreeCellRenderer(folderIcon, fileIcon));

        setShowsRootHandles(true); // Показывать иконки у корневых узлов
        setRootVisible(true); // Показывать корневой узел
        Font BigFontTR = new Font("TimesRoman", Font.BOLD, 20);// Тут все про шрифт)

        this.setFont(BigFontTR);// применяем шрифт к кнопке

        // Загрузка содержимого корневой папки
        try {
            File rootFolder = new File(path);
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

        // код инициализации
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

                        if (userObject instanceof FileNode) {
                            FileNode fileNode = (FileNode) userObject;
                            String filePath = fileNode.getPath(); // Получение полного пути к файлу
                            File file = new File(filePath); // Создание объекта File
                            System.out.println("Right-clicked fileName: " + filePath);
                            if (file.exists()) {
                                if (getSelectionCount() > 1) {
                                   
                                    // Вывод списка выбранных файлов и папок
                                    TreePath[] selectedPaths = getSelectionPaths();
                                    List<String> selectedPathsList = new ArrayList<>();
                                    
                                    if (selectedPaths != null) {
                                        for (TreePath selPath : selectedPaths) {
                                            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                                            FileNode selectedFileNode = (FileNode) selNode.getUserObject();
                                            String selectedFilePath = selectedFileNode.getPath();
                                            selectedPathsList.add(selectedFilePath);
                                           
                                        }
                                         // Правый клик с множественным выбором
                                         System.out.println("Selected file or folder: " + selectedPathsList);
                                     showPopupMenu("multi", fileNode, e.getComponent(), e.getX(), e.getY(), selectedPathsList);

                                    }

                                    System.out.println("Right-clicked with multiple selection");
                                } else {
                                    List<String> selectedPathsList = new ArrayList<>();
                                    selectedPathsList.add(filePath);
                                    if (file.isDirectory()) {
                                        // Обработка события для каталога
                                        showPopupMenu("folder", fileNode, e.getComponent(), e.getX(), e.getY(), selectedPathsList);
                                        System.out.println("Right-clicked on directory: " + filePath);
                                    } else {
                                        // Обработка события для файла
                                        showPopupMenu("file", fileNode, e.getComponent(), e.getX(), e.getY(), selectedPathsList);
                                        System.out.println("Right-clicked on file: " + filePath);
                                    }
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

    private void showPopupMenu(String type, FileNode fileNode, Component component, int x, int y, List<String> selectedPaths) {
        // Создание и отображение выпадающего меню
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem createFile = new JMenuItem("Создать файл");
        JMenuItem createFolder = new JMenuItem("Создать папку");
        JMenuItem createFoldersFiles = new JMenuItem("Создать папки с файлами");
        JMenuItem copyFile = new JMenuItem("Копировать");
        JMenuItem cutFile = new JMenuItem("Вырезать");
        JMenuItem pasteFile = new JMenuItem("Вставить");
        JMenuItem deleteFile = new JMenuItem("Удалить");
        JMenuItem renameFile = new JMenuItem("Переименовать");
        JMenuItem property = new JMenuItem("свойства");

        createFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);

            }
        });

        createFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ((DefaultTreeModel) getModel()).reload();
                System.out.println(selectedPaths);            }
        });

        createFoldersFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ((DefaultTreeModel) getModel()).reload();
                System.out.println(selectedPaths);
            }
        });

        copyFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ((DefaultTreeModel) getModel()).reload();
                System.out.println(selectedPaths);
            }
        });

        cutFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);
            }
        });

        pasteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);
            }
        });

        deleteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);
            }
        });

        renameFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);
            }
        });

        property.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(selectedPaths);

            }
        });

        // Добавление пунктов меню в выпадающее меню
        if (type == "file") {
            popupMenu.add(renameFile);
        } else if (type == "folder") {
            popupMenu.add(createFile);
            popupMenu.add(createFolder);
            popupMenu.add(createFoldersFiles);
            popupMenu.add(renameFile);
        } else if (type == "multi") {

        }

        popupMenu.add(copyFile);
        popupMenu.add(cutFile);
        popupMenu.add(pasteFile);
        popupMenu.add(deleteFile);
        popupMenu.add(property);

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

    private void copyDir(String targetSourceDir) {
        // folderCopy = new File(sourceDirName);

        File[] listOfFiles = folderCopy.listFiles();

        Path destDir = Paths.get(targetSourceDir);
        if (listOfFiles != null)
            for (File file : listOfFiles)
                try {
                    Files.copy(file.toPath(), destDir.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);

                } catch (IOException e) {
                    // Обрабатываем исключение
                    e.printStackTrace();
                }

    }
}