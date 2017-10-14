package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.MetricTemplatesPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.MetricTemplatesPageLocators.*;

/**
 * Class for Metric Templates.
 * Example of usage:
 * <p>
 * MetricTemplatesPage page = new MetricTemplatesPage();
 * page.open();
 * MetricTemplatesPage.FirstFrame fr1 = page.createNew();
 * fr1.setName("name_" + CoreProperties.getTestTemplate());
 * fr1.setDescription("description " + CoreProperties.getTestTemplate());
 * fr1.setPeriod(CoreProperties.getTestTemplate(), CoreProperties.getTestTemplate() + 2 * 30 * 24 * 60 * 60 * 1000L);
 * fr1.setBreakDownTag(TestData.getFirstTag().getName());
 * fr1.selectWorkItems();
 * MetricTemplatesPage.SecondFrame fr2 = fr1.next();
 * fr2.setCurrentFinancialYear();
 * MetricTemplatesPage.ThirdFrame fr3 = fr2.next();
 * for (int i = 1; i < 4; i++) {
 * fr3.setItem(i, String.valueOf(i), "item #" + i + " " + CoreProperties.getTestTemplate());
 * }
 * fr3.next().next().submit();
 * </p>
 * User: zss
 * Date: 03.10.11
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public class MetricTemplatesPage extends PSPage {

    private boolean hasViews;

    @Override
    public void open() {
        clickAdminMetricTemplates();
        /*Frame frame = getFrame();
        frame.waitForLoad();*/
    }

    public class AFrame extends Frame {
        protected AFrame() {
            super(FRAME);
        }

        public Link getNext() {
            Link res = new Link(NEXT);
            res.setFrame(this);
            return res;
        }

        protected void clickNext() {
            PSLogger.save("Before next");
            Link n = getNext();
            n.waitForVisible();
            n.click(false);
            waitForReload(MetricTemplatesPage.this);
            PSLogger.save("After next");
        }

        protected void setInputList(List<Input> inputs, ILocatorable loc) {
            for (Element e : Element.searchElementsByXpath(this, loc)) {
                Input in = new Input(e);
                in.setFrame(this);
                inputs.add(in);
            }
        }

        protected void setSelectList(List<SelectInput> inputs, ILocatorable loc) {
            for (Element e : Element.searchElementsByXpath(this, loc)) {
                SelectInput in = new SelectInput(e);
                in.setFrame(this);
                inputs.add(in);
            }
        }

    }

    public class FirstFrame extends AFrame {

        public void setName(String name) {
            Input in = new Input(NAME);
            in.setFrame(this);
            in.type(name);
        }

        public void setCalendar(String name) {
            if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_4) || name == null) return;
            SelectInput select = new SelectInput(CALENDAR);
            select.setFrame(this);
            select.select(name);
        }

        public void setDescription(String desc) {
            TextArea d = new TextArea(DESCRIPTION);
            d.setFrame(this);
            d.setText(desc);
        }

        /**
         * @param type 0, 1, 2, 3, 4
         *             0 - monthly
         *             1 - quarterly
         *             2 - monthly (13)
         *             3 - quarterly (13)
         *             3 - yearly
         *             4 - no frequency
         */
        public void setFrequency(MetricTemplate.FrequencyType type) {
            SelectInput select = new SelectInput(FREQUENCY);
            select.setFrame(this);
            switch (type) {
                case MONTHLY:
                case MONTHLY_13:
                    select.select(FREQUENCY_MONTHLY);
                    break;
                case QUARTERLY:
                case QUARTERLY_13:
                    select.select(FREQUENCY_QUARTERLY);
                    break;
                case YEARLY:
                    select.select(FREQUENCY_YEARLY);
                    break;
                case NO:
                    select.select(FREQUENCY_NO);
                    break;
                default:
                    PSLogger.warn("setFrequency: Incorrect type specified");
            }
        }

        public void setPeriod(Long start, Long end) {
            setPeriod(start, START);
            setPeriod(end, END);
        }

        public void setHasView(boolean yes) {
            new RadioButton(yes ? HAS_VIEW_YES : HAS_VIEW_NO).click();
            hasViews = yes;
        }

        private void setPeriod(Long date, ILocatorable loc) {
            if (date == null) return;
            String toSet = new SimpleDateFormat(CoreProperties.getDateFormat()).format(date);
            Input in = new Input(loc);
            in.setFrame(this);
            in.type(toSet);
        }

        public void setBreakDownTag(String tag) {
            SelectInput select = new SelectInput(BREAKDOWN_TAG);
            select.setFrame(this);
            select.selectLabel(tag);
        }

        public void setPercentageAllocation(boolean yes) {
            PSLogger.info("Set percentage allocation " + yes);
            RadioButton rb = new RadioButton(yes ? PERCENTAGE_ALLOCATION_YES : PERCENTAGE_ALLOCATION_NO);
            rb.setFrame(this);
            rb.waitForVisible();
            rb.click();
            Assert.assertTrue(rb.getChecked(), "Radiobutton is not checked");
        }

        public void selectWorkItems() {
            selectType(CHECKBOX_WORK_ITEMS);
        }

        public void selectMspProject() {
            selectType(CHECKBOX_MSP_PROJECTS);
        }

        public void selectTollgate() {
            selectType(CHECKBOX_GP);
        }

        public void selectTollgateNotEsg() {
            selectType(CHECKBOX_GP_NOT_ESG);
        }

        private void selectType(MetricTemplatesPageLocators loc) {
            CheckBox ch = new CheckBox(loc);
            ch.setFrame(this);
            ch.click();
        }

        public DisplayFrame next() {
            clickNext();
            return new DisplayFrame();
        }
    }

    public class DisplayFrame extends AFrame {

        public void setCurrentFinancialYear() {
            RadioButton rb = new RadioButton(CURRENT_FINANCIAL_YEAR);
            rb.setFrame(this);
            rb.click();
        }

        public void setDisplayTotal(MetricTemplate.DisplayTotal val) {
            if (val == null) return;
            SelectInput select = new SelectInput(DISPLAY_TOTAL_SELECTOR);
            select.setFrame(this);
            switch (val) {
                case NEVER:
                    select.selectValue(DISPLAY_TOTAL_SELECTOR_NEVER);
                    break;
                case QUARTERLY:
                    select.selectValue(DISPLAY_TOTAL_SELECTOR_QUARTERLY);
                    break;
                case YEARLY:
                    select.selectValue(DISPLAY_TOTAL_SELECTOR_YEARLY);
                    break;
                case INFINITELY:
                    select.selectValue(DISPLAY_TOTAL_SELECTOR_INFINITELY);
                    break;
                case DISPLAY_RANGE:
                    select.selectValue(DISPLAY_TOTAL_SELECTOR_RANGE);
                    break;
                default:
                    break;
            }

        }

        public AFrame next() {
            clickNext();
            if (hasViews)
                return new ViewFrame();
            return new ItemFrame();
        }
    }

    public abstract class AItemsFrame extends AFrame {
        protected int index;
        private List<Input> seqInputs;
        private List<Input> nameInputs;
        private List<SelectInput> typeSelects;
        private ILocatorable iSeq;
        private ILocatorable iName;
        private ILocatorable iType;

        protected AItemsFrame(ILocatorable seq, ILocatorable name, ILocatorable type) {
            iSeq = seq;
            iName = name;
            iType = type;
        }

        protected void init() {
            setInputList(seqInputs = new ArrayList<Input>(), iSeq);
            setInputList(nameInputs = new ArrayList<Input>(), iName);
            setSelectList(typeSelects = new ArrayList<SelectInput>(), iType);
        }

        public void addItem(int seq, String name, String type) {
            setItem(index++, seq, name, type);
        }

        public void addItem(int seq, String name) {
            setItem(index++, seq, name, null);
        }

        protected void setItem(int i, int seq, String name, String type) {
            if (index != 0 && index > seqInputs.size()) {
                more();
            }
            seqInputs.get(i).type(String.valueOf(seq));
            nameInputs.get(i).type(name);
            if (type != null)
                typeSelects.get(i).selectLabel(type);
        }

        public abstract Link getMoreLink();

        public void more() {
            PSLogger.save("Before more");
            Link more = getMoreLink();
            more.waitForVisible();
            more.click(false);
            waitForReload(MetricTemplatesPage.this);
            defaultElement = null;
            init();
            PSLogger.save("After more");
        }
    }


    public class ViewFrame extends AItemsFrame {

        private ViewFrame() {
            super(VIEW_SEQ, VIEW_NAME, VIEW_COST_MAPPING);
            init();
        }

        public AFrame next() {
            clickNext();
            return new ItemFrame();
        }

        @Override
        public Link getMoreLink() {
            Link more = new Link(VIEWS_MORE);
            more.setFrame(this);
            return more;
        }
    }


    public class ItemFrame extends AItemsFrame {

        private List<Input> descInputs;
        private ILocatorable iDesc;
        private boolean isStaticConstraintValuesNext;

        private ItemFrame() {
            super(ITEM_SEQ, ITEM_NAME, ITEM_TYPE);
            iDesc = ITEM_DESCRIPTION;
            init();
        }

        public void addItem(int seq, String name, String type, String description) {
            super.addItem(seq, name, type);
            if (description != null)
                descInputs.get(index - 1).type(description);
            if (!isStaticConstraintValuesNext)
                isStaticConstraintValuesNext = type != null && type.equalsIgnoreCase(MetricTemplate.ItemTypes.COST_MONETARY.getName());
        }

        protected void init() {
            super.init();
            setInputList(descInputs = new ArrayList<Input>(), iDesc);
        }

        public AFrame next() {
            clickNext();
            return isStaticConstraintValuesNext ? new StaticConstraintValuesFrame() : new FormulaFrame();
        }

        @Override
        public Link getMoreLink() {
            Link more = new Link(ITEMS_MORE);
            more.setFrame(this);
            return more;
        }
    }

    public class StaticConstraintValuesFrame extends AFrame {

        private List<SelectInput> viewCostMappingList;

        private StaticConstraintValuesFrame() {
            super();
            setSelectList(viewCostMappingList = new ArrayList<SelectInput>(), CONSTRAINT_VALUES_COST_MAPPING);
        }

        public FormulaFrame next() {
            clickNext();
            return new FormulaFrame();
        }

        public void setCostMapping(int num, String val) {
            SelectInput in = viewCostMappingList.get(num);
            String _val = null;
            for (String l : in.getSelectOptions()) {
                if (l.trim().equalsIgnoreCase(val)) {
                    _val = l;
                    break;
                }
            }
            Assert.assertNotNull(_val, "Can't find label " + val);
            in.select(_val);
        }

    }

    public class FormulaFrame extends AFrame {

        public FormulaFrame() {
            super();
        }

        public FinishFrame next() {
            clickNext();
            return new FinishFrame();
        }

        private List<Link> items;

        public void setTextMode() {
            Link link = new Link(FORMULA_TEXT_MODE);
            link.setFrame(this);
            if (!link.exists()) return;
            link.click(false);
            link.waitForDisapeared();
        }

        public List<Link> getItems() {
            if (items != null) return items;
            items = new ArrayList<Link>();
            for (Element e : Element.searchElementsByXpath(this, FORMULA_ITEMS)) {
                Link l = new Link(e);
                l.setFrame(this);
                l.setName(l.getDEText());
                items.add(l);
            }
            return items;
        }

        public void setFormula(String item, String formula) {
            if (formula == null) return;
            PSLogger.debug("Set formula '" + formula + "' for item " + item);
            //setDefaultElement();
            //Link link = new Link(getChildByXpath(FORMULA_ITEM.replace(item)));
            Link link = new Link(FORMULA_ITEM.replace(item));
            //if (getDriver().getType().isRCDriverIE()) {
            //    new TimerWaiter(5000).waitTime();
            //} else {
            //
            //}
            if (!link.exists())
                link.waitForPresent(10000);
            //Assert.assertTrue(link.exists(), "Can't find link for item " + item);
            link.waitForVisible(10000);
            link.click(false);
            waitForReload(MetricTemplatesPage.this);
            TextArea txt = new TextArea(FORMULA_TEXT);
            txt.setFrame(this);
            Assert.assertTrue(txt.exists(), "Can't find textarea for formula");
            txt.setText(formula);
            Button bt = new Button(new Link(FORMULA_SAVE));
            bt.setFrame(this);
            bt.click(false);
            waitForReload(MetricTemplatesPage.this);
        }
    }

    public class FinishFrame extends AFrame {
        public MetricTemplatesPage submit() {
            Button bt = new Button(new Link(SUBMIT));
            bt.setFrame(this);
            bt.click(false);
            waitForReload(MetricTemplatesPage.this);
            return MetricTemplatesPage.this;
        }
    }

    public FirstFrame createNew() {
        Frame frame = getFrame();
        frame.select();
        Button createNew = new Button(new Link(CREATE_NEW));
        createNew.setFrame(frame);
        createNew.waitForVisible();
        createNew.click(false);
        frame.waitForReload(this);
        return new FirstFrame();
    }


}
