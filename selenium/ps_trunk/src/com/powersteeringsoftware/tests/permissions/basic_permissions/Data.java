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

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.objects.PermissionsObject;
import com.powersteeringsoftware.libs.objects.User;

/**
 * Class Description
 * <p/>
 * <p/>
 * Date: 26/10/12
 *
 * @author karina
 */
public class Data {
    PSPermissions _perm;
    User _user;
    PermissionsObject _obj;

    public Data(User user, PSPermissions perm, PermissionsObject object) {
        _user = user;
        _perm = perm;
        _obj = object;
    }

    public PSPermissions getPerm() {
        return _perm;
    }

    public User getUser() {
        return _user;
    }

    public PermissionsObject getObj() {
        return _obj;
    }

    public String toString() {
        //TODO:
        return _user.getFormatFullName();
    }
}
