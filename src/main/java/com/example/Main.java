package com.example;

import javax.swing.*;

import com.example.database.SqlHelper;
import com.example.my_companents.MenuBar;
import com.example.my_companents.WinSplit;

public class Main implements Runnable {

    
    MenuBar menuBar = new MenuBar();
    WinSplit winSplit = new WinSplit();

    public static void main(String[] args) {
        SqlHelper myHelper = new SqlHelper();
        try {
            myHelper.Conn(); 
            myHelper.CreateDB();
            // myHelper.WriteDB();
            String style = myHelper.ReadDB("style");
            myHelper.CloseDB();
            UIManager.setLookAndFeel(style);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } 
        
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run() {
        // // Установка нового шрифта для всех компонентов
        // Font newFont = new Font("Arial", Font.PLAIN, 20); // Создание нового шрифта
        // UIManager.put("Button.font", newFont); // Установка нового шрифта для кнопок
        // UIManager.put("Label.font", newFont); // Установка нового шрифта для меток
        // UIManager.put("Menu.font", newFont); // Установка нового шрифта для меню
        // // И так далее для всех необходимых компонентов
  
        // Создание и настройка окна
        JFrame frame = new JFrame("Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(winSplit);
        frame.setJMenuBar(menuBar);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}