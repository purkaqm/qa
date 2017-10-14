package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.awt.*;

import static com.powersteeringsoftware.libs.enums.elements_locators.HeaderLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.11.2010
 * Time: 17:00:15
 */
public class Header extends Element {
    public Header() {
        super(THIS);
        setId(ID);
    }

    public Color getBackgroundColor() {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            return new Element(HEADER_110).getBackgroundColor();
        }
        return super.getBackgroundColor();
    }
}
