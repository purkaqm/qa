package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.FrameLocators;
import com.powersteeringsoftware.libs.enums.page_locators.WorkTemplates93PageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.WorkTemplates93PageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 01.10.2010
 * Time: 13:31:08
 */
public class WorkTemplates93Page extends WorkTemplatesPage {

    WorkTemplates93Page() {
        if (TestSession.isVersionPresent() && TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
            PSSkipException.skip(getClass().getSimpleName() + ": is only for <9.3");
        }
    }

    private abstract class AFrame extends Frame {
        private AFrame() {
            super(FrameLocators.IFRAME);
        }

        public void waitForReload() {
            waitForReload(WorkTemplates93Page.this);
        }

        protected void checkErrors() {
            Element err = new Element(ERROR);
            if (err.exists()) {
                StringBuffer sb = new StringBuffer();
                setDefaultElement();
                for (Element e : Element.searchElementsByXpath(this, ERROR)) {
                    PSLogger.warn(e.getDEText());
                    sb.append(e.getDEText()).append("\n");
                }
                Assert.fail("Has errors: " + sb);
            }
        }
    }

    public class ListFrame extends AFrame {

        public List<TemplateElement> getTemplates() {
            List<TemplateElement> list = new ArrayList<TemplateElement>();
            setDefaultElement();
            for (Element details : Element.searchElementsByXpath(this, TEMPLATE_DETAILS_LINK_IMG)) {
                TemplateElement e = new TemplateElement(details.getParent());
                list.add(e);
                Element parent = e.getParent();
                e.name = parent.getChildByXpath(TEMPLATE_DETAILS_LINK_PARENT_NAME).getDEText();
                e.isReady = !parent.getChildByXpath(TEMPLATE_STRUCTURE_MESSAGE).isDEPresent();
            }
            return list;
        }

        public Link getTemplateLinkByName(String name) {
            List<TemplateElement> list = getTemplates();
            for (TemplateElement template : list) {
                if (name.equals(template.name)) return template;
            }
            return null;
        }

        public DetailsFrame details(String name) {
            Link link = getTemplateLinkByName(name);
            if (((ListFrame.TemplateElement) link).isReady) return null;
            link.click(false);
            waitForReload();
            return new DetailsFrame();
        }

        public CreateNewFrame createNew() {
            Link link = new Link(CREATE_NEW_BUTTON_LINK);
            link.waitForPresent(5000);
            link.focus();
            link.click(false);
            waitForLoad();
            return new CreateNewFrame();
        }


        private class TemplateElement extends Link {
            private String name;
            private boolean isReady;

            protected TemplateElement(Element e) {
                super(e);
                setFrame(this);
            }

            @Override
            public String toString() {
                return name + (isReady ? "" : "(not completed)");
            }
        }
    }

    public class CreateNewFrame extends ACreateNewFrame {
        public DetailsFrame submit() {
            doSubmit();
            waitForReload();
            return new DetailsFrame();
        }
    }

    private abstract class ACreateNewFrame extends AFrame {

        public void setName(String name) {
            Input in = new Input(NAME_INPUT_FIELD);
            in.waitForVisible();
            in.type(name);
        }

        public void enableSelectChildrenInput() {
            new RadioButton(SELECT_CHILDREN_INPUT_RADIOBUTTON_YES).click(false);
        }

        public void disableSelectChildrenInput() {
            new RadioButton(SELECT_CHILDREN_INPUT_RADIOBUTTON_NO).click(false);
        }

        public void enableRequireDateEndDatesInput() {
            new RadioButton(REQUIRED_DATE_END_DATES_INPUT_RADIOBUTTON_YES).click(false);
        }

        public void disableRequireDateEndDatesInput() {
            new RadioButton(REQUIRED_DATE_END_DATES_INPUT_RADIOBUTTON_NO).click(false);
        }

        public void setControlCost(boolean doSet) {
            PSLogger.info("Set Control Cost to " + doSet);
            if (doSet) {
                new RadioButton(CONTROL_COST_RADIOBUTTON_YES).click();
            } else {
                new RadioButton(CONTROL_COST_RADIOBUTTON_NO).click();
            }
        }

        public void setManualScheduled(boolean doSet) {
            PSLogger.info("Set Manual Scheduling to " + doSet);
            if (doSet) {
                new RadioButton(MANUAL_SHEDULING_RADIOBUTTON_YES).click();
            } else {
                new RadioButton(MANUAL_SHEDULING_RADIOBUTTON_NO).click();
                if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
                    waitForReload();
                }
            }
        }


