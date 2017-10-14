package com.powersteeringsoftware.libs.pages;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.MetricLoaderPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.08.11
 * Time: 20:40
 */
public class MetricLoaderPage extends PSPage {
    private Input upload;
    private Button go;

    @Override
    public void open() {
        clickBrowseImportExportMetric();
    }

    public void upload(String file) {
        PSLogger.info("Attach file " + file);
        getUpload().attachFile(file);
    }

    public void upload(File file) {
        Assert.assertTrue(file.exists(), "Can't find " + file.getAbsolutePath());
        upload(file.getAbsolutePath());
    }

    public Input getUpload() {
        if (upload == null) upload = new Input(UPLOAD);
        return upload;
    }

    public Button getGo() {
        if (go == null) go = new Button(GO);
        return go;
    }

    public void pushGo() {
        PSLogger.info("Go");
        getGo().click(false);
        waitForPageToLoad();
    }

    public List<String> getErrors() {
        List<String> errors = new ArrayList<String>();
        for (Element e : getElements(ERROR_CELL)) {
            errors.add(e.getDEText());
        }
        return errors;
    }
}
