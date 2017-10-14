package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Frame;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.AgentsPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.AgentsPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.10.11
 * Time: 17:10
 */
public class AgentsPage extends PSPage {
    @Override
    public void open() {
        clickAdminAgents();
        getFrame(FRAME).select();
    }

    public class AgentFrame extends Frame {
        private AgentFrame() {
            super(FRAME);
        }

        public AgentFrame run() {
            Link n = new Link(RUN_AGENT);
            n.setFrame(this);
            n.click(false);
            waitForReload(AgentsPage.this);
            PSLogger.save("After run");
            return this;
        }
    }

    public AgentFrame openAgent(String name) {
        Frame frame = getFrame();
        Link link = new Link(AGENT_LINK.replace(name));
        link.setFrame(frame);
        link.waitForVisible();
        link.click(false);
        frame.waitForReload(this);
        return new AgentFrame();
    }

    public AgentFrame openAgent(ILocatorable loc) {
        return openAgent(loc.getLocator());
    }

    public void reindexBasicSearch() {
        runAgent(REINDEX_BASIC_SEARCH);
    }

    public void timeConversion() {
        runAgent(TIME_CONVERSION_AGENT);
    }

    public void reindexDocuments() {
        runAgent(DOCUMENT_SEARCH_REINDEX);
    }

    private void runAgent(AgentsPageLocators loc) {
        PSLogger.info("Run agent '" + loc.getLocator() + "'");
        AgentFrame fr = openAgent(loc);
        fr.run();
        fr.selectUp();
    }


}
