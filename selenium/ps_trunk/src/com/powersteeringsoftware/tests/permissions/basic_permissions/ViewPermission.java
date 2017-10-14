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
package com.powersteeringsoftware.tests.permissions.basic_permissions;

import com.powersteeringsoftware.libs.elements.SearchEngine;
import com.powersteeringsoftware.libs.elements.SearchResult;
import com.powersteeringsoftware.libs.elements.WorkTreeElement;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.WorkTreePage;

import java.util.List;

/**
 * Class to run View Permission tests
 * <p/>
 * Date: 25/08/12
 *
 * @author karina
 */

public class ViewPermission {
    /**
     * View Work tree test.
     *
     * @param data     from testData
     * @param testType true when test case positive; false when test case negative
     * @return true if test passes and false if test fails
     */
    public static boolean viewWorkTreeTest(Data data) {
        // Select Browse ? Work Tree
        PSLogger.info("Opening Work tree");
        WorkTreePage workTree = new WorkTreePage();
        workTree.open(false);
        PSLogger.info("Opened Work tree");

        // Get all works
        WorkTreeElement wtElem = workTree.getWorkTree();
        List<String> works = wtElem.getAllWorks();

        // Look at user project, should be able to see
        Work work = (Work) data.getObj();
        if (works.contains(work.getName())) {
            PSLogger.info("viewWorkTreeTest: '" + work.getName() + "' is present.");
            return true;
        } else {
            PSLogger.info("viewWorkTreeTest: '" + work.getName() + "' is not present.");
            return false;
        }
    }

    /**
     * Search test
     *
     * @param data from testData
     */
    public static boolean viewSearchTest(Data data) {
        // Open simple search


        SearchEngine quickSearch = PSPage.getEmptyInstance().openQuickSearch();

        // Search projects user shouldn't be able to see
        Work work = (Work) data.getObj();
        User user = data.getUser();
        String projectName = work.getName();
        SearchResult ssp = quickSearch.makeSearch(projectName, SearchEngine.Type.PROJECTS);

        // Verify Expected results
        if (ssp.verifyData(projectName, SearchEngine.Type.PROJECTS)) {
            PSLogger.info("viewSearchTest: User '" + user.getFullName() + "' can find '" + projectName + "'.");
            return true;
        } else {
            PSLogger.info("viewSearchTest: User '" + user.getFullName() + "' can not find '" + projectName + "'.");
            return false;
        }
    }

}
