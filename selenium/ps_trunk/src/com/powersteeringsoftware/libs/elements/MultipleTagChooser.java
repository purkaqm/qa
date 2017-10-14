package com.powersteeringsoftware.libs.elements;

import org.dom4j.Document;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.TagChooserLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.MultipleTagChooserLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.07.2010
 * Time: 17:13:48
 */
public class MultipleTagChooser extends TagChooser {
    private Element searchDiv;
    private Link displayAllLink;

    public MultipleTagChooser(ILocatorable locator) {
        super(locator);
    }

    public MultipleTagChooser(String locator) {
        super(locator);
    }

    public MultipleTagChooser(Element e) {
        super(e);
    }

    public MultipleTagChooser(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void search(String txt) {
        PSLogger.info("Search by '" + txt + "'");
        getSearchInput().type(txt);
        getSearchButton().click(false);
        getPopup().getChildByXpath(TagChooserLocators.POPUP_LOADING).waitForUnvisible();
        searchForLabels();
    }

    protected void setElements() {
        super.setElements();
        searchDiv = null;
        displayAllLink = null;
        getDisplayAllLink();
        getSearchDiv();
    }

    public void showChecked() {
        PSLogger.info("Show checked");
        getShowCheckedButton().click(false);
        getPopup().getChildByXpath(TagChooserLocators.POPUP_LOADING).waitForUnvisible();
        searchForLabels();
    }

    public void clickAllDisplayed() {
        PSLogger.info("Click all displayed");
        getDisplayAllLink().click(false);
    }

    public Input getSearchInput() {
        return new Input(getSearchDiv().getChildByXpath(SEARCH_INPUT));
    }

    public Button getSearchButton() {
        return new Button(getSearchDiv().getChildByXpath(SEARCH_SUBMIT));
    }

    public Button getShowCheckedButton() {
        return new Button(getSearchDiv().getChildByXpath(SEARCH_SHOW_CHECKED));
    }

    public Link getDisplayAllLink() {
        return displayAllLink != null ? displayAllLink : (displayAllLink = new Link(getPopup().getChildByXpath(ALL_DISPLAYED)));
    }

    private Element getSearchDiv() {
        return searchDiv != null ? searchDiv : (searchDiv = getPopup().getChildByXpath(SEARCH_DIV));
    }
}
