package ps5.wbs;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.AbstractMessages;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps5.components.project.create.RequiredInfo;
import ps5.psapi.project.GateBean;
import ps5.psapi.project.create.CreateWorkWizard;
import ps5.psapi.project.create.WorkTypeBean;
import ps5.services.state.Visit;
import ps5.support.PSDispModels.PSDispModel;
import ps5.support.PSDispModels.PSDispModel_Base;
import ps5.wbs.beans.CheckpointBean;
import ps5.wbs.beans.GPBean;
import ps5.wbs.logic.ProjectEditorModel;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.servlets.UserInputErrors;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.WorkUtil;
import com.cinteractive.scheduler.Duration;

/**
 * At the moment only tollgate's and gates' planned and system dates are tested, nothing else.<br />
 * Tollgate template with at least two phases should be created in the db manually. Its id should be assigned to the
 * {@link #templateId}.<br />
 * Expected dates (like {@link #gate1ExpectedStartDate}) should be set in the following format: MM/dd/yyyy<br />
 * User's hollidays are not processed, so you need to make sure that dates you set don't touch user's hollidays.<br />
 * According to the <a
 * href="https://internal.psteering.com/confluence/display/FeatRef/Enforced+Sequential+Gates">specification</a> the
 * following rules should be applied to the ESG dates:
 * <ul>
 * <li>tollgate's and gates' system and planned dates should be the same</li>
 * <li>tollgate's start date should be the same as first gate start date</li>
 * <li>tollgate's end date should be the same as last gate end date</li>
 * <li>tollgate's duration should be calculated as sum of all gates' durations</li>
 * <li>gate's start date should be set to the next day after the previous date's end date</li>
 * </ul>
 * 
 * @author Valentin Fedoskin <valentin.fedoskin@simbirsoft.com>
 */
@SuppressWarnings({ "synthetic-access", "nls", "serial", "javadoc" })
public class CreateEsgViaWorkWizardTest extends WBSBaseTest {

    private class CreateWorkWizardInternal extends CreateWorkWizard {

        public CreateWorkWizardInternal() {
            super(null, CreateWorkWizard.CODE + "." + Uuid.create().toString(), messages, dispModel);
        }

        @Override
        public Visit getVisit() {
            return WBSBaseTest.getVisit();
        }
    }

    private static class MessagesInternal extends AbstractMessages {

        @Override
        protected String findMessage(String arg0) {
            return arg0;
        }

        @Override
        protected Locale getLocale() {
            return Locale.getDefault();
        }
    }

    private class PSDispModelInternal extends PSDispModel_Base {

        public PSDispModelInternal() {
            super(WBSBaseTest.getUser(), "(insufficient permission)", "CREATEWORK");
        }

        @Override
        public String getHTML(Object x, boolean canSelect, boolean isSearch) {
            return x.toString();
        }
    }

    private static class UserInputErrorsInternal implements UserInputErrors {

        @Override
        public Object getRequest() {
            return null;
        }

        @Override
        public User getUser() {
            return WBSBaseTest.getUser();
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public void markError(String error, String fieldName) {
        }

        @Override
        public void markError(String messageKey, String error, String fieldName) {
        }

        @Override
        public void reportError(String messageKey, String fieldName) {
        }

        @Override
        public void reportError(String messageKey, String fieldName, Object[] messageParams) {
        }

        @Override
        public void setConfirmation(String msg) {
        }

        @Override
        public void setError(String error) {
        }

        @Override
        public void setErrorField(String field) {
        }
    }

    private static final String gate1ExpectedEndDate = "07/17/2012";

    private static final String gate1ExpectedStartDate = "07/16/2012";

    private static final String gate2ExpectedEndDate = "07/19/2012";

    private static final String gate2ExpectedStartDate = "07/18/2012";

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateEsgViaWorkWizardTest.class);

    private static final PersistentKey templateId = Uuid.get("o2k35g00000jh56l8qc0000000");

    private static final String tollgateExpectedEndDate = gate2ExpectedEndDate;

    private static final String tollgateExpectedStartDate = gate1ExpectedStartDate;

