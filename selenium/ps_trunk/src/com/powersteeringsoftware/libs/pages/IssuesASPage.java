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

import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.elements.WorkChooserDialog;
import org.testng.Assert;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.AdvancedSearchPageLocators.*;

/**
 * Class Description
 * <p/>
 * <p/>
 * Date: 08/09/11
 *
 * @author karina
 */
public class IssuesASPage extends AbstractASPage {

    @Override
    public void open() {

    }

    /**
     * Select Descendent From
     *
     * @param descendent - the descendent to select
     */
    public void selectDescendedFrom(String descendent, boolean howToSet) {
        WorkChooserDialog dialog = new WorkChooserDialog(ISSUE_DESCENDED_FIELD, ISSUE_DESCENDED_POPUP);
        dialog.open();
        if (howToSet) {
            // set using browse
            dialog.openBrowseTab();
            dialog.chooseWorkOnBrowseTab(descendent);
        } else {
            //set using searching
            dialog.openSearchTab();
            dialog.getSearchTab().choose(descendent);
        }
        Assert.assertFalse(dialog.getPopup().isVisible(),
                "popup still visible after searching parent location");
    }

    public List<String> getResultList() {
        return getResultList(ISSUE_NAME_COLUMN);
    }

    public void setStartDate(String date) {
        DatePicker dp = new DatePicker(START_DATEPICKER);
        dp.waitForVisible();
        dp.useDatePopup(false);
        dp.set(date);
    }

    public void setEndDate(String date) {
        DatePicker dp = new DatePicker(END_DATEPICKER);
        dp.waitForVisible();
        dp.useDatePopup(false);
        dp.set(date);
    }


}
