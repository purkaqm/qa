package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.DijitPopupLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.EditWorkPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 23.07.2010
 * Time: 17:16:54
 */
public class EditWorkPage extends AbstractWorkPage {

    private static final TimerWaiter SAVE_TIMEOUT = new TimerWaiter(20000);
    private static final TimerWaiter SUBMIT_TIMEOUT = new TimerWaiter(1500);
    private List<YesNoRadioButtons> buttons;
    private static boolean waitLoading = true;

    public static void setWaitLoading(boolean waitLoading) {
        EditWorkPage.waitLoading = waitLoading;
    }

    @Override
    public void open() {
        if (workId == null) throw new NullPointerException("url-id is not specified");
        open(makeUrl(URL, workId));
    }

    public void setName(String name) {
        new Input(WORK_NAME).type(name);
    }

    public void setObjective(String obj) {
        new TextArea(WORK_OBJECTIVE).setText(obj);
    }

    public void checkPlanResources() {
        getRadioButton(RESOURCE_PLANNING).select(true);
    }

    public void uncheckPlanResources() {
        getRadioButton(RESOURCE_PLANNING).select(false);
    }

    public SummaryWorkPage setManualScheduling(boolean onOrOff) {
        PSLogger.info("Set Manual Scheduling to " + (onOrOff ? "Yes" : "No"));
        selectManualScheduling(onOrOff);
        return submitChanges();
    }

    public SummaryWorkPage setPlanResources(boolean on) {
        PSLogger.info("Set Plan Resources to " + (on ? "Yes" : "No"));
        if (on) {
            checkPlanResources();
        } else {
            uncheckPlanResources();
        }
        return submitChanges();
    }


    public void checkManualScheduling() {
        selectManualScheduling(true);
    }

    public void uncheckManualScheduling() {
        selectManualScheduling(false);
    }

