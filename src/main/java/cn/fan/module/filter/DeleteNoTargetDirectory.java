package cn.fan.module.filter;

import org.junit.Test;

import java.io.File;

/**
 * 删除没有target目录的文件
 */
public class DeleteNoTargetDirectory {

    /**
     *
     * @param directory
     */
    public static void delete(File directory){
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            boolean flag = false;
            for(File file:files){
                if(file.getName().equals("target")){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                for(File file:files){
                    file.delete();
                }
                directory.delete();
            }
        }
    }

    @Test
    public void test(){
        File file = new File("/home/inspur/zfy/java_repository/pomFileCollection");
        File[] files = file.listFiles();
        for(File tmp :files){
            delete(tmp);
        }
    }
}
