package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;

import static com.powersteeringsoftware.libs.enums.elements_locators.WorkChooserDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 15:28:05
 */
public class WorkChooserDialog extends TabsDialog {
    private static final int NUMBER_OF_ATTEMPTS_TO_SEARCH = 3;
    public static final long TIMEOUT_FOR_SEARCHING = 120000; //ms
    private static final long TIME_STEP = 1000; //ms
    protected SearchTab searchTab;

    public WorkChooserDialog(DisplayTextBox e) {
        this(e.getIcon());
        setPopup(POPUP);
    }

    protected WorkChooserDialog(Element e) {
        super(e);
    }

    public WorkChooserDialog(String loc, ILocatorable pop) {
        this(loc, pop.getLocator());
    }

    public WorkChooserDialog(String loc, String pop) {
        super(loc);
        setPopup(pop);
    }

    public WorkChooserDialog(ILocatorable loc, ILocatorable pop) {
        this(loc.getLocator(), pop);
    }


    public void openBrowseTab() {
        openTab(TAB_BROWSE);
    }

    protected class WorkSearchTab extends SearchTab {

        public WorkSearchTab() {
            super(getPopup(),
                    TAB_SEARCH_INPUT,
                    TAB_SEARCH_SUBMIT,
                    TAB_SEARCH_LOADING,
                    TAB_SEARCH_ERROR);
        }

        public Link getLink(String name) {
            //div[@id='searchTree' or @id='resultsDiv']//div[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]
            for (Element e : Element.searchElementsByXpath(this, TAB_SEARCH_LINK)) {
                if (!name.equals(e.getDEText())) continue;
                if (e.exists())
                    return new Link(e);
            }

            PSLogger.warn("Can't find de for " + name);
            PSLogger.debug(asXML());
            return null;
        }
    }

    protected void initSearchTab() {
        searchTab = new WorkSearchTab();
    }

    public void openSearchTab() {
        openTab(TAB_SEARCH);
        initSearchTab();
    }

    /**
     * @param pathToWork e.g. 'ROOT WORK FOR AUTOTESTS>WORK_ITEM_ANALYSIS>CHILD_1'
     */
    @Deprecated
    public void chooseWorkOnBrowseTab(String pathToWork) {
        WorkTreeElement tree = new WorkTreeElement();
        tree.setPopup(getPopup());
        String toOpen;
        if (pathToWork.matches(".*\\s*" + Work.NAME_SEPARATOR + "\\s*.*")) {
            String[] works = pathToWork.split("\\s*" + Work.NAME_SEPARATOR + "\\s*");
            for (int i = 0; i < works.length - 1; i++) {
                tree.openTree(works[i]);
            }
            toOpen = works[works.length - 1];
        } else {
            tree.openTree();
            toOpen = pathToWork;
        }
        Link link = tree.getWorkLink(toOpen);
        if (link == null) {
            PSLogger.warn("Can't get link for work " + pathToWork);
            PSLogger.save();
            return;
        }
        link.click(false);
        waitAfterMakingChoice();
    }

    public void chooseWorkOnBrowseTab(Work work) {
        WorkTreeElement tree = new WorkTreeElement();
        tree.setPopup(getPopup());
        Link link = tree.openTree(work);
        if (link == null) {
            PSLogger.warn("Can't get link for work " + work.getFullName());
            PSLogger.save();
            return;
        }
        link.click(false);
        waitAfterMakingChoice();
    }


    public void chooseWorkOnSearchTab(Work work) {
        for (int i = 0; i < NUMBER_OF_ATTEMPTS_TO_SEARCH; i++) {
            try {
                getSearchTab().choose(work.getName());
                waitAfterMakingChoice();
                break;
            } catch (AssertionError ae) {
                if (i == NUMBER_OF_ATTEMPTS_TO_SEARCH - 1) throw ae;
                PSLogger.warn(ae);
            }
        }
    }

    public void selectWorkOnBrowseTab(String pathToWork) {
        WorkTreeElement tree = new WorkTreeElement();
        tree.setPopup(getPopup());
        String[] works = null;
        if (pathToWork.matches(".*\\s*" + Work.NAME_SEPARATOR + "\\s*.*")) {
            works = pathToWork.split("\\s*" + Work.NAME_SEPARATOR + "\\s*");
        }
        if (works == null) {
            if (pathToWork != null) works = new String[]{pathToWork};
            else return;
        }

        for (int i = 0; i < works.length; i++) {
            Link lnk = tree.getWorkLink(works[i]);
            if (i == works.length - 1) {
                lnk.click(false);
                waitAfterMakingChoice();
            } else {
                tree.openWorkNode(works[i]);
            }
        }
    }

    public void clear() {
        new Link(getPopup().getChildByXpath(DLG_CLEAR_LINK)).click(false);
        getPopup().waitForUnvisible();
    }

    public void waitAfterMakingChoice() {
        getPopup().waitForUnvisible();
    }

    public SearchTab getSearchTab() {
        return searchTab;
    }

    public void setWithWaitingForReindexSearching(String toSet) {
        String msg = null;
        long start = System.currentTimeMillis();
        do {
            if (msg != null) {
                PSLogger.info(msg);
                if (System.currentTimeMillis() - start > TIMEOUT_FOR_SEARCHING) {
                    PSLogger.warn("Timeout for searching is exceeded");
                    PSLogger.save();
                    break;
                }
                try {
                    Thread.sleep(TIME_STEP);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
            getSearchTab().doSearch(toSet);
        } while ((msg = getSearchTab().getErrorMessage()) != null);
        Link link = getSearchTab().getLink(toSet);
        link.click(false); // its don't work for chrome 12 and web-driver 2.0b2 or later
        waitAfterMakingChoice();
    }
}
