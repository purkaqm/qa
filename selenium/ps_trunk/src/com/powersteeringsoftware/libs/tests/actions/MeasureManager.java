package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.objects.measures.MeasureInstance;
import com.powersteeringsoftware.libs.objects.measures.MeasureTemplate;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class MeasureManager {


    /**
     * <p>Create Measure. You can select fields:
     * -name,<br>
     * -description,<br>
     * -data,<br>
     * -collection,<br>
     * -indicator type.<br>
     * </p>
     * <p>You mustn't call Selenium API method waitForPageToLoad(...) after this method.</p>
     * <p>Before using this method you have to navigate to the page "Measure
     * Template: Library" or to the page "Manage Measures".</p>
     *
     * @param mtName                 -
     *                               the name for new measure
     * @param mtDescription          - the measure description
     * @param isDataCollectionManual -
     *                               the data collection of new measure template.
     * @param indicatorType          -
     *                               the indicator type of new measure template.
     * @return name of the created measure/measure template
     */
    @Deprecated
    private static MeasuresPage processMeasure(String mtName,
                                               String mtDescription,
                                               boolean isDataCollectionManual,
                                               int indicatorType,
                                               AddEditMeasureInstancePage.AbstractAddEditMeasureBlock editAdapter) {

        if (isDataCollectionManual) {
            editAdapter.setDataCollectionManual();
        } else {
            editAdapter.setDataCollectionFormula();
        }

        if (indicatorType == 0) {
            editAdapter.setIndicatorTypeGoal();
            editAdapter.setThresholdGoal1(50);
            editAdapter.setThresholdGoal2(100);
        } else if (indicatorType == 1) {
            editAdapter.setIndicatorTypeVariance();
            editAdapter.setThresholdVariance1(100);
            editAdapter.setThresholdVariance2(200);
            editAdapter.setThresholdVariance3(300);
            editAdapter.setThresholdVariance4(400);
        }

        editAdapter.setName(mtName);
        editAdapter.setDescription(mtDescription);
        MeasuresPage list = editAdapter.submit();
        list.testUrl();
        Assert.assertTrue(list.getMeasureLink(mtName) != null, "Can't find link " + mtName + " after creating");
        PSLogger.save("After creating " + mtName);
        return list;
    }

    @Deprecated
    private static MeasureTemplatesPage createMeasureTemplate(String mName, String mDescr, boolean isManual, int indicatorType) {
        MeasureTemplatesPage page = new MeasureTemplatesPage();
        page.open();
        AddEditMeasureTemplatePage edit = page.addNew();
        return (MeasureTemplatesPage) processMeasure(mName, mDescr, isManual, indicatorType, edit.getContent());
    }


    @Deprecated
    public static MeasureTemplatesPage createMeasureTemplateManualGoal(String mName, String mDescr) {
        return createMeasureTemplate(mName, mDescr, true, 0);
    }


    @Deprecated
    public static MeasureTemplatesPage createMeasureTemplateFormulaGoal(String mName, String mDescr) {
        return createMeasureTemplate(mName, mDescr, false, 0);
    }

    public static AddEditMeasureInstancePage openInstanceEditPage(String name, Work work) {
        MeasureInstancesPage list = WorkManager.open(work).openManageMeasure();
        MeasuresPage.AbstractMeasureMenu menu = list.openMenu(name);
        AddEditMeasurePage res = menu.edit();
        PSLogger.save("Edit Measure " + name + " instance for " + work);
        return (AddEditMeasureInstancePage) res;
    }


    @Deprecated
    public static MeasureTemplatesPage createMeasureTemplateManualVariance(String mName, String mDescr) {
        return createMeasureTemplate(mName, mDescr, true, 1);
    }


    @Deprecated
    public static MeasureTemplatesPage createMeasureTemplateFormulaVariance(String mName, String mDescr) {
        return createMeasureTemplate(mName, mDescr, false, 1);
    }


    /**
     * Attach measure template to the project
     *
     * @param mtName
     * @param wiId   - work item UID
     */
    @Deprecated
    public static MeasureInstancesPage attachMeasureTemplateByName(Work work, String mtName) {
        MeasureInstancesPage list = WorkManager.open(work).openManageMeasure();
        MeasureInstancesPage.AttachMeasureInstancesPage attach = list.attach();
        attach.testUrl();
        list = attach.attach(mtName);
        list.testUrl();
        Assert.assertTrue(list.getMeasureLink(mtName) != null, "Can't find link " + mtName + " after attaching");
        PSLogger.save("After attaching " + mtName + " to " + work);
        return list;
    }

    public static List<MeasureInstance> getMeasures(Work work) {
        List<MeasureInstance> res = new ArrayList<MeasureInstance>();
        for (Measure m : TestSession.getMeasuresList()) {
            if (!m.exist()) continue;
            if (m.isLibrary()) continue;
            if (work.equals(((MeasureInstance) m).getWork())) {
                res.add((MeasureInstance) m);
            }
        }
        return res;
    }

    public static boolean isMeasureTemplateExist(Measure template) {
        MeasureTemplatesPage list = openTemplates();
        return list.getMeasureLink(template.getName()) != null;
    }

    public static void copyMeasureTemplate(MeasureTemplate source, MeasureTemplate destination) {
        PSLogger.info("Copy template " + source + ". New name is " + destination.getName());
        MeasureTemplatesPage list = openTemplates();
        AddEditMeasureTemplatePage edit = list.openMenu(source.getName()).copy();
        setAndSubmitMeasure(edit.getContent(), destination);
        destination.merge(source.copy(), false); // now destination has the same fields as source
        destination.setCreated();
    }

    public static void editMeasureInstance(MeasureInstance instance, Measure edit) {
        PSLogger.info("Set new data to instance " + instance);
        MeasureInstancesPage list = openInstances(instance);
        AddEditMeasureInstancePage.AbstractAddEditMeasureBlock content = list.openMenu(instance.getName()).edit().getContent();
        editMeasure(instance, edit, content);
    }

    public static void editMeasureTemplate(MeasureTemplate template, Measure edit) {
        PSLogger.info("Set new data to template " + template);
        MeasureTemplatesPage list = openTemplates();
        AddEditMeasureInstancePage.AbstractAddEditMeasureBlock content = list.openMenu(template.getName()).edit().getContent();
        editMeasure(template, edit, content);
    }


    private static void editMeasure(Measure init, Measure edit, AddEditMeasureInstancePage.AbstractAddEditMeasureBlock content) {
        setMeasure(content, edit);
        PSLogger.save("Before saving measure " + init);
        MeasuresPage list = content.submit();
        list.testUrl();
        Assert.assertTrue(list.getMeasureLink(init.getName()) != null, "Can't find link " + init + " after creating");
        init.setCreated();
        init.merge(edit);
    }


    public static void deleteMeasureTemplate(MeasureTemplate template, boolean delete) {
        PSLogger.info("Delete template " + template + (delete ? " with all children." : ""));
        MeasureTemplatesPage list = openTemplates();
        MeasuresPage.AbstractMeasureMenu.DeleteDialog dialog = list.openMenu(template.getName()).delete();
        if (delete)
            dialog.delete();
        else
            dialog.detach();
        dialog.commit();

        // assert MT is deleted
        Assert.assertNull(list.getMeasureLink(template.getName()), "Measure template " + template + " hasn't been deleted.");
        template.setDeleted();

        //assert attached measure is not deleted
        for (MeasureInstance i : template.getInstances()) {
            MeasureInstancesPage list2 = openInstances(i);
            if (delete) {
                Assert.assertNull(list2.getMeasureLink(i.getName()), "Attached Measure " + i + " has not been deleted.");
                i.setDeleted();
            } else {
                Assert.assertNotNull(list2.getMeasureLink(i.getName()), "Attached Measure " + i + " has been deleted!");
            }
        }
    }

    public static void deleteMeasureInstance(MeasureInstance instance) {
        deleteMeasureInstance(instance, true);
    }

    public static void deleteMeasureInstance(MeasureInstance instance, boolean doCheck) {
        MeasureInstancesPage list = openInstances(instance);
        deleteMeasureInstance(list, instance, doCheck);
    }

    public static void deleteMeasureInstance(MeasureInstancesPage list, MeasureInstance instance, boolean doCheck) {
        PSLogger.info("Remove instance " + instance);
        MeasureInstancesPage.MeasureInstanceManageMenu menu = (MeasureInstancesPage.MeasureInstanceManageMenu) list.openMenu(instance.getName(), instance.getId());
        menu.remove().yes();
        if (!doCheck) return;
        Assert.assertNull(list.getMeasureLink(instance.getName()), "There is instance after deleting " + instance);
        instance.setDeleted();
    }

    public static MeasureInstance attachMeasureTemplate(MeasureTemplate template, Work work) {
        PSLogger.info("Attach template " + template + " to work " + work);
        MeasureInstancesPage list = openInstances(work);
        return attachMeasureTemplate(list, template, work);
    }

    public static MeasureInstance attachMeasureTemplate(MeasureInstancesPage list, MeasureTemplate template, Work work) {
        PSLogger.info("Attach template " + template + " to work " + work);
        MeasureInstancesPage.AttachMeasureInstancesPage attach = list.attach();
        attach.testUrl();
        list = attach.attach(template.getName());
        list.testUrl();
        Assert.assertTrue(list.getMeasureLink(template.getName()) != null, "Can't find link " + template.getName() + " after attaching");
        PSLogger.save("After attaching " + template.getName() + " to " + work);
        MeasureInstance res = template.toInstance();
        res.setWork(work);
        template.addInstance(res);
        res.setCreated();
        return res;
    }


    public static MeasureTemplatesPage createMeasureTemplate(MeasureTemplate template) {
        PSLogger.info("Create template " + template);
        MeasureTemplatesPage page = openTemplates();
        AddEditMeasureTemplatePage edit = page.addNew();
        return (MeasureTemplatesPage) setAndSubmitMeasure(edit.getContent(), template);
    }

    public static MeasureInstancesPage createMeasureInstance(MeasureInstance instance) {
        PSLogger.info("Define new measure instance " + instance);
        SummaryWorkPage sum = WorkManager.open(instance.getWork());
        MeasureInstancesPage list = sum.openManageMeasure();
        AddEditMeasureInstancePage edit = list.addNew();
        return (MeasureInstancesPage) setAndSubmitMeasure(edit.getContent(), instance);
    }

    private static void setMeasure(AddEditMeasureInstancePage.AbstractAddEditMeasureBlock editAdapter, Measure measure) {
        if (measure.hasIndicatorType()) {
            if (measure.isManual()) {
                editAdapter.setDataCollectionManual();
            } else {
                editAdapter.setDataCollectionFormula();
            }
        }
        if (measure.hasThresholds()) {
            List<MeasureTemplate.Threshold> thresholds = measure.getThresholds();
            if (measure.getIndicatorType().equals(MeasureTemplate.IndicatorType.GOAL)) {
                editAdapter.setIndicatorTypeGoal();
                editAdapter.setThresholdGoal1(thresholds.get(0).getValue());
                editAdapter.setThresholdGoal2(thresholds.get(1).getValue());
            } else if (measure.getIndicatorType().equals(MeasureTemplate.IndicatorType.VARIANCE)) {
                editAdapter.setIndicatorTypeVariance();
                editAdapter.setThresholdVariance1(thresholds.get(0).getValue());
                editAdapter.setThresholdVariance2(thresholds.get(1).getValue());
                editAdapter.setThresholdVariance3(thresholds.get(2).getValue());
                editAdapter.setThresholdVariance4(thresholds.get(3).getValue());
            } else {
                editAdapter.setIndicatorTypeNone();
            }
        }
        if (measure.hasDisplayFormat()) {
            editAdapter.setDisplayFormat(measure.getDisplayFormat().getIndex());
        }
        if (measure.hasEffectiveDates()) {
            editAdapter.setEffectiveDates(measure.getEffectiveDates().ordinal(), measure.getEffectiveStartDate(), measure.getEffectiveEndDate());
        }

        if (measure.isManual()) {
            if (measure.hasReminderSchedule()) {
                processSchedule(measure.getReminderSchedule(), editAdapter.getReminderSchedule());
            }
        } else {
            if (measure.hasTestSchedule()) {
                processSchedule(measure.getTestSchedule(), editAdapter.getTestSchedule());
            }
            if (measure.hasHistorySchedule()) {
                processSchedule(measure.getHistorySchedule(), editAdapter.getHistorySchedule());
            }
        }
        if (measure.getName() != null)
            editAdapter.setName(measure.getName());
        if (measure.getDescription() != null)
            editAdapter.setDescription(measure.getDescription());
        if (measure.getUnitsMessage() != null) {
            editAdapter.setUnits(measure.getUnitsMessage());
        }
    }

    private static MeasuresPage setAndSubmitMeasure(AddEditMeasureInstancePage.AbstractAddEditMeasureBlock editAdapter, Measure measure) {
        setMeasure(editAdapter, measure);
        PSLogger.save("Before saving measure " + measure);
        MeasuresPage list = editAdapter.submit();
        list.testUrl();
        Assert.assertTrue(list.getMeasureLink(measure.getName()) != null, "Can't find link " + measure + " after creating");
        PSLogger.save("After creating " + measure.getName());
        measure.setCreated();
        return list;
    }

    private static void processSchedule(Measure.Schedule s, AddEditMeasurePage.AbstractAddEditMeasureBlock.ScheduleElement e) {
        Assert.assertNotNull(e, "Can't find schedule " + s.getType());
        PSLogger.info("Process schedule " + e.getTitle());
        if (s.isNever()) {
            e.setNever();
        } else if (s.isDaily()) {
            e.setDaily();
            e.setDailyEvery(s.getEvery());
            e.setTimeOfDay(s.getTime());
        }
        //todo: for other elements
    }

    public static MeasureInstancesPage openInstances(MeasureInstance instance) {
        Work work = instance.getWork();
        if (work == null) throw new IllegalArgumentException("No work in instance " + instance);
        return openInstances(work);
    }

    public static MeasureInstancesPage openInstances(Work work) {
        if (work.getId() != null) {
            MeasureInstancesPage res = new MeasureInstancesPage();
            if (res.isThisPage(work.getId())) return res;
        }
        SummaryWorkPage sum = WorkManager.open(work);
        return sum.openManageMeasure();
    }

    public static MeasureInstancePage openInstance(MeasureInstance instance) {
        Work work = instance.getWork();
        if (work == null) throw new IllegalArgumentException("No work in instance " + instance);
        SummaryWorkPage sum = WorkManager.open(work);
        return sum.openMeasure(instance.getName());
    }

    public static MeasureTemplatesPage openTemplates() {
        MeasureTemplatesPage res = new MeasureTemplatesPage();
        if (res.isThisPage()) return res;
        res.open();
        return res;
    }

    public static void validateMeasureInstance(MeasureInstance instance) {
        MeasureInstancesPage list = openInstances(instance);
        MeasuresPage.AbstractMeasureMenu menu = list.openMenu(instance.getName());
        AddEditMeasurePage edit = menu.edit();
        PSLogger.save("Edit Measure " + instance.getName());
        validateMeasure(edit.getContent(), instance);
    }

    public static void validateMeasureTemplate(MeasureTemplate template) {
        MeasureTemplatesPage page = openTemplates();
        // open Measure Template for editing and check all fields
        AddEditMeasureTemplatePage edit = page.openMenu(template.getName()).edit();
        validateMeasure(edit.getContent(), template);
    }

    private static void validateMeasure(AddEditMeasureInstancePage.AbstractAddEditMeasureBlock editAdapter, Measure measure) {
        String testedName = editAdapter.getName();
        Assert.assertEquals(testedName, measure.getName(), "Incorrect name for " + measure);
        String descAct = editAdapter.getDescription();
        String descExp = measure.getDescription();
        String unitsExp = measure.getUnitsMessage();
        String unitsAct = editAdapter.getUnits();
        if (descExp == null) descExp = "";
        Assert.assertEquals(descAct, descExp, "Incorrect description for " + measure);
        if (unitsExp == null) unitsExp = "";
        Assert.assertEquals(unitsAct, unitsExp, "Incorrect units message for " + measure);

        boolean dataCollectionFieldId = editAdapter.getDataCollectionType();
        Assert.assertEquals(dataCollectionFieldId, measure.isManual(), "Incorrect Data Collection option for " + measure);

        Measure.IndicatorType indicatorTypeFieldId = editAdapter.getIndicatorTypeType();
        Assert.assertEquals(indicatorTypeFieldId, measure.getIndicatorType(), "Incorrect Indicator Type for " + measure);
        List<Measure.Threshold> thresholds = measure.getThresholds();
        if (measure.isGoal()) {
            checkThreshold(editAdapter.getThresholdGoal1(), 0, thresholds, measure);
            checkThreshold(editAdapter.getThresholdGoal2(), 1, thresholds, measure);
        } else {
            checkThreshold(editAdapter.getThresholdVariance1(), 0, thresholds, measure);
            checkThreshold(editAdapter.getThresholdVariance2(), 1, thresholds, measure);
            checkThreshold(editAdapter.getThresholdVariance3(), 2, thresholds, measure);
            checkThreshold(editAdapter.getThresholdVariance4(), 3, thresholds, measure);
        }

        if (measure.isManual()) {
            AddEditMeasurePage.AbstractAddEditMeasureBlock.ScheduleElement sc = editAdapter.getReminderSchedule();
            Assert.assertNotNull(sc, "Can't find reminder schedule");
            if (measure.hasReminderSchedule()) {
                checkSchedule(measure.getReminderSchedule(), sc);
            }
        } else {
            AddEditMeasurePage.AbstractAddEditMeasureBlock.ScheduleElement sc1 = editAdapter.getTestSchedule();
            AddEditMeasurePage.AbstractAddEditMeasureBlock.ScheduleElement sc2 = editAdapter.getHistorySchedule();
            Assert.assertNotNull(sc1, "Can't find test schedule");
            Assert.assertNotNull(sc2, "Can't find history schedule");
            if (measure.hasTestSchedule()) {
                checkSchedule(measure.getTestSchedule(), sc1);
            }
            if (measure.hasHistorySchedule()) {
                checkSchedule(measure.getHistorySchedule(), sc2);
            }
        }

        editAdapter.cancelChangesWithUpperButton();
    }

    private static void checkSchedule(Measure.Schedule sc, AddEditMeasurePage.AbstractAddEditMeasureBlock.ScheduleElement e) {
        Assert.assertEquals(e.getSelectedType().ordinal(), sc.getIndex(), "Incorrect type selected. should be " + sc.getName());
        if (sc.isDaily()) {
            if (sc.getEvery() != null) {
                Assert.assertEquals(e.getDailyEvery(), sc.getEvery(), "Incorrect Every day selected");
            }
            if (sc.getTime() != null) {
                Assert.assertEquals(e.getTimeOfDay(), sc.getTime(), "Incorrect time-of-day selected. should be " + sc.getTimeName());
            }
        }
        // todo: add validation for other elements.
    }

    private static void checkThreshold(double actual, int index, List<Measure.Threshold> thresholds, Measure measure) {
        Assert.assertEquals(actual, thresholds.get(index).getValue(), "Incorrect threshold #" + (index + 1) + " for measure " + measure);
    }


}
