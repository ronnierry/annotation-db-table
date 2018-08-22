package com.ronnie.annotationdbtable;

import com.ronnie.annotationdbtable.annotation.Column;
import com.ronnie.annotationdbtable.annotation.Id;
import com.ronnie.annotationdbtable.annotation.Table;
import com.ronnie.annotationdbtable.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AnnotationDbTableApplicationTests {

    @Test
    public void contextLoads() throws ClassNotFoundException {
        Class<?> cl = Class.forName(Member.class.getName());
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
                if (!StringUtils.isEmpty(keyDDl)){
                    create_table.append(keyDDl);
                }else {
                    throw new IllegalArgumentException("no key for DDL!");
                }
                create_table.append(") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;");
                String string = create_table.toString();
                log.info(string);
            }

        }
    }

}
