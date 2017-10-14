package com.cinteractive.ps3.test;

import junit.framework.TestCase;

import com.cinteractive.ps3.PSHome;
import com.cinteractive.ps3.contexts.ContextAware;
import com.cinteractive.ps3.contexts.InstallationContext;


public abstract class PSTestBase extends TestCase implements ContextAware {

    public static final String CONTEXT_NAME_PROP = "com.cinteractive.ps3.contexts.InstallationContext.name";

    private String _contextName = null;

    public PSTestBase(String testName) {
        super(testName);
    }

    @Override
    public InstallationContext getContext() {
        return InstallationContext.get(_contextName);
    }

    @Override
    protected void setUp() {
        _contextName = System.getProperty(CONTEXT_NAME_PROP);
        if (_contextName == null) {
            _contextName = "default";
        }
        String path = "web";
        try {
            PSHome.getHome();
        } catch (Exception e) {
            PSHome home = new PSHome(path);
            try {
                home.init();
                PSHome.setInstance(home, false);
            } catch (PSHome.InitializationException ex) {
                throw new RuntimeException("PSObjectTestBase::setUp()>>home.init() failed " + ex.getMessage());
            }
        }
        getContext();
    }

    @Override
    protected void tearDown() {
        _contextName = null;
    }
}