package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 11.10.13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public interface LeftNavLocators extends ILocatorable {
    Storage getStorage();

    LeftNavLocators getParent();

    enum Storage implements ILocatorable {
        HOME(toCssSpanClassLoc("fui-home")),
        INBOX(toCssSpanClassLoc("fui-ps-inbox2")),
        ADD("Add", toCssSpanClassLoc("fui-plus")),
        REVIEW("Review", toCssSpanClassLoc("fui-ext-folder-clock")),
        ADMIN("Admin", toCssSpanClassLoc("fui-gear")),
        PROJECT("Project", toCssSpanClassLoc("ps-projects")),
        FAVORITES(toCssSpanClassLoc("fui-star-2")),
        HISTORY(toCssSpanClassLoc("fui-ext-back-time")),
        IMPORTANT_LINKS("Important Links", toCssSpanClassLoc("fui-ext-important-links")),;

        private enum Id {
            HomeStorage,
            InboxStorage,
            AddStorage,
            ReviewStorage,
            AdminStorage,
            ProjectStorage,
            FavoritesStorage,
            HistoryStorage,
            ImportantStorage,
        }

        Storage(String l) {
            for (Id _id : Id.values()) {
                if (ordinal() == _id.ordinal()) {
                    this.id = _id.name();
                    break;
                }
            }
            //locator = l;
            //hotfix:
            locator = toLinkStorageLoc(id);
        }

        Storage(String n, String l) {
            this(l);
            name = n;
        }

        private String locator;
        private String id;
        private String name;

        @Override
        public String getLocator() {
            return locator;
        }

        public String getName() {
            return name != null ? name : name().toLowerCase();
        }

        public String getTitleLoc() {
            return "//div[contains(text(), '" + name().replace("_", " ") + "')]";
        }

        public static String toDivIdLinkLoc(String id) {
            return "//a/div[@id='" + id + "']";
        }


        private static String _toDivTitleLoc(String title) {
            return "/div[@title='" + title + "']";
        }

        public static String toDivTitleLoc(String title) {
            return "/" + _toDivTitleLoc(title);
        }

        public static String toDivTitleLinkLoc(String title) {
            return "//a" + _toDivTitleLoc(title);
        }

        private static String toCssSpanClassLoc(String loc) {
            return "css=span." + loc;
        }

        public static String toDivIdSpanLoc(String id) {
            return "//div[@id='" + id + "']/span";
        }

        private static String toLinkStorageLoc(String id) {
            return "//a[@storageid='" + id + "']";
        }

    }
}
