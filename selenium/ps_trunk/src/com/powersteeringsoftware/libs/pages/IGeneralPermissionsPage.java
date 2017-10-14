package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.enums.page_locators.DefinePermissionsPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSPermissions;

import static com.powersteeringsoftware.libs.enums.page_locators.DefinePermissionsPageLocators.SELECTOR;
import static com.powersteeringsoftware.libs.enums.page_locators.DefinePermissionsPageLocators.SelectorOptions;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 28.01.12
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public interface IGeneralPermissionsPage {

    void selectCategory(PSPermissions.BasicTarget option);

    PSPermissions.BasicTarget getCategory();

    class CommonActions {
        static boolean selectCategory(PSPermissions.BasicTarget option, PSPage page) {

            SelectorOptions opt = DefinePermissionsPageLocators.SelectorOptions.get(option);

            PSLogger.info("Select category '" + opt.getName() + "'");
            SelectInput select = new SelectInput(SELECTOR);
            select.setDefaultElement(page.getDocument());
            if (select.getSelectedLabel().equalsIgnoreCase(opt.getName())) {
                PSLogger.debug("Option '" + opt.getName()+ "' already selected");
                return false;
            }
            select.select(opt.getLocator());
            page.waitForPageToLoad();
            page.getDocument();
            return true;
        }
        static PSPermissions.BasicTarget getCategory() {
            String label = new SelectInput(SELECTOR).getSelectedLabel();
            return DefinePermissionsPageLocators.SelectorOptions.getByLabel(label);
        }
    }

}
