package pl.hazam.hjoin.Database.DbUtil;

import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    public static Connection getConnection (YamlConfiguration globalConfig){
        try (Connection con = DriverManager.getConnection("jdbc:mysql://" + globalConfig.getString("server") + ":" + globalConfig.getString("port") + "/" + globalConfig.getString("database") + "?useSSL=" + globalConfig.getString("ssl") + "&serverTimezone=" + globalConfig.getString("timezone"), globalConfig.getString("user"), globalConfig.getString("password"))) {
            return con;
        } catch (SQLException e) {
            System.out.println("[FJ] MYSQL ERR CONNECTION NULL" + e);
            return null;
        }
    }
    public static Connection getSQLiteConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:database.db";
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            System.out.println("[FJ] SQLITE ERR CONNECTION NULL" + e);
            return null;
        }
    }
}
