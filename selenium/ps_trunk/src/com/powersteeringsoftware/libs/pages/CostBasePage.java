package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.CostBasePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.CostBasePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 20.11.13
 * Time: 15:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class CostBasePage extends AbstractWorkPage {

    public void open() {
        if (workId == null) throw new NullPointerException("Work id is null");
        CostBasePageLocators url;
        if (this instanceof EstimatedPage) {
            url = URL_ESTIMATED;
        } else {
            url = URL_ACTUAL;
        }
        super.open(makeUrl(url, workId));
    }

    public AddDialog callAddDialog() {
        getAdd().click(false);
        AddDialog res = new AddDialog();
        res.waitForVisible();
        return res;
    }

    public Button getAdd() {
        return new Button(OPEN_DIALOG);
    }

    public class AddDialog extends Dialog {
        public AddDialog() {
            super(DIALOG);
            setPopup(DIALOG);
        }

        public void setAmount(String a) {
            new Input(DIALOG_AMOUNT).type(a);
        }

        public void setDescription(String txt) {
            new TextArea(DIALOG_DESCRIPTION).setText(txt);
        }

        public DatePicker getDateDP() {
            return new DatePicker(DIALOG_DATE);
        }

        public void setDate(String time, boolean useInput) {
            if (useInput) {
                getDateDP().type(time);
            } else {
                getDateDP().set(time);
            }
        }

        public void setDate(String time) {
            setDate(time, false);
        }

        public void add() {
            Button bt = getAddBtn();
            bt.click(false);
            new Element(DIALOG_TABLE).waitForVisible();
        }

        private Button getAddBtn() {
            return new Button(getElement(false, DIALOG_ADD));
        }

        public void submit() {
            new Button(DIALOG_SUBMIT).submit();
            waitPage();
            document = null;
        }
    }

    public List<CostRow> getRows() {
        List<CostRow> res = new ArrayList<CostRow>();
        for (Element tr : getElements(false, TABLE_TR)) {
            Element ae = tr.getChildByXpath(TABLE_AMOUNT);
            if (ae == null || !ae.isDEPresent()) continue;
            res.add(toRow(tr));
        }
        return res;
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        waitPage();
        document = null;
    }

    private void waitPage() {
        new Element(OPEN_DIALOG).waitForVisible();
    }

    public void more() {
        Link next = new Link(NEXT);
        Link more = new Link(MORE);
        if (next.exists() && more.exists()) {
            PSLogger.info("Push 'More'");
            more.clickAndWaitNextPage();
            waitPage();
            document = null;
            PSLogger.save("After 'more'");
        }
    }

    public List<Cost> getCosts(Work w) {
        more();
        List<Cost> res = new ArrayList<Cost>();
        for (CostRow cr : getRows()) {
            res.add(cr.toCost(w));
        }
        return res;
    }

    public List<String> getStringRows() {
        List<String> rows = new ArrayList<String>();
        for (CostRow cr : getRows()) {
            rows.add(cr.toString());
        }
        return rows;
    }

    protected CostRow toRow(Element tr) {
        CostRow res = new CostRow(StrUtil.replaceNumberSpaces(tr.getChildByXpath(TABLE_AMOUNT).getDEInnerText()).trim(), tr.getChildByXpath(TABLE_DATE).getDEText(),
                tr.getChildByXpath(TABLE_ACTIVITY).getDEText());
        return res;
    }

    protected class CostRow {
        private String amount;
        private String date;
        private String activity;
        private String description;
        private boolean isEstimated;
        private String addType;
        private String hours;
        private String resourceRole;
        private String resourceUser;
        private String resource;

        private CostRow(String _amount, String _date, String _activity) {
            this.amount = _amount;
            this.date = _date;
            activity = _activity;
        }

        public String toString() {
            return "(" + Cost.toString(isEstimated ? Cost.Type.ESTIMATED.name() : Cost.Type.ACTUAL.name(),
                    amount, date, getAddType(), description, activity, resource) + ")";
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Cost toCost(Work w) {
            PSCalendar s = null;
            PSCalendar e = null;
            Double v = Cost.parseAmount(w, amount);
            if (date != null)
                if (date.contains("-")) {
                    String start = date.replaceAll("\\s*-.*", "");
                    String end = date.replaceAll(".*-\\s*", "");
                    s = w.getCalendar().set(start);
                    e = w.getCalendar().set(end);
                    e.toEnd();
                } else {
                    s = w.getCalendar().set(date);
                }
            s.toStart();

            Cost cost;
            if (!isEstimated && Cost.AddType.TIMESHEETS.getValue().equals(getAddType())) {
                Double d = hours.isEmpty() ? null : Double.parseDouble(hours);
                cost = new Cost.Time(d, v, s.getTime());
            } else {
                cost = new Cost(isEstimated ? Cost.Type.ESTIMATED : Cost.Type.ACTUAL, v, description, s.getTime(), e != null ? e.getTime() : null);
            }
            if (isEstimated) {
                if (resource != null) {
                    cost.setAddType(Cost.AddType.LABOR);
                    if (resourceUser != null) {
                        cost.setResource(User.getUserByFullName(resourceUser));
                    }
                    if (resourceRole != null) {
                        cost.setResource(BuiltInRole.getRoleByName(resourceRole));
                    }
                }
            }
            cost.setWork(w);
            cost.setFormattedAmount(amount);
            if (activity != null && !activity.isEmpty())
                cost.setActivity(activity);
            return cost;
        }

        private String getAddType() {
            return addType != null ? addType : Cost.AddType.MANUAL.getValue();
        }

        private String getActivity() {
            return activity == null ? "" : activity;
        }

        public void setEstimated(boolean estimated) {
            isEstimated = estimated;
        }

        public void setAddType(String addType) {
            this.addType = addType;
        }

        public void setHours(String h) {
            this.hours = h;
        }

        public void setResourceRole(String resourceRole) {
            this.resourceRole = resourceRole;
            resource = resourceRole;
        }

        public void setResourceUser(String resourceUser) {
            this.resourceUser = resourceUser;
            resource = resourceUser;
        }
    }
}
