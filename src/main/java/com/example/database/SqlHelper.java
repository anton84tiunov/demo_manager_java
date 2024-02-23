package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlHelper {
    public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;


    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
	public void Conn() throws ClassNotFoundException, SQLException 
    {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:Db.s3db");
        
        System.out.println("База Подключена!");
    }

      // --------Закрытие--------
      public void CloseDB() throws ClassNotFoundException, SQLException
      {
       conn.close();
       statmt.close();
       resSet.close();
       
       System.out.println("Соединения закрыты");
      }

 
 // --------Создание таблицы--------
 public void CreateDB() throws ClassNotFoundException, SQLException
    {
     statmt = conn.createStatement();
     statmt.execute(
        "CREATE TABLE if not exists 'Settings' (" +
        "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "'key' text, " + 
        "'val' text" +
        ");"
        );
     System.out.println("Таблица создана или уже существует.");
    }
 
 // --------Заполнение таблицы--------
 public void WriteDB() throws SQLException, ClassNotFoundException
 {
        statmt.execute("INSERT INTO 'Settings' ('key', 'val') VALUES ('style', 'com.sun.java.swing.plaf.gtk.GTKLookAndFeel'); ");
       
        System.out.println("Таблица заполнена");
 }
 
 // -------- Вывод таблицы--------
 public String ReadDB(String key) throws ClassNotFoundException, SQLException
    {
    String value = null;
    resSet = statmt.executeQuery("SELECT val FROM Settings where key = '" + key + "'");
     
     while(resSet.next())
     {

      value = resSet.getString("val");

     }	
     System.out.println("Таблица выведена");
     return value;
     }

     public void UpdateDB(String key, String newValue) throws SQLException, ClassNotFoundException {
        String query = "UPDATE Settings SET val = ? WHERE key = ?";
    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
        preparedStatement.setString(1, newValue);
        preparedStatement.setString(2, key);
        preparedStatement.executeUpdate();
        System.out.println("Запись обновлена");
    }
}
   

}
