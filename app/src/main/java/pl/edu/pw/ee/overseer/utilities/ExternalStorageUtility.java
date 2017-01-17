package pl.edu.pw.ee.overseer.utilities;


import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExternalStorageUtility {
    private static final File ROOT = new File(Environment.getExternalStorageDirectory(), "Overseer");

    public static boolean createRootFolder() {
        return ROOT.exists() || ROOT.mkdir();
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
}
