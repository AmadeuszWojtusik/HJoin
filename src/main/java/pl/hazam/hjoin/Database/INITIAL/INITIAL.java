package pl.hazam.hjoin.Database.INITIAL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class INITIAL {
    public boolean INITIALSQL(Connection connection) throws IOException, SQLException {
        // Otwieranie pliku ze skryptem SQL
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("First.sql");
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            StringBuilder query = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Sprawdzamy, czy linia jest pusta lub jest komentarzem.
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    query.append(line).append(" ");
                    // Jeśli linia kończy się średnikiem, wykonujemy zapytanie SQL
                    if (line.trim().endsWith(";")) {
                        String singleQuery = query.toString().trim();
                        // Wykonanie pojedynczego zapytania SQL
                        Statement statement = connection.createStatement();
                        statement.execute(singleQuery);
                        statement.close();
                        query.setLength(0);
                    }
                }
            }

            // Zamknięcie połączenia i zasobów
            reader.close();
        } catch (IOException e) {
            System.out.println("[FJ] INITIAL IO ERROR" + e);
            return false;
        } catch (SQLException e) {
            System.out.println("[FJ] INITIAL SQL ERROR" + e);
            return false;
        } catch (AssertionError e) {
            System.out.println("[FJ] INITIAL Assertion ERROR" + e);
            return false;
        } catch (NullPointerException e) {
            System.out.println("[FJ] INITIAL NULL POINTER ERROR" + e);
            return false;
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("[FJ] INITIAL CONNECTION CLOSE ERROR" + e);
            }
        }
        return true;
    }
}
