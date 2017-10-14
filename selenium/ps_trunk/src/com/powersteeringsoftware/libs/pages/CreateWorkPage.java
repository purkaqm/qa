package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.CreateWorkPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 21:37:54
 * To change this template use File | Settings | File Templates.
 */
public class CreateWorkPage extends PSPage {

    private static boolean doNotWaitFinish;

    public static void setDoNotWaitFinish(boolean b) {
        doNotWaitFinish = b;
    }

    public void open() {
        clickBrowseNewWork();
        //super.open(MainMenuLocators.BROWSE_NEW_WORK);
    }

    public void open(int type) {
        if (type == 0)
            clickBrowseNewWork();
        else if (type == 1) {
            clickBrowseNewOrg();
        }

    }

    public void selectType(ILocatorable type) {
        selectType(type.getLocator());
    }

    public void selectType(String type) {
        PSLogger.info(getClass().getSimpleName() + ".selectType(" + type + ")");
        SelectInput select = getSelectTypeInput();
        select.waitForVisible();
        Assert.assertTrue(select.hasOption(type), "Can't find label");
        select.selectNotTrim(type);
        String fromPage = StrUtil.trim(select.getSelectedLabel());
        Assert.assertEquals(fromPage, type, "Incorrect type after selecting");
    }

    public SelectInput getSelectTypeInput() {
        return new SelectInput(WORK_TYPE_SELECT);
    }

    public void selectWorkType() {
        selectType(WORK_TYPE_SELECT_WORK_OPTION);
    }

    public CreateWorkPage next() {
        Button sb = new Button(FIRST_STEP_CONTINUE_BUTTON);
        sb.waitForVisible(5000);
        PSLogger.info("next");
        sb.submit();
        if (getErrorBoxMessage() != null) {
            return null;
        }
        return this;
    }

    protected Button getFinishButton() {
        return new Button(SECOND_STEP_FINISH_BUTTON);
    }

    /**
     * Can return SummaryWorkPage or DeliverablesListingPage
     *
     * @return AbstractWorkPage
     */
    public AbstractWorkPage finish() {
        return finish(true);
    }

    public AbstractWorkPage finish(boolean doCheck) {
        PSLogger.save("before finish and save work");
        SummaryWorkPage res = SummaryWorkPage.getInstance(doCheck);
        Button submit = getFinishButton();
        submit.waitForVisible(5000);
        submit.setResultPage(res);
        PSLogger.info("finish");
        if (doNotWaitFinish) {
            submit.click(false);
            TimerWaiter.waitTime(2000);
            stop();
            return null;
        }
        submit.submit();
        if (getErrorBoxMessage() != null) {
            return null;
        }
        return res;
    }

    public void setName(String txt) {
        new Input(NAME_INPUT_FIELD).type(txt);
    }

    public void setConstraintStartDate(String txt) {
        setConstraintStartDate(txt, null, null);
    }

    public void setConstraintEndDate(String txt) {
        setConstraintEndDate(txt, null, null);
    }

    public void setConstraintStartDate(Object txt, Boolean usePopup, Boolean useArrow) {
        setConstraintDate(getConstraintStartDP(), txt, usePopup, useArrow);
    }

    public void setConstraintEndDate(Object txt, Boolean usePopup, Boolean useArrow) {
        setConstraintDate(getConstraintEndDP(), txt, usePopup, useArrow);
    }

    private static void setConstraintDate(DatePicker dp, Object txt, Boolean usePopup, Boolean useArrow) {
        if (usePopup != null)
            dp.useDatePopup(usePopup);
        if (useArrow != null)
            dp.useDropDownOrArrows(useArrow);
        if (txt instanceof Long) {
            dp.set((Long) txt);
        } else {
            dp.set(String.valueOf(txt));
        }
    }

    public boolean isConstraintEndDatePresent() {
        return getConstraintEndDP().exists();
    }

    public boolean isConstraintStartDatePresent() {
        return getConstraintStartDP().exists();
    }

    public DatePicker getConstraintEndDP() {
        return new DatePicker(END_DATE);
    }

    public DatePicker getConstraintStartDP() {
        return new DatePicker(START_DATE);
    }

    public DatePicker getGateConstraintEndDP(String gate) {
        return getGateConstraintDP(gate, false);
    }

    public DatePicker getGateConstraintStartDP(String gate) {
        return getGateConstraintDP(gate, true);
    }

    public String getGateConstraintEndDate(String gate) {
        return getGateConstraintDate(gate, false);
    }

    public String getGateConstraintStartDate(String gate) {
        return getGateConstraintDate(gate, true);
    }

    private DatePicker getGateConstraintDP(String gate, boolean start) {
        List<CreateWorkDP> dps = getDatePickers(start);
        Assert.assertFalse(dps.size() == 0, "Can not find date-pickers for gates");
        for (CreateWorkDP d : dps) {
            if (gate.equals(d.getLabel())) return d;
        }
        return null;
    }

    private String getGateConstraintDate(String gate, boolean start) {
        return getGateConstraintDP(gate, start).get();
    }

    public List<CreateWorkDP> getDatePickers(boolean start) {
        List<CreateWorkDP> res = new ArrayList<CreateWorkDP>();
        for (Element e : getElements(false, start ? GATE_START_DP : GATE_END_DP)) {
            res.add(new CreateWorkDP(e));
        }
        return res;
    }

    private class CreateWorkDP extends DatePicker {
        private String label;

        private CreateWorkDP(Element e) {
            super(e);
            for (Element l : Element.searchElementsByXpath(e.getParent().getParent(), GATE_DATE_LABEL)) {
                String sL = l.getDEText();
                if (sL != null && !sL.isEmpty()) {
                    label = sL;
                    break;
                }
            }
        }

        public String getLabel() {
            return label;
        }

        public void unFocus() {
            super.unFocus();
            new TimerWaiter(1500).waitTime();
            getDocument();
        }

        public String get() {
            String txt = getDEText();
            if (txt.isEmpty()) txt = super.get();
            return txt;
        }
    }

    public void setParent(Work parent, boolean howToSet) {
        WorkChooserDialog dialog = new WorkChooserDialog(LOCATION_FIELD, LOCATION_POPUP);
        dialog.open();
        if (howToSet) {
            // set using browse
            dialog.openBrowseTab();
            dialog.chooseWorkOnBrowseTab(parent);
        } else {
            //set using searching
            dialog.openSearchTab();
            dialog.setWithWaitingForReindexSearching(parent.getName());
        }
        Assert.assertFalse(dialog.getPopup().isVisible(),
                "popup still visible after searching parent location");
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


}
