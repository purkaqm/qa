package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;


public class WorkTemplateManager {

    public static SummaryWorkPage create(Template template) {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
            return create94(template);
        } else {
            return create93(template);
        }
    }

    private static SummaryWorkPage create94(Template template) {
        WorkTemplates94Page templates = (WorkTemplates94Page) openTemplates();
        CreateWorkTemplatePage page = templates.createNew();

        Work work = template.getStructure();
        page.setName(template.getName());
        if (template.getDescription() != null)
            page.setDescription(template.getDescription());
        //page.setRootWorkName(work.getName());

        if (work.isFolder()) {
            page.setFolder();
        } else if (work.isGated()) {
            if (((GatedProject) work).getEnforceSequential()) {
                page.setGatedProjectESG();
            } else {
                page.setGatedProjectNESG();
            }
            page.setProcess(((GatedProject) work).getProcess().getName());
        } else {
            page.setWork();
        }
        templates = page.submit();
        SummaryWorkPage sum1 = templates.openSummaryForTemplate(template.getName());
        WorkManager.checkSummarySaveButton(sum1, work);
        template.setCreated();
        try {
            template.setId(sum1.getUrlId());
            SummaryWorkPage sum2 = sum1.openSummaryFor(work.getName());
            WorkManager.checkSummarySaveButton(sum2, work);
            work.setId(sum2.getUrlId());
            return WorkManager.editProject(sum2, work);
        } catch (Exception e) {
            PSLogger.error(e);
            // default work settings. e.g. bug #89973 for ie
            if (work.isGated() && !((GatedProject) work).getEnforceSequential()) {
                work.setConstraint(Work.Constraint.SNET);
                work.setManualScheduling(false);
                work.setManualSchedulingForDependencies(false);
            }
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    private static SummaryWorkPage create93(Template template) {

        WorkTemplates93Page.DetailsFrame frame1 = createWorkTemplateContainer(template);

        WorkTemplates93Page.SelectWorkTypeFrame frame2 = frame1.create();

        Work work = template.getStructure();


        WorkTemplates93Page.GatedDetailsFrame frame3;
        if (work.isFolder()) {
            frame3 = frame2.setFolder();
        } else if (work.isGated()) {
            if (((GatedProject) work).getEnforceSequential()) {
                frame3 = frame2.setGatedProjectESG();
            } else {
                frame3 = frame2.setGatedProjectNESG();
            }
        } else {
            frame3 = frame2.setWork();
        }

        frame3.setName(work.getName());

        if (work.getStatusReporting() != null)
            frame3.setUseStatusReporting(work.getStatusReporting());

        frame3.setInheritPermissions(work.getInheritPermissions());


        frame3.setInheritControls(work.getInheritControls());
        if (!work.getInheritControls()) {
            frame3.setControlCost(work.getControlCost());
            frame3.setManualScheduled(work.getManualScheduling());
        }


        if (work.getWebFolder() != null)
            frame3.setWebFolder(work.getWebFolder());

        frame3.setScheduleConstraint(work.getConstraint().getValue());

        if (work.isConstraintEnd()) {
            frame3.setConstraintEnd(work.getConstraintEndDate());
        }

        if (work.isConstraintStart()) {
            frame3.setConstraintStart(work.getConstraintStartDate());
        }

        if (work.isGated())
            frame3.setProcess(((GatedProject) work).getProcess().getName());

        SummaryWorkPage res = frame3.submit();
        template.setCreated();
        template.setId(res.getUrlId());
        return res;
    }


    private static WorkTemplates93Page.DetailsFrame createWorkTemplateContainer(Template template) {
        WorkTemplates93Page templates = (WorkTemplates93Page) openTemplates();

        WorkTemplates93Page.ListFrame list = templates.getFirstFrame();
        list.select();

        WorkTemplates93Page.CreateNewFrame create = list.createNew();

        create.setName(template.getName());

        if (template.getIsShowSelectChildrenStep() != null) {
            if (template.getIsShowSelectChildrenStep()) {
                create.enableSelectChildrenInput();
            } else {
                create.disableSelectChildrenInput();
            }
        }

        if (template.getIsRequireGateEndDates() != null) {
            if (template.getIsRequireGateEndDates()) {
                create.enableRequireDateEndDatesInput();
            } else {
                create.disableRequireDateEndDatesInput();
            }
        }

        return create.submit();
    }

    public static WorkTemplatesPage openTemplates() {
        WorkTemplatesPage templates = WorkTemplatesPage.getInstance();
        templates.open();
        return templates;
    }

    public static WBSPage openWBS(Template t) {
        WBSPage res = open(t).openProjectPlanning();
        // now fix db
        WorkManager.checkSummarySaveButton(res.getGrid(), t.getStructure(), false);
        return res;
    }

    public static SummaryWorkPage open(Template t) {
        return open(t, true, false);
    }

    public static SummaryWorkPage open(Template t, boolean doCheck, boolean doRefresh) {
        PSLogger.info("Open summary page for template '" + t.getName() + "'");
        SummaryWorkPage sum;
        if (t.getId() != null) {
            sum = SummaryWorkPage.getInstance(doCheck, t.getId());
            if (sum.checkUrl()) {
                PSLogger.info("Summary for template " + t.getName() + " is already opened");
                if (doRefresh) {
                    PSLogger.debug("Refresh summary");
                    sum.refresh();
                }
                return sum;
            }
            if (AbstractWorkPage.useDirectLink()) {
                sum.open();
                return sum;
            }
        }
        WorkTemplatesPage templates = openTemplates();
        sum = templates.openSummaryForTemplate(t.getName(), doCheck);
        WorkManager.checkSummarySaveButton(sum, t.getStructure());
        t.setCreated();
        t.setId(sum.getUrlId());
        return sum;
    }


}