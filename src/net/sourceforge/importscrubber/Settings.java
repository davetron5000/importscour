package net.sourceforge.importscrubber;

import java.io.*;
import java.util.Properties;

public class Settings
{
    private String _appName;
    private Properties _props;

    public Settings(String appName)
    {
        _appName = appName;
        File homeDir = new File(System.getProperty("user.home"));
        if(!homeDir.exists()) {
            homeDir.mkdirs();
        }
        File appDir = new File(System.getProperty("user.home"), _appName);
        if(!appDir.exists()) {
            appDir.mkdir();
        }
        load();
    }

    public void load()
    {
        _props = new Properties();
        try {
            File appDir = new File(System.getProperty("user.home"), _appName);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(appDir, _appName)));
            _props.load(in);
            in.close();
        } catch(Exception exception) { }
    }

    public void put(String pKey, String pValue)
    {
        _props.put(pKey, pValue);
    }

    public String get
        (String pKey)
    {
        return _props.getProperty(pKey);
    }

    public void save() throws IOException
    {
        File appDir = new File(System.getProperty("user.home"), _appName);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(appDir, _appName)));
        _props.store(out, "Importscrubber");
        out.close();
    }

}
