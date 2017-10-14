package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.PortfolioDetailsPageLocators.EDIT;
import static com.powersteeringsoftware.libs.enums.page_locators.PortfolioDetailsPageLocators.URL;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 17.01.14
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class PortfolioDetailsPage extends PSTablePage {
    @Override
    public void open() {
        if (url == null) throw new NullPointerException(getClass().getName() + ": url should be specified");
        open(url);
        document = null;
    }


    public static PortfolioDetailsPage openPage(String id) {
        String url = makeUrl(URL, id);
        PortfolioDetailsPage res = new PortfolioDetailsPage();
        res.url = url;
        res.open();
        return res;
    }


    public SummaryWorkPage openWork(String name) {
        List<Link> links = getNameLinks();
        for (Link l : links) {
            if (name.equals(l.getName())) {
                SummaryWorkPage res = SummaryWorkPage.getInstance();
                l.setResultPage(res);
                l.clickAndWaitNextPage();
                return res;
            }
        }
        PSLogger.info("There are " + links + " works on page");
        Assert.fail("Can't find specified work '" + name + "'");
        return null;
    }

    public EditPortfolioPage edit() {
        new Link(EDIT).clickAndWaitNextPage();
        return new EditPortfolioPage();
    }

}
