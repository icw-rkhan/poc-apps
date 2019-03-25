package com.icw.rds;

        import java.sql.*;

class MysqlCon {
    public static void main(String args[]) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/test?useSSL=false", "root", "test1234");

            String getDBUSERByUserIdSql = "{call get_suffix(?)}";
            CallableStatement stmt = con.prepareCall(getDBUSERByUserIdSql);
            stmt = con.prepareCall(getDBUSERByUserIdSql);
            ((CallableStatement) stmt).registerOutParameter(1, Types.JAVA_OBJECT);

            stmt.executeUpdate();


            System.out.println(stmt.getString(1));

            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}