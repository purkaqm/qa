/*
 * Copyright (c) PowerSteering Software 2011
 * All rights reserved.
 *
 * This software and documentation contain valuable trade secrets and proprietary information belonging to
 * PowerSteering Software Inc.  None of the foregoing material may be copied, duplicated or disclosed without the
 * express written permission of PowerSteering.  Reverse engineering, decompiling and disassembling are explicitly
 * prohibited. POWERSTEERING SOFTWARE EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR ANY
 * PARTICULAR PURPOSE, AND WARRANTIES OF NON-INFRINGEMENT OF INTELLECTUAL PROPERTY RIGHTS OF A
 * THIRD PARTY, PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE OF DEALING
 * OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED WITH RESPECT TO THE USE OF THE
 * SOFTWARE OR DOCUMENTATION.  Under no circumstances shall PowerSteering Software be liable for incidental,
 * special, indirect, direct or consequential damages or loss of profits, interruption of business, or related expenses which
 * may arise from use of software or documentation, including but not limited to those resulting from defects in software
 * and/or documentation, or loss or inaccuracy of data of any kind.
 */
package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.AdvancedSearchPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.SimpleSearchPageLocators.*;

//import static com.powersteeringsoftware.libs.enums.elements_locators.QuickSearchLocators.*;

/**
 * This class is only for 9.1 or latter
 * Class to test Simple Search
 * <p/>
 * Date: 24/08/11
 *
 * @author karina
 */
public class SimpleSearchPage extends AbstractASPage implements SearchResult {

    /*
     * Implement the abstract method open
     */
    @Override
    public void open() {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_1) || TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            PSSkipException.skip(getClass().getName() + " is only for versions 9.1-10.0");
        }
        QuickSearchDialog quickSearchD = (QuickSearchDialog) openQuickSearch();
        quickSearchD.clickSearchButton();
    }

    public void setQuickSearchText(String txt) {
        Element input = new Element(SIMPLE_SEARCH_FIELD);
        new Input(SIMPLE_SEARCH_FIELD).type(txt);
    }

    /**
     * Set a text into search field
     *
     * @param text the text to search
     */
    public void setSearchText(String text) {
        new Input(SIMPLE_SEARCH_FIELD).type(text);
    }

    /**
     * Click search button
     */
    public void clickSearchButton() {
        new Button(SIMPLE_SEARCH_SEARCH_BUTTON).submit();
    }

    /**
     * Click clear button
     */
    public void clickClearButton() {
        new Button(SIMPLE_SEARCH_CLEAR_BUTTON).submit();
    }


    public boolean verifyData(String txt, SearchEngine.Type type) {
        ILocatorable nameLoc = type.equals(SearchEngine.Type.ISSUES) ? AdvancedSearchPageLocators.ISSUE_NAME_COLUMN : AdvancedSearchPageLocators.GENERAL_NAME_COLUMN;
        List<Result> res = getResults();
        PSLogger.info("Searching result: " + res);
        //todo: check type?
        for (Result r : res) {
            String name = r.get(nameLoc.getLocator());
            if (txt.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

}
