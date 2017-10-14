package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.MetricLoaderPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

/**
 * This is example of parallel running tests.
 */
public class MetricLoaderTestDriver {

    private static final int COUNT = 3;
    private static final int NUM = 100;
    private static String template;

    @BeforeTest
    public void before() {
        //todo: enable uix 'Metric importer.',
        //todo: add metric template 'Financial Metric' with Breakdown tagSet 'AUTOTESTS TAG 1' and item='item a'
        //todo: and metric instance with tag a to 'ROOT WORK FOR AUTOTESTS'.
        //todo: see metricloadtest.txt
        SeleniumDriverFactory.setParallel(true);
        CoreProperties.loadProperties();
        template = CoreProperties.getServerFolder() + File.separator +
                "metricloadtest.txt";
    }

    @AfterTest
    public void after() {
        SeleniumDriverFactory.setParallel(false);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
    }

    @Test(description = "MetricLoader dead lock (#74128)",
            threadPoolSize = COUNT,
            invocationCount = COUNT, groups = TestSettings.FIREFOX_ONLY_GROUP)
    public void test() {
        BasicCommons.logIn();
        File file = new File(template);
        MetricLoaderPage metric = new MetricLoaderPage();
        metric.open();
        for (int i = 0; i < NUM; i++) {
            PSLogger.info("Iter #" + i);
            metric.upload(file);
            long s = System.currentTimeMillis();
            metric.pushGo();
            long e = System.currentTimeMillis();
            PSLogger.info("Duration(GO) for #" + i + ": " + (e - s) / 1000L + "s");
            PSLogger.info(metric.getErrors());
            Assert.assertTrue(e - s < CoreProperties.getWaitForElementToLoad(), "Found deadlock");
        }
    }
}
