package cn.fan.module.filter;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TranformeSourceAndJar {
    /**
     *
     * @param sourceDirectory "/home/inspur/zfy/java_repository/prior_repository"
     * @param newDirectory "/media/inspur/TOSHIBA EXT/jarAndSource/"
     * @param filterFileNames 需要找到的目标文件
     */
    public static void tranformSource(File sourceDirectory,File newDirectory,String[] filterFileNames) throws IOException {
        File[] files = sourceDirectory.listFiles();
        HashMap<String,String> projectToFile = new HashMap<>();
        for(File projectFile:files){
            if(projectFile.isDirectory()){
                projectToFile.put(projectFile.getName(),projectFile.getAbsolutePath());
                findProject(projectFile,projectToFile,1);
            }
        }
        for(String str:filterFileNames){
            System.out.println("开始处理source项目:"+str);
            File file = new File(projectToFile.get(str));
            if(file!=null) {
                if (file.isDirectory()) {
                    // "/media/inspur/TOSHIBA EXT/jarAndSource/"+file[i].name
                    transformJar(file, new File(newDirectory + File.separator + str));
                }
            }else{
                System.out.println("没有找到项目"+str);
            }
        }
    }

    public static void findProject(File directory,HashMap<String,String> projectToFile,int n){
        File[] files = directory.listFiles();
        if(n>3){
            return;
        }
        for(File file:files){
            if(file.isDirectory()){
                projectToFile.put(file.getName(),file.getAbsolutePath());
                findProject(file,projectToFile,n+1);
            }
        }
    }

    /**
     *
     * @param jarDirectory "/home/inspur/zfy/java_repository/pomFileCollection"
     * @param newDirectory "/media/inspur/TOSHIBA EXT/jarAndSource"
     */
    public static void transformJar(File jarDirectory, File newDirectory) throws IOException {
        File[] files = jarDirectory.listFiles();
        for(File projectFile:files){
            System.out.println("开始处理jar文件:"+projectFile.getAbsolutePath());
            //"/media/inspur/TOSHIBA EXT/jarAndSource/"+file[i].name
            if(projectFile.isDirectory()) {
                File aimDirectory = new File(newDirectory.getAbsolutePath()+File.separator+projectFile.getName());
                if(!aimDirectory.exists()) {
                    aimDirectory.mkdir();
                }
                transformJar(projectFile,aimDirectory);
            }
            if(projectFile.isFile()){
                File aimFile = new File(newDirectory.getAbsolutePath()+File.separator+projectFile.getName());
                if(!aimFile.exists()) {
                    aimFile.createNewFile();
                }
                transformFile(projectFile,aimFile);
            }
        }
    }

    private static void transformFile(File oldFile,File newFile){
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(oldFile);
            fileOutputStream = new FileOutputStream(newFile);
            int hasRead = 0;
            byte[] data = new byte[1024];
            while ((hasRead = fileInputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, hasRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test(){
        try {
            transformJar(new File("/home/inspur/zfy/java_repository/pomFileCollection"),new File("/home/inspur/zfy/java_repository/JarAndSource"));
            File file = new File("/home/inspur/zfy/java_repository/pomFileCollection");
            File[] files = file.listFiles();
            ArrayList<String> filterNames = new ArrayList<>();
            for(File directory:files){
                filterNames.add(directory.getName());
            }
            String[] data = new String[filterNames.size()];
            filterNames.toArray(data);
            tranformSource(new File("/home/inspur/zfy/java_repository/prior_repository"),new File("/home/inspur/zfy/java_repository/JarAndSource"),data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
