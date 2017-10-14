package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.PortfoliosPageLocators.NEW;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 17.01.14
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class PortfoliosPage extends PSTablePage {
    @Override
    public void open() {
        clickPortfolios();
    }

    public PortfolioDetailsPage openPortfolio(String name) {
        List<Link> links = getNameLinks();
        for (Link l : links) {
            if (name.equals(l.getName())) {
                l.clickAndWaitNextPage();
                return new PortfolioDetailsPage();
            }
        }
        PSLogger.info("Portfolios: " + links);
        Assert.fail("No link with name '" + name + "' found");
        return null;
    }

    public EditPortfolioPage add() {
        Button add = new Button(NEW);
        add.submit();
        return new EditPortfolioPage();
    }
}
