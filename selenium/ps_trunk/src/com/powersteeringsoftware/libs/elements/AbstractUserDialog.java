package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.UserDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.09.11
 * Time: 15:27
 */
public abstract class AbstractUserDialog extends Dialog {

    private final static int ITERATIONS_TO_SEARCH = 5;
    private final static TimerWaiter STEP = new TimerWaiter(2000);

    /**
     * The constructors
     *
     * @param locator
     */
    public AbstractUserDialog(ILocatorable loc, ILocatorable pop) {
        super(loc);
        setPopup(pop);
    }

    public AbstractUserDialog(String locator) {
        super(locator);
    }

    public AbstractUserDialog(Element e) {
        super(e);
    }

    public void search(User user) {
        search(user.getFirstName());
    }


    public void search(String toSearch) {
        search(toSearch, ITERATIONS_TO_SEARCH);
    }

    public void search(String toSearch, int num) {
        PSLogger.info("Search '" + toSearch + "'.");
        Input in = new Input(getPopup().getChildByXpath(FIND_INPUT_FIELD));
        in.type(toSearch);
        for (int i = 0; i < num; i++) {
            PSLogger.debug("Attempt #" + (i + 1));
            doGo();
            Element noMsg = getPopup().getChildByXpath(NO_RESULTS_MSG);
            if (noMsg.isDEPresent()) {
                PSLogger.warn(noMsg.getDEText());
                STEP.waitTime();
            } else {
                return;
            }
        }
    }

    protected void doGo() {
        getPopup().getChildByXpath(GO_BUTTON).click(false);
        try {
            new Element(ONE_MOMENT_PLEASE).waitForDisapeared();
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(we.getMessage());
        }
        getPopup().setDefaultElement();
    }

    public List<Element> getSearchResults() {
        return Element.searchElementsByXpath(getPopup(), RESULT_LINK);
    }

    public Element getSearchResult(String user) {
        for (Element res : getSearchResults()) {
            if (res.getDEText().equals(user)) {
                return res;
            }
        }
        return null;
    }

}
