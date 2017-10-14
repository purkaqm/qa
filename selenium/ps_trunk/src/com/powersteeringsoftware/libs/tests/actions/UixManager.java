package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.UixFeature;
import com.powersteeringsoftware.libs.pages.UixJspPage;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 28.09.12
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class UixManager {

    public static void setFeatures(Object[] ... features) {
        UixJspPage.Edit edit = null;
        UixJspPage.Edit _edit = null;
        List<UixFeature> toAdd = new ArrayList<UixFeature>();
        for (Object[] f : features) {
            UixFeature i = toUixFeature((UixFeature.Code) f[0], f[1]);
            if (i == null) continue;
            _edit = setFeature(i, _edit);
            if (_edit == null) continue;
            edit = _edit;
            toAdd.add(i);
        }
        if (edit != null)
            edit.submitAndReturnToHome();
        for (UixFeature u: toAdd) {
            TestSession.setUixFeature(u);
        }
    }

    private static UixFeature toUixFeature(UixFeature.Code code, Object value) {
        if (code == null || code.getName() == null) return null; // unsupported feature
        if (value == null) return null;
        Integer v = null;
        if (value instanceof Integer) v = (Integer) value;
        if (value instanceof Boolean) v = (Boolean) value ? 0 : 1;
        if (value instanceof String) v = Boolean.valueOf(value.toString().replace("on", "true")) ? 0 : 1;
        if (v == null) return null;
        UixFeature f = new UixFeature(code.getName());
        PSLogger.debug2("Set " + v + " to new copy of uix-feature " + code);
        f.setValue(v);
        return f;
    }

    private static UixJspPage.Edit setFeature(UixFeature newFeature, UixJspPage.Edit edit) {
        UixFeature oldFeature = getUixFeature(newFeature.getName());
        if (oldFeature != null && newFeature.getValue().equals(oldFeature.getValue())) {
            PSLogger.debug("Uix feature is already " + oldFeature);
            return null;
        }
        if (edit == null) {
            UixJspPage page = new UixJspPage();
            page.open();
            edit = page.edit();
        }
        edit.set(newFeature);
        return edit;
    }

    public static void setFeature(UixFeature f) {
        UixJspPage.Edit edit = setFeature(f, null);
        if (edit == null) return; // already seted
        edit.submitAndReturnToHome();
        TestSession.setUixFeature(f);
    }

    public static UixFeature getUixFeature(UixFeature.Code code) {
        return getUixFeature(code.getName());
    }


    private static UixFeature getUixFeature(String code) {
        for (UixFeature u : TestSession.getUixFeatures()) {
            if (u.getName().equals(code)) return u;
        }
        return null;
    }

    public static void setFeature(UixFeature.Code code, boolean v) {
        setFeature(toUixFeature(code, v));
    }

    public static void setResourcePlanningOn() {
        setFeature(UixFeature.Code.RESOURCE_PLANING, true);
    }
    public static void setResourcePlanningOff() {
        setFeature(UixFeature.Code.RESOURCE_PLANING, false);
    }

    private static boolean isFeatureOn(UixFeature.Code code) {
        return getUixFeature(code).getValue() == 0;
    }

    public static boolean isResourcePlanningOn() {
        return isFeatureOn(UixFeature.Code.RESOURCE_PLANING);
    }

    public static boolean isResourceManagerOn() {
        return isFeatureOn(UixFeature.Code.RESOURCE_MANAGER);
    }

    public static boolean isImmediateDelegationOn() {
        return isFeatureOn(UixFeature.Code.IMMEDIATE_DELEGATION);
    }

}
