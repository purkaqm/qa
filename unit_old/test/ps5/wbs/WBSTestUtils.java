package ps5.wbs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ps5.psapi.project.create.WorkTypeBean;
import ps5.psapi.project.create.WorkTypeHelp;
import ps5.services.state.UnitTestVisit;
import ps5.wbs.logic.ProjectEditorUtils;
import ps5.wbs.logic.handlers.VisitProxy;

import com.cinteractive.ps3.PSHome;
import com.cinteractive.ps3.PSHome.InitializationException;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.UrlDocument;
import com.cinteractive.ps3.entities.Admins;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.LoginException;
import com.cinteractive.ps3.entities.ResourcePool;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.HierarchyUtils;
import com.cinteractive.ps3.resources.Need;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.TollPhase;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.Milestone;
import com.cinteractive.ps3.work.Template;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.util.SortedList;

@SuppressWarnings("javadoc")
public class WBSTestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WBSTestUtils.class);

    /**
     * @see #createTollgate(String, VisitProxy)
     */
    public static void addNewTollgateDeliverable(String name, Checkpoint checkpoint, VisitProxy visitProxy) {
        Tollgate tollgate = createTollgate(name, visitProxy);
        checkpoint.addDeliverable(tollgate, visitProxy.getPrincipal());
        checkpoint.save();
    }

    /**
     * @see #createWork(String, User, Work)
     */
    public static void addNewWorkDeliverable(String name, Checkpoint checkpoint, User user) {
        Work deliverable = createWork(name, user, null);
        checkpoint.addDeliverable(deliverable, user);
        checkpoint.save();
    }

    /**
     * First it will call {@link #deletePSO(String, User, PSType)} and only then create a new document
     * 
     * @param name
     * @param user user should has context assigned
     * @see #deletePSO(String, User, PSType)
     */
    public static UrlDocument createDocument(String name, User user) {
        deletePSO(name, user, UrlDocument.TYPE);
        UrlDocument document = UrlDocument.createNew(name, name, user);
        document.save();
        return document;
    }

    /**
     * First it will call {@link #deletePSO(String, User, PSType)} and only then create a new milestone
     * 
     * @param name
     * @param user user should has context assigned
     * @see #deletePSO(String, User, PSType)
     */
    public static Milestone createMilestone(String name, User user) {
        deletePSO(name, user, Milestone.TYPE);
        Milestone milestone = (Milestone) Work.createNew(Milestone.TYPE, name, user);
        milestone.save();
        return milestone;
    }

    public static Need createNeed(Work work, User user) {
        work.setUseResourcePlanning(true, user);
        Need need = work.addNeed(Collections.<PSTag> emptyList(), user);
        need.setAssignedUser(user, user);
        need.save();
        return need;
    }

    /**
     * @param webRootDirectory directory where "contexts" directory is located, usually it is "web"
     */
    public static PSHome createPSHome(String webRootDirectory) {
        PSHome home = new PSHome(webRootDirectory);
        try {
            home.init();
        } catch (InitializationException e) {
            LOGGER.error("Unable to create PSHome instance", e);
        }
        PSHome.setInstance(home, false);
        return home;
    }

    /**
     * First it will call {@link #deletePSO(String, User, PSType)} and only then create a new resource pool
     * 
     * @param name
     * @param user user should has context assigned
     * @see #deletePSO(String, User, PSType)
     */
    public static ResourcePool createResourcePool(String name, User user) {
        deletePSO(name, user, ResourcePool.TYPE);
        ResourcePool pool = ResourcePool.createNew(name, user, user.getContext());
        pool.save();
        return pool;
    }

    /**
     * First it will call {@link #deletePSO(String, User, PSType)} and only then create a new tollgate
     * 
     * @param name
     * @param visitProxy should contain both visit and principal
     * @see #deletePSO(String, User, PSType)
     */
    public static Tollgate createTollgate(String name, VisitProxy visitProxy) {
        deletePSO(name, visitProxy.getPrincipal(), Tollgate.TYPE);
        /*
        WorkTypeBeanEx template = null;
        List<WorkTypeBean> types = WorkTypeHelp.getCreatingWorkTemplates(visitProxy.getVisit());
        for (WorkTypeBean workType : types) {
            if (workType.getType().equals(Tollgate.TYPE)) {
                template = new WorkTypeBeanEx(workType, visitProxy);
                break;
            }
        }
        Tollgate tollgate = (Tollgate) ProjectEditorUtils.createWork(template, name, visitProxy.getPrincipal(), null);
        tollgate.save();
        */
        SortedList<PSTag> phases = TollPhase.get(visitProxy.getContext()).getRoots();
        List<PSTag> validPhases = new ArrayList<PSTag>();
        PSTag process = null;
        for (PSTag phase : phases) {
            if (phase.getChildren().isEmpty() || phase.isLocked()) {
                continue;
            }
            process = phase;
        }        
        Tollgate tollgate = Tollgate.createNew(name, process, Tollgate.TYPE, Template.createNew(name, visitProxy.getPrincipal()), visitProxy.getPrincipal());
        return tollgate;
    }

    /**
     * Creates tollgate, adds two work deliverables to the first checkpoint and one tollgate deliverable to the second
     * checkpoint
     * 
     * @param name
     * @param visitProxy should contain both visit and principal
     * @param deliverablesName name template, sequence number will be added at the end
     * @see #createTollgate(String, VisitProxy)
     * @see #addNewWorkDeliverable(String, Checkpoint, User)
     * @see #addNewTollgateDeliverable(String, Checkpoint, VisitProxy)
     */
    public static Tollgate createTollgateWithDeliverables(String name, VisitProxy visitProxy, String deliverablesName) {
        Tollgate tollgate = createTollgate(name, visitProxy);
        if (tollgate.getCheckpoints().size() < 2) {
            throw new RuntimeException("Tollgate should has at least 2 checkpoints");
        }
        Checkpoint checkpoint1 = tollgate.getCheckpoint(0);
        Checkpoint checkpoint2 = tollgate.getCheckpoint(1);
        addNewWorkDeliverable(deliverablesName + "1", checkpoint1, visitProxy.getPrincipal());
        addNewWorkDeliverable(deliverablesName + "2", checkpoint1, visitProxy.getPrincipal());
        addNewTollgateDeliverable(deliverablesName + "1", checkpoint2, visitProxy);
        return tollgate;
    }

    /**
     * User will be assigned to the admins group
     */
    public static User createUserIfAbsence(String email, InstallationContext context, boolean autologin) {
        User user = User.getByEmailAddress(email, context);
        if (user == null) {
            user = User.createNewUser(email, context);
            user.setTimeZoneId(TimeZone.getDefault().getID());
            user.save();
        }
        Group g = Admins.get(context);
        if (!g.isMember(user)) {
            g.addUser(user);
            g.setModified();
            g.save();
        }
        if (autologin) {
            try {
                User.loginByUsername(user.getLogin(), context);
            } catch (LoginException e) {
                LOGGER.error("Unable to login user", e);
            }
        }
        return user;
    }

    public static VisitProxy createVisitProxy(User user) {
        PSSession session = PSSession.createAutoSession(user);
        return new VisitProxy(UnitTestVisit.createVisit(session));
    }

    /**
     * First it will call {@link #deletePSO(String, User, PSType)} and only then create a new work
     * 
     * @param name
     * @param user user should has context assigned
     * @param parentWork can be null
     * @see #deletePSO(String, User, PSType)
     */
    public static Work createWork(String name, User user, Work parentWork) {
        deletePSO(name, user, Work.TYPE);
        Work work = Work.createNew(Work.TYPE, name, user);
        if (parentWork != null) {
            work.setParentWork(parentWork, user);
        }
        work.setUseResourcePlanning(true, user);
        work.save();
        return work;
    }

    /**
     * Delete all psobjects with the provided name and its children
     * 
     * @param name
     * @param user user should has context assigned
     * @param type
     * @see #deletePSOChildren(String, User, PSType)
     */
    public static void deletePSO(String name, User user, PSType type) {
        deletePSOChildren(name, user, type);
        List<PSObject> objects = PSObject.getByName(type, name, Boolean.FALSE, null, null, user.getContext());
        if (objects != null && !objects.isEmpty()) {
            for (PSObject psObject : objects) {
                psObject.deleteSoft(user);
            }
        }
    }

    /**
     * @param name
     * @param user user should has context assigned
     * @param type
     */
    public static void deletePSOChildren(String name, User user, PSType type) {
        List<PSObject> objects = PSObject.getByName(type, name, Boolean.FALSE, null, null, user.getContext());
        if (objects != null && !objects.isEmpty()) {
            for (PSObject pso : objects) {
                List<PSObject> children = HierarchyUtils.getChildren(pso);
                Iterator<PSObject> it = children.iterator();
                while (it.hasNext()) {
                    PSObject child = it.next();
                    if (child instanceof Work) {
                        ((Work) child).deleteAll(null, user);
                    } else {
                        child.deleteSoft(user);
                    }
                }
                pso.save();
            }
        }
    }

    private WBSTestUtils() {
        // utility class should not be instantiated
    }

    public static Checkpoint createCheckpoint(String name, VisitProxy visitProxy) {
        Tollgate tollgate = createTollgateWithDeliverables(name, visitProxy, name);
        return tollgate.getCheckpoint(0);
    }
}