package murach.dao;

import java.sql.*;
import murach.data.ConnectionPool;
import murach.sql.SQLUtil;

public class SQLDAO {

    public String executeSql(String sql) {
        String result = "";

        if (sql == null || sql.trim().isEmpty()) {
            return "<p>No SQL statement provided.</p>";
        }

        sql = sql.trim();
        ConnectionPool pool = ConnectionPool.getInstance();
        try (Connection conn = pool.getConnection();
             Statement stmt = conn.createStatement()) {

            if (sql.toLowerCase().startsWith("select")) {

                // SELECT → trả bảng HTML
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    result = SQLUtil.getHtmlTable(rs);
                }

            } else {

                // INSERT / UPDATE / DELETE / DDL
                int affected = stmt.executeUpdate(sql);

                if (affected == 0) {
                    result = "<p>The statement executed successfully.</p>";
                } else {
                    result = "<p>The statement executed successfully.<br>"
                            + affected + " row(s) affected.</p>";
                }
            }

        } catch (SQLException ex) {
            result = "<p>Error executing SQL:<br>" + ex.getMessage() + "</p>";
        }

        return result;
    }
}