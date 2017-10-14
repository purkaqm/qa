package ps5.support.grid.model2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.ObjectUtils;

import ps5.psapi.WorkHelp;
import ps5.services.state.Visit;
import ps5.support.grid.GridCell;
import ps5.support.grid.GridColumn;
import ps5.support.grid.model3.ISummaryFunction;
import ps5.wbs.logic.BeanModel;
import ps5.wbs.model.sections.Pagination;
import ps5.wbs.model.sections.SectionContainer;

import com.cinteractive.database.Uuid;
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
import com.cinteractive.util.StringUtilities;




/**
 * What to test:
 * - load dashboard data into a model
 * - define a couple of columns
 * - sort, filter, group the data
 * - modify and save the data
 * - modify and save the layout
 */
public class DashboardGridTest extends PSTestBase {
    private static final String TEST_USER_EMAIL = "test@cinteractive.com";
    private static final String TEST_PROJECT_NAME = "Test Project 101";

    public DashboardGridTest() {
        super("DashboardGridTest");
    }
    
    User user;
    Work project1;
    List<Work> portfolio;
    
    @Override
    protected void setUp() {
        super.setUp();
        
        user = User.createNewUser(TEST_USER_EMAIL, getContext());
        User.setCurrentPrincipal( new MockPSSession(getContext(), user) );
        
        project1 = Work.createNew(Work.TYPE, TEST_PROJECT_NAME, user);
        portfolio = Arrays.asList(
                project1
        );
    }
    
