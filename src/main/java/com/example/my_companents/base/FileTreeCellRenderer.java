package com.example.my_companents.base;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    private Icon folderIcon;
    private Icon fileIcon;

    public FileTreeCellRenderer(Icon folderIcon, Icon fileIcon) {
        this.folderIcon = folderIcon;
        this.fileIcon = fileIcon;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        if (userObject instanceof FileNode) {
            FileNode fileNode = (FileNode) userObject;
            if (fileNode.isDirectory()) {
                setIcon(folderIcon);
            } else {
                setIcon(fileIcon);
            }
        }

        return c;
    }
}