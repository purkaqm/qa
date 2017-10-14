package com.powersteeringsoftware.libs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is util for converting xpath->css, css->xpath, css->dojo
 * <p/>
 * User: szuev
 * Date: 29.07.11
 * Time: 16:29
 */
public class Locators {

    public static final String MY_LOCATOR_PATTERN = "my_";
    public static final String ID_LOCATOR_PATTERN = "id=";
    public static final String IDENTIFIER_LOCATOR_PATTERN = "identifier=";
    public static final String NAME_LOCATOR_PATTERN = "name=";
    public static final String CSS_LOCATOR_PATTERN = "css=";
    public static final String LINK_LOCATOR_PATTERN = "link=";
    public static final String XPATH_LOCATOR_PATTERN = "xpath=";
    public static final String DOM_LOCATOR_PATTERN = "dom=";
    public static final String DOM_LOCATOR_DOCUMENT = "document";
    public static final String DOM_LOCATOR_DOCUMENT_PATTERN = DOM_LOCATOR_PATTERN + DOM_LOCATOR_DOCUMENT;
    public static final String DOM_LOCATOR_WINDOW = "window";
    public static final String DOM_LOCATOR_FRAMES = ".frames";
    public static final String DOM_LOCATOR_WINDOW_PATTERN = DOM_LOCATOR_PATTERN + DOM_LOCATOR_WINDOW;


    /**
     * convert simple xpath locator to css selector.
     * Examples:
     * //a[contains(text(),'RRRR') and contains(@onclick, 'TesT')]                        => css=a:contains('RRRR')[onclick*='TesT']
     * //a[@id='II' or @name='NNNN']                                                      => css=a[id='II'],[name='NNNN']
     * //a[contains(@href, '9.0/t/pt/S.epage?sp=Uop67jrg0000imd1qb9bg000000')]            => css=a[href*='9.0/t/pt/S.epage?sp=Uop67jrg0000imd1qb9bg000000']
     * //div[@id='BrowseMenu']//a[contains(@href, 'WorkTree.page')]                       => css=div[id='BrowseMenu'] a[href*='WorkTree.page']
     * //div[@id='AdminMenu']//a[contains(@href, 'DefinePermissions')]                    => css=div[id='AdminMenu'] a[href*='DefinePermissions']
     * //div[@class='xxx']//tag[@id='yyy']//*[@style='zzz']                               => css=div[class='xxx'] tag[id='yyy'] [style='zzz']
     * //div[@class='xxx X X.X']/a[@href="www.ru'.test'"]                                 => css=div[class='xxx X X.X'] > a[href="www.ru'.test'"]
     * //div//a                                                                           => css=div a
     * //div[@id='intro']//a[contains(@class, 'active')]/*[contains(@class,'test')]       => css=div[id='intro'] a[class*='active'] > [class*='test']
     * //*[@id='apple']//pear[contains(@class, 'lemon')]                                  => css=[id='apple'] pear[class*='lemon']
     * //ul[contains(@class, 'nav')]//li[1]//a                                            => css=ul[class*='nav'] li a
     * //body/div/p[3]                                                                    => css=body > div > p:nth-child(3)
     * //li/input[@type='text']                                                           => css=li > input[type='text']
     *
     * @param xpath           e.g. "//div[@id='dndArea'][1]//div[@class='www']"
     * @param depth           for xpath
     * @param doParseNthChild - if true then convert locators like //div[3]/div[5]. otherwise skippp
     * @return e.g. "css=div#dndArea div.www" or bull if locator is unparsable
     */
    public static String xpath2css(String str, int depth, boolean doParseNthChild) {
        if (str == null) return null;
        if (isCss(str)) return str;
        if (!isXpath(str)) return null;
        // skip sibling
        if (str.contains("following-sibling::")) return null;

        // remove last parent. '..part0/part1/parent::*' -> '..part0'
        String _str = str.replaceAll("(.*[^/])/[^/:]+/parent::(\\w+|\\*)$", "$1");
        // skipp other parents
        if (_str.contains("parent::")) return null;

        Pattern p1 = Pattern.compile("('[^']+')|(\"[^\"]+\")");
        Matcher m1 = p1.matcher(_str);
        Map<String, String> reps = new HashMap<String, String>();
        int index = 0;
        while (m1.find()) {
            String key = "=" + index++ + "=";
            String group = m1.group();
            String rep = group.contains("'") ? group : "'" + group.replace("\"", "") + "'";
            reps.put(key, rep);
            _str = _str.replace(group, key);
        }
        StringBuffer sb = new StringBuffer(CSS_LOCATOR_PATTERN);
        _str = _str.replace(XPATH_LOCATOR_PATTERN, "");

        //_str = _str.replace("[1]", "");
        if (_str.matches(".*\\]\\[\\d+\\].*"))
            return null; // do not convert locators like //div[@class='zzz'][2], but convert //div[2]
        if (_str.matches(".*\\[\\d+\\].*") && !doParseNthChild)
            return null; // temp skipp
        if (_str.contains("text()")) return null; // do not support this because equivalence is not ambiguous

        String[] parts = _str.split("/+");
        String[] spaces = _str.split("[^/]+");
        if (depth != -1 && parts.length > depth) return null;
        for (int i = 1; i < parts.length; i++) {
            String separator = i == parts.length - 1 ? "" : (spaces[i].equals("//") ? " " : " > ");
            String part = parts[i].
                    // spaces
                            replaceAll("\\s+", " ").
                    // and:
                            replaceAll("(?i)\\sand\\s", "][").
                    // or:
                            replaceAll("(?i)\\sor\\s", "],[").
                    // not :
                            replaceAll("(?i)\\[not\\(([^\\]]+)\\)\\]", ":not([$1])").
                    // text to contains text:
                            replaceAll("text\\(\\)\\s*=(=\\d+=)", "contains(text(),$1)").
                    // contains text
                            replaceAll("(?i)\\[contains\\(text\\(\\),\\s*([^\\)]+)\\)\\]", ":contains($1)").
                    // contains attribute:
                            replaceAll("(?i)contains\\(@([^,]+),\\s*([^\\)]+)\\)", "@$1*=$2").
                    // empty node:
                            replaceAll("\\*([^=])", "$1").
                    // any attribute:
                            replace("@", "").
                    // nth-child:
                            replaceAll("\\[(\\d+)\\]", ":nth-child($1)").

                    /* :last-child */
                            replace("[last()]", ":last-child");
            sb.append(part).append(separator);
        }
        String res = sb.toString();
        for (String key : reps.keySet()) {
            String rep = reps.get(key);
            res = res.replace(key, rep);
        }
        if (res.equals(str)) return null;
        return res;
    }

