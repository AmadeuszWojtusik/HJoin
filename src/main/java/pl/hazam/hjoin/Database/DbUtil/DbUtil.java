package pl.hazam.hjoin.Database.DbUtil;

import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    public static Connection getConnection (YamlConfiguration globalConfig){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + globalConfig.getString("Server") + ":" + globalConfig.getString("Port") + "/" + globalConfig.getString("Database") + "?useSSL=" + globalConfig.getString("ssl") + "&serverTimezone=" + globalConfig.getString("timezone"), globalConfig.getString("User"), globalConfig.getString("Password"))) {
            return con;
        } catch (SQLException e) {
            System.out.println("[FJ] MYSQL ERR CONNECTION NULL" + e);
            return null;
        }
    }
    public static Connection getSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:plugins/HPlugin/HJoin/database.db";
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[FJ] SQLITE ERR CONNECTION NULL" + e);
            return null;
        }
    }
}
