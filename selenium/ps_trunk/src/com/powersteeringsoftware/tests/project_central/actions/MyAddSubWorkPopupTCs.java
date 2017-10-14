package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for actions in my work in add new subwork popup (creted by AuxiliaryTCs.createFirstWorkGroup)
 * for depends see TestDriver
 * User: szuev
 * Date: 21.05.2010
 * Time: 12:41:39
 */
public class MyAddSubWorkPopupTCs extends GeneralAddSubWorkPopupTCs {


    public void addUnderAndDragAndDrop(Work main, Work a, Work b, Work c) {
        WBSPage pc = WorkManager.openWBS(main, false);

        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();
        popup.setChildWorks(a, b, c);
        PSLogger.info("Move " + c.getName() + " between " + a.getName() + " and " + b.getName());
        popup.dragAndDropRow(3, 1);
        popup.submit();
        List<String> errs = pc.getErrorMessagesFromTop();
        Assert.assertTrue(errs.size() == 0, "There are errors on page: " + errs);
        if (pc.isSaveAreaEnabled()) {
            PSLogger.knis(70242);
            PSLogger.warn("'Save' is enabled!");
            pc.saveArea();
        }
        PSLogger.info("Check that children present and sequence is correct");
        Assert.assertTrue(pc.getGrid().isWorkPresent(a.getName()) &&
                        pc.getGrid().isWorkPresent(b.getName()) &&
                        pc.getGrid().isWorkPresent(c.getName()),
                "Some children are not found"
        );
        PSLogger.info("Validate drag'n'drop");
        main.addChild(a);
        main.addChild(c);
        main.addChild(b);

        List<String> listFromPage = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + listFromPage);
        List<String> expectedList = new ArrayList<String>();
        expectedList.add(main.getName());
        for (Work w : main.getChildren())
            expectedList.add(w.getName());

        Assert.assertEquals(listFromPage, expectedList, "Incorrect list after adding children by 'Add Under'");
        //main.replaceChildren(1, 2);
        for (Work w : new Work[]{a, b, c}) {
            w.setCreated();
            PSLogger.debug(w.getConfig());
            PSLogger.debug(w + "," + w.getCreationPSDate());
        }

    }

    public void addAfterForFirstChild(Work main, Work a, Work d, Work e, Work f) {
        WBSPage pc = WorkManager.openWBS(main, false);

        PSLogger.info("Add After for " + a.getName());
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu(a.getName()).addAfter();
        popup.setChildWorks(d, e, f);
        popup.submit();
        main.addChild(d);
        main.addChild(e);
        main.addChild(f);
        a.insertAfter(d, e, f);

        PSLogger.info("Check that children present and sequence is correct");
        Assert.assertTrue(pc.getGrid().isWorkPresent(d.getName()), d.getName() + " not found");
        Assert.assertTrue(pc.getGrid().isWorkPresent(e.getName()), e.getName() + " not found");
        Assert.assertTrue(pc.getGrid().isWorkPresent(f.getName()), f.getName() + " not found");
        PSLogger.info("Validate project tree");
        List<String> listFromPage = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + listFromPage);

        List<String> expectedList = new ArrayList<String>();
        expectedList.add(main.getName());
        for (Work w : main.getChildren())
            expectedList.add(w.getName());
        Assert.assertEquals(listFromPage, expectedList, "Incorrect list after adding children by 'Add After'");

        List<String> errs = pc.getErrorMessagesFromTop();
        if (errs.size() != 0) {

            // E has index 4
            String msg = WBSEPageLocators.MESSAGE_ERROR_MOVE.replace(4);
            if (errs.size() == 1 && errs.get(0).contains(msg)) {
                if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4)) {
                    PSLogger.info("Warn: " + errs); //#83285
                } else if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._8_2_2)) {
                    PSLogger.knis(70966);
                } else {
                    Assert.fail(errs.toString());
                }
            } else {
                Assert.fail(errs.toString());
            }
        }
        // Assert.assertTrue(errs.size() == 0, "There are errors on page: " + errs);
        if (pc.isSaveAreaEnabled()) {
            PSLogger.warn("'Save' is enabled!");
            PSLogger.knis(70242);
            pc.saveArea();
        }
        d.setCreated();
        e.setCreated();
        f.setCreated();
    }

    public void checkIndentOutdentTreeInPopup(Work parent, Work i) {
        PSLogger.info("Create branch " + i.getName() + " in parent " + parent.getName());
        WBSPage pc = WBSPage.openWBSPage(parent, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();
        popup.setChildWorks(i);
        popup.submit();
        if (pc.isSaveAreaEnabled()) {
            PSLogger.warn("'Save' is enabled after adding folder!");
            PSLogger.knis(70242);
            pc.saveArea();
        }
        i.setCreated();
        parent.addChild(i);
        //Assert.assertFalse(pc.isSaveAreaEnabled(), "Save button is enabled");

        PSLogger.info("Create tree for " + i.getName());
        popup = pc.getGrid().callSubMenu(i.getName()).addUnder();
        popup.setChildTree(i);
        String tree1 = popup.makeStringTreeForParent(i);
        PSLogger.info(tree1);
        popup.submit();
        List<String> messages = pc.getMessagesFromTopAndClose();
        PSLogger.info(messages);
        Assert.assertEquals(tree1, i.print(), "Incorrect tree in popup for " + i.getName());
        if (pc.isSaveAreaEnabled()) {
            PSLogger.warn("'Save' is enabled after adding tree!");
            PSLogger.knis(70242);
            pc.saveArea();
        }
        i.setChildrenCreated();
        //Assert.assertFalse(pc.isSaveAreaEnabled(), "Save button is enabled");
    }


}
