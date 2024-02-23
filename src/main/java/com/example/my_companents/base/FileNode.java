package com.example.my_companents.base;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class FileNode {
    private File file;
    private String name;
    private String path;

    public FileNode(String name, String path, File file) {
        this.name = name;
        this.path = path;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }

     public Icon getIcon() {
        FileSystemView view = FileSystemView.getFileSystemView();
        if (file.isDirectory()) {
            return view.getSystemIcon(file); // Получить иконку папки
        } else {
            return view.getSystemIcon(file); // Получить иконку файла
        }
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }
}