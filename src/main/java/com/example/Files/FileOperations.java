package com.example.Files;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {

    // Копирование файлов в буфер обмена
    public static void copyFilesToClipboard(List<File> sourceFiles) {
        List<FileTransferable> fileTransferables = new ArrayList<>();
        for (File file : sourceFiles) {
            fileTransferables.add(new FileTransferable(file));
        }
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.javaFileListFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(DataFlavor.javaFileListFlavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    List<File> fileList = new ArrayList<>();
                    for (FileTransferable transferable : fileTransferables) {
                        fileList.add(transferable.getFile());
                    }
                    return fileList;
                }
                throw new UnsupportedFlavorException(flavor);
            }
        }, null);
    }

    // Вставка файлов из буфера обмена
 
    public static List<File> pasteFilesFromClipboard(String[] paths) {
        List<File> fileList = new ArrayList<>();
        for (String path : paths) {
            fileList.add(new File(path));
        }
        return fileList;
    }
    // Копирование файлов в указанную папку
    public static void copyFiles(List<File> sourceFiles, String destinationFolder) {
        for (File file : sourceFiles) {
            try {
                Files.copy(file.toPath(), new File(destinationFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// Пользовательский Transferable для передачи файлов в буфер обмена
class FileTransferable implements Transferable {
    private final File file;

    public FileTransferable(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.javaFileListFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.javaFileListFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            List<File> fileList = new ArrayList<>();
            fileList.add(file);
            return fileList;
        }
        throw new UnsupportedFlavorException(flavor);
    }
}