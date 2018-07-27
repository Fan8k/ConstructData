package cn.fan.module.filter;

import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * 从整个java源文件库中提取pom文件
 */
public class ExtractPom {
    private static HashMap<String,String> projectToPom = new HashMap<>();
    private static String[] values = {"cn.fan","ConstructData","2.0"};
    private static String path = "/home/inspur/zfy/java_repository/pomFileCollection/";

    public static void AccessDirectory(File directory) throws IOException, DocumentException {
        boolean[] flag = new boolean[3];
        File pom = null;
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            for(File file:files){
                if(file.isDirectory()&&file.getName().equals("src")) {
                   flag[0] = true;
                }
                //
                if(file.isFile()&&file.getName().equals("pom.xml")){
                   flag[1] = true;
                   pom = file;
                }
                if(file.isFile()&&file.getName().equals("gradle.properties")){
                    flag[2] = true;
                }
            }
            //开始判断是否需要回退
            if(flag[1]){
               //建立项目名字目录
                File pathNew = new File(path+"/"+pom.getParent().substring(pom.getParent().lastIndexOf('/')));
                pathNew.mkdir();
               String fileName = pathNew.getAbsolutePath()+"/pom.xml";
               boolean succ = ParserPomXml.oneStopAddAndChange(values, pom.getPath(), fileName);
               if(succ) {
                   //路径和pom文件对应起来
                   projectToPom.put(directory.getAbsolutePath(), fileName);
               }
               return;
            }
            if(flag[0]||flag[2]){
                return;
            }
            for(File file:files){
                if(file.isDirectory()){
                    //递归
                    System.out.println("进入"+file.getPath()+"中处理");
                    AccessDirectory(file);
                    System.out.println(file.getPath()+"处理完毕");
                }
            }
        }
    }

    public static HashMap<String, String> getProjectToPom() {
        return projectToPom;
    }
}
