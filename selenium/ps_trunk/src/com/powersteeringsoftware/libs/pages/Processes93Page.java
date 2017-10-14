package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.Processes93PageLocators.*;

public class Processes93Page extends ProcessesPage {

    public SecondFrame addNew() {
        FirstFrame frame = new FirstFrame();
        frame.select();
        WAITER.waitTime();
        return frame.add();
    }

    public class FirstFrame extends Frame {
        private FirstFrame() {
            super(PROCESSES_PAGE_ADMIN_AREA_FRAME_LOCATOR);
        }

        public SecondFrame add() {
            PSLogger.info("Push 'Add' button");
            Link add = new Link(ADD_BUTTON_LINK);
            add.setFrame(PROCESSES_PAGE_ADMIN_AREA_FRAME_LOCATOR);
            add.clickAndWaitNextPage();
            return new SecondFrame();
        }

        public void check(String name) {
            Link processLink = new Link(PARTICULAR_PROCESS_LINK.replace(name));
            Assert.assertTrue(processLink.exists(), "Can't find process " + name + " on page after submit");
        }

    }

    public class SecondFrame extends Frame {
        private SecondFrame() {
            super(PROCESSES_PAGE_ADMIN_AREA_FRAME_LOCATOR);
        }

        public void setPhases(int num) {
            if (num < 3 || num > 13)
                PSSkipException.skip("Less than 3 or more than 13 phases are not supported yet");
            SelectInput select = new SelectInput(PHASES_SELECT);
            select.select(PHASES_SELECT_VALUE.replace(num));
            waitForPageToLoad();
            select();
        }

        public void setPhase(int num, String name, String percent) {
            Input in1 = new Input(PHASE_NAME_INPUT_FIELD_PREFIX.replace(num));
            in1.type(name);
            if (null != percent)
                new Input(PHASE_PERCENTAGE_INPUT_FIELD_PREFIX.replace(num)).type(percent);
        }

        public void setName(String name) {
            PSLogger.info("Set name: " + name);
            Input input = new Input(NAME_INPUT_FIELD);
            input.type(name);
        }

        public void setDescription(String desc) {
            PSLogger.info("Set description : " + desc);
            TextArea tx = new TextArea(DESCRIPTION_TEXTAREA);
            tx.setText(desc);
        }

        public ThirdFrame submit() {
            PSLogger.info("Do Submit");
            Button submit = new Button(new Link(SUBMIT_BUTTON_LINK));
            submit.waitForVisible();
            submit.click(true);
            return new ThirdFrame();
        }
    }

    public class ThirdFrame extends Frame {
        private ThirdFrame() {
            super(PROCESSES_PAGE_ADMIN_AREA_FRAME_LOCATOR);
        }

        public FirstFrame backToList() {
            Img back = new Img(BACK_TO_MAIN_PAGE_LINK);
            back.waitForVisible(); // its for debug
            back.click(true);
            return new FirstFrame();
        }

    }


}
