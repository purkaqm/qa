package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by admin on 09.04.14.
 */
public class Currency extends ConfigObject {
    public static final String NAME = "currency";

    public Currency(Config c) {
        super(c);
    }

    public Currency(String name) {
        super();
        setName(name);
    }

    public boolean equals(Object o) {
        return o instanceof Currency && getName().equals(((Currency) o).getName());
    }

    public void setCreated() {
        TestSession.putCurrency(this);
    }

    public boolean exist() {
        return true;
    }

    public void setDeleted() {

    }

    public boolean isDefault() {
        return Boolean.parseBoolean(getConfig().getAttribute("default"));
    }

    public String toString() {
        return getName();
    }

    public static Currency getCurrencyByName(String name) {
        for (Currency c : TestSession.getCurrencyList()) {
            if (c.getName().equals(name)) return c;
        }
        return null;
    }

    public static Currency getDefaultCurrency() {
        for (Currency c : TestSession.getCurrencyList()) {
            if (c.isDefault()) return c;
        }
        return null;
    }

    public Locale getLocale() {
        Config loc = getConfig().getChByName("locale");
        if (loc == null) return null;
        if (loc.hasChild("variant")) {
            return new Locale(loc.getText("language"), loc.getText("country"), loc.getText("variant"));
        }
        return new Locale(loc.getText("language"), loc.getText("country"));
    }

    public NumberFormat getCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(getLocale());
    }

    public NumberFormat getNumberFormat() {
        return NumberFormat.getInstance(getLocale());
    }

    public Double getExchangeRate() {
        if (isDefault() || !getConfig().hasChild("exchange-rate")) return 1d;
        return Double.parseDouble(getConfig().getText("exchange-rate"));
    }


}
