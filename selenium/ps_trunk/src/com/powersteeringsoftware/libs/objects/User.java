package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 28.06.2010
 * Time: 14:52:10
 */
public class User extends ConfigObject implements TagsObject, Comparable {
    private static final String DEFAULT_TIMEZONE = "GMT";
    public static final String NAME = "user";

    public User(String login, String password, String fullName) {
        this(login, password);
        setFullName(fullName);
    }

    public User(String login, String password, String fullName, String id) {
        this(login, password, fullName);
        setId(id);
    }

    public User(String login, String password, String firstName, String lastName, String email) {
        this(login, password);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public User(String login, String password) {
        this();
        setLogin(login);
        setPassword(password);
    }

    public User(Config c) {
        super(c);
    }

    public User() {
        super(Config.createConfig(NAME));
    }

    public User(String firstName) {
        this();
        setFirstName(firstName);
    }

    public String getName() {
        return getLogin();
    }

    public String getFirstName() {
        return getConfig().getText("first-name");
    }

    public String getLastName() {
        return getConfig().getText("second-name");
    }

    public String getFullName() {
        if (getFirstName() == null) return null;
        if (getLastName() == null) return getFirstName();
        return getFirstName() + " " + getLastName();
    }

    public boolean hasFullName() {
        return getFirstName() != null && getLastName() != null;
    }

    public String getFormatFullName() {
        return getLastName() + ", " + getFirstName();
    }

    public String getEmail() {
        return getConfig().getText("email");
    }

    public void setFirstName(String name) {
        getConfig().setText("first-name", name);
    }

    public void setLastName(String name) {
        getConfig().setText("second-name", name);
    }

    public void setName(String n) {
        setLogin(n);
    }

    public void setFullName(String fullName) {
        if (fullName == null) return;
        setFirstName(fullName.contains(" ") ? fullName.split("\\s+")[0] : fullName);
        setLastName(fullName.contains(" ") ? fullName.split("\\s+")[1] : null);
    }

    public void setFormatFullName(String fullName) {
        if (fullName == null) return;
        if (!fullName.contains(",")) throw new IllegalArgumentException("Incorrect full formatted name specified");
        String last = fullName.split(",\\s+")[0];
        String first = fullName.split(",\\s+")[1];
        setFirstName(first);
        setLastName(last);
    }

    public boolean isEditWorkEnabled() {
        return isAdmin();
    }

    public boolean isAdmin() {
        return getBooleanFalse("is_admin");
    }

    public void setIsAdmin(boolean b) {
        set("is_admin", b);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getLogin() != null) {
            sb.append(getLogin()).append(":").append(getPassword() != null ? getPassword() : "");
        }
        if (getId() != null) {
            sb.append("[").append(getId()).append("]");
        } else {
            if (getFullName() != null) {
                sb.append("(").append(getFullName()).append(")");
            }
            if (getEmail() != null) {
                sb.append("(").append(getEmail()).append(")");
            }
        }
        return sb.toString();
    }

    public void setLogin(String login) {
        getConfig().setText("login", login);
    }

    public void setPassword(String password) {
        getConfig().setText("password", password);
    }

    public void setEmail(String email) {
        getConfig().setText("email", email);
    }

    public String getLogin() {
        return getConfig().getText("login");
    }

    public String getPassword() {
        return getConfig().getText("password");
    }

    public Map<Role, Float> getRolesCapacity() {
        Map<Role, Float> res = new HashMap<Role, Float>();
        for (Config cRole : getConfig().getChsByName("role")) {
            Role role = BuiltInRole.getRoleByName(cRole.getText());
            float capacity;
            String attr = cRole.getAttribute("capacity");
            if (!attr.isEmpty())
                capacity = Float.valueOf(attr);
            else
                capacity = 0;
            res.put(role, capacity);
        }
        return res;
    }

    public float getRoleCapacity(Role role) {
        Map<Role, Float> res = getRolesCapacity();
        if (!res.containsKey(role)) return 0;
        return res.get(role);
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        // if id or login equals then it is the same user:
        if (getId() != null && getId().equals(u.getId())) return true;
        if (getLogin() != null && getLogin().equals(u.getLogin())) return true;

        // otherwise if (full name equals:
        if (getFirstName() != null && getLastName() != null) {
            if (getFirstName().equals(u.getFirstName()) && getLastName().equals(u.getLastName()))
                return true;
        }
        return false;
    }

    public int hashCode() {
        if (getId() != null) return getId().hashCode();
        return super.hashCode();
    }

    public PSCalendar getCalendar() {
        return super.getCalendar();
    }

    public void setCalendar(PSCalendar calendar) {
        removeCalendars();
        conf.addChild(calendar.getConfig());
    }

    public void addDaysOff(PSCalendar start, PSCalendar end) {
        Config range = conf.addElement("non-work-days");
        range.setAttributeValue("start", start.toString());
        range.setAttributeValue("end", end.toString());
    }

    public List<PSCalendar[]> getDaysOffs() {
        List<PSCalendar[]> ranges = new ArrayList<PSCalendar[]>();
        for (Config range : conf.getChsByName("non-work-days")) {
            PSCalendar start = PSCalendar.getEmptyCalendar().set(range.getAttribute("start"));
            PSCalendar end = PSCalendar.getEmptyCalendar().set(range.getAttribute("end"));
            ranges.add(new PSCalendar[]{start, end});
        }
        return ranges;
    }

    public TimeZone getTimeZone() {
        String zone = conf.getText("time-zone");
        if (zone == null) zone = DEFAULT_TIMEZONE;
        return TimeZone.getTimeZone(zone);
    }

    public void setTimeZone(String zone) {
        conf.setText("time-zone", zone);
    }

    public void removeDaysOff() {
        while (conf.getChByName("non-work-days") != null) {
            conf.removeChild("non-work-days");
        }
    }

    private void removeCalendars() {
        while (conf.getChByName(PSCalendar.NAME) != null) {
            conf.removeChild(PSCalendar.NAME);
        }
    }

    public synchronized Object clone() {
        try {
            return new User(conf.copy(true));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCreated() {
        super.setCreated();
        TestSession.putUser(this);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof User)) return -1;
        User u = (User) o;
        if (hasFullName() && u.hasFullName()) {
            return getFormatFullName().compareTo(u.getFormatFullName());
        }
        if (getLogin() != null && u.getLogin() != null) {
            return getLogin().compareTo(getLogin());
        }
        return 0;
    }

    public List<PSTag> getTags() {
        return super.getTags();
    }

    public void setTags(PSTag... tags) {
        super.setTags(tags);
    }

    public void addTag(PSTag tg) {
        super.addTag(tg);
    }

    public void setTags(List<PSTag> tags) {
        super.setTags(tags);
    }

    public static User getUserByFullFormattedName(String user) {
        for (User u : TestSession.getAllUsers()) {
            if (u.getFormatFullName().equalsIgnoreCase(user)) {
                return u;
            }
        }
        return null;
    }

    public static User getUserByFullName(String user) {
        for (User u : TestSession.getAllUsers()) {
            if (u.getFullName().equalsIgnoreCase(user)) {
                return u;
            }
        }
        return null;
    }
}
