//package com.icw.rds;
//
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.rds.AmazonRDS;
// import com.amazonaws.services.rds.AmazonRDSClientBuilder;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.*;
//import java.util.Properties;
//import java.util.UUID;
//import java.util.logging.Logger;
//
//public class RDSConnectionTest {
//    final static Logger logger = Logger.getLogger(AWSRDSService.class.getName());
//    private AWSCredentialsProvider credentials;
//    private AmazonRDS amazonRDS;
//    private String db_username;
//    private String db_password;
//    private String db_database;
//    private String db_hostname;
//
//    /*
//     * User access key and secret key must be set before execute any operation.
//     * Follow the link on how to get the user access and secret key
//     * https://aws.amazon.com/blogs/security/wheres-my-secret-access-key/
//     * **/
//    public RDSConnectionTest() throws IOException {
//        //Init RDS client with credentials and region.
//        Properties prop = new Properties();
//        prop.load(AWSRDSService.class.getClassLoader().getResourceAsStream("db.properties"));
//
//        credentials = new
//                AWSStaticCredentialsProvider(
//                new BasicAWSCredentials(prop.getProperty("access_key"), prop.getProperty("secret_key")));
//        amazonRDS = AmazonRDSClientBuilder.standard().withCredentials(credentials)
//                .withRegion(Regions.US_EAST_1).build();
//
//        db_username = prop.getProperty("db_username");
//        db_password = prop.getProperty("db_password");
//        db_database = prop.getProperty("db_database");
//    }
//
//    public RDSConnectionTest(AmazonRDS amazonRDS) {
//        this.amazonRDS = amazonRDS;
//    }
//
//    public void runJdbcTests() throws SQLException, IOException {
//        // Getting database properties from db.properties
//        Properties prop = new Properties();
//        InputStream input = AWSRDSService.class.getClassLoader().getResourceAsStream("db.properties");
//        prop.load(input);
//        db_hostname = prop.getProperty("db_hostname");
//        String jdbc_url = "jdbc:mysql://" + db_hostname + ":3306/" + db_database;
//
//        DriverManager.setLoginTimeout(60);
//        // Create a connection using the JDBC driver
//        try (Connection conn = DriverManager.getConnection(jdbc_url, db_username, db_password)) {
//
//            // Create the test table if not exists
//            Statement statement = conn.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS jdbc_test (id SERIAL PRIMARY KEY, content VARCHAR(80))";
//            statement.executeUpdate(sql);
//
//            // Do some INSERT
//            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO jdbc_test (content) VALUES (?)");
//            String content = "" + UUID.randomUUID();
//            preparedStatement.setString(1, content);
//            preparedStatement.executeUpdate();
//            logger.info("INSERT: " + content);
//
//            // Do some SELECT
//            sql = "SELECT  * FROM jdbc_test";
//            ResultSet resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
//                String count = resultSet.getString("content");
//                logger.info("Total Records: " + count);
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException, SQLException {
//        AWSRDSService awsrdsService = new AWSRDSService();
//        awsrdsService.runJdbcTests();
//    }
//}