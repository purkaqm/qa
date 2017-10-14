package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.actions.UixManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for actions in 'ROOT WORK FOR AUTOTESTS' in add new subwork popup
 * for depends see TestDriver
 * User: szuev
 * Date: 13.05.2010
 * Time: 16:28:08
 */
public class OurAddSubWorkPopupTCs extends GeneralAddSubWorkPopupTCs {

    public void addUnderCancel(Work root, Work r, Work g) {
        WBSPage pc = WorkManager.openWBS(root, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();
        checkCancel(pc, popup, r, g);
    }

    public void addAfterCancel(Work root, Work s, Work h) {
        WBSPage pc = WorkManager.openWBS(root, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu(root.getChild(3).getName()).addAfter();
        checkCancel(pc, popup, s, h);
    }

    public void addUnderPopupHeader(Work w) {
        WBSPage pc = WorkManager.openWBS(w, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();
        addPopupHeader(popup,
                WBSEPageLocators.EXPECTED_TITLE_POPUP_UNDER_PREFIX.getLocator() + w.getName(),
                WBSEPageLocators.getAddSubWorkHeader(UixManager.isResourcePlanningOn()));
    }


    public void addAfterPopupHeader(Work parent) {
        WBSPage pc = WorkManager.openWBS(parent, false);
        Work w;
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu((w = parent.getChildren().get(0)).getName()).addAfter();
        addPopupHeader(popup,
                WBSEPageLocators.EXPECTED_TITLE_POPUP_AFTER_PREFIX.getLocator() + w.getName(),
                WBSEPageLocators.getAddSubWorkHeader(UixManager.isResourcePlanningOn()));
    }

    public void checkAllTypesFromPopup(Work root) {
        WBSPage pc = WorkManager.openWBS(root, false);
        WBSPage.AddSubWorkDialog popup = null;
        try {
            popup = pc.getGrid().callSubMenu().addUnder();
            WorkType[] childs = popup.getAllWorkTypes();
            List<WorkType> actual = Arrays.asList(childs);
            List<WorkType> expected = TestSession.getWorkTypeList();
            PSLogger.info("Types from page: " + actual);
            PSLogger.info("Expected types : " + expected);
            Assert.assertTrue(actual.containsAll(expected), "Seems incorrect list of types. Should be " + expected);
        } catch (AssertionError t) {
            PSLogger.save(t);
            PSLogger.fatal(t);
            throw t;
        } finally {
            try {
                if (popup != null)
                    popup.close();
            } catch (Exception e) {
                PSLogger.warn("checkAllTypesFromPopup: " + e);
            }
        }
    }

    public void addUnderTypeColumn(Work root) {
        WBSPage pc = WorkManager.openWBS(root, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();
        addTypeColumn(popup);
    }


    public void addAfterTypeColumn(Work root) {
        WBSPage pc = WorkManager.openWBS(root, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu(root.getChild(3).getName()).addAfter();
        addTypeColumn(popup);
    }

    public void validatePredecessorsWithIndentOputdentUnder(Work parent, Work _parent) {

        WBSPage pc = WorkManager.openWBS(parent, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();

        popup.setChildTree(_parent);
        validatePredecessor(false, popup, parent, _parent.getAllChildrenArray());
    }

    public void validatePredecessorsUnder(Work parent, Work _parent) {
        WBSPage pc = WorkManager.openWBS(parent, false);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu().addUnder();

        Work[] childs = new Work[]{_parent.getChild("D"), _parent.getChild("E"), _parent.getChild("F")};
        popup.setChildWorks(childs);
        validatePredecessor(false, popup, parent, childs);
    }

    public void validatePredecessorsWithIndentOputdentAfter(Work parent, Work _parent) {
        Work tmp = parent.getChild(3);
        WBSPage pc = WorkManager.openWBS(parent);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu(tmp.getName()).addAfter();

        popup.setChildTree(_parent);
        validatePredecessor(true, popup, tmp, _parent.getAllChildrenArray());
    }

    public void validatePredecessorsAfter(Work parent, Work _parent) {
        WBSPage pc = WorkManager.openWBS(parent, false);
        Work tmp = parent.getChildren().get(0);
        WBSPage.AddSubWorkDialog popup = pc.getGrid().callSubMenu(tmp.getName()).addAfter();
        Work[] childs = new Work[]{_parent.getChild("D"), _parent.getChild("E"), _parent.getChild("F")};
        popup.setChildWorks(childs);
        validatePredecessor(true, popup, tmp, childs);
    }


    private void checkCancel(WBSPage pc, WBSPage.AddSubWorkDialog popup, Work a, Work b) {
        popup.setChildWorks(a, b);
        popup.cancel();
        pc.refresh();
        PSLogger.info("Validate project tree");
        List<String> listFromPage = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + listFromPage);
        Assert.assertFalse(listFromPage.contains(b.getName()), "There is " + b.getName() +
                " child on page");
        Assert.assertEquals(listFromPage.indexOf(a.getName()),
                listFromPage.lastIndexOf(a.getName()), "There are more then one " + a.getName() +
                        " children on page"
        );
    }

    private void addTypeColumn(WBSPage.AddSubWorkDialog popup) {
        List<WorkType> types = TestSession.getWorkTypeList();
        PSLogger.info("All types: " + types);
        try {
            List<String> expectedList = new ArrayList<String>();
            for (int i = 0; i < WBSEPageLocators.POPUP_DEFAULT_ROWS_NUMBER.getInt(); i++) {
                expectedList.add(types.get(0).getName());
            }
            for (int i = 0; i < types.size(); i++) {
                popup.setChildWork(types.get(i), i + 1);
                if (i + 1 >= expectedList.size()) {
                    expectedList.add(null); // add new line
                }
                for (int j = i; j < expectedList.size(); j++) {
                    expectedList.set(j, types.get(i).getName());
                }
                PSLogger.save();
                List<String> listFromPage = popup.getValuesFromTypesColumn();
                PSLogger.info("From page: " + listFromPage);
                PSLogger.info("Expected: " + expectedList);
                Assert.assertEquals(listFromPage, expectedList, "Incorrect popups behaviour");
            }
        } catch (AssertionError t) {
            PSLogger.save(t);
            throw t;
        } finally {
            popup.close();
        }
    }

    private void addPopupHeader(WBSPage.AddSubWorkDialog popup, String title, List<String> expectedList) {
        try {
            List<String> listFromPage = popup.getTableHead();
            PSLogger.info("Page header: " + listFromPage);
            String titleFromPage = popup.getTitle();
            PSLogger.info("Page title: " + titleFromPage);
            PSLogger.info("Expected header: " + expectedList);
            Assert.assertEquals(listFromPage, expectedList, "Incorrect header for popup");
            Assert.assertEquals(titleFromPage, title, "Seems title is incorrect");
        } catch (AssertionError t) {
            PSLogger.save(t);
            throw t;
        } finally {
            PSLogger.save("Before closing");
            popup.close();
        }
    }

    private void validatePredecessor(boolean after, WBSPage.AddSubWorkDialog popup, Work parent, Work... childs) {
        try {
            int index = parent.getGeneralIndex();
            List<String> expectedPredecessors = new ArrayList<String>();
            expectedPredecessors.add(after ? String.valueOf(index) : "");
            for (int i = 1; i < childs.length + 1; i++) {
                String actual = popup.getNumber(i);
                String expected = index + "." + i;
                Work w = childs[i - 1];
                w.setGeneralIndex(i + index);
                Assert.assertEquals(actual, expected, "Incorrect work index in dialog for project " + w.getName());
                Work p = w.getPredecessorSibling();
                if (i != 1)
                    expectedPredecessors.add(p == null ? "" : index + "." + (p.getGeneralIndex() - index));
            }
            expectedPredecessors.add("");
            while (expectedPredecessors.size() < WBSEPageLocators.ADD_NEW_SUB_WORK_ROWS_NUM.getInt()) {
                expectedPredecessors.add("");
            }
            popup.setPredecessor();
            List<String> listFromPage = popup.getPredecessors();
            PSLogger.info("list from page: " + listFromPage);
            PSLogger.info("expected: " + expectedPredecessors);
            Assert.assertEquals(listFromPage, expectedPredecessors, "Incorrect predecessors");
        } catch (AssertionError t) {
            PSLogger.save(t);
            throw t;
        } finally {
            PSLogger.save("before closing");
            popup.close();
        }
    }

}
