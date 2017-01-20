package pl.edu.pw.ee.overseer.utilities;


import android.os.Environment;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ExternalStorageUtility {
    private static final File ROOT = new File(Environment.getExternalStorageDirectory(), "Overseer");

    public static boolean createRootFolder() {
        return ROOT.exists() || (ROOT.mkdir() && new File(ROOT, "avatar").mkdir() && new File(ROOT, "detail").mkdir());
    }

    public static void deleteRootFolder() {
        deleteRecursive(ROOT);
    }

    private static void deleteRecursive(File file) {
        if (file.isDirectory())
            for (File child : file.listFiles())
                deleteRecursive(child);
        file.delete();
    }


    public static void writeImage(String path, String image) throws IOException {
        File file = new File(ROOT, path);
        if (file.exists())
            file.delete();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(Base64.decode(image, Base64.DEFAULT));
        fileOutputStream.close();
    }

    public static FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(new File(ROOT, path));
    }

    public static boolean exists(String path) {
        File file = new File(ROOT, path);
        return file.exists();
    }

    public static boolean delete(String path) {
        File file = new File(ROOT, path);
        return !file.exists() || file.delete();
    }

    public static void saveJSONObject(String path, JSONObject jsonObject) throws IOException {
        File file = new File(ROOT, path);
        if (file.exists())
            file.delete();

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        outputStreamWriter.write(jsonObject.toString());
        outputStreamWriter.close();
    }

    public static JSONObject readJSONObject(String path) throws IOException, JSONException {
        File file = new File(ROOT, path);
        String result = "";
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        int c;
        while ((c = inputStreamReader.read()) > 0) {
            result += String.valueOf((char) c);
        }
        return new JSONObject(result);
    }
}
