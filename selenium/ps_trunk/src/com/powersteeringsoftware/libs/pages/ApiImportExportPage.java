package com.powersteeringsoftware.libs.pages;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.powersteeringsoftware.libs.enums.page_locators.ApiExportPageLocators;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import static com.powersteeringsoftware.libs.enums.page_locators.ApiExportPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 14.09.11
 * Time: 13:33
 */
public class ApiImportExportPage extends XMLPage {
    private User user;
    private static final String DATE_FORMAT = "yyyy_MM_dd__HH_mm_ss";

    public void openExportWorks() {
        String url = getUrl(URL_WORKS);
        open(url);
    }

    private String getUrl(ApiExportPageLocators jsp) {
        return getUrl(URL_EXPORT, jsp);
    }

    private String getUrl(ApiExportPageLocators prefix, ApiExportPageLocators jsp) {
        return URL_DIR.getLocator() + "/" + prefix.getLocator() + jsp.getLocator() +
                "?" + URL_LOGIN.getLocator() + "=" + getUser().getLogin() +
                "&" + URL_PASSWORD.getLocator() + "=" + getUser().getPassword();
    }


    public User getUser() {
        if (user == null) {
            user = CoreProperties.getDefaultUser();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImportFile(Date date) {
        return "context_" + user.getLogin() + "_" + new SimpleDateFormat(DATE_FORMAT).format(date) + ".xml";
    }
}
