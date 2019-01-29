package com.icw.rds;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.CreateDBInstanceRequest;
import com.amazonaws.services.rds.model.DBInstance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AwsTest {
    static AmazonDynamoDB dynamoDB;
    static AmazonRDS rdsDB;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        init();
        //connectAndCreateDynamoDbTables(123, "link", "some-value");

        //connectAndCreateRDSDbTables(123, "link", "some-value");
        CreateDbInstance.test();
    }

    public static void init(){
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

        try {
            credentialsProvider.getCredentials();
            System.out.println("AWS Authentication OK");
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();

        rdsDB = AmazonRDSClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_EAST_1)
                .build();


    }

    public static int connectAndCreateRDSDbTables(int linkId,String link,String uuid) throws Exception {
        init();
        int returnLinkId = 0;

        try {
            String tableName = "Britrail";

            // Create a table with a primary hash key named 'name', which holds a string
            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                    .withKeySchema(new KeySchemaElement().withAttributeName("uuid").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("uuid").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

            CreateDBInstanceRequest req = new CreateDBInstanceRequest()
                    .withDBInstanceIdentifier("test")
                    .withAllocatedStorage(2)
                    .withDBName("test")
                    .withAvailabilityZone(Regions.US_EAST_1.getName())
                    .withEngine("mysql")
                    .withMasterUsername("test")
                    .withMasterUserPassword("test1234");

            DBInstance dbInstance = rdsDB.createDBInstance(req);
            System.out.println(dbInstance.getDBInstanceStatus());

            // Describe our new table
//            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
//            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
//            System.out.println("Table Description: " + tableDescription);
//            // Add an item
//            Map<String, AttributeValue> item = newItem(linkId,link,uuid);
//            PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
//            PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
//            returnLinkId = 1;
            System.out.println("table created");

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw ace;

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("My exception in Dynamodb conn and create");
            throw ex;
        }

        return returnLinkId;
    }

    private static Map<String, AttributeValue> newItem(int linkId,String link,String uuid) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("linkId", new AttributeValue().withN(Integer.toString(linkId)));
        item.put("link", new AttributeValue(link));
        item.put("uuid", new AttributeValue(uuid));
        item.put("creationDate",new AttributeValue(LocalDateTime.now().format(formatter)));
        item.put("lastAccessedDate",new AttributeValue(LocalDateTime.now().format(formatter)));

        return item;
    }

    public static String[] updateDynamoDbTables(String uuid) throws Exception {
        init();
        String linkParams = null;
        try {
            String tableName = "Britrail";

            // Scan items
            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            // get only those with matching uuid and linkId >= 1
            Condition condition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ.toString())
                    .withAttributeValueList(new AttributeValue().withS(uuid));
            Condition condition2 = new Condition()
                    .withComparisonOperator(ComparisonOperator.GE.toString())
                    .withAttributeValueList(new AttributeValue().withN("1"));
            scanFilter.put("uuid", condition);
            scanFilter.put("linkId", condition2);
            ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
            ScanResult scanResult = dynamoDB.scan(scanRequest);
            if(scanResult.getCount()==1){

                // update the item with linkId +1
                String existingLinkId = scanResult.getItems().get(scanResult.getCount()-1).get("linkId").getN();
                int returnLinkId = Integer.parseInt(existingLinkId)+1;
                Map<String, AttributeValue> attributeValues = new HashMap<>();
                attributeValues.put(":linkId", new AttributeValue().withN(Integer.toString(returnLinkId)));
                attributeValues.put(":lastAccessedDate", new AttributeValue().withS(LocalDateTime.now().format(formatter)));

                UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                        .withTableName(tableName)
                        .addKeyEntry("uuid",new AttributeValue().withS(uuid))
                        .withUpdateExpression("set linkId = :linkId,lastAccessedDate = :lastAccessedDate")
                        .withExpressionAttributeValues(attributeValues);
                UpdateItemResult updateItemResult = dynamoDB.updateItem(updateItemRequest);
                linkParams = scanResult.getItems().get(scanResult.getCount()-1).get("link").getS();
            }
            else{
                System.out.println("No record to update ");
                throw new Exception("No record to update");
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            throw ase;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw ace;
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("My exception in Dynamodb update");
            throw ex;
        }
        return linkParams.split("&");
    }

    public static int connectAndCreateDynamoDbTables(int linkId,String link,String uuid) throws Exception {
        init();
        int returnLinkId = 0;

        try {
            String tableName = "Britrail";

            // Create a table with a primary hash key named 'name', which holds a string
            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName)
                    .withKeySchema(new KeySchemaElement().withAttributeName("uuid").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("uuid").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));

            // Create table if it does not exist yet
            TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
            // wait for the table to move into ACTIVE state
            TableUtils.waitUntilActive(dynamoDB, tableName);


            // Describe our new table
            DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
            TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
            System.out.println("Table Description: " + tableDescription);
            // Add an item
            Map<String, AttributeValue> item = newItem(linkId,link,uuid);
            PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
            PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
            returnLinkId = 1;
            System.out.println("table created");

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw ace;

        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("My exception in Dynamodb conn and create");
            throw ex;
        }

        return returnLinkId;
    }

    public String disableMpassLink(String uuid) throws  Exception{
        init();
        String linkId = null;
        try {
            String tableName = "Britrail";

            // Scan items
            HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
            // get only those with matching uuid and linkId >= 1
            Condition condition = new Condition()
                    .withComparisonOperator(ComparisonOperator.EQ.toString())
                    .withAttributeValueList(new AttributeValue().withS(uuid));

            scanFilter.put("uuid", condition);
            ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
            ScanResult scanResult = dynamoDB.scan(scanRequest);
            if(scanResult.getCount()==1){

                // update the item with linkId = 0
                Map<String, AttributeValue> attributeValues = new HashMap<>();
                attributeValues.put(":linkId", new AttributeValue().withN(Integer.toString(0)));
                attributeValues.put(":lastAccessedDate", new AttributeValue().withS(LocalDateTime.now().format(formatter)));

                UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                        .withTableName(tableName)
                        .addKeyEntry("uuid",new AttributeValue().withS(uuid))
                        .withUpdateExpression("set linkId = :linkId,lastAccessedDate = :lastAccessedDate")
                        .withExpressionAttributeValues(attributeValues);
                UpdateItemResult updateItemResult = dynamoDB.updateItem(updateItemRequest);
                linkId = scanResult.getItems().get(scanResult.getCount()-1).get("link").getS();
            }
            else{
                System.out.println("No record to update ");
                throw new Exception("No record to update");
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to AWS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            throw ase;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with AWS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw ace;
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("My exception in Dynamodb disable");
            throw ex;
        }
        return linkId;
    }
}
