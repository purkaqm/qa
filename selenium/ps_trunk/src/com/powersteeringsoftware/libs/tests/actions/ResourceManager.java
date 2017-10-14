package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.RateCode;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.pages.*;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.powersteeringsoftware.libs.util.session.TestSession.*;

/**
 * Created by admin on 10.04.2014.
 */
public class ResourceManager {

    public static RateTablesPage openRateTables() {
        PSLogger.info("Open RateTables page");
        RateTablesPage r = new RateTablesPage();
        r.open();
        return r;
    }

    public static ResourcePoolListPage openPools() {
        PSLogger.info("Open ResourcePoolList page");
        ResourcePoolListPage page = new ResourcePoolListPage();
        page.open();
        return page;
    }

    public static RateCodesPage openRateCodes() {
        PSLogger.info("Open RateCodes page");
        RateTablesPage r = new RateTablesPage();
        r.open();
        return r.openRateCodes();
    }

    public static ConfigurationPage openConfig() {
        PSLogger.info("Open Configuration page");
        RateTablesPage r = new RateTablesPage();
        r.open();
        return r.openConfigurationPage();
    }

    public static RateTableViewPage openRateTable(RateTable table) {
        PSLogger.info("Open RateTableView page for rate '" + table.getName() + "'");
        RateTablesPage page = new RateTablesPage();
        page.open();
        return page.openTable(table.getName());
    }

    public static void addRateCode(RateCode... codes) {
        RateCodesPage page = openRateCodes();
        for (RateCode code : codes) {
            PSLogger.info("Add Rate Code " + code);
            RateCodesPage.CodeDialog dialog = page.addNew();
            if (code.hasDate()) {
                dialog.setDate(code.getSDate());
            }
            dialog.setCodeDetails(code.getAmount(), code.getCurrency().getName());
            dialog.setCode(code.getName());
            dialog.submit();
            //todo validation here
            code.setCreated();
        }
    }

    public static void setDefaultTable(RateTable table) {
        PSLogger.info("Set default rate table " + table.getName());
        RateTablesPage page = ResourceManager.openRateTables();
        page.setDefault(table.getName());
        Assert.assertEquals(page.getDefault(), table.getName(), "Incorrect default rate table");
        table.setDefault();
    }

    public static void createRateTable(RateTable table) {
        RateTablesPage page1 = openRateTables();

        PSLogger.info("Create rate table " + table.getName());

        List<String> before = page1.getTables();
        RateTablesPage.TablesDialog dialog1 = page1.addNew();
        dialog1.setName(table.getName());
        if (table.getDescription() != null)
            dialog1.setDescription(table.getDescription());
        dialog1.submit();

        List<String> after = page1.getTables();
        PSLogger.info("After creation: " + after);
        if (before.isEmpty()) {
            Assert.assertEquals(page1.getDefault(), table.getName(), "Incorrect default table");
        } else {
            after.removeAll(before);
            Assert.assertTrue(after.size() == 1 && after.get(0).equals(table.getName()), "Should be one new table");
            Assert.assertNotSame(page1.getDefault(), table.getName(), "New table should not be default");
        }

        table.setCreated();
        RateTableViewPage page2 = page1.openTable(table.getName());
        for (RateTable.Rate r : table.getRates()) {
            addRate(page2, r);
        }
    }

    public static void addRates(RateTable table, RateTable.Rate... rates) {
        RateTableViewPage page = openRateTable(table);
        for (RateTable.Rate rate : rates) {
            addRate(page, rate);
            table.addRate(rate);
        }
    }

    private static void addRate(RateTableViewPage page, RateTable.Rate rate) {
        PSLogger.info("Add rate: " + rate);
        RateTableViewPage.RateDialog dialog = page.addNew();
        dialog.setRole(rate.getRole().getName());
        if (rate.hasActivity()) {
            dialog.setActivity(rate.getActivity().getName());
        }
        if (rate.hasDate()) {
            dialog.setDate(rate.getSDate());
        }
        RateCode rc = rate.getCode();
        if (rc.isCustom()) {
            dialog.setCodeDetails(rc.getAmount(), rc.getCurrency().getName());
        } else {
            dialog.setCode(rc.getName());
        }
        dialog.submit();
        //todo validation here
        rate.setCreated();
    }

    public static void enableLaborCosts() {
        if (isLaborCostsEnabled()) {
            return;
        }
        setLaborCosts(true);
    }

    public static boolean isLaborCostsEnabled() {
        return !isObjectNull(Keys.LABOR_COSTS) && Boolean.TRUE.equals(getObject(Keys.LABOR_COSTS));
    }

    public static void disableLaborCosts() {
        if (!isObjectNull(Keys.LABOR_COSTS) && Boolean.FALSE.equals(getObject(Keys.LABOR_COSTS))) {
            return;
        }
        setLaborCosts(false);
    }

    public static void setLaborCosts(boolean b) {
        ConfigurationPage page = openConfig();
        if (page.setCosts(b)) {
            page.save();
        }
        putObject(Keys.LABOR_COSTS, b);
    }

    public static void createPool(ResourcePool pool) {
        ResourcePoolListPage page1 = openPools();
        List<String> before = page1.getPools();
        PSLogger.info("Pool List before " + before);
        AddEditResourcePoolPage page2 = page1.addNew();
        page2.setName(pool.getName());
        if (pool.getDescription() != null)
            page2.setDescription(pool.getDescription());
        page1 = page2.submit().back();
        List<String> after = page1.getPools();
        PSLogger.info("Pool List after " + after);
        List<String> expected = new ArrayList<String>(before);
        List<String> actual = new ArrayList<String>(after);
        expected.add(pool.getName());
        Collections.sort(actual);
        Collections.sort(expected);
        Assert.assertEquals(after, expected, "Incorrect list of pools after adding new");
    }
}