    public void selectManualScheduling(boolean yes) {
        YesNoRadioButtons rb;
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) {
            rb = getRadioButton(MANUAL_SCHEDULING);
        } else {
            rb = getRadioButton(MANUAL_SCHEDULING_93);
        }
        rb.select(yes, true);
        PSLogger.save();
    }

    public void selectManualSchedulingForDependencies(boolean yes) {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) return;
        getRadioButton(MANUAL_SCHEDULING_LOWER_93).select(yes, false);
        PSLogger.save();
    }

    public void setInheritPermissions(Boolean yes) {
        setControl(INHERIT_PERMISSIONS, yes);
    }

    public void setInheritControls(Boolean yes) {
        Boolean enabled = isControlSelected(INHERIT_COST_SETTING);
        Assert.assertNotNull(enabled, "Inherit Control Cost control is disabled");
        if (enabled != yes) {
            setControl(INHERIT_COST_SETTING, yes);
        }
    }

    public void setControlCost(Boolean yes) {
        Boolean enabled = isControlCostEnabled();
        Assert.assertNotNull(enabled, "Control Cost control is disabled");
        boolean warning = enabled != yes;
        setControl(CONTROL_COST, yes, false);
        if (warning) {
            Element warn = new Element(CONTROL_COST_WARNING.replace(yes ? CONTROL_COST_ENABLE : CONTROL_COST_DISABLE));
            warn.waitForVisible();
            PSLogger.info((yes ? "Enable" : "Disable") + " Control Cost");
            new Button(CONTROL_COST_WARNING_YES.replace(yes ? CONTROL_COST_ENABLE : CONTROL_COST_DISABLE)).click(false);
            warn.waitForUnvisible();
        }
    }

    public boolean isControlCostEnabled() {
        return isControlSelected(CONTROL_COST);
    }

    private void setControl(ILocatorable loc, Boolean yes) {
        setControl(loc, yes, true);
    }

    private void setControl(ILocatorable loc, Boolean yes, boolean wait) {
        if (yes == null) return;
        getRadioButton(loc).select(yes, wait);
    }


    private Boolean isControlSelected(ILocatorable loc) {
        YesNoRadioButtons b = getRadioButton(loc);
        if (b == null) return null;
        return b.isSelected();
    }

    public AssignUsersComponent getAssignUserComponent() {
        return new AssignUsersComponent(this, ASSIGN_USER_SUFFIX);
    }

    private TagsComponent comp;

    public TagsComponent getTagsComponent() {
        return comp != null ? comp : (comp = new TagsComponent(this));
    }

    public WorkOptionsComponent getOptions() {
        return new WorkOptionsComponent(STATUS, PRIORITY);
    }

    private class YesNoRadioButtons extends Element {
        private YesNoRadioButton yes;
        private YesNoRadioButton no;
        private String label;
        private Element _this;

        public YesNoRadioButtons(Element e) {
            super(e);
            _this = e;
            yes = new YesNoRadioButton(RADIO_BUTTON_YES);
            no = new YesNoRadioButton(RADIO_BUTTON_NO);
            label = getChildByXpath(RADIO_BUTTONS_LABEL).getDEText();
            yes.setName(label + "::Yes");
            no.setName(label + "::No");
        }

        private class YesNoRadioButton extends RadioButton {
            private YesNoRadioButton(ILocatorable l) {
                super(_this.getChildByXpath(l));
            }

            public void click(boolean doWait) {
                super.click(false);
                if (doWait) {
                    waitForPageToLoad();
                } else {
                    waitForSaveButton();
                }
            }
        }

        public void select(boolean yes) {
            select(yes, false);
        }

        public boolean isSelected() {
            return yes.getChecked();
        }

        public void select(boolean yes, boolean doWait) {
            YesNoRadioButton rb;
            if (yes) {
                rb = this.yes;
            } else {
                rb = this.no;
            }
            if (!rb.getChecked()) {
                PSLogger.info((yes ? "Select" : "Unselect") + " '" + label + "'.");
                rb.click(doWait);
            } else {
                PSLogger.info("'" + label + "' is already " + (yes ? "selected" : "unselected"));
            }
        }

        public String toString() {
            return label;
        }
    }

    private Button waitForSaveButton() {
        Button bt = new Button(SAVE, getSummaryInstance());
        if (waitLoading)
            bt.waitForVisible(SAVE_TIMEOUT);
        return bt;
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        if (!waitLoading) return;
        waitForSaveButton();
        new TimerWaiter(1200).waitTime();
        getDocument();
        buttons = null;
    }

    public List<YesNoRadioButtons> getRadioButtons() {
        if (buttons != null) return buttons;
        buttons = new ArrayList<YesNoRadioButtons>();
        for (Element e : getElements(false, RADIO_BUTTONS)) {
            buttons.add(new YesNoRadioButtons(e));
        }
        PSLogger.info("All radiobuttons: " + buttons);
        return buttons;
    }

    public YesNoRadioButtons getRadioButton(ILocatorable loc) {
        for (YesNoRadioButtons e : getRadioButtons()) {
            if (e.label.equals(loc.getLocator())) {
                return e;
            }
        }
        return null;
    }


    public SummaryWorkPage submitChangesWithConfirmation() {
        PSLogger.info("Save Changes and wait for dialog");
        Button bt = waitForSaveButton();
        bt.click(false);
        Element popup = new Element(SAVE_DIALOG);
        popup.waitForVisible();
        PSLogger.save("before saving");
        SummaryWorkPage res = getSummaryInstance();
        new Button(popup.getChildByXpath(SAVE_DIALOG_YES), res).submit();
        if (waitLoading)
            SUBMIT_TIMEOUT.waitTime();
        return res;
    }


    public SummaryWorkPage submitChanges() {
        PSLogger.info("Save Changes without waiting for dialog");
        return clickSubmitChanges(true);
    }

    public SummaryWorkPage clickSubmitChanges(boolean wait) {
        PSLogger.debug("click 'Save Changes'");
        SummaryWorkPage res = getSummaryInstance();
        Button bt = waitForSaveButton();
        bt.setResultPage(res);
        PSLogger.save("before saving");
        bt.click(wait);
        if (waitLoading)
            SUBMIT_TIMEOUT.waitTime();
        return wait ? res : null;
    }

    public SummaryWorkPage clickCancelChanges(boolean wait) {
        PSLogger.debug("click 'Cancel'");
        SummaryWorkPage res = getSummaryInstance();
        Button bt = new Button(CANCEL, res);
        PSLogger.save("before canceling");
        bt.click(wait);
        if (waitLoading)
            SUBMIT_TIMEOUT.waitTime();
        return wait ? res : null;
    }

    public SummaryWorkPage cancelChanges() {
        PSLogger.info("Cancel editing");
        return clickCancelChanges(true);
    }

    public SummaryWorkPage submitChanges(boolean confirmation) {
        return confirmation ? submitChangesWithConfirmation() : submitChanges();
    }

    public void setStatusReporting(Work.StatusReportingFrequency statusReporting) {
        SelectInput select = new SelectInput(STATUS_REPORTING);
        ILocatorable loc = null;
        switch (statusReporting) {
            case NO_FREQUENCY:
                loc = STATUS_REPORTING_OFF;
                break;
            case WEEKLY:
                loc = STATUS_REPORTING_WEEKLY;
                break;
            case BIWEEKLY:
                loc = STATUS_REPORTING_BI_WEEKLY;
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

    public void setScheduleConstraint(Work.IConstraint con) {
        if (con.equals(GatedProject.ESGP_CONSTRAINT) || con.equals(GatedProject.EMPTY_CONSTRAINT)) {
            PSLogger.info("Can't set constraint " + con);
            return;
        }
        PSLogger.info("Set constraint '" + con.getName() + "'");
        SelectInput select = new SelectInput(CONSTRAINT);
        Work.Constraint con2 = (Work.Constraint) con;
        ILocatorable loc = null;
        switch (con2) {
            case ASAP:
                loc = CONSTRAINT_ASAP;
                break;
            case ALAP:
                loc = CONSTRAINT_ALAP;
                break;
            case SNET:
                loc = CONSTRAINT_SNET;
                break;
            case SNLT:
                loc = CONSTRAINT_SNLT;
                break;
            case FNLT:
                loc = CONSTRAINT_FNLT;
                break;
            case FNET:
                loc = CONSTRAINT_FNET;
                break;
            case MSO:
                loc = CONSTRAINT_MSO;
                break;
            case MFO:
                loc = CONSTRAINT_MFO;
                break;
            case FD:
                loc = CONSTRAINT_FD;
                break;

        }
        if (loc != null) {
            if (select.getSelectedLabel().equals(loc.getLocator())) {
                PSLogger.debug("Constraint already selected");
                return;
            }
            select.select(loc);
            waitForPageToLoad();
        }
    }

    public DatePicker getConstraintEndDP() {
        return new EditDatePicker(CONSTRAINT_END);
    }

    public DatePicker getConstraintStartDP() {
        return new EditDatePicker(CONSTRAINT_START);
    }

    public void setConstraintEnd(String date) {
        getConstraintEndDP().type(date);
    }

    public void setConstraintStart(String date) {
        getConstraintStartDP().type(date);
    }

    private class EditDatePicker extends DatePicker {

        public EditDatePicker(ILocatorable loc) {
            super(getElement(false, loc));
        }

        protected void setUsingInput(String time) {
            super.setUsingInput(time);
            waitInputEditable();
        }

        protected void setUsingPopup(String time) {
            super.setUsingPopup(time);
            waitInputEditable();
        }

        private void waitInputEditable() { // in 10.0 due to #83478
            getParent().mouseDownAndUp();
            waitForClassChanged(DijitPopupLocators.DISABLED);
        }
    }

    public static EditWorkPage doNavigatePageEdit(String wiUid) {
        EditWorkPage res = new EditWorkPage();
        res.workId = wiUid;
        res.open();
        return res;
    }


}
