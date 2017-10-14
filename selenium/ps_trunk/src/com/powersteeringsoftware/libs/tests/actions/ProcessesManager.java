package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSProcess;
import com.powersteeringsoftware.libs.pages.Processes93Page;
import com.powersteeringsoftware.libs.pages.Processes94Page;
import com.powersteeringsoftware.libs.pages.ProcessesPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 19.12.12
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public class ProcessesManager {

    public static ProcessesPage open() {
        PSLogger.info("Open 'Processes.page' screen");
        ProcessesPage page = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? new Processes94Page() : new Processes93Page();
        page.open();
        return page;
    }

    public static void create(PSProcess process) {
        PSLogger.info("Create process " + process);
        ProcessesPage page = open();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4))
            create((Processes94Page) page, process);
        else
            create((Processes93Page) page, process);
        process.setCreated();

    }

    private static void create(Processes93Page page, PSProcess process) {
        Processes93Page.SecondFrame fr2 = page.addNew();
        fr2.setPhases(process.getPhasesList().size());
        fr2.setName(process.getName());
        fr2.setDescription(process.getDescription());

        for (int i = 0; i < process.getPhasesList().size(); ) {
            PSProcess.Phase phase = process.getPhasesList().get(i);
            fr2.setPhase(++i, phase.getName(), phase.getPercent());
        }

        Processes93Page.FirstFrame list = fr2.submit().backToList();
        list.check(process.getName());
        list.selectUp();
    }

    private static void create(Processes94Page page, PSProcess process) {
        List<String> res = page.getProcesses();
        PSLogger.info("All processes before: " + res);
        Processes94Page.ProcessDialog dialog = page.addNew();
        dialog.setName(process.getName());
        dialog.setDescription(process.getDescription());
        for (PSProcess.Phase p : process.getPhasesList()) {
            dialog.addPhase(p.getName(), p.getPercent());
        }
        dialog.submit();
        res = page.getProcesses();
        PSLogger.info("All processes after: " + res);
        Assert.assertTrue(res.contains(process.getName()), "Can't find process " + process.getName());
    }

}
