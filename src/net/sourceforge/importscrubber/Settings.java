package net.sourceforge.importscrubber;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
    private String appName;
    private Properties props;

    public Settings(String appName)
    {
        this.appName = appName;
        File homeDir = new File(System.getProperty("user.home"));
        if(!homeDir.exists()) {
            homeDir.mkdirs();
        }
        File appDir = new File(System.getProperty("user.home"), this.appName);
        if(!appDir.exists()) {
            appDir.mkdir();
        }
        load();
    }

    public void load()
    {
        props = new Properties();
        try {
            File appDir = new File(System.getProperty("user.home"), appName);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(appDir, appName)));
            props.load(in);
            in.close();
        } catch(Exception exception) { }
    }

    public void put(String pKey, String pValue)
    {
        props.put(pKey, pValue);
    }

    public String get
        (String pKey)
    {
        return props.getProperty(pKey);
    }

    public void save() throws IOException
    {
        File appDir = new File(System.getProperty("user.home"), appName);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(appDir, appName)));
        props.store(out, "Importscrubber");
        out.close();
    }

}
