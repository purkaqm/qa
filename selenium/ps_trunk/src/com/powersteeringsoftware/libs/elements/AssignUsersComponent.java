package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.elements_locators.AssignUserComponentLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.10.13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class AssignUsersComponent extends Element {
    public static final long TIMEOUT_FOR_DND = 30000; //ms
    private PSPage page;
    private ILocatorable suffix;

    public AssignUsersComponent(PSPage page, ILocatorable suffix) {
        this.page = page;
        this.suffix = suffix;
    }

    public Map<String, Element> searchPeople(String people) {
        Map<String, Element> res = new HashMap<String, Element>();
        PSLogger.info("Search people by '" + people + "'");
        new Input(SEARCH_PEOPLE_INPUT.replace(suffix)).type(people);
        new Button(SEARCH_PEOPLE_GO.replace(suffix)).click(false);
        Element result = new Element(SEARCH_PEOPLE_RESULT);
        result.waitForClassChanged(SEARCH_PEOPLE_WAIT);
        if (result.getElementClass().equals(SEARCH_PEOPLE_ERROR.toString())) {
            PSLogger.warn("Nobody found");
            PSLogger.save();
            return res;
        }
        for (Element e : page.getElements(SEARCH_PEOPLE_RESULT)) {
            String link = e.getChildByXpath(SEARCH_PEOPLE_RESULT_LINK).getDEText();
            res.put(link, e);
        }
        return res;
    }

    private static List<String> dragAndDrop(PSPage page, String role, Element from) {
        try {
            return linksToString(_dragAndDrop(page, role, from));
        } catch (Wait.WaitTimedOutException ww) { // its hotfix for chrome 12 !
            PSLogger.warn("CreateWorkPage.dragAndDrop: " + ww.getMessage());
            PSLogger.save();
            return linksToString(_dragAndDrop(page, role, from));
        }
    }

    private static List<String> linksToString(List<Link> links) {
        List<String> res = new ArrayList<String>();
        for (Link l : links)
            res.add(l.getDEText());
        return res;
    }

    private static List<Link> _dragAndDrop(PSPage page, String role, Element from) {
        PSLogger.debug("Start d'n'd on " + page.getName() + " for role " + role);
        Element to = new Element(ROLES_DND_PREFIX.append(role));
        to.waitForVisible();
        from.waitForVisible();
        from.mouseOver();
        from.waitForClass(SEARCH_PEOPLE_RESULT_MOUSE_OVER, TIMEOUT_FOR_DND);
        from.mouseDownAt();
        from.mouseMoveAt(to);
        to.mouseOver();
        to.waitForClass(ROLES_COLOR_PINK, TIMEOUT_FOR_DND);
        to.mouseUp();
        to.mouseOut();
        to.waitForClass(ROLES_COLOR_GREEN, TIMEOUT_FOR_DND);
        Link team = new Link(TEAM_LINK.replace(role));
        team.waitForVisible(TIMEOUT_FOR_DND);

        List<Link> res = new ArrayList<Link>();
        for (Element e : page.getElements(true, TEAM_LINK.replace(role))) {
            res.add(new Link(e));
        }
        PSLogger.debug("Finish d'n'd on " + page.getName() + " for role " + role);
        return res;
    }

    public void doAssign(User user, Role role) {
        String roleLoc = null;
        if (role.equals(BuiltInRole.OWNER)) {
            //if (BasicCommons.getCurrentUser().equals(user)) {
            //    PSLogger.info("Skip assigned owner " + user + ". Should be already assigned");
            //    return;
            //}
            roleLoc = ROLES_OWNER.getLocator();
        } else if (role.equals(BuiltInRole.CONTRIBUTOR)) {
            roleLoc = ROLES_CONTRIBUTOR.getLocator();
        } else if (role.equals(BuiltInRole.CHAMPION)) {
            roleLoc = ROLES_CHAMPION.getLocator();
        } else if (role.equals(BuiltInRole.FINANCIAL_REP)) {
            roleLoc = ROLES_FINREP.getLocator();
        } else { // configurable:
            for (Element e : page.getElements(false, ROLES_)) {
                String txt = e.getDEText();
                if (txt.equalsIgnoreCase(role.getName())) {
                    String id = e.getParent().getDEId();
                    if (id != null && !id.isEmpty()) {
                        roleLoc = id.replace(ROLES_DND_PREFIX.getLocator(), "");
                        break;
                    }
                }
            }
        }
        Assert.assertNotNull(roleLoc, "Can't find role " + role);
        PSLogger.info("Assign user " + user + " to role " + role);
        assignRoleToUser(user.getFormatFullName(), roleLoc);
    }

    private void assignRoleToUser(String toSearch, String role) {
        Map<String, Element> res = searchPeople(toSearch);
        Element user = null;
        String people = null;
        for (String found : res.keySet()) {
            if (found.toLowerCase().startsWith(toSearch.toLowerCase())) {
                user = res.get(people = found);
                break;
            }
        }
        Assert.assertNotNull(user, "Can't find user '" + toSearch + "'");
        PSLogger.info("Try to assign " + role + " to user " + people);
        List<String> fromPage = dragAndDrop(page, role, user);
        PSLogger.info("After dnd " + role + ":" + fromPage);
        PSLogger.save("After assign role to user " + people);
        for (String s : fromPage) {
            if (s.toLowerCase().contains(toSearch.toLowerCase())) return;
        }
        Assert.fail("Can't find user after assign to role " + role + " : " + fromPage);
    }

    public void makeOwner(String people) {
        PSLogger.info("Make '" + people + "' owner");
        assignRoleToUser(people, ROLES_OWNER.getLocator());
    }

    public void makeChampion(String people) {
        PSLogger.info("Make '" + people + "' champion");
        assignRoleToUser(people, ROLES_CHAMPION.getLocator());
    }

    public void makeContributor(String people) {
        PSLogger.info("Make '" + people + "' contributor");
        assignRoleToUser(people, ROLES_CONTRIBUTOR.getLocator());
    }


    @Deprecated
    public void oldMakeChampion(String people) {
        if (people == null) return;
        PSLogger.info("Make '" + people + "' champion");
        String championString = oldGetChampionOnCreatingString(people);
        String jsQuery = OLD_SEARCH_PEOPLE_SET_VALUE.replace(championString);
        getDriver().getEval(jsQuery);
    }

    @Deprecated
    private String oldGetChampionOnCreatingString(String toSearch) {
        //Only add the first champion for now
        new Input(OLD_SEARCH_PEOPLE_SEARCH).type(toSearch);
        new Button(OLD_SEARCH_PEOPLE_SUBMIT).click(false);

        new Element(OLD_SEARCH_PEOPLE_LOADING).waitForDisapeared();

        Element item = new Element(OLD_SEARCH_PEOPLE_ITEM);
        item.setDefaultElement(page.getDocument());

        String attr = item.getAttribute(OLD_SEARCH_PEOPLE_ITEM_ATTR);
        String eval = item.asXML(); //SeleniumDriverFactory.getDriver().getEval("window.dojo.query('div.col1 li.dojoDndItem')[0].innerHTML");
        String championsString = "user|||" + attr + "|||true|||" + eval + "||||||";
        //Delete, delete the magical quotes!
        championsString = championsString.replaceAll("\"", "").replaceAll("'", "");
        return championsString + "user";
    }


    public void removeAll(User... excludes) {
        List<String> list = new ArrayList<String>();
        for (User u : excludes) {
            if (u != null)
                list.add(u.getFormatFullName());
        }
        for (Element e : page.getElements(false, REMOVE)) {
            Element li = e.getParent(REMOVE_LI);
            for (Element a : Element.searchElementsByXpath(li, REMOVE_LINK)) {
                String txt = a.getDEText();
                if (txt != null && !txt.isEmpty()) {
                    if (list.contains(txt)) {
                        PSLogger.info("Do not delete user '" + txt + "'");
                        break;
                    }
                    PSLogger.info("Remove user '" + txt + "'");
                    e.click(false);
                    a.waitForDisapeared();
                    break;
                }
            }
        }
    }

    public Map<String, List<String>> getUsers() {
        Map<String, List<String>> res = new HashMap<String, List<String>>();
        Element parent = page.getElement(false, TEAM_ALL);
        List<Element> list = Element.searchElementsByXpath(parent, TEAM_MEM);
        for (int i = 0; i < list.size(); i++) {
            Element strong = list.get(i).getChildByXpath(TEAM_ROLE);
            if (strong.isDEPresent()) {
                String role = strong.getDEText();
                List<String> users = new ArrayList<String>();
                for (i++; i < list.size(); i++) {
                    Element li = list.get(i);
                    if (li.getDEClass() == null || !li.getDEClass().contains(TEAM_USER_CLASS.getLocator())) {
                        i--;
                        break;
                    }
                    Element a = li.getChildByXpath(TEAM_USER);
                    users.add(a.getDEText());
                }
                res.put(role, users);
            }
        }
        PSLogger.debug("All users: " + res);
        return res;
    }

}
