package com.collager.trillo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;


public class FileUtil {

  static public String getNameWithoutExtension(String fileName) {
    return FilenameUtils.removeExtension(fileName);
  }
  
  static public String getFileExtension(String fileName) {
    return FilenameUtils.getExtension(fileName);
  }
  
  static public void copyFile(File in, File out) {
    try {
      InputStream fis = new BufferedInputStream(new FileInputStream(in));
      OutputStream fos = new BufferedOutputStream(new FileOutputStream(out));
      byte[] buf = new byte[1024];
      int i = 0;
      while ((i = fis.read(buf)) != -1) {
        fos.write(buf, 0, i);
      }
      fis.close();
      fos.close();
    } catch (Exception exc) {
      throw new RuntimeException("Failed to copy file : " + in.getName());
    }
  }

  static public void copyFile(InputStream inputStream, File outFile) {
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

      FileOutputStream fw = new FileOutputStream(outFile);
      OutputStreamWriter os = new OutputStreamWriter(fw, "UTF-8");
      BufferedWriter bw = new BufferedWriter(os);

      String s;
      while ((s = reader.readLine()) != null) {
        s = s + "\n";
        bw.write(s, 0, s.length());
      }
      reader.close();
      bw.close();
    } catch (Exception exc) {
      throw new RuntimeException("Failed to stream");
    }
  }

  static public void renameTo(File fromFile, File toFile) {
    if (!fromFile.renameTo(toFile)) {
      copyFile(fromFile, toFile);
      fromFile.delete();
    }
  }

  static public StringBuilder readFile(File file) {
    return readFile(file, -1);
  }

  static public StringBuilder readFile(File file, int numberOfLines) {

    StringBuilder buf = new StringBuilder();
    FileInputStream fr = null;
    InputStreamReader is = null;
    BufferedReader br = null;

    try {
      fr = new FileInputStream(file);
      is = new InputStreamReader(fr, "UTF-8");
      br = new BufferedReader(is);

      String s;
      int n = 0;
      while ((s = br.readLine()) != null) {
        buf.append(s + "\n");
        n++;
        if (numberOfLines != -1 && n >= numberOfLines) {
          break;
        }
      }
    } catch (Exception exc) {
      throw new RuntimeException(
          "Failed to read file: " + file.getName() + ", Error: " + exc.getMessage());
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception exc) {
        }
      }
    }
    return buf;
  }

  static public void writeFile(File file, String buf) {
    FileOutputStream fw = null;
    OutputStreamWriter os = null;
    BufferedWriter bw = null;
    try {
      fw = new FileOutputStream(file);
      os = new OutputStreamWriter(fw, "UTF-8");
      bw = new BufferedWriter(os);
      bw.write(buf.toString(), 0, buf.length());

    } catch (Exception exc) {
      throw new RuntimeException(
          "Failed to write file: " + file.getName() + ", Error: " + exc.getMessage());
    } finally {
      if (bw != null)
        try {
          bw.close();
        } catch (Exception exc) {
        } ;
      if (os != null)
        try {
          os.close();
        } catch (Exception exc) {
        } ;
      if (fw != null)
        try {
          fw.close();
        } catch (Exception exc) {
        } ;
    }
  }

  static public void writeFile(File file, byte[] bytes) {
    try {
      Path path = Paths.get(file.getAbsolutePath());
      Files.write(path, bytes);
    } catch (Exception exc) {
      throw new RuntimeException(
          "Failed to write file: " + file.getName() + ", Error: " + exc.getMessage());
    }
  }

  static public void copyTextFile(File file1, File file2) throws IOException {
    FileInputStream fr = null;
    InputStreamReader is = null;
    BufferedReader br = null;
    FileOutputStream fw = null;
    OutputStreamWriter os = null;
    BufferedWriter bw = null;

    try {
      fr = new FileInputStream(file1);
      is = new InputStreamReader(fr, "UTF-8");
      br = new BufferedReader(is);

      fw = new FileOutputStream(file2);
      os = new OutputStreamWriter(fw, "UTF-8");
      bw = new BufferedWriter(os);


      String s;
      while ((s = br.readLine()) != null) {
        s = s + "\n";
        bw.write(s, 0, s.length());
      }
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception exc) {
        }
      }

      if (bw != null) {
        try {
          bw.close();
        } catch (Exception exc) {
        } ;
      }
    }
  }

  static public long getFileTimestamp(File f) {
    return f.lastModified();
  }

  static public boolean isFileModifiedBefore(File f, long ts) {
    return getFileTimestamp(f) < ts;
  }

  static public boolean isFileModifiedBeforeDelta(File f, long ts, long delta) {
    return (ts - getFileTimestamp(f)) > delta;
  }

  static public BufferedReader getFileBufferedReader(File file) {
    FileInputStream fr = null;
    InputStreamReader is = null;
    BufferedReader br = null;
    try {
      fr = new FileInputStream(file);
      is = new InputStreamReader(fr, "UTF-8");
      br = new BufferedReader(is);

    } catch (Exception e) {
      if (br != null) {
        try {
          br.close();
        } catch (Exception exc) {
        }
      }
    }
    return br;
  }

  public static String getUniqueFileName(String dirName, String fileNamePrefix, String ext) {
    if (ext == null)
      ext = "";
    if (ext.length() > 0 && !ext.startsWith(".")) {
      ext = "." + ext;
    }
    int n = 1;

    while (true) {
      File file = new File(dirName, fileNamePrefix + "-" + n + ext);
      if (!file.exists()) {
        break;
      }
      n++;
    }
    return fileNamePrefix + "-" + n + ext;
  }
}
