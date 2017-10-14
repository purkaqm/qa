package com.powersteeringsoftware.libs.objects.resources;

import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.SingleStartDateConfigObject;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by admin on 09.04.14.
 */
public class RateCode extends SingleStartDateConfigObject {
    public static final String DEFAULT_EFFECTIVE_SINCE_DATE = "01/01/2000";

    public static final String NAME = "rate-code";

    public RateCode(Config c) {
        super(c);
    }

    RateCode(String name, Double v, Currency c, String date) {
        super();
        if (name != null)
            setName(name);
        setAmount(v);
        setCurrency(c);
        if (date != null)
            setDate(date);
    }

    public String toString() {
        String res = "(amount=" + getAmount() + ",currency=" + getCurrency() + (getSDate() != null ? ",date=" + getSDate() : "") + ")";
        if (getName() != null) {
            return getName() + res;
        }
        return res;
    }

    public boolean isCustom() {
        return getName() == null;
    }

    public void setAmount(Double d) {
        set("amount", d);
    }

    public Double getAmount() {
        try {
            return Double.parseDouble(getString("amount"));
        } catch (NumberFormatException nfe) {
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    }

    public Currency getCurrency() {
        return super.getCurrency();
    }

    public void setCurrency(Currency c) {
        super.setCurrency(c);
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putRateCode(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeRateCode(this);
    }

    public static PSCalendar getDefaultDate() {
        PSCalendar s = PSCalendar.getEmptyCalendar().set(DEFAULT_EFFECTIVE_SINCE_DATE);
        s.toStart();
        return s;
    }

}
