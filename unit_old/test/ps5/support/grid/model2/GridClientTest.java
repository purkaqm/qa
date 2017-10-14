package ps5.support.grid.model2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ps5.services.state.UnitTestVisit;
import ps5.services.state.Visit;
import ps5.support.grid.ActionResult;
import ps5.support.grid.BaseSection;
import ps5.support.grid.GridSectionType;
import ps5.support.grid.GridSerializationController;
import ps5.support.grid.models.IDataModel;
import ps5.support.grid.models.IModelsContainer;
import ps5.support.grid.models.ModelsContainerBase;
import ps5.wbs.logic.BeanModel;
import ps5.wbs.model.sections.DataView;
import ps5.wbs.model.sections.SectionContainer;

import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.currency.Currency;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.documents.FileDocument;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.test.MockPSSession;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;


public class GridClientTest extends PSTestBase {
	
	public GridClientTest() {
		super("GridClientTest");
	}

	User user;
	PSSession session;
	Visit visit;
	
	GridController controller;
	
	String dataStoreId;
	IDataModel adapter;
	IModelsContainer modelsContainer;
	
	
	GridEnvironment env = new GridEnvironment() {

        private Map<String, Object> properties = new HashMap<String, Object>();
        
        @Override
        public InstallationContext getContext() {
            return GridClientTest.this.getContext();
        }

        @Override
        public User getUser() {
            return user;
        }
        
        public Currency getCurrency() {
        	return getUser().getCurrency();
        }

        @Override
        public Object getProperty(String key) {
            return properties.get(key);
        }

        @Override
        public Locale getLocale() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public Visit getVisit() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public PSSession getSession() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public SectionContainer getContainer() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public BeanModel getModel() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public DataView getView() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public void setProperty(String key, Object value) {
            // XXX Auto-generated method stub
            
        }
        
    };
    
    Work makeWork(String name, WorkStatus status, Integer priority) {
        Work work = Work.createNew(Work.TYPE, name, user);
        if (status != null)
            work.setStatus(status);
        if (priority != null)
            work.setAssignedPriority(priority);
        return work;
    }
    
    Comparator<Work> nameComparator = new Comparator<Work>() {
        @Override
        public int compare(Work o1, Work o2) {
            return o1.getName() .compareTo( o2.getName() );
        }
    };
    
	PortfolioDataSource preparePortfolioDataSource() {
        PortfolioDataSource source = new PortfolioDataSource("Portfolio 42", user, super.getContext());
        
        Document doc01 = FileDocument.createNew("doc01", user);
        Document doc02 = FileDocument.createNew("doc02", user);
        Document doc03 = FileDocument.createNew("doc03", user);
        getContext().getCache().put(doc01);
        getContext().getCache().put(doc02);
        getContext().getCache().put(doc03);
        
        Work work01 = makeWork("Work 01", WorkStatus.COMPLETED, 3);
        work01.addDocument(doc01);
        
        Work work02 = makeWork("Work 02", WorkStatus.ON_TRACK, 2);
        work02.addDocument(doc01);
        work02.addDocument(doc02);
        
        Work work03 = makeWork("Work 03", WorkStatus.PROPOSED, 1);
        work03.addDocument(doc01);
        work03.addDocument(doc02);
        work03.addDocument(doc03);
        
        source.addWork( work03 );
        source.addWork( work01 );
        source.addWork( work02 );
        
        return source;
    }
	
	
	@Override
	protected void setUp() {
		super.setUp();
		
		setupEnvironment();
        
		PortfolioDataSource dataSource = preparePortfolioDataSource();
        DataSourceFilter<Work> sourceFilter = new DataSourceFilter<Work>(dataSource);
        sourceFilter.applySorting(nameComparator);
        DashboardGridColumnModel columnModel = new DashboardGridColumnModel(env);
        
        controller = new GridController<Work>(sourceFilter, columnModel);
        
        adapter = new SectionDataModelAdapter(controller, 
        		"DASHBOARD_MODEL", GridSectionType.DASHBOARD_MASTER_SECTION, GridSectionType.DASHBOARD_MAIN_SECTION);
        
        modelsContainer = new ModelsContainerBase(Arrays.asList(adapter));
	}


