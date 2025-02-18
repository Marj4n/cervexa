package generalplus.com.GPCamLib;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/* loaded from: classes2.dex */
public class GPINIReader {
    private static final String INIReader_SaveLog = "SaveLog";
    private static final String INIReader_ShowLog = "ShowLog";
    private static String TAG = "GPINIReader";
    private static boolean _EnableSaveLog = false;
    private static boolean _EnableShowLog = false;
    private static GPINIReader m_Instance;
    private Properties configuration;
    private String configurationFile;

    public GPINIReader() {
        this.configurationFile = "";
        m_Instance = this;
        this.configuration = new Properties();
        this.configurationFile = Environment.getExternalStorageDirectory().getPath() + "/" + CamWrapper.CamDefaulFolderName + "/" + CamWrapper.ConfigFileName;
        File file = new File(this.configurationFile);
        if (!file.exists() && !file.isDirectory()) {
            try {
                FileWriter fileWriter = new FileWriter(this.configurationFile);
                fileWriter.write(("" + String.format("%s = false\n", INIReader_SaveLog)) + String.format("%s = false\n", INIReader_ShowLog));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (load()) {
            String str = get(INIReader_SaveLog);
            if (str != null && str.toUpperCase().equalsIgnoreCase("TRUE")) {
                _EnableSaveLog = true;
            }
            if (get(INIReader_ShowLog) == null || !get(INIReader_ShowLog).toUpperCase().equalsIgnoreCase("TRUE")) {
                return;
            }
            _EnableShowLog = true;
        }
    }

    public static GPINIReader getInstance() {
        return m_Instance;
    }

    public boolean IsEnableShowLog() {
        return _EnableShowLog;
    }

    public boolean IsEnableSaveLog() {
        return _EnableSaveLog;
    }

    public boolean load() {
        try {
            this.configuration.load(new FileInputStream(this.configurationFile));
            return true;
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
            return false;
        }
    }

    public String get(String str) {
        return this.configuration.getProperty(str);
    }
}
