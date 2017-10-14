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
package com.powersteeringsoftware.tests.search;

import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class Description
 * <p/>
 * <p/>
 * Date: 07/09/11
 *
 * @author karina
 */
public class TestData extends PSTestData {

    public class Data {
        private String value;
        private boolean present;

        private Data(String s) {
            value = s;
            present = true;
        }

        public String getValue() {
            return value;
        }

        public boolean shouldBeFound() {
            return present;
        }

        public String toString() {
            return value;
        }
    }

    public Set<Data> getIdeasSearchValues() {

        Set<Data> res = getSearchValues("words-to-search-ideas");
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) { // no ideas actually
            for (Data d : res) d.present = false;
        }
        return res;
    }

    public Set<Data> getSearchValues(String path) {
        Set<Data> types = new HashSet<Data>();
        Config wordsToSearchData = conf.getChildByXPath("//" + path);
        List<Config> wTypeConfigs = wordsToSearchData.getChilds();
        for (Config typeConf : wTypeConfigs) {
            types.add(new Data(typeConf.getAttributeValue("name")));
        }
        return types;
    }

    public String getDate(String path) {
        Config wordsToSearchData = conf.getChildByXPath("//" + path);
        return wordsToSearchData.getAttributeValue("date");
    }


    public String getDocUrl() {
        Config wordsToSearchData = conf.getChildByXPath("//" + "docUrl");
        return wordsToSearchData.getChByName("doc").getAttributeValue("url");
    }

}