	private void setupEnvironment() {
		user = User.createNewUser("test@cinteractive.com", getContext());
		session = new MockPSSession(getContext(), user);
        User.setCurrentPrincipal(session);
        visit = UnitTestVisit.createVisit(session);
	}
	
	
	public void testGetHeaderData() throws Exception {
		List<IDataModel> models = Arrays.asList(adapter);
		String sIds = "[dashboard.Master, dashboard.Main]";
    	List<String> sectionsIds = (sIds != null) ? JSONArray.toList(new JSONArray(sIds)) : null;
        JSONObject data = GridSerializationController.serializeHeaderData(models, sectionsIds);
        assertEquals("", data.toString(4));
        
        String visColIds = "{\"DASHBOARD_MODEL\":{\"disp\":true,\"sections\":{" +
        		"\"dashboard.Master\":{\"disp\":true,\"cols\":[\"treeCol\",\"numCol\"]}," +
        		"\"dashboard.Main\":{\"disp\":true,\"cols\":[\"RESOURCE_DURATION\",\"EFFORT\",\"ALLOCATION\",\"PERSON\",\"ROLE\",\"RESOURCE_POOL\",\"RT:fs000080000g26openi0000000\"]}" +
        		"}}}";
        JSONObject visibleCols = (visColIds != null) ? new JSONObject(visColIds) : null;
        List<ActionResult> results = setVisibleCols(visit, dataStoreId, visibleCols);
	}
	
	public void testGetTotalsData() {
		
	}
	
	public void testGetColsData() {
		// TODO Auto-generated method stub

	}
	
	public void testGetContent() {
		
	}
	
	public void testCalcChanges() {
		
	}
	
	public void testRefreshData() {
		
	}
	
	public void testSetVisibleColumns() {
		
	}
	
	// service methods
	
	private List<ActionResult> setVisibleCols(Visit visit, String dataStoreId, JSONObject visColIds) {
        List<ActionResult> ret = new ArrayList<ActionResult>();
    	if (visColIds != null) {
    		IModelsContainer container = this.getModelsContainer(visit, dataStoreId);
    	    for (Iterator<String> modelIter = visColIds.keys(); modelIter.hasNext();) {
                String modelId = (String) modelIter.next();
                IDataModel model = container.getModelByUID(modelId);
                
                JSONObject modelData = visColIds.getJSONObject(modelId);
                ActionResult result = model.changeVisibility(modelData.getBoolean("disp"));
                if (result != null && !result.isEmpty()) {
                    ret.add(result);
                }
                JSONObject sectionsData = modelData.getJSONObject("sections");
                for (Iterator<String> colIter = sectionsData.keys(); colIter.hasNext();) {
                    String sectionId = (String) colIter.next();
                    JSONObject sectionData = sectionsData.getJSONObject(sectionId);
                    BaseSection section = getSectionById(visit, dataStoreId, modelId, sectionId);
                    if (section != null) {
                        JSONArray ids = sectionData.getJSONArray("cols");
                        result = section.setVisibleColumns(ids != null ? JSONArray.toList(ids) : new ArrayList());
                        if (result != null && !result.isEmpty()) {
                            ret.add(result);
                        }
                    }
                }
    	    }
    	}
    	return ret;
    }

	private IModelsContainer getModelsContainer(Visit visit, String dataStoreId) {
		return modelsContainer;
	}
	
	private BaseSection getSectionById(Visit visit, String dataStoreId, String modelUID, String sectionId) {
        IDataModel model = getModelsContainer(visit, dataStoreId).getModelByUID(modelUID);
        List<BaseSection> sections = model.getAllSections();
        for (BaseSection section : sections) {
            if (section.getId().equals(sectionId)) {
                return section;
            }
        }
        return null;
    }
}


