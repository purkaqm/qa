package com.powersteeringsoftware.libs.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.MultiDateSelectorLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 01.06.2010
 * Time: 19:21:19
 */
public class MultiDateSelector extends Element {
    private List<DatePicker> datePickers = new ArrayList<DatePicker>();

    public MultiDateSelector(ILocatorable locator) {
        super(locator);
    }

    public MultiDateSelector(String locator) {
        super(locator);
    }

    public MultiDateSelector(Element e) {
        super(e);
    }

    public void set(String... dates) {
        set(Arrays.asList(dates));
    }

    public void set(List<String> dates, Boolean[]... howtoset) {
        String locator = Element.searchElementByXpath(this, DATE_PICKER).getLocator();
        int count = Element.searchElementsByXpath(this, CHILDREN).size();
        for (int i = 0; i < dates.size(); i++) {
            PSLogger.info("Set date " + dates.get(i));
            Element add = new Img(getChildByXpath(ADD));
            add.waitForVisible();
            DatePicker dp = new DatePicker(getDefaultElement().getDocument(), locator);
            if (howtoset.length > i) {
                dp.useDatePopup(howtoset[i][0]);
                dp.useDropDownOrArrows(howtoset[i][1]);
            }
            dp.set(dates.get(i));
            dp.setSimpleLocator(true);
            if (i != dates.size() - 1) {
                Element ch = getChildByXpath(CHILD.replace(count + 1));
                add.click(false);
                ch.waitForVisible();
                setDefaultElement();
            }
            datePickers.add(dp);
            PSLogger.save("Save after setting " + dates.get(i));
        }
    }

    /**
     * @param index 0,1,...
     */
    public void remove(int index) {
        DatePicker dp = datePickers.get(index);
        if (!dp.exists()) {
            PSLogger.warn("Can't remove #" + index + " datepicker: not exist");
            return;
        }
        PSLogger.debug("Remove #" + index + " datepicker " + dp.get());
        Element delete = new Img(dp.getParent().getChildByXpath(DELETE));
        delete.setSimpleLocator();
        delete.click(false);
        delete.waitForDisapeared();
        datePickers.remove(dp);
    }

    public List<String> get() {
        List<String> res = new ArrayList<String>();
        for (DatePicker dp : datePickers) {
            if (dp.exists()) {
                res.add(dp.get());
            } else {
                PSLogger.debug("DataPicker " + dp.getLocator() + " is not existed");
            }
        }
        return res;
    }
}
