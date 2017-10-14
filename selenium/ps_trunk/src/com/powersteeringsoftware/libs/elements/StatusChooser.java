package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.StatusChooserLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.StrUtil;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.StatusChooserLocators.POPUP_INPUTS;
import static com.powersteeringsoftware.libs.enums.elements_locators.StatusChooserLocators.POPUP_LABELS;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.06.2010
 * Time: 17:23:29
 */
public class StatusChooser extends TagChooser {
    public StatusChooser(ILocatorable locator) {
        super(locator);
    }

    public StatusChooser(Element e) {
        super(e);
    }

    protected void searchForLabels() {
        //popup = new DijitPopup();
        items.clear();
        List<Element> labelsList = Element.searchElementsByXpath(popup, POPUP_LABELS);
        List<Element> inputsList = Element.searchElementsByXpath(popup, POPUP_INPUTS);
        if (inputsList.size() == 0 || labelsList.size() != inputsList.size()) {
            PSLogger.debug(popup.asXML());
            PSLogger.warn("Can't find any checkboxes on popup or popup is incorrect");
            return;
        }
        for (int i = 0; i < labelsList.size(); i++) {
            String txt = StrUtil.replaceSpaces(labelsList.get(i).getDEText()).trim();
            CheckBox ch = new CheckBox(inputsList.get(i));
            ch.setName(txt);
            Item item = new Item(txt);
            item.set(ch);
            items.add(item);
        }
        getPopup().setDynamic();
    }

    public void selectAll() {
        Link link = new Link(StatusChooserLocators.POPUP_ALL);
        link.click(false);
    }

    public void selectNone() {
        Link link = new Link(StatusChooserLocators.POPUP_NONE);
        link.click(false);
    }

}
