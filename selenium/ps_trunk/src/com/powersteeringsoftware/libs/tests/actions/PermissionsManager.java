package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.objects.PermissionsObject;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 06.09.12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class PermissionsManager {

    public static void setAll(PSPermissions.AllSet perms) {
        setDefine(perms);
        setDefault(perms);
    }

    public static void setPermissions(PermissionsObject object, PSPermissions perms) {
        if (object instanceof Work) {
            WorkManager.editPermissions((Work) object, perms);
        } else {
            // todo: create for other objects
            throw new UnsupportedOperationException("TODO");
        }
    }


    public static void setDefault(PSPermissions.AllSet perms) {
        PSLogger.info("Set default permissions");
        DefaultPermissionsPage dpp = openDefault();
        for (PSPermissions p : perms.getPermissions()) {
            setDefault(dpp, p);
        }
    }

    public static void setDefault(PSPermissions perms) {
        PSLogger.info("Set default permissions");
        DefaultPermissionsPage dpp = openDefault();
        setDefault(dpp, perms);
    }

    private static void setDefault(DefaultPermissionsPage dpp, PSPermissions p) {
        dpp.selectCategory(p.getCategoryTarget());
        PSLogger.save("After selecting category " + p.getCategoryTarget());
        PSPermissions was = setDefault(dpp.getContent(), p);
        TestSession.getPermissions().mergeSets(was);
    }

    static PSPermissions setDefault(IPermissionsPage.IContent dpp, PSPermissions p) {
        PSPermissions was = load(dpp);
        if (p.hasCoreSet())
            PSLogger.info("Process core set");
        for (PSPermissions.CategoryRole category : p.getCoreSetCategories()) {
            for (PSPermissions.PSRole r : category.getRoles()) {
                dpp.selectCoreSetsPermission(r.getName(), r.getLevel(), category.getName());
            }
        }

        if (p.hasCustomSet())
            PSLogger.info("Process custom set");
        for (PSPermissions.CategoryRole category : p.getCustomSetCategories()) {
            for (User u : category.getUsers()) {
                dpp.createCustomSet(u, category.getName());
            }
        }
        was.mergeSets(p);
        PSPermissions now1 = load(dpp);
        //Assert.assertEquals(now1, was, "Incorrect custom/core sets");
        checkSets(p.getCategoryTarget(), now1, was);


        if (p.hasCoreSet())
            dpp.saveCoreSet();
        if (p.hasCustomSet())
            dpp.saveCustomSet();

        if (p.hasCoreSet() || p.hasCustomSet()) {
            PSPermissions now2 = load(dpp);
            //Assert.assertEquals(now2, was, "Incorrect custom/core sets after submitting");
            checkSets(p.getCategoryTarget(), now2, was);
        }
        return was;
    }

    public static PermissionsWorkPage removeCustomSets(Work w) {
        PermissionsWorkPage edit = WorkManager.openPermissions(w);
        removeCustomSets(edit.getContent());
        return edit;
    }

    private static void removeCustomSets(IPermissionsPage.IContent dpp) {
        if (dpp.getCustomSets().isEmpty()) {
            PSLogger.info("CustomSet is empty");
        } else {
            HashSet<String> csNames = new HashSet<String>();
            for (DefaultPermissionsPage.DPRadioButton cs : dpp.getCustomSets()) {//because the customSet list has more than 1 item per custom set
                csNames.add(cs.getUser());
            }
            for (String name : csNames) {
                IPermissionsPage.Content.DeleteCustomSetDialog deleteDialog = dpp.pushCustomSetDeleteImg(name);
                deleteDialog.waitForVisible();
                deleteDialog.ok();
            }
        }
    }

    private static void checkSets(PSPermissions.BasicTarget target, PSPermissions actual, PSPermissions expected) {
        List<String> eRoles = expected.getCategoryRoleNames();
        List<String> aRoles = actual.getCategoryRoleNames();
        PSLogger.debug("Categories from page " + aRoles);
        PSLogger.debug("Expected categories " + eRoles);
        Assert.assertEquals(aRoles, eRoles, "Incorrect list of category roles for target " + target);
        for (String c : eRoles) {
            PSPermissions.CategoryRole eRole = expected.getCategory(c);
            PSPermissions.CategoryRole aRole = actual.getCategory(c);
            List<PSPermissions.PSRole> _aRoles = aRole.getRoles();
            List<PSPermissions.PSRole> _eRoles = eRole.getRoles();
            Collections.sort(_aRoles);
            Collections.sort(_eRoles);
            List<User> _aUsers = aRole.getUsers();
            List<User> _eUsers = eRole.getUsers();
            Collections.sort(_aUsers);
            Collections.sort(_eUsers);
            PSLogger.info("Expected roles : " + _eRoles);
            PSLogger.info("Actual roles : " + _aRoles);
            PSLogger.info("Expected users: " + _eUsers);
            PSLogger.info("Actual users: " + _aUsers);
            Assert.assertEquals(_aRoles, _eRoles, "Incorrect roles selected for category role " + c + " and target " + target);
            Assert.assertEquals(_aUsers, _eUsers, "Incorrect users selected for category role " + c + " and target " + target);
        }
    }

    public static PSPermissions load(DefinePermissionsPage page) {
        PSPermissions res = new PSPermissions();
        PSPermissions.BasicTarget category = page.getCategory();
        res.setCategoryTarget(category);
        for (DefinePermissionsPage.PermCheckBox ch : page.getAll()) {
            res.addPermission(ch.getName(), ch.getCategory(), ch.getChecked(), !ch.isEditable());
        }
        return res;
    }

    public static PSPermissions load(IPermissionsPage.IContent page) {
        PSPermissions res = new PSPermissions();
        if (page.getPage() instanceof IGeneralPermissionsPage) {
            PSPermissions.BasicTarget category = ((IGeneralPermissionsPage) page.getPage()).getCategory();
            res.setCategoryTarget(category);
        }
        for (DefaultPermissionsPage.DPRadioButton dp : page.getCoreSets()) {
            res.addCoreSet(dp.getUser(), dp.getLevel(), dp.getCategory(), dp.getChecked());
        }
        for (DefaultPermissionsPage.DPRadioButton dp : page.getCustomSets()) {
            res.addCustomSet(dp.getUser(), dp.getCategory(), dp.getChecked());
        }
        return res;
    }

    public static void setDefine(PSPermissions.AllSet perms) {
        if (!perms.hasPermissions()) return;
        PSLogger.info("Define new permissions");
        DefinePermissionsPage dpp = openDefine();
        for (PSPermissions p : perms.getPermissions()) {
            setDefine(dpp, p);
        }
    }

    public static void setDefine(PSPermissions perms) {
        PSLogger.info("Define new permissions");
        DefinePermissionsPage dpp = openDefine();
        setDefine(dpp, perms);
    }

    private static void setDefine(DefinePermissionsPage page, PSPermissions perms) {
        page.selectCategory(perms.getCategoryTarget());
        PSLogger.save("After selecting category " + perms.getCategoryTarget());
        PSPermissions was = load(page);
        PSLogger.debug("To test: " + perms.getConfig().toString());
        for (PSPermissions.CategoryRole category : perms.getCategoryRoles()) {
            PSLogger.info("define permission for category " + category);
            for (PSPermissions.CategoryRole.Permission perm : category.getPermissions()) {
                page.selectPermission(perm.getName(), category.getName(), perm.getValue(), perm.isDisabled());
            }
        }
        was.mergePermissions(perms);
        PSPermissions now1 = load(page);
        checkPermissions(perms.getCategoryTarget(), now1, was);
        page.update();

        PSPermissions now2 = load(page);
        was.setAllOtherIgnored(perms);
        checkPermissions(perms.getCategoryTarget(), now2, was);
        TestSession.getPermissions().mergePermissions(now2);
    }

    public static DefinePermissionsPage openDefine() {
        DefinePermissionsPage dpp = new DefinePermissionsPage();
        if (!dpp.checkUrl())
            dpp.open();
        return dpp;
    }

    public static DefinePermissionsPage openDefine(PSPermissions.BasicTarget t) {
        DefinePermissionsPage dpp = openDefine();
        dpp.selectCategory(t);
        return dpp;
    }

    public static DefaultPermissionsPage openDefault() {
        DefaultPermissionsPage dpp = new DefaultPermissionsPage();
        if (!dpp.checkUrl())
            dpp.open();
        return dpp;
    }

    public static DefaultPermissionsPage openDefault(PSPermissions.BasicTarget t) {
        DefaultPermissionsPage dpp = openDefault();
        dpp.selectCategory(t);
        return dpp;
    }

    private static void checkPermissions(PSPermissions.BasicTarget target, PSPermissions actual, PSPermissions expected) {
        List<String> eRoles = expected.getCategoryRoleNames();
        List<String> aRoles = actual.getCategoryRoleNames();
        PSLogger.debug("Categories from page " + aRoles);
        PSLogger.debug("Expected categories " + eRoles);
        Assert.assertEquals(aRoles, eRoles, "Incorrect list of category roles for target " + target);
        for (String c : eRoles) {
            PSPermissions.CategoryRole eRole = expected.getCategory(c);
            PSPermissions.CategoryRole aRole = actual.getCategory(c);
            Assert.assertEquals(aRole.getPermissions(), eRole.getPermissions(), "Incorrect permissions for category role " + c + " and target " + target);
        }
    }

    public static void createCategory(PSPermissions.CustomCategory role, boolean doSwitch, boolean doSubmit) {
        DefinePermissionsPage page = openDefine();
        if (doSwitch) {
            page.selectCategory(role.getObjectType());
        }

        DefinePermissionsPage.AddNewCategoryDialog dialog = page.pushAddNew();

        // Fill form and click 'Add' or 'Cancel'
        dialog.setName(role.getDisplayedName());
        dialog.setDescription(role.getDescription());
        dialog.setSequence(role.getSequence());
        if (doSwitch) {
            Assert.assertEquals(dialog.getObjectType(), role.getObjectType(), "Incorrect object type in dialog");
        } else {
            dialog.setObjectType(role.getObjectType());
        }

        if (doSubmit) {
            dialog.submit();
        } else {
            dialog.cancel();
        }
        if (!doSwitch) {
            page.selectCategory(role.getObjectType()); // select to validate
        }
        PSPermissions perms = load(page);
        PSPermissions.CategoryRole act = perms.getCategory(role.getDisplayedName());
        if (doSubmit) {
            Assert.assertNotNull(act, "Can't find created category " + role.getName());
            TestSession.getPermissions().mergePermissions(perms);
            for (PSPermissions.CategoryRole.Permission p : act.getPermissions()) {
                Assert.assertFalse(p.isDisabled(), "Permission " + p + " is disabled");
                Assert.assertFalse(p.getValue(), "Permission " + p + " is selected");
            }
        } else {
            Assert.assertNull(act, "There is category " + role.getName());
        }
    }

    public static void deleteCategory(PSPermissions.CustomCategory role, boolean doSubmit) {
        DefinePermissionsPage page = openDefine(role.getObjectType());
        DefinePermissionsPage.DeleteCategoryDialog dialog = page.pushDeleteCategory(role.getDisplayedName());
        //  click 'OK' or 'Cancel'
        if (doSubmit) {
            dialog.submit();
        } else {
            dialog.cancel();
        }
        PSPermissions perms = load(page);
        PSPermissions.CategoryRole act = perms.getCategory(role.getDisplayedName());
        if (doSubmit) {
            Assert.assertNull(act, "There is a category " + role.getName() + " after deleting");
            TestSession.getPermissions().get(role.getObjectType()).removeCategory(role.getDisplayedName());
        } else {
            Assert.assertNotNull(act, "Can't find created category " + role.getName());
        }

    }

    public static void createRole(PSPermissions.CustomPSRole role) {
        DefaultPermissionsPage page = openDefault();
        page.selectCategory(PSPermissions.BasicTarget.WI);
        PSPermissions was = load(page.getContent());
        String error = doCreateRole(page, role);
        Assert.assertNull(error, "Role create error : " + error);
        was.addCustomRole(role);
        PSPermissions now = load(page.getContent());
        checkSets(was.getCategoryTarget(), now, was);
        TestSession.getPermissions().mergePermissions(now);
        role.setCreated();
    }

    public static String doCreateRole(PSPermissions.CustomPSRole role) {
        return doCreateRole(openDefault(), role);
    }

    private static String doCreateRole(DefaultPermissionsPage page, PSPermissions.CustomPSRole role) {
        /* --- Create new role --- */
        IPermissionsPage.Content.AddNewDialog dialog = page.getContent().pushAddNew();

        setCoreSetRoleData(dialog, role);
        dialog.save();

        return page.getErrorBoxMessage();
    }

    private static void setCoreSetRoleData(IPermissionsPage.Content.AddNewDialog dialog, PSPermissions.CustomPSRole role) {
        if (role.getSequence() != null)
            dialog.setSequence(role.getSequence());
        if (role.getName() != null)
            dialog.setName(role.getName());
        if (role.getPluralName() != null)
            dialog.setPluralName(role.getPluralName());
        if (role.getDescription() != null)
            dialog.setDescription(role.getDescription());
    }

    public static void validateRole(PSPermissions.CustomPSRole role) {
        DefaultPermissionsPage page = openDefault();
        page.selectCategory(PSPermissions.BasicTarget.WI);
        IPermissionsPage.Content.AddNewDialog dialog = page.getContent().pushLinkToEdit(role.getName());

        Assert.assertEquals(dialog.getSequence(), role.getSequence(), "Incorrect sequence for role " + role.getName());
        Assert.assertEquals(dialog.getName(), role.getName(), "Incorrect name for role " + role.getName());
        Assert.assertEquals(dialog.getPluralName(), role.getPluralName(), "Incorrect plural name for role " + role.getName());
        Assert.assertEquals(dialog.getDescription(), role.getDescription(), "Incorrect description for role" + role.getName());
        dialog.cancel();
    }

    public static void editRole(PSPermissions.CustomPSRole was, PSPermissions.CustomPSRole now) {
        DefaultPermissionsPage page = openDefault();
        page.selectCategory(PSPermissions.BasicTarget.WI);

        PSPermissions perms1 = load(page.getContent());
        IPermissionsPage.Content.AddNewDialog dialog = page.getContent().pushLinkToEdit(was.getName());
        setCoreSetRoleData(dialog, now);
        dialog.save();

        if (now.getName() != null && !was.getName().equals(now.getName())) {
            for (PSPermissions.PSRole r : perms1.getRoles(was)) {
                r.setName(now.getName());
            }
        }
        PSPermissions perms2 = load(page.getContent());
        checkSets(perms1.getCategoryTarget(), perms2, perms1);
        if (!was.getName().equals(now.getName()))
            TestSession.getPermissions().mergePermissions(perms2);
    }

    public static void deleteRole(PSPermissions.CustomPSRole role) {
        DefaultPermissionsPage page = openDefault();
        page.selectCategory(PSPermissions.BasicTarget.WI);
        PSPermissions perms1 = load(page.getContent());

        IPermissionsPage.Content.DeleteCoreSetDialog dialog2 = page.getContent().pushCoreSetDeleteImg(role.getName());
        dialog2.ok();
        perms1.removeRole(role);
        PSPermissions perms2 = load(page.getContent());
        checkSets(perms1.getCategoryTarget(), perms2, perms1);
        TestSession.getPermissions().get(PSPermissions.BasicTarget.WI).removeRole(role);
        role.setDeleted();
    }

    public static void deleteUser(PSPermissions.BasicTarget target, User user) {
        DefaultPermissionsPage page = openDefault(target);
        PSPermissions perms1 = load(page.getContent());
        IPermissionsPage.Content.DeleteCustomSetDialog dialog = page.getContent().pushCustomSetDeleteImg(user.getFormatFullName());
        dialog.ok();
        perms1.removeUser(user);
        //Ensure the role is deleted
        Assert.assertFalse(page.getContent().hasCustomSet(user.getFormatFullName()), "Custom set for " + user + " still exist after deleting");
        PSPermissions perms2 = load(page.getContent());
        checkSets(perms1.getCategoryTarget(), perms2, perms1);
        TestSession.getPermissions().get(target).removeUser(user);
    }


}