    public static boolean isXpath(String locator) {
        return locator.startsWith(XPATH_LOCATOR_PATTERN) || locator.startsWith("/") || locator.startsWith("./");
    }

    public static boolean isCss(String locator) {
        return locator.startsWith(CSS_LOCATOR_PATTERN);
    }

    public static boolean isLink(String locator) {
        return locator.startsWith(LINK_LOCATOR_PATTERN);
    }

    public static boolean isDom(String locator) {
        return locator.startsWith(DOM_LOCATOR_PATTERN);
    }

    public static boolean isFrame(String locator) {
        return isDom(locator) && locator.contains(DOM_LOCATOR_FRAMES);
    }


    public static boolean isSimpleLocator(String locator) {
        return !isXpath(locator) && !isCss(locator) && !isLink(locator) && !isDom(locator);
    }

    public static String xpath2css(String xpath) {
        return xpath2css(xpath, -1, true);
    }

    /**
     * convert simple css to xpath.
     * ignore css without prefix.
     * Example:
     * css=a[href*='9.0/t/pt/S.epage?sp=Uop67jrg0000imd1qb9bg000000'] => //a[contains(@href, '9.0/t/pt/S.epage?sp=Uop67jrg0000imd1qb9bg000000')]
     * css=div#BrowseMenu a[href*=WorkTree.page]                      => //div[@id='BrowseMenu']//a[contains(@href, 'WorkTree.page')]
     * css=div#AdminMenu a[href*=DefinePermissions]                   => //div[@id='AdminMenu']//a[contains(@href, 'DefinePermissions')]
     * css=div[class=xxx] tag[id=yyy] [style=zzz]                     => //div[@class='xxx']//tag[@id='yyy']//*[@style='zzz']
     * css=div[class='xxx X X.X']>a[href="www.ru'.test'"]             => //div[@class='xxx X X.X']/a[@href="www.ru'.test'"]
     * div a                                                          => //div//a
     * div#intro a.active > .test                                     => //div[@id='intro']//a[contains(@class, 'active')]/*[contains(@class,'test')]
     * #apple pear.lemon                                              => //*[@id='apple']//pear[contains(@class, 'lemon')]
     * ul.nav li:first-child a                                        => //ul[contains(@class, 'nav')]//li[1]//a
     * body > div > p:nth-child(3)                                    => //body/div/p[3]
     * li > input[type="text"]                                        => //li/input[@type='text']
     * css=div:contains('Problem detected')                           => //div[contains(text(),'Problem detected')]
     * css=[id='AddConfigurabeRole'],[name='AddConfigurabeRole'] textarea[id='description'] => //*[@id='AddConfigurabeRole' or @name='AddConfigurabeRole']//a[@id='description']
     *
     * @param str - css locator
     * @return xpath locator
     */
    public static String css2xpath(String str) {
        if (str == null) return null;
        if (isXpath(str)) return str;
        if (!isCss(str)) return null;
        String res = str;

        Map<String, String> reps = new HashMap<String, String>();
        int index = 0;
        for (String pattern : new String[]{
                "(?i)\\[\\w+\\**=([^\\]]+)\\]",
                "(?i)\\(('[^'\\)]+')\\)"}) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(res);
            while (m.find()) {
                String key = "=" + index++ + "=";
                String group = m.group(1);
                String rep = group.replaceFirst("^'", "").replaceFirst("^\"", "").replaceFirst("'$", "").replaceFirst("\"$", "");
                String quote = rep.contains("'") ? "\"" : "'";
                rep = quote + rep + quote;
                reps.put(key, rep);
                res = res.replace(group, key);
            }
        }


