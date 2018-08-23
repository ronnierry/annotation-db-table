package com.ronnie.annotationdbtable.jpa;

import com.ronnie.annotationdbtable.annotation.Column;
import com.ronnie.annotationdbtable.annotation.Id;
import com.ronnie.annotationdbtable.annotation.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

/**
 * @Description:
 * @Author: rongyu
 * @CreateDate: 2018/8/22$ 16:23$
 * @Remark:
 */
@Slf4j
public class Jpa {
    static LinkedList<String> DDLs = new LinkedList();

    public static void DDL(Class<?> cl) {
        Table table = cl.getAnnotation(Table.class);
        if (table != null) {
            StringBuffer create_table = new StringBuffer("CREATE TABLE ");
            String tableName = table.name();
            if (StringUtils.isEmpty(tableName)) {
                tableName = cl.getName().toLowerCase();
            }
            create_table.append(tableName);
            create_table.append("(");
            Field[] fields = cl.getDeclaredFields();
            if (fields.length > 0) {
                String keyDDl = null;
                for (Field field : fields) {
                    Annotation[] annotations = field.getDeclaredAnnotations();
                    if (null != annotations && 0 < annotations.length) {
                        for (Annotation annotation : annotations) {
                            if (annotation instanceof Id) {
                                if (null == keyDDl) {
                                    String name = field.getName();
                                    keyDDl = "PRIMARY KEY (`" + name + "`)";
                                    create_table.append("`" + name + "` bigint(20) NOT NULL AUTO_INCREMENT,");
                                } else {
                                    throw new IllegalArgumentException("to many key for DDL!");
                                }
                            } else if (annotation instanceof Column) {
                                Column column = (Column) annotation;
                                String name = column.name();
                                String definition = column.columnDefinition();
                                create_table.append("`" + name + "`" + definition + ",");
                            }
                        }
                    }
                }
                if (!StringUtils.isEmpty(keyDDl)) {
                    create_table.append(keyDDl);
                } else {
                    throw new IllegalArgumentException("no key for DDL!");
                }
                create_table.append(") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;");
                String string = create_table.toString();
                DDLs.add(string);
                log.info(string);
            }

        }
    }

    public static void createTable() {
        if (0 < DDLs.size()) {


            // JDBC driver name and database URL
            final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            final String DB_URL = "jdbc:mysql://192.168.1.12:3306/annotation?characterEncoding=utf-8";
            //  Database credentials
            final String USER = "root";
            final String PASS = "lcex123";
            Connection conn = null;
            Statement stmt = null;
            try {
                //STEP 2: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                //STEP 4: Execute a query
                System.out.println("Creating statement...");
                stmt = conn.createStatement();

                Statement finalStmt = stmt;
                DDLs.forEach(
                        DDL -> {
                            try {
                                finalStmt.execute(DDL);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
                //STEP 6: Clean-up environment
                stmt.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                }// nothing we can do
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }//end finally try
            }//end try
            System.out.println("There are so thing wrong!");
        }
    }
}
