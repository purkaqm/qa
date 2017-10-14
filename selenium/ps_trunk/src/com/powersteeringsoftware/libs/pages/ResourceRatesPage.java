package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.page_locators.RateTablesPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.List;

/**
 * Created by admin on 07.04.14.
 */
public abstract class ResourceRatesPage extends PSPage {
    @Override
    public void open() {
        clickResourceRates();
    }

    public RateCodesPage openRateCodes() {
        clickRateCodes(false);
        RateCodesPage res = new RateCodesPage();
        res.waitForPageToLoad();
        return res;
    }

    protected void clickRateCodes(boolean wait) {
        new Link(RateTablesPageLocators.RATE_CODES_TAB).click(wait);
    }

    public RateTablesPage openRateTables() {
        new Link(RateTablesPageLocators.RATE_TABLES_TAB).click(false);
        RateTablesPage res = new RateTablesPage();
        res.waitForPageToLoad();
        return res;
    }

    public ConfigurationPage openConfigurationPage() {
        clickConfig(false);
        ConfigurationPage res = new ConfigurationPage();
        res.waitForPageToLoad();
        return res;
    }

    protected void clickConfig(boolean wait) {
        new Link(RateTablesPageLocators.RATE_CONFIG_TAB).click(wait);
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        reset();
    }

    protected void reset() {
        document = null;
        rows = null;
        PSLogger.debug("Rows: " + getRows());
    }

    protected List<ARow> rows;

    protected List<ARow> getRows() {
        return rows;
    }

    protected abstract class ARow {
    }

}
