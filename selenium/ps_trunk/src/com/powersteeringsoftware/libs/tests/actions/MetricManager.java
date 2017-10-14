package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.05.12
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class MetricManager {

    private static final int SAVING_ATTEMPTS_NUMBER = 2;

    public static void createTemplate(MetricTemplate template) {
        Assert.assertFalse(template.getItems().isEmpty(), "No items in template");
        MetricTemplatesPage page = new MetricTemplatesPage();
        page.open();

        MetricTemplatesPage.FirstFrame fr1 = page.createNew();
        fr1.setName(template.getName());
        if (template.getDescription() != null)
            fr1.setDescription(template.getDescription());
        fr1.setPeriod(template.getStartDateLong(), template.getEndDateLong());
        if (template.isBreakdown())
            fr1.setBreakDownTag(template.getBreakdownTag().getName());
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            fr1.setPercentageAllocation(template.isPercentageAllocation());
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
            fr1.setCalendar(template.getMetricCalendar());
        }
        if (!MetricTemplate.FrequencyType.MONTHLY.equals(template.getPeriodType())) {
            fr1.setFrequency(template.getPeriodType());
        }

        if (template.hasWorkType(MetricTemplate.WorkTypes.WORK_ITEM))
            fr1.selectWorkItems();
        if (template.hasWorkType(MetricTemplate.WorkTypes.MSP_PROJECT))
            fr1.selectMspProject();
        if (template.hasWorkType(MetricTemplate.WorkTypes.GATED_PROJECT))
            fr1.selectTollgate();
        if (template.hasWorkType(MetricTemplate.WorkTypes.GATED_PROJECT_NOT_ESG))
            fr1.selectTollgateNotEsg();

        if (template.hasViews())
            fr1.setHasView(true);

        MetricTemplatesPage.DisplayFrame fr2 = fr1.next();

        if (template.getFiscalYear())
            fr2.setCurrentFinancialYear();
        fr2.setDisplayTotal(template.getDisplayTotal());

        MetricTemplatesPage.ItemFrame itemFrame;
        if (template.hasViews()) {
            MetricTemplatesPage.ViewFrame viewFrame = (MetricTemplatesPage.ViewFrame) fr2.next();
            for (MetricTemplate.View view : template.getViews()) {
                viewFrame.addItem(view.getSequence(), view.getName(), view.getTypeString());
            }
            itemFrame = (MetricTemplatesPage.ItemFrame) viewFrame.next();
        } else {
            itemFrame = (MetricTemplatesPage.ItemFrame) fr2.next();
        }

        List<MetricTemplate.Item> items = template.getItems();
        for (MetricTemplate.Item item : items) {
            itemFrame.addItem(item.getSequence(), item.getName(), item.getTypeString(), item.getDescription());
        }
        MetricTemplatesPage.AFrame next = itemFrame.next();
        Collections.sort(items);
        MetricTemplatesPage.FormulaFrame formulaFrame;
        if (next instanceof MetricTemplatesPage.FormulaFrame) {
            formulaFrame = (MetricTemplatesPage.FormulaFrame) next;
        } else { // todo
            MetricTemplatesPage.StaticConstraintValuesFrame frame = (MetricTemplatesPage.StaticConstraintValuesFrame) next;
            List<MetricTemplate.Item> _items = template.getActualItems(true);
            for (int i = 0; i < _items.size(); i++) {
                MetricTemplate.Item item = _items.get(i);
                MetricTemplate.CostMapping map = item.getCostMapping();
                if (map != null) {
                    frame.setCostMapping(i, map.getName());
                }
            }
            formulaFrame = frame.next();
        }
        if (template.hasFormula()) {
            formulaFrame.setTextMode();
            for (MetricTemplate.Item item : template.getItems()) {
                formulaFrame.setFormula(item.getName(), item.getFormula());
            }
        }
        formulaFrame.next().submit().selectTopFrame();
        template.setCreated();
    }

    public static MetricInstance attachTemplate(MetricTemplate template, Work work) {
        SummaryWorkPage sum = WorkManager.open(work);
        MetricsPage page = sum.openManageMetrics();
        MetricsPage.AttachDialog attach = page.attach();
        attach.select(template.getName());
        page = attach.submit();
        MetricInstance res = new MetricInstance(work, template);
        res.setCreated();
        return res;
    }

    public static MetricInstancePage open(MetricInstance instance) {
        return open(instance, false);
    }

    public static MetricInstancePage open(MetricInstance instance, boolean forceOpen) {
        String id = instance.getId();
        MetricInstancePage page;
        if (id != null) {
            page = new MetricInstancePage(id);
            if (forceOpen) {
                String _id = PSPage.getEmptyInstance().getUrlId();
                if (_id.equals(id)) {
                    page.init();
                    return page;
                }
            }
            page.open();
            return page;
        }
        SummaryWorkPage sum = WorkManager.open(instance.getWork());
        page = sum.openMetricInstancePage(instance.getName());
        instance.setId(page.getId());
        instance.setCreated();
        return page;
    }

    public static void simpleValidateTemplate(MetricInstancePage page, MetricInstance instance) {
        PSLogger.info("Simple validate attached template");
        TemplatePage templatePage = page.info();
        List<TemplatePage.GeneralInfo> info = templatePage.getInfo();
        List<TemplatePage.Item> items = templatePage.getItems();
        PSLogger.info(info);
        PSLogger.info(items);
        Assert.assertEquals(templatePage.getInfo("name"), instance.getName(), "incorrect name");
        Assert.assertTrue(StrUtil.equals(templatePage.getInfo("description"), instance.getDescription()), "incorrect description");

        List<MetricTemplate.Item> expectedItems = instance.getActualItems();
        Assert.assertEquals(items.size(), expectedItems.size(), "Incorrect list of items");
        for (int i = 0; i < items.size(); i++) {
            MetricTemplate.Item expected = expectedItems.get(i);
            TemplatePage.Item actual = items.get(i);
            Assert.assertEquals(actual.get("line item"), expected.getName(), "Incorrect item " + expected.getName());
            Assert.assertEquals(actual.get("type"), expected.getType().getShortName());
        }
    }


    /**
     * set and save data
     *
     * @param page
     * @param data
     */
    public static void setSaveMetricInstanceData(MetricInstancePage page, MetricInstance.DataItemsList data) {
        setSaveMetricInstanceData(page, data, true);
    }

    public static void setSaveMetricInstanceData(MetricInstancePage page, MetricInstance.DataItemsList data, boolean doCheckAll) {
        for (int i = 0; i < SAVING_ATTEMPTS_NUMBER; i++) {
            setMetricInstanceData(page, data);
            validateMetricInstanceData(page, data, doCheckAll);
            if (page.getGrid().save()) {
                return;
            }
            PSLogger.knis(80390);
            PSLogger.warn("Saving unsuccessful. Try again (attempt #" + (i + 1) + ")");
            page.getGrid().reset();
        }
        Assert.fail("Can't set data to metric");
    }

    /**
     * @param page
     * @param data
     */
    public static void setMetricInstanceData(MetricInstancePage page, MetricInstance.DataItemsList data) {
        PSLogger.info("Enter data : " + data.getConfig());
        // enter data:
        for (MetricInstance.ItemRow dataRow : data) {
            MetricInstancePage.Grid.Row row;
            if (dataRow.hasParent()) {
                MetricInstancePage.Grid.Row parent = page.getGrid().getRow(dataRow.getParent().getName());
                Assert.assertNotNull(parent, "Can't find item " + dataRow.getName());
                parent.expand();
                row = parent.getRow(dataRow.getName());
            } else {
                row = page.getGrid().getRow(dataRow.getName());
            }
            Assert.assertNotNull(row, "Can't find item " + dataRow.getName());

            for (MetricInstance.ItemCell dataCell : dataRow.getCells()) {
                MetricInstancePage.Grid.Row.Cell cell = row.getCell(dataCell.getColumn().hashCode());
                cell.setTxt(dataCell.getValue());
            }
        }

        // validate edit values:
        for (MetricInstance.ItemRow dataRow : data) {
            MetricInstancePage.Grid.Row row;
            if (dataRow.hasParent()) {
                MetricInstancePage.Grid.Row parent = page.getGrid().getRow(dataRow.getParent().getName());
                Assert.assertNotNull(parent, "Can't find item " + dataRow.getName());
                parent.expand();
                row = parent.getRow(dataRow.getName());
            } else {
                row = page.getGrid().getRow(dataRow.getName());
            }
            Assert.assertNotNull(row, "Can't find item " + dataRow.getName());

            for (MetricInstance.ItemCell dataCell : dataRow.getCells()) {
                MetricInstancePage.Grid.Row.Cell cell = row.getCell(dataCell.getColumn().hashCode());
                String actual = cell.getValue();
                Assert.assertEquals(actual, dataCell.getEditValue(), "Incorrect edit value on cell " + cell + ". (expected " + dataCell.getDValue() + ")");
            }
        }
    }

    /**
     * validate data on specified view.
     *
     * @param page
     * @param data
     */
    public static void validateMetricInstanceData(MetricInstancePage page, MetricInstance.DataItemsList data) {
        validateMetricInstanceData(page, data, true);
    }

    /**
     * @param page     - PSPage
     * @param data     - Data
     * @param checkAll - if true check all existing rows, otherwise only specified in data
     */
    public static void validateMetricInstanceData(MetricInstancePage page, MetricInstance.DataItemsList data, boolean checkAll) {
        PSLogger.info("Work: " + data.getWork().getConfig());

        List<MetricInstancePage.Grid.Header> actualHeader = page.getGrid().getHeader();
        List<MetricInstance.Column> expectedHeader = data.getHeader();
        PSLogger.info("Header : " + actualHeader);
        Assert.assertEquals(actualHeader.size(), expectedHeader.size(), "Incorrect header, actual=" + actualHeader + ", expected=" + expectedHeader);
        for (int i = 0; i < expectedHeader.size(); i++) {
            MetricInstance.Column h = expectedHeader.get(i);
            if (h.getName() == null) continue;
            Assert.assertEquals(actualHeader.get(i).getMonth(), h.getName(), "Incorrect header name for column #" + i);
        }

        // validate only for single view. data can be unsaved.
        for (MetricInstancePage.Grid.Row row : page.getGrid().getRows()) {
            String child = null;
            String parent = row.getName();
            if (row.hasParent()) {
                child = parent;
                parent = row.getParent().getName();
                row.getParent().expand();
            }
            if (!checkAll && !data.hasRow(parent, child)) {
                continue; // skipp this row
            }
            String aTitle = row.getTitle();
            String eTitle = null;
            PSLogger.info("Actual Formula/Description for " + row.getFullName() + " is " + "'" + aTitle + "'");

            MetricInstance.ItemRow dataRow = data.getRow(parent, child);
            if (dataRow != null) eTitle = dataRow.getTitle();
            PSLogger.info("Expected Formula/Description '" + eTitle + "'");
            Assert.assertEquals(aTitle, eTitle, "Incorrect title for row " + (dataRow != null ? dataRow.getName() : ""));

            for (MetricInstancePage.Grid.Row.Cell cell : row.getCells()) {
                MetricInstance.ItemCell expectedCell = data.getCell(parent, child, cell.getColumn().getIndex());
                String actual = cell.getTxt();
                String expected = expectedCell.getResultValue();
                String msg = dataRow != null && dataRow.hasFormula() ? "Calculated value is incorrect" : "Incorrect value";
                Assert.assertEquals(actual, expected, msg + " for cell " + cell +
                        ". (expected " + expectedCell.getDValue() + ")");
            }
        }

    }

    public static void setLock(MetricInstance mi, boolean yes) {
        Assert.assertNotNull(mi.getWork(), "No work in instance");
        MetricInstancePage page = open(mi, true);
        MetricInstancePage.EditProperties dialog = page.edit();
        dialog.openPropertiesTab().lock(yes);
        dialog.update();
        mi.setLocked(yes);
    }

    public static void setReadyForRollup(MetricInstance mi, boolean yes) {
        Assert.assertNotNull(mi.getWork(), "No work in instance");
        MetricInstancePage page = open(mi, true);
        MetricInstancePage.EditProperties dialog = page.edit();
        dialog.openPropertiesTab().readyForRollup(yes);
        dialog.update();
        mi.setReadyForRollup(yes);
    }

    public static List<MetricInstance> collectInstances(Work w) {
        SummaryWorkPage sum = WorkManager.open(w);
        MetricsPage page = sum.openManageMetrics();
        List<MetricInstance> res = new ArrayList<MetricInstance>();
        List<MetricInstance> allInstances = TestSession.getMetricList();
        List<MetricTemplate> allTemplates = TestSession.getMetricTemplateList();
        Map<String, MetricInstance> _instances = new HashMap<String, MetricInstance>();
        Map<String, MetricTemplate> _templates = new HashMap<String, MetricTemplate>();
        for (MetricInstance mi : allInstances) {
            if (w.equals(mi.getWork())) {
                _instances.put(mi.getName(), mi);
            }
        }
        for (MetricTemplate mt : allTemplates) {
            _templates.put(mt.getName(), mt);
        }
        for (String s : page.getMetrics()) {
            if (_instances.containsKey(s)) {
                res.add(_instances.get(s));
            } else if (_templates.containsKey(s)) {
                MetricInstance mi = new MetricInstance(w, _templates.get(s));
                mi.setCreated();
                res.add(mi);
            } else {
                MetricTemplate mt = new MetricTemplate(s, null, null);
                mt.setCreated();
                MetricInstance mi = new MetricInstance(w, mt);
                mi.setCreated();
                res.add(mi);
            }
        }
        return res;
    }

}
