package com.powersteeringsoftware.libs.elements;

import org.dom4j.Document;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.DateRangeTypeSelectionLocators.END_DATE;
import static com.powersteeringsoftware.libs.enums.elements_locators.DateRangeTypeSelectionLocators.START_DATE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.08.2010
 * Time: 15:25:53
 */
public class DateRangeTypeSelection extends SelectInput {
    private DatePicker start;
    private DatePicker finish;

    public DateRangeTypeSelection(ILocatorable locator) {
        super(locator);
        setDatePickers();
    }

    public DateRangeTypeSelection(String locator) {
        super(locator);
        setDatePickers();
    }

    public DateRangeTypeSelection(Element e) {
        super(e);
        setDatePickers();
    }

    public DateRangeTypeSelection(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void select(ILocatorable opt) {
        select(opt.getLocator());
    }

    public void select(String opt) {
        PSLogger.info("Select option '" + opt + "' in date range selection element");
        super.select(opt);
        setDatePickers();
    }

    private void setDatePickers() {
        Element start = getParent().getChildByXpath(START_DATE);
        Element end = getParent().getChildByXpath(END_DATE);
        this.start = null;
        this.finish = null;
        if (start.exists() && start.isVisible()) {
            this.start = new DatePicker(start);
        }
        if (end.exists() && end.isVisible()) {
            this.finish = new DatePicker(end);
        }
    }

    public DatePicker getStartDatePicker() {
        return start;
    }

    public DatePicker getFinishDatePicker() {
        return finish;
    }

}
