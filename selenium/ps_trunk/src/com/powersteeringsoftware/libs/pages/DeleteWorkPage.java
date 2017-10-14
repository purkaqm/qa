package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.DeleteWorkPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 07.06.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class DeleteWorkPage extends AbstractWorkPage {

    public Results delete() {
        PSLogger.info("Delete work");
        Button bt = new Button(DELETE);
        bt.submit();
        return new Results();
    }

    public boolean checkUrl() {
        return super.checkUrl() && !getResultsInstance().checkUrl();
    }


    public Results getResultsInstance() {
        return new Results();
    }

    public static class Results extends PSPage {

        private Results() {
            super();
        }

        public boolean checkUrl() {
            return checkUrl(URL_RESULT);
        }

        @Override
        public void open() {
            throw new UnsupportedOperationException("Don't use this");
        }

        public HomePage ok() {
            Button bt = new Button(DELETE_OK);
            bt.submit();
            return new HomePage();
        }
    }
}
