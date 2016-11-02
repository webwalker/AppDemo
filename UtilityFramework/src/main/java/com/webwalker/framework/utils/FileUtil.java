package com.webwalker.framework.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

/**
 * 文件操作工具类
 *
 * @author webwalker
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    public static final String FILE_SEPARATOR = "/";
    public final static String TEMP_SUFFIX = ".temp";
    private static final int BYTE_BUFFER_SIZE = 1024 * 64;

    public static final int FILE_SUCCESS_WITH_WRITE_FILE_EXISTED = 0x01;
    public static final int FILE_SUCCESS = 0x00;
    public static final int FILE_ERROR_READ_PARAM = (0 - 0x01);
    public static final int FILE_ERROR_WRITE_PARAM = (0 - 0x02);
    public static final int FILE_ERROR_READ_FILE_NOT_FOUND = (0 - 0x03);
    public static final int FILE_ERROR_WRITE_FILE_NOT_FOUND = (0 - 0x04);
    public static final int FILE_ERROR_READ_IO_EXCEPTION = (0 - 0x05);
    public static final int FILE_ERROR_WRITE_IO_EXCEPTION = (0 - 0x06);
    public static final int FILE_ERROR_CREATE_DIR = (0 - 0x10);
    public static final int FILE_ERROR_NOT_ENOUGH_SPACE = (0 - 0x20);
    public static final int FILE_ERROR_UNKNOWN = (0 - 0xFF);

    private final static Pattern fileSuffixPattern = Pattern.compile("\\.\\w+");

    public static void deleteDir(String path, boolean bDelRoot) {
        try {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] tmp = dir.listFiles();
                if (null != tmp) {
                    for (int i = 0; i < tmp.length; i++) {
                        if (tmp[i].isDirectory()) {
                            deleteDir(path + "/" + tmp[i].getName(), true);
                        } else {
                            tmp[i].delete();
                        }
                    }
                }
                if (bDelRoot) {
                    dir.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedOutputStream getOutputStream(File file)
            throws IOException {
        return new BufferedOutputStream(new FileOutputStream(file), 64 * 1024);
    }

    public static BufferedOutputStream getOutputStream(String file)
            throws IOException {
        return getOutputStream(new File(file));
    }

    public static void close(Closeable in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            in = null;
        }
    }

    public static boolean createFileIfNotExist(String filePath) {
        boolean ret = false;
        File file = new File(filePath);
        if (!file.isFile()) {
            try {
                createDirIfNotExist(file);
                ret = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static boolean createDirIfNotExist(File file) {
        boolean ret = true;
        if (null == file) {
            ret = false;
            return ret;
        }
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                ret = parentFile.mkdirs();
                // chmod777(parentFile, null);
            }
        }
        return ret;
    }

    public static boolean createDirIfNotExist(String filePath) {
        File file = new File(filePath);
        return createDirIfNotExist(file);
    }

    public static boolean dirExisted(String path) {
        boolean ret = false;
        File file = new File(path);
        if (file.isDirectory() && file.exists()) {
            ret = true;
        }
        return ret;
    }

    public static boolean fileExisted(String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists();

    }

    public static boolean fileExisted(String filePath, boolean hasContent) {
        boolean ret = false;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            ret = !(hasContent && (file.length() <= 0));
        }
        return ret;
    }

    public static void writeFile(String str, String descFile, boolean append) {
        if ((null == str) || (null == descFile)) {
            return;
        }

        createDirIfNotExist(descFile);

        BufferedOutputStream out = null;

        try {
            byte[] src = str.getBytes("UTF-8");
            out = new BufferedOutputStream(new FileOutputStream(descFile,
                    append), 1024 * 64);
            out.write(src);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
    }

    public static void appendFile(String str, String descFile) throws Exception {
        writeFile(str, descFile, true);
    }

    public static String getTempName(String file) {
        return file + TEMP_SUFFIX;
    }

    public static int randomWriteBigFile(InputStream inputStream,
                                         String descFile, long offset) {
        Loggers.d(TAG, "enter randomWriteBigFile(" + inputStream + ", "
                + descFile + ", " + offset + ")");
        int ret = FILE_SUCCESS;
        RandomAccessFile random = null;

        try {
            do {
                if (null == inputStream) {
                    ret = FILE_ERROR_READ_PARAM;
                    Loggers.d(TAG, "error param inputStream is null.");
                    break;
                }
                if (StringUtil.isNull(descFile)) {
                    ret = FILE_ERROR_WRITE_PARAM;
                    Loggers.d(TAG, "error param descFile=" + descFile);
                    break;
                }

                createDirIfNotExist(descFile);
                File tempFile = new File(descFile + TEMP_SUFFIX);
                try {
                    random = new RandomAccessFile(tempFile, "rwd");
                    random.seek(offset);
                } catch (FileNotFoundException e) {
                    ret = FILE_ERROR_WRITE_FILE_NOT_FOUND;
                    break;
                } catch (IOException e) {
                    ret = FILE_ERROR_WRITE_IO_EXCEPTION;
                    break;
                }
                byte[] buffer = new byte[BYTE_BUFFER_SIZE];
                int count = -1;
                try {
                    while ((count = inputStream.read(buffer)) != -1) {
                        try {
                            random.write(buffer, 0, count);
                        } catch (IOException e) {
                            ret = FILE_ERROR_WRITE_IO_EXCEPTION;
                            break;
                        }
                    }
                } catch (IOException e) {
                    ret = FILE_ERROR_READ_IO_EXCEPTION;
                    break;
                }
            } while (false);
        } catch (Throwable e) {
            e.printStackTrace();
            ret = FILE_ERROR_UNKNOWN;
        } finally {
            close(random);
        }

        return ret;
    }

    public static void chmod777(File file, String root) {
        try {
            if (null == file || !file.exists()) {
                Loggers.d(TAG, "chmod777 param error. file=" + file);
                return;
            }

            Runtime.getRuntime().exec("chmod 777 " + file.getAbsolutePath());
            File tempFile = file.getParentFile();
            String tempName = tempFile.getName();
            if (tempFile.getName() == null || "".equals(tempName)) {
                Loggers.d(TAG, "chmod777 to the root directory. return");
                return;
            } else if (!StringUtil.isNull(root) && root.equals(tempName)) {
                Runtime.getRuntime().exec(
                        "chmod 777 " + tempFile.getAbsolutePath());
                Loggers.d(TAG, "chmod777 match return. root=" + root);
                return;
            }
            chmod777(file.getParentFile(), root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void chmod777(String file, String root) {
        Loggers.d(TAG, "enter chmod777 : " + file + ", " + root);
        chmod777(new File(file), root);
    }

    public static void clearDir(File file, String[] exceptSuffix) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File tempFile : files) {
                    deleteFileEx(tempFile, exceptSuffix);
                }
            }
        } else {
            Loggers.d(TAG, "Is not a valid directory!" + file.getAbsolutePath());
        }
    }

    private static void deleteFileEx(File file, String[] exceptSuffix) {
        if (file.isFile()) {
            boolean del = true;
            if (null != exceptSuffix) {
                for (String suffix : exceptSuffix) {
                    if ((null != suffix) && (file.getName().endsWith(suffix))) {
                        del = false;
                        break;
                    }
                }
            }
            if (del) {
                Loggers.d(TAG, "delete file : " + file.getAbsolutePath());
                file.delete();
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                file.delete();
            } else {
                for (File tempFile : files) {
                    deleteFileEx(tempFile, exceptSuffix);
                }
                file.delete();
            }
        }
    }

    /**
     * 解压文件
     * <p/>
     * 如解压文件存放路径不是有效目录，则解压后文件存放在待解压文件同级目录里
     *
     * @param srcFile  待解压文件
     * @param destFile 解压文件存放路径
     * @throws Exception
     */
    public static void decompress(File srcFile, File destFile) throws Exception {

        if (null == srcFile || !srcFile.isFile()) {
            return;
        }

        if (null == destFile || !destFile.isDirectory()) {

            destFile = srcFile.getParentFile();
        }

        ZipInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new ZipInputStream(new FileInputStream(srcFile));
            ZipEntry entry = null;
            byte[] b = new byte[BYTE_BUFFER_SIZE];
            int len = -1;

            while (null != (entry = in.getNextEntry())) {
                File file = new File(destFile.getAbsolutePath()
                        + FILE_SEPARATOR + entry.getName());

                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                if (!file.getParentFile().isDirectory()) {
                    file.getParentFile().mkdirs();
                }
                out = new BufferedOutputStream(new FileOutputStream(file));
                while (-1 != (len = in.read(b))) {
                    out.write(b, 0, len);
                }
                out.close();
                out = null;
            }

        } finally {
            close(out);
            close(in);
        }
    }

    public static String getFileNameUrl(String url) {
        Pattern pattern = Pattern.compile("([^\\\\/]+\\.\\w+)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getFileName(String url) {
        File file = new File(url);
        return file.getName();
    }

    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameByPath(String filePath) {
        if (StringUtil.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    public static String getUrlSuffix(String url) {
        String ret = "";
        String fileName = getFileName(url);
        ret = fileName.substring(fileName.lastIndexOf(".") + 1);
        return ret;
    }

    public static String getFileSuffix(String path) {
        String ret = "";
        if (TextUtils.isEmpty(path))
            return ret;
        if (path.indexOf(".") > -1)
            ret = "." + path.substring(path.lastIndexOf(".") + 1);
        return ret;
    }

    // 通过正则获取匹配的文件后缀名
    public static String getMatchFileSuffix(String url) {
        Matcher matcher = fileSuffixPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group().toString();
        }
        return "";
    }

    public static boolean isZipFile(String url) {
        boolean ret = false;
        final String suffixZip = "zip";
        final String suffixRar = "rar";
        final String suffix7Z = "7z";
        String suffix = getFileSuffix(url);
        if ((0 == suffixZip.compareToIgnoreCase(suffix))
                || (0 == suffixRar.compareToIgnoreCase(suffix))
                || (0 == suffix7Z.compareToIgnoreCase(suffix))) {
            ret = true;
        }
        return ret;
    }

    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            if (isZipFile(imageUrl)) {
                return drawable;
            }
            String srcImg = "upgrade_desc" + getFileSuffix(imageUrl);
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), srcImg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Loggers.d(TAG, "loadImageFromNetwork(" + imageUrl + ") return "
                + drawable);

        return drawable;
    }

    public static String readFile(String srcFile) {
        String ret = "";
        try {
            File file = new File(srcFile);
            if (file.exists()) {
                ret = readByInputStream(new FileInputStream(file));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String readByInputStream(InputStream is) {
        StringBuffer sb = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            sb = new StringBuffer();
            String line = "";
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
        } catch (IOException e) {
            sb = null;
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (null != sb) {
            return sb.toString();
        }

        return null;
    }

    /**
     * 重命名文件
     */
    public static void renameFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            File newFile = new File(destPath);
            boolean result = srcFile.renameTo(newFile);
            if (!result) {
                throw new RuntimeException("重命名文件出错！" + newFile);
            }
        }
    }

    public static void close(OutputStream out) {
        if (null != out) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = null;
        }
    }

    public static void close(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
        }
    }

    public static String getFileNames(String url) {
        Pattern pattern = Pattern.compile("([^\\\\/]+\\.\\w+)$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getSDCardAbsolutePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
     *
     * @param context
     */
    public static void write(Context context, String fileName, String content) {
        if (content == null)
            content = "";

        try {
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            fos.write(content.getBytes());

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String read(Context context, String fileName) {
        try {
            FileInputStream in = context.openFileInput(fileName);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        StringBuilder sb = new StringBuilder();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = "";
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString + "\r\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * 随机读取文件内容
     */
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     */
    public static void readFileByChars(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                    System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("以字符为单位读取文件内容，一次读多个字节：");
            // 一次读多个字符
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示
                if ((charread == tempchars.length)
                        && (tempchars[tempchars.length - 1] != '\r')) {
                    System.out.print(tempchars);
                } else {
                    for (int i = 0; i < charread; i++) {
                        if (tempchars[i] == '\r') {
                            continue;
                        } else {
                            System.out.print(tempchars[i]);
                        }
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String inputStream2String(InputStream is) {
        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        try {
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            result = baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
                baos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * A方法追加文件：使用RandomAccessFile
     */

    public static void appendMethodA(String fileName, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * B方法追加文件：使用FileWriter
     */
    public static void appendMethodB(String fileName, String content) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName + fileName);
    }

    /**
     * 向手机写图片
     *
     * @param buffer
     * @param folder
     * @param fileName
     * @return
     */
    public static boolean writeFile(byte[] buffer, String folder,
                                    String fileName) {
        boolean writeSucc = false;

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory()
                    + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }

        File fileDir = new File(folderPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File file = new File(folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            writeSucc = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writeSucc;
    }

    public static boolean writeFile(byte[] buffer, File file) {
        FileOutputStream fos = null;
        try {
            if (file.exists()) file.delete();
            createDirIfNotExist(file);
            fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 将字符串写入文件
     */
    public static void writeFile(String filePath, String fileContent) {
        OutputStream os = null;
        Writer w = null;
        try {
            FileUtil.createFile(filePath);
            os = new BufferedOutputStream(new FileOutputStream(filePath));
            w = new OutputStreamWriter(os, "UTF-8");
            w.write(fileContent);
            w.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (w != null) {
                    w.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeImageFile(byte[] buffer, File file) {
        FileOutputStream fos = null;
        try {
            if (file.exists()) file.delete();
            createDirIfNotExist(file);
            file.createNewFile();
            fos = new FileOutputStream(file);

            Bitmap bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean writeImageFile(Bitmap bitmap, File file) {
        FileOutputStream fos = null;
        try {
            if (file.exists()) file.delete();
            createDirIfNotExist(file);
            file.createNewFile();
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 根据文件的绝对路径获取文件名但不包含扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameNoFormat(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return "";
        }
        int point = filePath.lastIndexOf('.');
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
                point);
    }

    /**
     * 获取文件名，不加上扩展名
     *
     * @param name 文件全名
     * @return
     */
    public static String getNameFromFileName(String name) {
        int index = name.lastIndexOf(".");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (StringUtil.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param size 字节
     * @return
     */
    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "M";
        } else {
            return df.format(temp) + "K";
        }
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir, List<File> fileList) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += getDirSize(file, fileList); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static double getDirMBSize(File dir) {
        long dirSize = getDirSize(dir);
        return Math.round((double) dirSize / 1024 / 1024 * 100) / 100.0;
    }

    public static double getDirMBSize(File dir, List<File> fileList) {
        long dirSize = getDirSize(dir, fileList);
        return Math.round((double) dirSize / 1024 / 1024 * 100) / 100.0;
    }

    /**
     * 获取目录文件个数
     *
     * @return
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    public static byte[] toBytes(InputStream is) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        try {
            while (-1 != (n = is.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }

    public static boolean checkFileExist(String name) {
        File file = new File(name);
        return file.exists();
    }

    /**
     * 检查文件是否存在
     *
     * @param name
     * @return
     */
    public static boolean checkFileExists(String name) {
        boolean status;
        if (!name.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + name);
            status = newPath.exists();
        } else {
            status = false;
        }
        return status;
    }

    /**
     * 检查路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean checkFilePathExists(String path) {
        return new File(path).exists();
    }

    /**
     * 新建SD卡中子目录
     *
     * @param directoryName
     * @return
     */
    public static boolean createDirectory(String directoryName) {
        boolean status;
        if (!directoryName.equals("")) {
            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + directoryName);
            status = newPath.mkdir();
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        status = sDCardStatus.equals(Environment.MEDIA_MOUNTED);
        return status;
    }

    /**
     * 检查是否安装外置的SD卡
     *
     * @return
     */
    public static boolean checkExternalSDExists() {

        Map<String, String> evn = System.getenv();
        return evn.containsKey("SECONDARY_STORAGE");
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param fileName
     * @return
     */
    public static boolean deleteDirectory(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                // delete all files within the specified directory and then
                // delete the directory
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!fileName.equals("")) {

            File path = Environment.getExternalStorageDirectory();
            File newPath = new File(path.toString() + fileName);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除空目录
     * <p/>
     * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
     *
     * @return
     */
    public static int deleteBlankPath(String path) {
        File f = new File(path);
        if (!f.canWrite()) {
            return 1;
        }
        if (f.list() != null && f.list().length > 0) {
            return 2;
        }
        if (f.delete()) {
            return 0;
        }
        return 3;
    }

    /**
     * 重命名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean reNamePath(String oldName, String newName) {
        File f = new File(oldName);
        return f.renameTo(new File(newName));
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡的根目录
     *
     * @return
     */
    public static String getSDRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取手机外置SD卡的根目录
     *
     * @return
     */
    public static String getExternalSDRoot() {

        Map<String, String> evn = System.getenv();

        return evn.get("SECONDARY_STORAGE");
    }

    /**
     * 列出root目录下所有子目录
     *
     * @return 绝对路径
     */
    public static List<String> listPath(String root) {
        List<String> allDir = new ArrayList<String>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    allDir.add(f.getAbsolutePath());
                }
            }
        }
        return allDir;
    }

    /**
     * 获取一个文件夹下的所有文件
     *
     * @param root
     * @return
     */
    public static List<File> listPathFiles(String root) {
        List<File> allDir = new ArrayList<File>();
        SecurityManager checker = new SecurityManager();
        File path = new File(root);
        checker.checkRead(root);
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isFile())
                allDir.add(f);
            else
                listPath(f.getAbsolutePath());
        }
        return allDir;
    }

    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }

    /**
     * 创建目录
     */
    public static PathStatus createPath(String newPath) {
        File path = new File(newPath);
        if (path.exists()) {
            return PathStatus.EXITS;
        }
        if (path.mkdir()) {
            return PathStatus.SUCCESS;
        } else {
            return PathStatus.ERROR;
        }
    }

    /**
     * 截取路径名
     *
     * @return
     */
    public static String getPathName(String absolutePath) {
        int start = absolutePath.lastIndexOf(File.separator) + 1;
        int end = absolutePath.length();
        return absolutePath.substring(start, end);
    }

    /**
     * 获取应用程序缓存文件夹下的指定目录
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getAppCache(Context context, String dir) {
        String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir
                + "/";
        File savedir = new File(savePath);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        savedir = null;
        return savePath;
    }

    /**
     * 计算一个文件或文件夹下的总大小
     *
     * @param file 要计算的文件或文件夹
     */
    public static long getFilesSize(File file) {
        long size = 0;
        if (file.isDirectory() == true) {
            File[] files = file.listFiles();
            for (File f : files) {
                size += getFilesSize(f);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    /**
     * 删除文件或文件夹下的所有文件
     */
    public static boolean deleteFiles(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                deleteFiles(f);
        }
        return file.delete();
    }

    /**
     * 检查读写权限
     */
    public static boolean checkCanWrite(String path) {
        File file = new File(path);
        return file.canWrite();
    }

    public static void writeText(String filePath, String text) throws IOException {
        writeText(new File(filePath), text);
    }

    public static void writeText(File file, String text) throws IOException {
        writeText(file, text, null);
    }

    public static void writeText(File file, String text, String encoding) throws IOException {
        if (file.exists()) file.delete();
        if (text == null) return;
        try {
            createDirIfNotExist(file);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes(encoding == null ? "UTF-8" : encoding));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String readText(File source) throws IOException {
        return readText(source, null);
    }

    public static String readText(File source, String encoding) throws IOException {
        if (source == null || !source.exists()) return null;
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = new FileInputStream(source);
        InputStreamReader isr = new InputStreamReader(fis, encoding == null ? "UTF-8" : encoding);
        char[] cs = new char[10240];
        int len;
        while ((len = isr.read(cs)) > 0) {
            sb.append(cs, 0, len);
        }
        isr.close();
        fis.close();
        return sb.toString();
    }

    public static String readText(InputStream source) throws IOException {
        return readText(source, null);
    }

    public static String readText(InputStream source, String encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(source, encoding == null ? "UTF-8" : encoding);
        char[] cs = new char[10240];
        int len;
        while ((len = isr.read(cs)) > 0) {
            sb.append(cs, 0, len);
        }
        isr.close();
        return sb.toString();
    }

    public static void copy(File source, File target) throws IOException {
        try {
            if (!source.exists()) return;
            createFileIfNotExist(target.getPath());
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(target);
            copy(fis, fos);
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(InputStream is, String file) {
        copy(is, new File(file));
    }

    public static void copy(InputStream is, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            copy(is, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copy(InputStream is, OutputStream os) {
        if (is == null || os == null) return;
        try {
            byte[] bs = new byte[1024];
            int len;
            while ((len = is.read(bs)) > 0) {
                os.write(bs, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFilenameOf(String uri) {
        return uri.replaceFirst("https?:\\/\\/", "").replaceAll("[^a-zA-Z0-9.]",
                "_");
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    static File getExternalCacheDir(final Context context) {
        return context.getExternalCacheDir();
    }

    static File cacheDir = null;

    public static File getCacheDir(Context context, String category) {
        if (cacheDir == null) {
            if (getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ?
                        getExternalCacheDir(context)
                        : new File(getExternalStorageDirectory().getPath() + "/Android/data/" + context.getPackageName() + "/cache/");
            } else {
                cacheDir = context.getCacheDir();
            }
            if (cacheDir != null && !cacheDir.exists()) {
                cacheDir.mkdirs();
            }
        }
        File fc = category == null ? cacheDir : new File(cacheDir, category);
        if (!fc.exists())
            fc.mkdir();
        return fc;
    }

    public static File getCacheOf(Context context, String category, String uri) {
        return new File(getCacheDir(context, category), getFilenameOf(uri));
    }

    public static File getFileInCard(String path) {
        if (getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return new File(getExternalStorageDirectory().getPath() + path);
        return null;
    }

    public static void mkdirForFile(File f) {
        mkdir(f.getParentFile());
    }

    public static void mkdir(File dir) {
        try {
            if (dir == null) return;
            if (!dir.exists()) {
                mkdir(dir.getParentFile());
                dir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean unzip(String zipFile, String outputFolder) {
        try {
            return unzip(new FileInputStream(zipFile), new File(outputFolder));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unzip(InputStream is, File folder) {
        if (is == null || folder == null) return false;
        byte[] buffer = new byte[1024];

        try {
            if (!folder.exists()) {
                folder.mkdir();
            }
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(folder, fileName);

                Log.d(TAG, "file unzip : " + newFile.getAbsoluteFile());

                newFile.getParentFile().mkdir();

                if (ze.isDirectory()) {
                    newFile.mkdir();
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            Log.d(TAG, "Done");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 获取随机文件名，避免通过时间、UUID获取唯一文件名时，写入文件出现相似重复文件信息
     *
     * @return
     */
    public static String getRandomFileName() {
        String[] randomStr = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < randomStr.length; i++) {
            int j = rand.nextInt((26 + 9));
            result.append(randomStr[j]);
        }
        return result.toString();
    }

    public static boolean deleteFile(File file) {
        try {
            boolean result = true;
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0, count = files.length; i < count; i++) {
                        result &= deleteFile(files[i]);
                    }
                    result &= file.delete(); // Delete empty directory.
                } else {
                    result &= file.delete();
                }
                return result;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String encodeBase64File(String path) {
        try {
            File file = new File(path);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 把图片压缩到200K
     *
     * @param oldpath
     *            压缩前的图片路径
     * @param newPath
     *            压缩后的图片路径
     * @return
     */
    /**
     * 把图片压缩到200K
     *
     * @param oldpath 压缩前的图片路径
     * @param newPath 压缩后的图片路径
     * @return
     */
    public static File compressFile(String oldpath, String newPath) {
        Bitmap compressBitmap = FileUtil.decodeFile(oldpath);
        Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();

        File file = null;
        try {
            file = FileUtil.getFileFromBytes(bytes, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (newBitmap != null) {
                if (!newBitmap.isRecycled()) {
                    newBitmap.recycle();
                }
                newBitmap = null;
            }
            if (compressBitmap != null) {
                if (!compressBitmap.isRecycled()) {
                    compressBitmap.recycle();
                }
                compressBitmap = null;
            }
        }
        return file;
    }

    private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
        int degree = readPictureDegree(filePath);
        return rotaingImageView(degree, bitmap);
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 图片压缩
     *
     * @param fPath
     * @return
     */
    public static Bitmap decodeFile(String fPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inDither = false; // Disable Dithering mode
        opts.inPurgeable = true; // Tell to gc that whether it needs free
        opts.inInputShareable = true; // Which kind of reference will be used to
        BitmapFactory.decodeFile(fPath, opts);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
            final int heightRatio = Math.round((float) opts.outHeight
                    / (float) REQUIRED_SIZE);
            final int widthRatio = Math.round((float) opts.outWidth
                    / (float) REQUIRED_SIZE);
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;//
        }
        Log.i("scale", "scal =" + scale);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(Bitmap.Config.ARGB_8888, false);
        return bm;
    }


    /**
     * 创建目录
     *
     * @param path
     */
    public static void setMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            Log.e("file", "目录不存在  创建目录    ");
        } else {
            Log.e("file", "目录存在");
        }
    }

    /**
     * 获取目录名称
     *
     * @param url
     * @return FileName
     */
    public static String getFilePathName(String url) {
        int lastIndexStart = url.lastIndexOf("/");
        if (lastIndexStart != -1) {
            return url.substring(lastIndexStart + 1, url.length());
        } else {
            return null;
        }
    }

    /**
     * 删除该目录下的文件
     *
     * @param path
     */
    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }
/*********************************************Added by caowen  2015/12/3**********************************************************************************/

    /**
     * 统一缓存路径 Android/data/com.ymatou.shop/path/
     *
     * @param path
     * @return
     */
    public static String getCachePath(Context context, String path) {
        String fileDir = "";
        path = path.trim();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }

        try {
            fileDir = String.format("%s/%s/", context.getExternalCacheDir().getPath(), path);
        } catch (Exception e) {
            e.printStackTrace();
            fileDir = String.format("%s/%s/", context.getCacheDir().getPath(), path);
        }
        File file = new File(fileDir);
        if (!file.exists()) {
            mkdir(file);
        }
        return fileDir;
    }

    /**
     * 基础了路径
     *
     * @return
     */

    public static String getBasePath() {
        String BASE_PATH = "";
        String sdcardState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //BASE_PATH = App.get().getCacheDir().getAbsolutePath();
        }
        return BASE_PATH;
    }

    public static boolean createFile(File file) {
        try {
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile());
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建文件
     */
    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                mkdir(parentDir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 社区图片保存路径
     *
     * @return
     */
    public static String getCropPath() {
        String fileDir = getBasePath() + "/DCIM/洋码头/";
        File file = new File(fileDir);
        if (!file.exists()) {
            mkdir(new File(fileDir));
        }
        return fileDir;
    }

    public static void checkPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            mkdir(new File(path));
        }

    }

    /**
     * 老的路径
     *
     * @return
     */
    public static String getOldCropPath() {
        return getBasePath() + "/洋码头/";
    }

    /**
     * 异步删除文件
     *
     * @param file
     */
    public static void deleteAsync(final File file) {
        if (file == null || !file.exists()) {
            return;
        }
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                if (file != null && file.exists()) {
                    delete(file);
                }
                return null;
            }
        }.execute();
    }

    public File getExtFile(String path) {
        return new File(getBasePath() + path);
    }


    //都是相对路径，一一对应
    public boolean copyAssetDirToFiles(Context context, String dirname) {
        try {
            AssetManager assetManager = context.getAssets();
            String[] children = assetManager.list(dirname);
            for (String child : children) {
                child = dirname + '/' + child;
                String[] grandChildren = assetManager.list(child);
                if (0 == grandChildren.length)
                    copyAssetFileToFiles(context, child);
                else
                    copyAssetDirToFiles(context, child);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //都是相对路径，一一对应
    public boolean copyAssetFileToFiles(Context context, String filename) {
        return copyAssetFileToFiles(context, filename, getExtFile("/" + filename));
    }

    private boolean copyAssetFileToFiles(Context context, String filename, File of) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = context.getAssets().open(filename);
            createFile(of);
            os = new FileOutputStream(of);

            int readedBytes;
            byte[] buf = new byte[1024];
            while ((readedBytes = is.read(buf)) > 0) {
                os.write(buf, 0, readedBytes);
            }
            os.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(is);
            closeStream(os);
        }
    }

    public boolean renameDir(String oldDir, String newDir) {
        File of = new File(oldDir);
        File nf = new File(newDir);
        return of.exists() && !nf.exists() && of.renameTo(nf);
    }

    /**
     * 复制单个文件
     */
    public void copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPath); //读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        } finally {
            closeStream(inStream);
            closeStream(fs);
        }
    }

    //读取assets文件
    public String readFromAsset(String fileName, Context context) {
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = context.getAssets().open(fileName);
            br = new BufferedReader(new InputStreamReader(is));
            String addonStr = "";
            String line = br.readLine();
            while (line != null) {
                addonStr = addonStr + line;
                line = br.readLine();
            }
            return addonStr;
        } catch (Exception e) {
            return null;
        } finally {
            closeStream(br);
            closeStream(is);
        }
    }

    public String getFromAssets(String fileName, Context context) throws Exception {

        InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line = "";
        String Result = "";
        while ((line = bufReader.readLine()) != null)
            Result += line;
        return Result;

    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }

    public static File createTmpFile(Context context) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // SD卡正常挂载 也就是SD卡可用
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(getCropPath(), fileName + ".jpg");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "multi_image_" + timeStamp + "";
            File tmpFile = new File(cacheDir, fileName + ".jpg");
            return tmpFile;
        }

    }

    public static String createPicName() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = dateFormat.format(date);
        String picName = format + ".jpeg";
        return picName;
    }

    public static void closeStream(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {

        }
    }
}