        protected void doSubmit() {
            PSLogger.save("Before submitting container");
            Button submit = new Button(SUBMIT_LINK_BUTTON);
            submit.setFrame(this);
            submit.waitForVisible(5000);
            submit.click(false);
        }
    }

    public class DetailsFrame extends AFrame {

        public SelectWorkTypeFrame create() {
            PSLogger.info("Create template in the container");
            Button create = new Button(new Link(RELATED_PROJECTS_CREATE_BUTTON_LINK));
            create.setFrame(this);
            create.waitForVisible();
            create.click(false);
            waitForReload();
            return new SelectWorkTypeFrame();
        }

    }

    public class SelectWorkTypeFrame extends AFrame {

        public GatedDetailsFrame setGatedProjectESG() {
            return setBasicType(SELECT_WORK_TYPE_GATED_PROJECT_ESG.getLocator());
        }

        public GatedDetailsFrame setGatedProjectNESG() {
            return setBasicType(SELECT_WORK_TYPE_GATED_PROJECT_NOT_ESG.getLocator());
        }

        public GatedDetailsFrame setWork() {
            return setBasicType(SELECT_WORK_TYPE_WORK.getLocator());
        }

        public GatedDetailsFrame setFolder() {
            return setBasicType(SELECT_WORK_TYPE_FOLDER.getLocator());
        }

        public GatedDetailsFrame setBasicType(String type) {
            Link link = new Link(SELECT_WORK_TYPE_BASIC.replace(type));
            link.setFrame(this);
            link.setDefaultElement();
            String sType = link.getParent().getChildByXpath(SELECT_WORK_TYPE_LABEL).getDEText();
            PSLogger.info("Select '" + sType + "'");
            link.click(false);
            waitForReload();
            return new GatedDetailsFrame();
        }

    }

    public class GatedDetailsFrame extends ACreateNewFrame {

        public void setUseStatusReporting(Work.StatusReportingFrequency statusReporting) {
            new Element(STATUS_REPORTING_CHOOSER).click(false);
            SelectInput select = new SelectInput(STATUS_REPORTING_SELECT);
            ILocatorable loc = null;
            switch (statusReporting) {
                case NO_FREQUENCY:
                    loc = STATUS_REPORTING_NO_FREQUENCY;
                    break;
                case WEEKLY:
                    loc = STATUS_REPORTING_WEEKLY;
                    break;
                case BIWEEKLY:
                    loc = STATUS_REPORTING_BIWEEKLY;
                    break;
                case MONTHLY:
                    loc = STATUS_REPORTING_MONTHLY;
                    break;
                case QUARTERLY:
                    loc = STATUS_REPORTING_QUARTERLY;
                    break;
            }
            if (loc != null)
                select.select(loc);
        }

        public void setInheritPermissions(boolean doSet) {
            PSLogger.info("Set Inherit Permissions to " + doSet);
            if (doSet) {
                new RadioButton(INHERIT_PERMISSIONS_RADIOBUTTON_YES).click();
            } else {
                new RadioButton(INHERIT_PERMISSIONS_RADIOBUTTON_NO).click();
            }
        }

        public void setInheritControls(boolean doSet) {
            PSLogger.info("Set Inherit Controls to " + doSet);
            if (doSet) {
                new RadioButton(INHERIT_CONTROLS_RADIOBUTTON_YES).click();
                if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
                    // refresh:
                    waitForReload();
                }
            } else {
                new RadioButton(INHERIT_CONTROLS_RADIOBUTTON_NO).click();
            }
        }

        public void setWebFolder(boolean doSet) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
                // hotfix for 69326
                return;
            }
            PSLogger.info("Set Web Folder to " + doSet);
            if (doSet) {
                new RadioButton(WEB_FOLDER_RADIOBUTTON_YES).click();
            } else {
                new RadioButton(WEB_FOLDER_RADIOBUTTON_NO).click();
            }
        }

        public void setScheduleConstraint(String value) {
            PSLogger.info("Set constraint '" + value + "'");
            SelectInput select = new SelectInput(NAME_DATE_CONSTRAINT_TYPE);
            select.setFrame(this);
            String label = StrUtil.replaceSpaces(select.getSelectedLabel());
            if (label.startsWith(value)) {
                PSLogger.debug("Constraint '" + value + "' is already selected");
                return;
            }
            Assert.assertTrue(select.hasOption(value), "Can't find option " + value);
            select.selectValue(value);
            // refresh page:
            waitForReload();
        }

        /**
         * Select process by default (this method select first process in the
         * process list)
         */
        public void setProcess(String name) {
            PSLogger.info("Set Process " + name);
            select();
            if (StringUtils.isBlank(name)) {
                RadioButton rb = new RadioButton(SELECTION_PROCESSES_RADIOBUTTON_LOCATOR);
                rb.waitForVisible();
                rb.click();
                return;
            }
            setDefaultElement();
            int i = 1;
            for (Element e : Element.searchElementsByXpath(this, SELECTION_PROCESSES_LABEL_LOCATOR)) {
                String label = e.getDEText();
                if (name.equals(label)) {
                    PSLogger.debug("label for process " + label + " found");
                    break;
                }
                i++;
            }
            RadioButton rb = new RadioButton(SELECTION_PROCESSES_RADIOBUTTONS.replace(i));
            rb.click();
        }

        private void setConstraintDate(WorkTemplates93PageLocators loc, String date) {
            Input in = new Input(loc);
            if (in.exists() && in.isVisible()) {
                in.type(date);
            }
        }

        public void setConstraintStart(String date) {
            setConstraintDate(NAME_DATE_CONSTRAINT_START, date);
        }

        public void setConstraintEnd(String date) {
            setConstraintDate(NAME_DATE_CONSTRAINT_END, date);
        }

        public SummaryWorkPage submit() {
            try {
                SummaryWorkPage res = SummaryWorkPage.getInstance();
                doSubmit();
                waitForUnLoad(res);
                new TimerWaiter(2000).waitTime(); // for ie9
                return res;
            } finally {
                checkErrors();
            }
        }

    }

    public ListFrame getFirstFrame() {
        return new ListFrame();
    }

    public SummaryWorkPage openSummaryForTemplate(String name, boolean doCheck) {
        ListFrame frame = getFirstFrame();
        Link link = frame.getTemplateLinkByName(name);
        SummaryWorkPage res = SummaryWorkPage.getInstance(doCheck);
        link.setResultPage(res);
        if (!((ListFrame.TemplateElement) link).isReady) return null;
        frame.select();
        link.click(false);
        frame.waitForUnLoad(res);
        return res;
    }

}
