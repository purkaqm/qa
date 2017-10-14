package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.Processes94PageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 19.12.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class Processes94Page extends ProcessesPage {

    private Button getAddNew() {
        return new Button(ADD);
    }

    public ProcessDialog addNew() {
        Button add = getAddNew();
        add.waitForVisible(WAITER);
        add.click(false);
        ProcessDialog res = new ProcessDialog();
        res.waitForVisible();
        return res;
    }

    public List<String> getProcesses() {
        List<String> res = new ArrayList<String>();
        for (Element e : getElements(false, TD_NAME)) {
            res.add(e.getDEText());
        }
        return res;
    }

    public class ProcessDialog extends Dialog {
        private static final long NEW_ROW_TIMEOUT = 60 * 1000;
        private static final long INPUT_TIMEOUT = 10 * 1000;
        private List<Phase> phases = new ArrayList<Phase>();

        public ProcessDialog() {
            super(DIALOG);
        }

        public void waitForVisible() {
            super.waitForVisible();
            setDefaultElement(getDocument());
        }

        public void setName(String n) {
            PSLogger.info("Set process name '" + n + "'");
            new Input(NAME).type(n);
            TimerWaiter.waitTime(500);
        }

        public void setDescription(String d) {
            PSLogger.info("Set process description '" + d + "'");
            new TextArea(getChildByXpath(DESCRIPTION)).setText(d);
            TimerWaiter.waitTime(500);
        }

        public void addPhase(String name, String percent) {
            if (phases.isEmpty()) {
                phases = initGrid();
            }
            Assert.assertTrue(phases.size() > 0, "Can't find any row in grid");
            Phase p = phases.get(phases.size() - 1);
            p.setName(name);
            if (percent != null)
                p.setPercent(percent);
            PSLogger.save("After add phase " + name);
        }

        private List<Phase> initGrid() {
            List<Phase> phases = new ArrayList<Phase>();
            setDefaultElement();
            for (Element e : Element.searchElementsByXpath(this, PHASE_NAME)) {
                phases.add(new Phase(e.getParent()));
            }
            return phases;
        }

        public void submit() {
            PSLogger.save("Before submit");
            new Button(UPDATE).click(false);
            try {
                waitForUnvisible();
            } catch (Wait.WaitTimedOutException w) {
                checkJSError();
                throw w;
            }
            phases.clear();
            getAddNew().waitForValue();
            WAITER.waitTime();
            PSLogger.save("After submit");
            Processes94Page.this.getDocument();
        }

        public class Phase extends Element {
            private Phase(Element e) {
                super(e);
            }

            public void setName(String n) {
                Element parent = getChildByXpath(PHASE_NAME);
                parent.setSimpleLocator();
                parent.click(false);
                Input in = new Input(parent.getChildByXpath(INPUT));
                in.waitForVisible(INPUT_TIMEOUT);
                in.type(n);
                ProcessDialog.this.mouseDownAndUp();
                try {
                    in.waitForUnvisible(INPUT_TIMEOUT);
                } catch (Wait.WaitTimedOutException ww) {
                    Assert.fail("The input field is still visible");
                }
                List<Phase> was = new ArrayList<Phase>(phases);
                long s = System.currentTimeMillis();
                while (System.currentTimeMillis() - s < NEW_ROW_TIMEOUT) {
                    WAITER.waitTime();
                    phases = initGrid();
                    if (phases.size() > was.size()) {
                        return;
                    }
                    was = new ArrayList<Phase>(phases);
                }
                Assert.fail("No new line");
            }

            public void setPercent(String p) {
                Element parent = getChildByXpath(PHASE_NUMBER);
                parent.setSimpleLocator();
                parent.click(false);
                Input in = new Input(parent.getChildByXpath(INPUT));
                in.waitForVisible(INPUT_TIMEOUT);
                in.type(p);
                ProcessDialog.this.mouseDownAndUp();
                in.waitForUnvisible(INPUT_TIMEOUT);
            }

        }
    }
}
