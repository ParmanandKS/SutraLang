package com.sutralang.ide.utils;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtils - Manages file operations for .sutra files
 * Handles saving, loading, deleting, and listing .sutra files
 */
public class FileUtils {
    private static final String FILE_EXTENSION = ".sutra";
    private static final String SUTRA_DIR = "SutraFiles";
    
    /**
     * Get the directory for storing .sutra files
     */
    private static File getSutraDirectory(Context context) {
        File sutraDir = new File(context.getFilesDir(), SUTRA_DIR);
        if (!sutraDir.exists()) {
            sutraDir.mkdirs();
        }
        return sutraDir;
    }
    
    /**
     * Save code to a .sutra file
     */
    public static boolean saveFile(Context context, String filename, String content) {
        try {
            // Add extension if not present
            if (!filename.endsWith(FILE_EXTENSION)) {
                filename += FILE_EXTENSION;
            }
            
            File file = new File(getSutraDirectory(context), filename);
            
            // Write content to file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Read code from a .sutra file
     */
    public static String readFile(Context context, String filename) {
        try {
            // Add extension if not present
            if (!filename.endsWith(FILE_EXTENSION)) {
                filename += FILE_EXTENSION;
            }
            
            File file = new File(getSutraDirectory(context), filename);
            if (!file.exists()) {
                return null;
            }
            
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fis.read(buffer);
            fis.close();
            
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * List all .sutra files in the directory
     */
    public static List<String> listFiles(Context context) {
        List<String> files = new ArrayList<>();
        File directory = getSutraDirectory(context);
        
        if (directory.exists() && directory.isDirectory()) {
            File[] fileList = directory.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile() && file.getName().endsWith(FILE_EXTENSION)) {
                        files.add(file.getName());
                    }
                }
            }
        }
        
        return files;
    }
    
    /**
     * Delete a .sutra file
     */
    public static boolean deleteFile(Context context, String filename) {
        try {
            // Add extension if not present
            if (!filename.endsWith(FILE_EXTENSION)) {
                filename += FILE_EXTENSION;
            }
            
            File file = new File(getSutraDirectory(context), filename);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Rename a .sutra file
     */
    public static boolean renameFile(Context context, String oldName, String newName) {
        try {
            // Add extension if not present
            if (!oldName.endsWith(FILE_EXTENSION)) {
                oldName += FILE_EXTENSION;
            }
            if (!newName.endsWith(FILE_EXTENSION)) {
                newName += FILE_EXTENSION;
            }
            
            File oldFile = new File(getSutraDirectory(context), oldName);
            File newFile = new File(getSutraDirectory(context), newName);
            
            if (oldFile.exists()) {
                return oldFile.renameTo(newFile);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a file exists
     */
    public static boolean fileExists(Context context, String filename) {
        try {
            if (!filename.endsWith(FILE_EXTENSION)) {
                filename += FILE_EXTENSION;
            }
            
            File file = new File(getSutraDirectory(context), filename);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
