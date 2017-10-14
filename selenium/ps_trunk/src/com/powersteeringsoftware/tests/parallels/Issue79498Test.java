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
package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.EditWorkPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WorkTreePage;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * Class Description
 * <p/>
 * <p/>
 * Date: 25/05/12
 *
 * @author karina
 */
public class Issue79498Test {

    @BeforeTest(description = "Preparing to test", groups = {"projectIssue79498.test", TestSettings.DEVELOPMENT_GROUP})
    public void preparingToTest() throws IOException {
        BasicCommons.logIn();
    }

    /*
    Edit project parallel method 1
    */
    @Test(description = "Edit & Save Project - Parallel method 1", groups = {"projectIssue79498.test",
            TestSettings.DEVELOPMENT_GROUP})
    public void parallelMethod1() {
        testEditProject();
    }

    /*
    Edit project parallel method 2
     */
    @Test(description = "Edit & Save Project - Parallel method 2", groups = {"projectIssue79498.test",
            TestSettings.DEVELOPMENT_GROUP})
    public void parallelMethod2() {
        testEditProject();
    }

    /*
    Method to test editing a project
     */
    public void testEditProject() {
        WorkTreePage tree = new WorkTreePage();
        tree.open();
        List<String> chs = tree.getWorkTree().getAllWorks();
        Work work = new Work(chs.get(0));

        SummaryWorkPage swp = WorkManager.open(work);
        EditWorkPage ewp = swp.edit();
        String objective = "New work objective";
        ewp.setObjective(objective);
        ewp.submitChanges();
    }

}
