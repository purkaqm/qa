package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.NonWorkDaysSelectorLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 19:32:41
 */
public abstract class ASelector extends Element {
    public ASelector(ILocatorable locator) {
        super(locator);
    }

    public ASelector(Document document, ILocatorable locator) {
        super(document, locator);
    }

    protected Element getFirstRow() {
        return getChildByXpath(FIRST_ROW);
    }

    protected Element getRow(int index) {
        return getChildByXpath(ROW.replace(index));
    }

    protected abstract void set(ARowItem item);

    public abstract void setList(List<ARowItem> items);

    public void pushAdd() {
        PSLogger.info("Push Add");
        click(false); //<- ist for web-driver (ie)
        Img img = firstAddImg();
        img.click(false);
        new TimerWaiter(1300).waitTime();
    }

    private Img firstAddImg() {
        return new Img(getFirstRow().getChildByXpath(ADD));
    }

    private Img deleteImg(Element row) {
        return new Img(row.getChildByXpath(DELETE));
    }

    protected abstract ARowItem get(Element row);

    public void remove(int index) {
        index += 3; // first row is title, second empty, third is 0
        Element row = getRow(index);
        ARowItem item = get(row);
        PSLogger.info("Remove " + item);
        Element delete = deleteImg(row);
        click(false); //<- ist for web-driver (ie)
        delete.setSimpleLocator();
        delete.click(false);
        delete.waitForDisapeared();
    }

    public List<ARowItem> getList() {
        setDefaultElement();
        List<ARowItem> items = new ArrayList<ARowItem>();
        for (int i = 2; ; i++) {
            Element row = getRow(i);
            if (!row.exists()) break;
            ARowItem item = get(row);
            items.add(item);
        }
        items.remove(0);
        return items;
    }

    public ASelector(String locator) {
        super(locator);
    }

    public ASelector(Element e) {
        super(e);
    }

    public static class ARowItem {

        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof ARowItem)) return false;
            return true;
        }

    }

}