    private static Date convertStringToDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH");
        try {
            // hack for the DateCalculator, since it sets all dates to the midday (12:00)
            return df.parse(str.concat(" 12"));
        } catch (ParseException e) {
            LOGGER.error("Unable to convert string to Date instance", e);
            throw new RuntimeException(e);
        }
    }

    private static Timestamp convertStringToTimestamp(String str) {
        return new Timestamp(convertStringToDate(str).getTime());
    }

    private static Duration getDuration(String startStr, String endStr) {
        Date startDate = convertStringToDate(startStr);
        Date endDate = convertStringToDate(endStr);
        // in our case duration should be increased by one, eg. duration between 10 and 11 days is 2.
        float days = Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays() + 1;
        return Duration.getInstance(days, "d");
    }

    private static float getDurationInDays(String startStr, String endStr) {
        Duration duration = getDuration(startStr, endStr);
        return duration.getAmount();
    }

    private PSDispModel dispModel;

    private Messages messages;

    private PersistentKey tollgateId;

    @Before
    public void createTollgate() {
        messages = new MessagesInternal();
        dispModel = new PSDispModelInternal();
        UserInputErrors errors = new UserInputErrorsInternal();
        CreateWorkWizard wizard = new CreateWorkWizardInternal();
        WorkTypeBean type = new WorkTypeBean(getContext(), Tollgate.TYPE, templateId);
        wizard.setType(type);
        RequiredInfo requiredInfo = (RequiredInfo) wizard.getSteps().get(1);
        requiredInfo.setName("esg-tollgate-" + UUID.randomUUID());
        requiredInfo.setObjective(null);
        requiredInfo.setParent(null);
        requiredInfo.init();
        wizard.create(errors);
        wizard.setCurrent("chooseWorkType");
        wizard.next();
        GateBean g1 = wizard.getGates().get(0);
        g1.setPlannedStart(convertStringToDate(gate1ExpectedStartDate));
        g1.setPlannedEnd(convertStringToDate(gate1ExpectedEndDate));
        g1.setPlannedDuration(getDurationInDays(gate1ExpectedStartDate, gate1ExpectedEndDate));
        GateBean g2 = wizard.getGates().get(1);
        g2.setPlannedStart(convertStringToDate(gate2ExpectedStartDate));
        g2.setPlannedEnd(convertStringToDate(gate2ExpectedEndDate));
        g2.setPlannedDuration(getDurationInDays(gate2ExpectedStartDate, gate2ExpectedStartDate));
        requiredInfo.submit(errors);
        wizard.finish(getUser(), errors, null);
        tollgateId = wizard.getProject().getId();
        LOGGER.info(wizard.getProject().getPSType() + " created: " + wizard.getProject().getId());
        runScheduler();
    }

    @After
    public void deleteTollgate() {
        PSObject tollgate = null;
        try {
            tollgate = PSObject.get(tollgateId, getContext());
        } catch (Exception e) {
            // do nothing
        }
        if (tollgate != null) {
            WBSTestUtils.deletePSO(tollgate.getName(), getUser(), tollgate.getPSType());
            LOGGER.info(tollgate.getPSType() + " deleted: " + tollgate.getId());
            tollgate = null;
        }
    }

    @Test
    public void gate1PlannedDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Checkpoint gate = tollgate.getCheckpoint(0);
        Timestamp actualStart = gate.getSchedules().getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate1ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSchedules().getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate1ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSchedules().getPlannedLaborTime();
        Duration expectedDuration = getDuration(gate1ExpectedStartDate, gate1ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void gate1SystemDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Checkpoint gate = tollgate.getCheckpoint(0);
        Timestamp actualStart = gate.getSchedules().getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate1ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSchedules().getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate1ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSchedules().getSystemLaborTime();
        Duration expectedDuration = getDuration(gate1ExpectedStartDate, gate1ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void gate2PlannedDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Checkpoint gate = tollgate.getCheckpoint(1);
        Timestamp actualStart = gate.getSchedules().getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate2ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSchedules().getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate2ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSchedules().getPlannedLaborTime();
        Duration expectedDuration = getDuration(gate2ExpectedStartDate, gate2ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void gate2SystemDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Checkpoint gate = tollgate.getCheckpoint(1);
        Timestamp actualStart = gate.getSchedules().getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate2ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSchedules().getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate2ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSchedules().getSystemLaborTime();
        Duration expectedDuration = getDuration(gate2ExpectedStartDate, gate2ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void tollgatePlannedDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Timestamp actualStart = tollgate.getSchedules().getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(tollgateExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = tollgate.getSchedules().getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(tollgateExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = tollgate.getSchedules().getPlannedLaborTime();
        Duration expectedDuration = getDuration(tollgateExpectedStartDate, tollgateExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void tollgateSystemDates() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Timestamp actualStart = tollgate.getSchedules().getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(tollgateExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = tollgate.getSchedules().getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(tollgateExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = tollgate.getSchedules().getSystemLaborTime();
        Duration expectedDuration = getDuration(tollgateExpectedStartDate, tollgateExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsGate1PlannedDates() {
        GPBean tollgate = createBeans();
        CheckpointBean gate = (CheckpointBean) tollgate.getChild(0);
        Timestamp actualStart = gate.getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate1ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate1ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getPlannedLaborTime();
        Duration expectedDuration = getDuration(gate1ExpectedStartDate, gate1ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsGate1SystemDates() {
        GPBean tollgate = createBeans();
        CheckpointBean gate = (CheckpointBean) tollgate.getChild(0);
        Timestamp actualStart = gate.getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate1ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate1ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSystemLaborTime();
        Duration expectedDuration = getDuration(gate1ExpectedStartDate, gate1ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsGate2PlannedDates() {
        GPBean tollgate = createBeans();
        CheckpointBean gate = (CheckpointBean) tollgate.getChild(1);
        Timestamp actualStart = gate.getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate2ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate2ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getPlannedLaborTime();
        Duration expectedDuration = getDuration(gate2ExpectedStartDate, gate2ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsGate2SystemDates() {
        GPBean tollgate = createBeans();
        CheckpointBean gate = (CheckpointBean) tollgate.getChild(1);
        Timestamp actualStart = gate.getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(gate2ExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = gate.getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(gate2ExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = gate.getSystemLaborTime();
        Duration expectedDuration = getDuration(gate2ExpectedStartDate, gate2ExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsTollgatePlannedDates() {
        GPBean tollgate = createBeans();
        Timestamp actualStart = tollgate.getPlannedStartDate();
        Timestamp expectedStart = convertStringToTimestamp(tollgateExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = tollgate.getPlannedEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(tollgateExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = tollgate.getPlannedLaborTime();
        Duration expectedDuration = getDuration(tollgateExpectedStartDate, tollgateExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    @Test
    public void wbsTollgateSystemDates() {
        GPBean tollgate = createBeans();
        Timestamp actualStart = tollgate.getSystemStartDate();
        Timestamp expectedStart = convertStringToTimestamp(tollgateExpectedStartDate);
        assertEquals(expectedStart, actualStart);
        Timestamp actualEnd = tollgate.getSystemEndDate();
        Timestamp expectedEnd = convertStringToTimestamp(tollgateExpectedEndDate);
        assertEquals(expectedEnd, actualEnd);
        Duration actualDuration = tollgate.getSystemLaborTime();
        Duration expectedDuration = getDuration(tollgateExpectedStartDate, tollgateExpectedEndDate);
        assertEquals(expectedDuration, actualDuration);
    }

    private GPBean createBeans() {
        ProjectEditorModel model = new ProjectEditorModel(getVisitProxy());
        model.loadTree(PSObject.get(tollgateId, getContext()));
        model.loadFullTree();
        return (GPBean) model.getBean(tollgateId);
    }

    private void runScheduler() {
        Tollgate tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        Checkpoint gate1 = tollgate.getCheckpoint(0);
        gate1.getSchedules().setPlannedStartDate(convertStringToTimestamp(gate1ExpectedStartDate));
        gate1.getSchedules().setPlannedEndDate(convertStringToTimestamp(gate1ExpectedEndDate));
        gate1.getSchedules().setPlannedLaborTime(getDuration(gate1ExpectedStartDate, gate1ExpectedEndDate));
        gate1.save();
        Checkpoint gate2 = tollgate.getCheckpoint(1);
        gate2.getSchedules().setPlannedStartDate(convertStringToTimestamp(gate2ExpectedStartDate));
        gate2.getSchedules().setPlannedEndDate(convertStringToTimestamp(gate2ExpectedEndDate));
        gate2.getSchedules().setPlannedLaborTime(getDuration(gate2ExpectedStartDate, gate2ExpectedEndDate));
        gate2.save();
        tollgate.save();
        tollgate = (Tollgate) PSObject.get(tollgateId, getContext());
        WorkUtil.setScheduleDatesInternal(tollgate);
        tollgate.save();
    }
}