package com.pjcstudio.pjcstudio.checksms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.util.Log;

public class ZipUtils {

    private static final int COMPRESSION_LEVEL = 8;

    private static final int BUFFER_SIZE = 1024 * 2;


    public static void zip(String sourcePath, String output) throws Exception {

        // ���� ���(sourcePath)�� ���丮�� ������ �ƴϸ� �����Ѵ�.
        File sourceFile = new File(sourcePath);
        if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
            throw new Exception("���� ����� ������ ã�� ���� �����ϴ�.");
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(output); 
            bos = new BufferedOutputStream(fos); 
            zos = new ZipOutputStream(bos); 
            zos.setLevel(COMPRESSION_LEVEL); // ���� ���� - �ִ� ������� 9, ����Ʈ 8
            
            zipEntry(sourceFile, sourcePath, zos); // Zip ���� ����
            zos.finish(); // ZipOutputStream finish
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { 
                return;
            }
            File[] fileArray = sourceFile.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos);
            }
        } else { // sourcehFile �� ���丮�� �ƴ� ���
            BufferedInputStream bis = null;
            
            try {
                String sFilePath = sourceFile.getPath();
                Log.i("aa", sFilePath);
                //String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
                StringTokenizer tok = new StringTokenizer(sFilePath,"/");
    			
    			int tok_len = tok.countTokens();
    			String zipEntryName=tok.toString();
    			while(tok_len != 0){
    				tok_len--;
    				zipEntryName = tok.nextToken();
    			}
                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                
                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;
                
                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }

    public static void unzip(String zipFile, String targetDir, boolean fileNameToLowerCase) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;

        try {
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(fis); 

            while ((zentry = zis.getNextEntry()) != null) {
                String fileNameToUnzip = zentry.getName();
                if (fileNameToLowerCase) { // fileName toLowerCase
                    fileNameToUnzip = fileNameToUnzip.toLowerCase();
                }

                File targetFile = new File(targetDir, fileNameToUnzip);

                if (zentry.isDirectory()) {// Directory �� ���
                    //FileUtils.makeDir(targetFile.getAbsolutePath()); // ���丮 ����
                    File path = new File(targetFile.getAbsolutePath());
                    path.mkdirs(); 
                } else { // File �� ���
                    File path = new File(targetFile.getParent());
                    path.mkdirs(); 
                    unzipEntry(zis, targetFile);
                }
            }
        } finally {
            if (zis != null) {
                zis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }


    protected static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }
}