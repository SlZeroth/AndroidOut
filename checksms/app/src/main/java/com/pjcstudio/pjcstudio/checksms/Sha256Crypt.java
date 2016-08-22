package com.pjcstudio.pjcstudio.checksms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.format.Formatter;
import android.util.Log;

public class Sha256Crypt {
	
	public static String extractFileHashSHA256(String filename) throws Exception {
        
        String SHA = ""; 
        int buff = 16384;
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");
 
            MessageDigest hashSum = MessageDigest.getInstance("SHA-256");
 
            byte[] buffer = new byte[buff];
            byte[] partialHash = null;
 
            long read = 0;
 
            // calculate the hash of the hole file for the test
            long offset = file.length();
            int unitsize;
            while (read < offset) {
                unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
                file.read(buffer, 0, unitsize);
 
                hashSum.update(buffer, 0, unitsize);
 
                read += unitsize;
            }
 
            file.close();
            partialHash = new byte[hashSum.getDigestLength()];
            partialHash = hashSum.digest();
             
            StringBuffer sb = new StringBuffer(); 
            for(int i = 0 ; i < partialHash.length ; i++){
                sb.append(Integer.toString((partialHash[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return SHA;
    }
}