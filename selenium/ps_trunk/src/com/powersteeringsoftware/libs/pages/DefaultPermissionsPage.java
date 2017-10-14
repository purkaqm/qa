package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import static com.powersteeringsoftware.libs.enums.page_locators.DefaultPermissionsPageLocators.CORE_SET_TABLE;
import static com.powersteeringsoftware.libs.enums.page_locators.DefaultPermissionsPageLocators.CUSTOM_SET_TABLE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.07.2010
 * Time: 15:15:11
 */
public class DefaultPermissionsPage extends PSPage implements IGeneralPermissionsPage, IPermissionsPage {

    private IGeneralContent cont;

    public IGeneralContent getContent() {
        if (cont == null)
            cont = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? new GeneralContent94() : new GeneralContent93();
        return cont;
    }

    public interface IGeneralContent extends IContent {
        Content.AddNewDialog pushLinkToEdit(String name);

        Content.AddNewDialog pushAddNew();

        Content.DeleteCoreSetDialog pushCoreSetDeleteImg(String name);
    }


    public class GeneralContent94 extends Content94 implements IGeneralContent {
        private GeneralContent94() {
            super(DefaultPermissionsPage.this);
        }

        public AddNewDialog pushAddNew() {
            return super.pushAddNew();
        }

        public AddNewDialog pushLinkToEdit(String name) {
            getCoreSetScroll().doScroll(1000); // hotfix
            return super.pushLinkToEdit(name);
        }

        public DeleteCoreSetDialog pushCoreSetDeleteImg(String name) {
            return super.pushCoreSetDeleteImg(name);
        }
    }

    public class GeneralContent93 extends Content93 implements IGeneralContent {

        private GeneralContent93() {
            super(DefaultPermissionsPage.this);
        }

        public AddNewDialog pushAddNew() {
            return super.pushAddNew();
        }

        public AddNewDialog pushLinkToEdit(String name) {
            return super.pushLinkToEdit(name);
        }

        public DeleteCoreSetDialog pushCoreSetDeleteImg(String name) {
            return super.pushCoreSetDeleteImg(name);
        }
    }


    public void open() {
        clickAdminDefaults();
        ((Content) getContent()).reset();
    }

    public void selectCategory(PSPermissions.BasicTarget option) {
        if (CommonActions.selectCategory(option, this)) {
            new Element(CORE_SET_TABLE).waitForPresent();
            new Element(CUSTOM_SET_TABLE).waitForPresent();
        }
        ((Content) getContent()).reset();
    }

    public PSPermissions.BasicTarget getCategory() {
        return CommonActions.getCategory();
    }


    @Override
    public String getErrorBoxMessage() {
        String errMsg = getAlert();
        if (errMsg != null) {
            return errMsg;
        }
        return super.getErrorBoxMessage();
    }


}
