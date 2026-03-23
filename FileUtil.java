
import java.io.*;
import java.util.ArrayList;

public class FileUtil {

    private static volatile FileUtil instance;

    private FileUtil() {
        System.out.println("FileUtil Singleton Initialized");
    }

    public static FileUtil getInstance() {
        if (instance == null) {
            synchronized (FileUtil.class) {
                if (instance == null) {
                    instance = new FileUtil();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> loadData(String filename) throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) ois.readObject();
        }
    }

    public <T> void saveData(ArrayList<T> data, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    public static <T> ArrayList<T> load(String filename) throws IOException, ClassNotFoundException {
        return getInstance().loadData(filename);
    }

    public static <T> void save(ArrayList<T> data, String filename) throws IOException {
        getInstance().saveData(data, filename);
    }

    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    public static boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

    public static long getFileSize(String filename) {
        File file = new File(filename);
        return file.exists() ? file.length() : 0;
    }

    public static String[] listDataFiles() {
        return new File(".").list((dir, name) -> name.endsWith(".dat"));
    }

    public static boolean backupFile(String filename) {
        try {
            File source = new File(filename);
            if (!source.exists()) {
                return false;
            }

            File backup = new File(filename + ".backup");
            try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(backup)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
