package com.powersteeringsoftware.libs.objects.resources;

import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 09.04.14.
 */
public class RateTable extends ConfigObject {

    public static final String NAME = "rate-table";
    public static final long DEFAULT_SINCE_DATE = 946756800000L; //"01/01/2000";

    public RateTable(Config c) {
        super(c);
    }

    public RateTable(String name) {
        super();
        setName(name);
    }

    public boolean isFake() {
        return Boolean.parseBoolean(getConfig().getAttribute("fake"));
    }

    public boolean isDefault() {
        return Boolean.parseBoolean(getConfig().getAttribute("default"));
    }

    public void setDefault() {
        for (RateTable rt : TestSession.getRateTableList()) {
            rt.setNoDefault();
        }
        getConfig().setAttributeValue("default", Boolean.TRUE.toString());
    }

    private void setNoDefault() {
        getConfig().removeAttribute("default");
    }

    public String toString() {
        return getName();
    }

    public boolean equals(Object o) {
        if (!(o instanceof RateTable)) return false;
        return getName().equals(((RateTable) o).getName());
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putRateTable(this);
        if (!isFake() && TestSession.getRealRateTableList().size() == 1) {
            setDefault();
        }
    }

    @Override
    public boolean exist() {
        return isFake() || super.exist();
    }

    @Override
    public void setDeleted() {
        if (isFake()) return;
        super.setDeleted();
        TestSession.removeRateTable(this);
    }
    public List<Rate> getRates() {
        return getRates(false);
    }

    private List<Rate> getRates(boolean checkExist) {
        List<Rate> res = new ArrayList<Rate>();
        for (Config c : getConfig().getChsByName(Rate.NAME)) {
            Rate r = new Rate(c);
            if (checkExist && !r.exist()) continue;
            res.add(r);
        }
        return res;
    }

    public void addRate(Rate r) {
        if (isFake()) return;
        getConfig().addChild(r.getConfig());
    }

    public void addRate(Role role, PSTag activity, String date, Currency c, Double amount) {
        if (isFake()) return;
        addRate(new Rate(role, activity, date, c, amount));
    }

    public void addRate(Role role, PSTag activity, String date, String code) {
        if (isFake()) return;
        addRate(new Rate(role, activity, date, code));
    }

    public class Rate extends SingleStartDateConfigObject {
        public static final String NAME = "rate";

        private Rate(Config c) {
            super(c);
        }

        private Rate(Role role, PSTag activity, String date) {
            super();
            setRole(role);
            if (activity != null)
                setActivity(activity);
            if (date != null) {
                setDate(date);
            }
        }

        private Rate(Role role, PSTag activity, String date, Currency c, Double amount) {
            this(role, activity, date);
            RateCode r = new RateCode(null, amount, c, null);
            setCode(r);
        }

        private Rate(Role role, PSTag activity, String date, String code) {
            this(role, activity, date);
            RateCode r = null;
            for (RateCode _r : TestSession.getRateCodeList()) {
                if (_r.getName().equals(code)) {
                    r = _r;
                    break;
                }
            }
            if (r == null) throw new IllegalArgumentException("Can't find rate code '" + code + "'");
            setCode(r);
        }

        public Long getDate() {
            return hasDate() ? super.getDate() : DEFAULT_SINCE_DATE;
        }

        public String toString() {
            return "role=" + getRole().toString() + "," +
                    (hasActivity() ? "activity=" + getActivity().getName() + "," : "") +
                    (getSDate() != null ? "date=" + getSDate() + "," : "") +
                    "code=" + getCode().toString();
        }

        public Role getRole() {
            return BuiltInRole.getRoleByName(getConfig().getAttribute("role"));
        }

        public void setRole(Role role) {
            getConfig().setAttributeValue("role", role.getName());
        }

        private PSTag activity;

        public PSTag getActivity() {
            if (activity != null) return activity;
            return activity = TestSession.getActivity().getChild(getConfig().getAttribute("activity"));
        }

        public boolean hasActivity() {
            return activity != null || getConfig().hasAttribute("activity");
        }

        public void setActivity(PSTag t) {
            if (TestSession.getActivity().getChild(t.getName()) == null) {
                throw new IllegalArgumentException("incorrect activity specified '" + t + "'");
            }
            getConfig().setAttributeValue("activity", t.getName());
            activity = t;
        }

        private RateCode code;

        public RateCode getCode() {
            if (code != null) return code;
            String name = getConfig().getText(RateCode.NAME);
            if (!name.isEmpty()) {
                for (RateCode rc : TestSession.getRateCodeList()) {
                    if (name.equals(rc.getName())) {
                        return code = rc;
                    }
                }
                Assert.fail("Can't find rate code " + name);
            }
            return code = new RateCode(getConfig().getChByName(RateCode.NAME));
        }

        public void setCode(RateCode rc) {
            getConfig().addChild(rc.getConfig());
            code = rc;
        }

        public boolean isCustom() {
            return getCode().isCustom();
        }

        public boolean isDefault() {
            return exist() && getBooleanFalse("default");
        }

        public void setDefault() {
            set("default", Boolean.TRUE);
            for (Rate r : RateTable.this.getRates(true)) {
                r.removeConfigs("default");
            }
        }

        public void setCreated() {
            if (RateTable.this.getRates(true).isEmpty()) {
                setDefault();
            }
            super.setCreated();
        }
    }
}
