package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.List;

public class PSProcess extends ConfigObject {
    public static final String NAME = "process";

    public PSProcess(String name, String description) {
        this(Config.createConfig(NAME));
        if (name != null) {
            conf.setText("name", name);
        }
        if (description != null) {
            conf.setText("description", description);
        }
    }

    public PSProcess(Config conf) {
        super(conf);
    }

    public void addPhase(String phaseName, String phasePercent) {
        conf.addChild(new Phase(phaseName, phasePercent).phase);
    }

    public void addPhase(String phaseName) {
        conf.addChild(new Phase(phaseName, null).phase);
    }

    public List<Phase> getPhasesList() {
        List<Phase> res = new ArrayList<Phase>();
        for (Config c : conf.getChsByName(Phase.PHASE)) {
            res.add(new Phase(c));
        }
        return res;
    }

    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putProcess(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeProcess(this);
    }

    public class Phase {
        public static final String PHASE = "phase";
        private Config phase;

        protected Phase(String name, String percent) {
            this(Config.createConfig(PHASE));
            if (name != null) {
                phase.setText("name", name);
            }
            if (percent != null) {
                phase.setText("percent", percent);
            }
        }

        private Phase(Config c) {
            this.phase = c;
        }

        public Phase(String name) {
            this(name, null);
        }

        public String getName() {
            return phase.getText("name");
        }

        public String getPercent() {
            return phase.getText("percent");
        }

        @Override
        public String toString() {
            return getName() + (phase.hasChild("percent") ? ":" + getPercent() : "");
        }

    }

    @Override
    public String toString() {
        return getName() + getPhasesList();
    }

}
