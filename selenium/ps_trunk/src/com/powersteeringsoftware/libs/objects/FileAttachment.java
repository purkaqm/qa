package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import sun.security.provider.certpath.SunCertPathBuilderException;

import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertPathValidatorException;

public class FileAttachment implements IAttachment {

    public static boolean compareURLWithLocalFile(String sessionCookie, String fileUrl, String path) {
        if (!path.contains(CoreProperties.getServerFolder())) path = CoreProperties.getServerFolder() + path;
        try {
            fileUrl = fileUrl.replace("%27", "'").replace("%2E", ".");
            PSLogger.debug("Check url '" + fileUrl + "'");

            URLConnection urlConn = new URL(fileUrl).openConnection();
            urlConn.setRequestProperty("Cookie", sessionCookie);
            urlConn.connect();

            String netContent = new BufferedReader(new InputStreamReader(urlConn.getInputStream())).readLine();
            String localContent = new RandomAccessFile(path, "r").readLine();

            PSLogger.debug("Net content: " + netContent);
            return netContent.equals(localContent);
        } catch (IOException ioe) {
            if (ioe instanceof SSLHandshakeException &&
                    (ioe.getMessage().contains(CertPathValidatorException.class.getName()) ||
                            ioe.getMessage().contains(SunCertPathBuilderException.class.getName()))) {
                PSSkipException.skip("certificate error: " + ioe.getMessage());
            }
            PSLogger.warn("Files compare error: " + ioe.getMessage());
            return false;
        }
    }


    private String location;
    private String title;
    private String description;
    private Boolean ppt;
    private String status;

    private FileAttachment(String location, String title) {
        if (location.contains(CoreProperties.getServerFolder()))
            this.location = location;
        else
            this.location = CoreProperties.getServerFolder() + location;
        this.title = title;
        this.description = null;
        this.ppt = null;
        this.status = null;
    }

    public static FileAttachment getFile(String path, String title) {
        return new FileAttachment(path, title);
    }

    public static FileAttachment getFile(String path) {
        return getFile(path, null);
    }

    public String getPath() {
        return location;
    }

    public void setPath(String path) {
        this.location = path;
    }

    public String getTitle() {
        if (title == null) return location.replace(CoreProperties.getServerFolder(), "");
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPpt() {
        return ppt;
    }

    public void setPpt(Boolean ppt) {
        this.ppt = ppt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return (title != null ? title : "") + "[" + location + "]";
    }
}
