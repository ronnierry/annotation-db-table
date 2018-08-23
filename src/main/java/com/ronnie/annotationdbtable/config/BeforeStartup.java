package com.ronnie.annotationdbtable.config;

import com.ronnie.annotationdbtable.jpa.Jpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: rongyu
 * @CreateDate: 2018/8/23$ 9:41$
 * @Remark:
 */
@Slf4j
@Configuration
public class BeforeStartup implements ApplicationListener<ContextRefreshedEvent> {
    List<String> classPaths = new ArrayList();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String property = System.getProperty("user.dir");
        File file = new File(property);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(
                    file1 -> {

                    }
            );
        }

        //String path = getClass().getResource("/").getPath();
        log.info("BeforeStartup!");

        //包名
        String basePack = "com.ronnie.annotationdbtable.entity";
        //先把包名转换为路径,首先得到项目的classpath
        String classpath = getClass().getResource("/").getPath();
        //然后把我们的包名basPach转换为路径名
        basePack = basePack.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePack;
        doPath(new File(searchPath));
        //这个时候我们已经得到了指定包下所有的类的绝对路径了。我们现在利用这些绝对路径和java的反射机制得到他们的类对象
        for (String s : classPaths) {
            //把 D:\work\code\20170401\search-class\target\classes\com\baibin\search\a\A.class 这样的绝对路径转换为全类名com.baibin.search.a.A
            String s1 = classpath.replace("/", "\\").replaceFirst("\\\\", "");
            String replace = s.replace(s1, "")
                    .replace("\\", ".")
                    .replace(".class", "");
            Class cls = null;
            try {
                cls = Class.forName(replace);
                Jpa.DDL(cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(cls);
        } // end of for
        Jpa.createTable();
    }


    /**
     * 该方法会得到所有的类，将类的绝对路径写入到classPaths中
     *
     * @param file
     */
    private void doPath(File file) {
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }
}
