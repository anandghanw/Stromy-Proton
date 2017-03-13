package file;

import java.io.*;

/**
 * Created by Anandghan W on 3/13/17.
 */

/*
filePath should be absolute path ending with '/'
example = '/Users/xyz/project/'
 */

public class FileUtils {

    private static void setupFile(String filePath, String fileName){
        try {
            new File(filePath).mkdirs();
            new File(filePath +fileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void emptyFile(String filePath, String fileName){
        try {
            PrintWriter writer = new PrintWriter(filePath+fileName);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String filePath, String fileName, String line){
        setupFile(filePath,fileName);
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(filePath+fileName,true);
            bw = new BufferedWriter(fw);
            bw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