        res = res.
                /* Attribute selectors */
                        replaceAll("[\\s]\\[([^\\]]+)\\]", " *[@$1]").
                replaceAll("[=]\\[([^\\]]+)\\]", "=[@$1]").
                replaceAll("[\\s/]\\[([^\\]]+)\\]", " /*[@$1]").
                replaceAll("(?i)([a-z0-9,\\)\\]])\\[([^\\]]+)\\]", "$1[@$2]").
                /* additional selectors (comma seperated) */
                        replaceAll("\\s*,\\s*([^\\[])", "'|//'$1").
                /* :contains text */
                        replaceAll(":contains\\(([^\\)]+)\\)", "[contains(text(),$1)]").
                /* :nth-child(n) */
                        replaceAll(":nth-child\\((\\d+)\\)", "[$1]").
                /* :last-child */
                        replaceAll(":last-child", "[last()]").
                /* :first-child */
                        replaceAll(":first-child", "[1]").
                /* "sibling" selectors */
                        replaceAll("\\s*\\+\\s*([^\\s]+?)", "/following-sibling::$1[1]").
                /* "child" selectors */
                        replaceAll("\\s*>\\s*", "/").
                /* Remaining Spaces */
                        replaceAll("\\s", "//").
                /* #id */
                        replaceAll("(?i)([a-z0-9])#([a-z][-a-z0-9_]+)", "$1[@id='$2']").
                replaceAll("(?i)#([a-z][-a-z0-9_]+)", "*[@id='$1']").
                /* css *= to xptah contains.*/
                        replaceAll("(?i)([a-z0-9\\]])\\[(@[a-z0-9]+)\\*=([=\\d]+)\\]", "$1[contains($2, $3)]").
                replaceAll("(?i)\\[(@[a-z0-9]+)\\*=([=\\d]+)\\]", "*[contains($1, $2)]").
                /* .className */
                        replaceAll("(?i)([a-z0-9])\\.([a-z][-a-z0-9]+)", "$1[contains(@class, '$2')]").
                replaceAll("(?i)\\.([a-z][-a-z0-9]+)", "*[contains(@class,'$1')]").
                /* or */
                        replaceAll("\\]\\s*,\\s*\\[", " or ").
                /* and */
                        replaceAll("\\]\\[", " and ").
                replaceAll("^//", "");
        res = "//" + (" " + res).
                /* remove css prefix */
                        replaceAll("^ " + CSS_LOCATOR_PATTERN, "").
                /* All blocks of 2 or more spaces */
                        replaceAll("\\s{2,}", " ");
        for (String key : reps.keySet()) {
            res = res.replace(key, reps.get(key));
        }
        if (res.equals(str)) return null;
        return res.replace("/[", "/*[").replace("**", "*");
    }

    public static String css2dom(String locator) {
        if (locator == null) return null;
        return "window.dojo.query('" + locator.replace(CSS_LOCATOR_PATTERN, "") + "')[0]";
    }

    public static String xpath2simple(String locator) {
        for (String attr : new String[]{"name", "id"})
            if (locator.matches(".*@" + attr + "='[^']+'.*"))
                return locator.replaceAll(".*@" + attr + "='([^']+)'.*", "$1");
        return null;
    }

    public static String xpath2dom(String locator) {
        String css = xpath2css(locator);
        return css2dom(StrUtil.escapeQuotes(css));
    }

    public static String id2dom(String id) {
        return "window.dojo.byId('" + id + "')";
    }

    public static String id2xpath(String id) {
        return id2xpath(id, null);
    }

    public static String id2xpath(String id, String node) {
        return attribute2xpath("id", id.replace(ID_LOCATOR_PATTERN, ""), node);
    }

    public static String name2xpath(String name, String node) {
        return attribute2xpath("name", name.replace(NAME_LOCATOR_PATTERN, ""), node);
    }

    public static String name2xpath(String name) {
        return name2xpath(name, null);
    }

    public static String idOrName2xpath(String loc, String node) {
        if (node == null) node = "*";
        return "//" + node + "[@id='" + loc.replace(ID_LOCATOR_PATTERN, "") +
                "' or @name='" + loc.replace(NAME_LOCATOR_PATTERN, "") + "']";
    }

    public static String idOrName2xpath(String loc) {
        return idOrName2xpath(loc, null);
    }

    public static String any2link(String loc) {
        if (isLink(loc)) return loc;
        return LINK_LOCATOR_PATTERN + loc;
    }

    public static String attribute2xpath(String attr, String val, String node) {
        if (node == null) node = "*";
        char c1 = 34; // "
        char c2 = 39; // '
        char quote = val.contains("'") ? c1 : c2;
        return "//" + node + "[@" + attr + "=" + quote + val + quote + "]";
    }

    public static String link2xpath(String locator) {
        return link2xpath(locator, false);
    }

    public static String link2xpath(String locator, boolean checkSpace) {
        String txt = locator.replace(LINK_LOCATOR_PATTERN, "");
        if (checkSpace && txt.contains(" "))
            return "//a[contains(text(), '" + txt + "')]";
        return "//a[text()='" + txt + "']";
    }

    public static String dom2any(String loc) {
        String res = null;
        if (loc.startsWith(Locators.DOM_LOCATOR_DOCUMENT_PATTERN)) {
            if (loc.matches(".*document\\.getElementsByName\\(['\"].+['\"]\\)(\\[0\\])*;*$") ||
                    loc.matches(".*document\\.getElementById\\(['\"].+['\"]\\);*$")) {
                // to simple name or id:
                res = loc.replaceAll(".*\\(['\"](.+)['\"]\\).*", "$1");
            }
        }
        if ((loc.startsWith(Locators.DOM_LOCATOR_WINDOW_PATTERN) || loc.startsWith(Locators.DOM_LOCATOR_WINDOW)) &&
                loc.split("\\.").length == 3) {

            if (loc.matches(".*window\\.dojo\\.byId\\(['\"].+['\"]\\)(\\[0\\])*;*$") ||
                    loc.matches(".*window\\.document\\.getElementById\\(['\"].+['\"]\\)(\\[0\\])*;*$")) {
                // just get id:
                res = loc.replaceAll(".*\\(['\"](.+)['\"]\\).*", "$1");
            } else if (loc.matches(".*window\\.dojo\\.query\\(['\"].+['\"]\\)(\\[0\\])*;*$")) {
                // get inner css locator:
                res = loc.replaceAll(".*\\(['\"](.+)['\"]\\).*", "$1");
                if (!res.matches("#[^\\s]+")) // otherwise it is id
                    res = Locators.CSS_LOCATOR_PATTERN + res;
                else res = res.replaceAll("^#", "");
            }
            //todo: make handler for other locators
        }
        return res;

    }

    public static String frame2any(String loc) {
        if (!isFrame(loc)) return null;
        return loc.replaceAll(DOM_LOCATOR_PATTERN + DOM_LOCATOR_WINDOW + DOM_LOCATOR_FRAMES +
                "\\[([^]]+)\\]", "$1").replace("'", "").replace("\"", "");
    }

    public static String any2frame(String loc) {
        if (isFrame(loc)) return loc;
        if (!isSimpleLocator(loc)) return null;
        String suffix = loc.matches("\\d+") ? ("[" + loc + "]") : ("['" + loc + "']");
        return DOM_LOCATOR_PATTERN + DOM_LOCATOR_WINDOW + DOM_LOCATOR_FRAMES + suffix;
    }

}
