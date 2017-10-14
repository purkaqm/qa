package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 10.10.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public interface LinkMenu {

    void open();

    List<String> getMenuItems();

    Link getMenuLink(String lab);

    Link getMenuLink(ILocatorable lab);
}
