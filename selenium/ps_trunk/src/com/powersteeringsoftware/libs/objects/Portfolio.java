package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.tests_data.Config;

/**
 * Created by admin on 09.05.2014.
 */
public class Portfolio extends ConfigObject {
    public static final String NAME = "portfolio";

    public Portfolio(Config c) {
        super(c);
    }

    public Portfolio(String name, String id) {
        super();
        setName(name);
        setId(id);
    }
}
