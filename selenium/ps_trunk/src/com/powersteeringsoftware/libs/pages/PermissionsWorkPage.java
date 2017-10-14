package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class PermissionsWorkPage extends AbstractWorkPage implements IPermissionsPage {

    private Content cont;

    @Override
    public IContent getContent() {
        if (cont == null)
            cont = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? new Content94(this) : new Content93(this);
        return cont;
    }
}
