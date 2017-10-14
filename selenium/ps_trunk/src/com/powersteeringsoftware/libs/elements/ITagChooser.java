package com.powersteeringsoftware.libs.elements;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 01.11.11
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public interface ITagChooser {

    List<String> getAllLabels();

    void setName(String label);

    String getName();

    void openPopup();

    void closePopup();

    void waitForPopup(long timeout);

    Element getPopup();

    void setLabel(String label);

    String getContentString();

}
