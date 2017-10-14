package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.StrUtil;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DocumentDetailsPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
public class DocumentDetailsPage extends AbstractWorkPage {
    @Override
    public void open() {
        //do not nothing
    }

    public static class Upload extends DocumentDetailsPage {
        public void setFile(String path) {
            if (!path.contains(CoreProperties.getServerFolder())) path = CoreProperties.getServerFolder() + path;
            new Input(UPLOAD_INPUT).attachFile(path);
        }

        public void setComments(String txt) {
            new TextArea(COMMENTS_TEXTAREA).setText(txt);
        }

        public DocumentDetailsPage submit() {
            new Button(new Link(UPLOAD_BUTTON_LINK)).submit();
            return new DocumentDetailsPage();
        }
    }

    public Edit edit() {
        new Button(new Link(EDIT_DETAILS_LINK)).submit();
        return new Edit();
    }


    public static class Edit extends AbstractWorkPage {

        @Override
        public void open() {
            // ?
        }

        public void setTitle(String title) {
            new Input(TITLE_INPUT).type(title);
        }

        public void setDescription(String txt) {
            if (txt != null)
                new TextArea(DESCRIPTION_TEXTAREA).setText(txt);
        }

        public DocumentDetailsPage submit() {
            new Button(SUBMIT).submit();
            return new DocumentDetailsPage();
        }
    }

    private Element getTable() {
        return getElement(TABLE);
    }

    private List<Element> getRows() {
        return Element.searchElementsByXpath(getTable(), ROW);
    }

    public Row getRow(int version) {
        String sVersion = VERSION_COLUMN_LINK_TXT.replace(version);
        for (Element row : getRows()) {
            Element eVer = row.getChildByXpath(VERSION_COLUMN);
            if (!eVer.isDEPresent()) continue;
            if (eVer.getChildByXpath(VERSION_COLUMN_LINK).getDEText().trim().equals(sVersion))
                return new Row(row, sVersion);
        }
        return null;
    }

    public class Row extends Element {
        private String ver;

        private Row(Element e, String ver) {
            super(e);
            this.ver = ver;
        }

        public String getHref() {
            Link link = new Link(getChildByXpath(VERSION_COLUMN).getChildByXpath(VERSION_COLUMN_LINK));
            String href = link.getHref();
            PSLogger.debug("Href for " + ver + " is '" + href + "'");
            String res = StrUtil.urlToString(href.replaceAll(".*\\([\"|']", "").replaceAll("[\"|']\\).*", ""), true);
            PSLogger.debug("herf: " + res);
            return res;
        }

        public String getComments() {
            return getChildByXpath(COMMENTS_COLUMN).getText().trim();
        }
    }
}
