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

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.EditWorkPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;

/**
 * Class to run Edit Permission tests
 * <p/>
 * <p/>
 * Date: 25/09/12
 *
 * @author karina & Claus
 */
public class EditPermission {
    /**
     * Tests if user can edit project
     *
     * @param data
     * @return true when user can edit and false when not
     */
    public static boolean editWorkTest(Data data) {
        Work work = (Work) data.getObj();
        User user = data.getUser();
        //Open Edit project

        SummaryWorkPage swp = WorkManager.open(work, false);
        if (swp.hasEditDetailsLink()) {  //it verifies if link is displayed
            PSLogger.info("EditDetailsLink opened");
            EditWorkPage editPage = swp.edit();

            String objective = "Objective edited by " + user.getFullName();
            editPage.setObjective(objective);
            editPage.submitChanges();
            //String oldObjective=editPage.getObjective();
            editPage = swp.edit();
            editPage.setObjective("");
            editPage.submitChanges();
            //String newObjective=editPage.getObjective();

            // if(objective.equals(oldObjective)&&newObjective.equals("")){
            PSLogger.info("editWorkTest: User '" + user.getFullName() + "'can edit '" + work.getName() + "'");
            return true;
                /*}else{
                    PSLogger.info("editWorkTest: User '"+user.getFullName()+ "'can not edit '"+work.getName()+"'");
                    return false;
                }  */
        } else {
            PSLogger.info("Couldn't open EditDetailsLink");
            return false;
        }
    }
}