    GridEnvironment env = new GridEnvironment() {

        private Map<String, Object> properties = new HashMap<String, Object>();
        
        @Override
        public InstallationContext getContext() {
            return DashboardGridTest.this.getContext();
        }

        @Override
        public User getUser() {
            return DashboardGridTest.this.user;
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
        public ps5.wbs.model.sections.DataView getView() {
            // XXX Auto-generated method stub
            return null;
        }

        @Override
        public void setProperty(String key, Object value) {
            // XXX Auto-generated method stub
            
        }
        
    };

    
    public void testSetUp() throws Exception {
        assertNotNull(user);
        assertEquals(TEST_USER_EMAIL, user.getEmailAddress());
        
        assertNotNull(project1);
        assertEquals(TEST_PROJECT_NAME, project1.getName());
        
        assertEquals(1, portfolio.size());
    }
    
    Work makeWork(String name, WorkStatus status, Integer priority) {
        Work work = Work.createNew(Work.TYPE, name, user);
        if (status != null)
            work.setStatus(status);
        if (priority != null)
            work.setAssignedPriority(priority);
        return work;
    }
    
    class HighPriorityFilter implements Filter<Work> {
        private int priority;
        HighPriorityFilter(int priority) {
            this.priority = priority;
        }
        
        @Override
        public boolean filter(Work obj) {
            int workPriority = (Integer) ObjectUtils.defaultIfNull(obj.getAssignedPriority(), Integer.MAX_VALUE);
            return workPriority <= this.priority;
        }
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
        
        Work work04 = makeWork("Work 04", WorkStatus.PROPOSED, 2);
        Work work05 = makeWork("Work 05", WorkStatus.PROPOSED, 3);
        
        source.addWork( work03 );
        source.addWork( work01 );
        source.addWork( work02 );
        source.addWork( work04 );
        source.addWork( work05 );
        
        return source;
    }
    
    public void testDataSource() throws Exception {
        PortfolioDataSource source = preparePortfolioDataSource();
        
        assertEquals(5, source.getChildCount(null));
        
        DataSourceFilter<Work> filter = new DataSourceFilter<Work>(source);
        
        assertEquals(5, filter.getChildCount(null));
        filter.applyFiltering( new HighPriorityFilter(1) );
        assertEquals(1, filter.getChildCount(null));
        filter.applyFiltering( new HighPriorityFilter(2) );
        assertEquals(3, filter.getChildCount(null));
        filter.applyFiltering( new HighPriorityFilter(3) );
        assertEquals(5, filter.getChildCount(null));
        
        filter.applySorting(nameComparator);
        {
            List<Work> works = filter.getChildren(null);
            assertEquals("Work 01", works.get(0).getName());
            assertEquals("Work 02", works.get(1).getName());
            
            Work work01 = works.get(0);
            assertEquals(1, WorkHelp.getDocsCountIncludecheckppoints(work01, null));
        }
        
        filter.applyFiltering( new HighPriorityFilter(2) );
        {
            List<Work> works = filter.getChildren(null);
            assertEquals("Work 02", works.get(0).getName());
            assertEquals("Work 03", works.get(1).getName());
        }
        
        filter.applySorting( ComparatorUtils.reversedComparator(nameComparator) );
        {
            List<Work> works = filter.getChildren(null);
            assertEquals("Work 04", works.get(0).getName());
            assertEquals("Work 03", works.get(1).getName());
        }
        
        filter.applyFiltering(null);
        {
            List<Work> works = filter.getChildren(null);
            assertEquals("Work 05", works.get(0).getName());
            assertEquals("Work 04", works.get(1).getName());
            assertEquals("Work 03", works.get(2).getName());
        }
    }
    
    public void testTreeNode() throws Exception {
        ViewNode<String> node1 = new ViewNode<String>( new LineItem<String>("node1", "node1") );
        assertEquals("node1", node1.getData());
        assertEquals(0, node1.getChildrenCount());
        ViewNode<String> node12 = new ViewNode<String>( new LineItem<String>("node12", "node12") );
        node1.addChild(node12);
        assertEquals(1, node1.getChildrenCount());
        assertEquals(0, node1.getPosition(node12));
        assertEquals(node12, node1.getChild(0));
    }
    
    public void testDataView() throws Exception {
        PortfolioDataSource source = preparePortfolioDataSource();
        List<String> sourceIds = source.getChildIds(null);
        
        DataView<Work> dataView = new DataView<Work>(source);
        
        List<String> rowIds = new ArrayList<String>( dataView.getCountRowsOnPage() );
        ViewItem<Work> parent = dataView.getItem(null);
        dataView.expand(parent);
        
        int firstIdx = dataView.getPageStart(parent, 0);
        int lastIdx = dataView.getPageEnd(parent, 0);
        for (int rowIdx = firstIdx; rowIdx < lastIdx; rowIdx++) {
            ViewItem<Work> item = dataView.getChild(parent, rowIdx);
            rowIds.add( item.getId() );
        }
        assertEquals(sourceIds, rowIds);
    }
    
    public void testControllerRowIds() throws Exception {
        PortfolioDataSource dataSource = preparePortfolioDataSource();
        List<String> sourceIds = dataSource.getChildIds(null);
        ColumnModel<Work> columnModel = new ColumnModel<Work>(env) {

            @Override
            public void load() {
            }

            @Override
            public void save() {
            }

			@Override
			public String getMasterColumnId() {
				return null;
			}
            
        };
        GridController<Work> controller = new GridController<Work>(dataSource, columnModel);
        List<String> rowIds = controller.getRowIds(0, null);
        assertEquals(sourceIds, rowIds);
    }
    
    public void testColumnModel() throws Exception {
        PortfolioDataSource dataSource = preparePortfolioDataSource();
        DataSourceFilter<Work> sourceFilter = new DataSourceFilter<Work>(dataSource);
        sourceFilter.applySorting(nameComparator);
        List<String> sourceIds = sourceFilter.getChildIds(null);
        DashboardGridColumnModel columnModel = new DashboardGridColumnModel(env);
        GridController<Work> controller = new GridController<Work>(sourceFilter, columnModel);
        List<String> rowIds = controller.getRowIds(0, null);
        assertEquals(sourceIds, rowIds);
        
        List<GridColumn> data = controller.getColumnsData(rowIds, Arrays.asList( 
                DashboardGridColumnModel.PROJECT_NAME.getId(),
                DashboardGridColumnModel.STATUS.getId(),
                DashboardGridColumnModel.DOCUMENT_COUNT.getId()));
        assertEquals(3, data.size());
        GridColumn nameColumn = data.get(0);
        assertEquals(DashboardGridColumnModel.PROJECT_NAME.getId(), nameColumn.getId());
        {
            List<GridCell> cells = nameColumn.getCells();
            assertEquals("Work 01", cells.get(0).getDispValue());
            assertEquals("Work 02", cells.get(1).getDispValue());
        }
        
        GridColumn statusColumn = data.get(1);
        assertEquals(DashboardGridColumnModel.STATUS.getId(), statusColumn.getId());
        {
            List<GridCell> cells = statusColumn.getCells();
            assertEquals(" Completed", StringUtilities.filterHTMLAll( cells.get(0).getDispValue() ));
            assertEquals(" On Track", StringUtilities.filterHTMLAll( cells.get(1).getDispValue() ));
        }
        
        GridColumn documentsColumn = data.get(2);
        assertEquals(DashboardGridColumnModel.DOCUMENT_COUNT.getId(), documentsColumn.getId());
        {
            List<GridCell> cells = documentsColumn.getCells();
            assertEquals("1", cells.get(0).getDispValue());
            assertEquals("2", cells.get(1).getDispValue());
        }
    }
 
    public void testTotal() throws Exception {
        PortfolioDataSource dataSource = preparePortfolioDataSource();
        DataSourceFilter<Work> sourceFilter = new DataSourceFilter<Work>(dataSource);
        sourceFilter.applySorting(nameComparator);
        List<String> sourceIds = sourceFilter.getChildIds(null);
        DashboardGridColumnModel columnModel = new DashboardGridColumnModel(env);
        GridController<Work> controller = new GridController<Work>(sourceFilter, columnModel);
        
        controller.addTotal(DashboardGridColumnModel.DOCUMENT_COUNT.getId(), "sum");
        
        List<String> rowIds = controller.getRowIds(0, null);
        assertEquals(sourceIds, rowIds.subList(0, rowIds.size() - 1));
        
        List<GridColumn> data = controller.getColumnsData(
                rowIds.subList(rowIds.size() - 1, rowIds.size()), 
                Arrays.asList( DashboardGridColumnModel.PROJECT_NAME.getId(), DashboardGridColumnModel.DOCUMENT_COUNT.getId() ));
        GridCell totalCountCell = data.get(1).getCells().get(0);
        assertEquals(String.valueOf(1 + 2 + 3), totalCountCell.getDispValue());
    }
    
    public void testGrouping() throws Exception {
    	PortfolioDataSource dataSource = preparePortfolioDataSource();
        DataSourceFilter<Work> sourceFilter = new DataSourceFilter<Work>(dataSource);
        sourceFilter.applySorting(nameComparator);
        List<String> sourceIds = sourceFilter.getChildIds(null);
        final DashboardGridColumnModel columnModel = new DashboardGridColumnModel(env);
        GridController<Work> controller = new GridController<Work>(sourceFilter, columnModel);
        
        IDataView<Work> dataView = controller.getDataView();
        dataView.setGrouping(new Grouping<Work>() {

			@Override
			public List<IColumnDefinition<Work, ?>> getGroupedColumns() {
				List<IColumnDefinition<Work, ?>> cols = new ArrayList<IColumnDefinition<Work, ?>>();
				cols.add(columnModel.PRIORITY);
				return cols;
			}

			@Override
			public Comparator<Work> getGroupingComparator() {
				return new Comparator<Work>() {

					@Override
					public int compare(Work o1, Work o2) {
						return o1.getAssignedPriority().compareTo( o2.getAssignedPriority() );
					}
					
				};
			}

			@Override
			public Comparator<Work> getInGroupSortingComparator() {
				return nameComparator;
			}

            @Override
            public Map<IColumnDefinition<Work, ?>, ISummaryFunction<?, ?>> getGroupSubtotals() {
                return Collections.emptyMap();
            }
        	
        });
        
        List<String> groupRowIds = controller.getRowIds(0, null);
        assertEquals(3, groupRowIds.size());
        
        List<GridColumn> groupRowData = controller.getColumnsData(groupRowIds, 
                Arrays.asList( DashboardGridColumnModel.PROJECT_NAME.getId(), DashboardGridColumnModel.PRIORITY.getId() ));
        GridCell p1GroupCell = groupRowData.get(1).getCells().get(0);
        assertEquals("1", p1GroupCell.getDispValue());
        
        List<String> p1RowIds = controller.getRowIds(0, groupRowIds.get(0));
        assertEquals(1, p1RowIds.size());
        List<String> p2RowIds = controller.getRowIds(0, groupRowIds.get(1));
        assertEquals(2, p2RowIds.size());
        List<GridColumn> p2RowData = controller.getColumnsData(p2RowIds, 
                Arrays.asList( DashboardGridColumnModel.PROJECT_NAME.getId(), DashboardGridColumnModel.PRIORITY.getId() ));
        assertEquals("Work 02", p2RowData.get(0).getCells().get(0).getDispValue());
        assertEquals("Work 04", p2RowData.get(0).getCells().get(1).getDispValue());
	}
    
    static class TestViewItem extends ps5.wbs.model.sections.ViewItem {

        static ps5.wbs.beans.WBSBean makeBean(String name) {
            ps5.wbs.beans.WBSBean bean = new ps5.wbs.beans.WBSBean( Uuid.create() );
            bean.setName(name);
            return bean;
        }
        
        public TestViewItem(String name) {
            super( makeBean(name) );
        }

        public void addChild(TestViewItem item,int position) {
            super.addChild(item, position);
        }
    }
    
    private ps5.wbs.model.sections.ViewItem makeLegacyViewItem(String name) {
        ps5.wbs.beans.WBSBean bean = new ps5.wbs.beans.WBSBean( Uuid.create() );
        bean.setName(name);
        return new ps5.wbs.model.sections.ViewItem(bean);
    }
    public void testPagination() throws Exception {
        Pagination paging = new Pagination();
        paging.setCountRowsOnPage(3);
        
        TestViewItem root = new TestViewItem("root");
        for (int i = 0; i < 5; ++i) {
            TestViewItem child = new TestViewItem("child0" + i);
            root.addChild(child, i);
        }
        paging.expand(root);
        assertEquals(2, paging.getPageCount(root));
        assertEquals(0, paging.getPageStart(root, 0));
        assertEquals(3, paging.getPageEnd(root, 0));
        assertEquals(3, paging.getPageStart(root, 1));
        assertEquals(5, paging.getPageEnd(root, 1));
        
        TestViewItem extra01 = new TestViewItem("extra01");
        root.addChild(extra01, 1);
        paging.addToPage(extra01);
        assertEquals(0, paging.getPage(extra01));
        assertEquals(2, paging.getPageCount(root));
        assertEquals(4, paging.getPageEnd(root, 0));
        assertEquals(4, paging.getPageStart(root, 1));
        assertEquals(6, paging.getPageEnd(root, 1));
        
        TestViewItem extra02 = new TestViewItem("extra02");
        root.addChild(extra02, 2);
        paging.addToNewPage(extra02);
        assertEquals(3, paging.getPageCount(root));     // extra page
        assertEquals(0, paging.getPage(extra02));       // whoops, the new item's still on the 0th page
        assertEquals(4, paging.getPageEnd(root, 0));    // but it hasn't expanded, no
        assertEquals(4, paging.getPageStart(root, 1));
        assertEquals(6, paging.getPageEnd(root, 1));    // in fact, the last page doesn't cover all children anymore
        assertEquals(3, paging.getPageStart(root, 2));  // here's the new page, after the insertion
        assertEquals(4, paging.getPageEnd(root, 2));
        
    }
}