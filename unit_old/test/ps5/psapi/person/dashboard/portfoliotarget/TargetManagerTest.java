package ps5.psapi.person.dashboard.portfoliotarget;

import java.util.Arrays;
import java.util.Collection;

import ps5.psapi.fields.TargetUniverse;
import ps5.psapi.person.dashboard.portfoliotarget.TargetDef.TargetDefConstant;
import ps5.support.grid.model2.DefaultGridEnvironment;
import ps5.support.grid.model2.GridEnvironment;
import ps5.support.grid.model3.TargetManager;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.dashboard.DashboardLayout;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.portfolio.Portfolio;
import com.cinteractive.ps3.test.PSTestBase;

public class TargetManagerTest extends PSTestBase {

	User nobody;
	
	String BUDGET_COLUMN = "budget";
	PersistentKey BUDGET_KEY = Uuid.get(BUDGET_COLUMN);
	String ACTUAL_COST_COLUMN = "cost_actual";
	PersistentKey ACTUAL_COST_KEY = Uuid.get(ACTUAL_COST_COLUMN);

	private GridEnvironment env;
	private TargetManager targetMgr;
	
	public TargetManagerTest() {
		super("TargetManagerTest");
	}
	
	@Override
	protected void setUp() {
		super.setUp();
		
		nobody = Nobody.get( getContext() );
		
		env = new DefaultGridEnvironment() {{
			this.context = TargetManagerTest.this.getContext();
		}};
		targetMgr = new TargetManager(env);
	}
	
	User getDefaultUser() {
		return nobody;
	}


	
	public void testManager() {
		targetMgr.registerTargetableColumn(BUDGET_COLUMN);
		targetMgr.registerTargetableColumn(ACTUAL_COST_COLUMN);
		
		assertFalse( targetMgr.hasActiveTargets() );
		assertEquals(0, targetMgr.getCurrentTargets().size());
		
		TargetDef.TargetDefConstant target = new TargetDef.TargetDefConstant();
		
		target.setColumnId( Uuid.get(BUDGET_COLUMN) );
		target.setNumericValue(42.0);
		targetMgr.setTarget(BUDGET_COLUMN, target);
		
		assertTrue( targetMgr.hasActiveTargets() );
		assertEquals(1, targetMgr.getCurrentTargets().size());
		TargetDef budgetTarget = targetMgr.getTarget(BUDGET_COLUMN);
		assertEquals(target, budgetTarget);
		
		//TODO
		
		targetMgr.updateVisibleColumns(Arrays.asList( ACTUAL_COST_COLUMN ));
		assertFalse( targetMgr.hasActiveTargets() );
		assertEquals(0, targetMgr.getCurrentTargets().size());
		
		targetMgr.updateVisibleColumns(Arrays.asList( BUDGET_COLUMN ));
		assertTrue( targetMgr.hasActiveTargets() );
		assertEquals(1, targetMgr.getCurrentTargets().size());
		budgetTarget = targetMgr.getTarget(BUDGET_COLUMN);
		assertEquals(target, budgetTarget);
	}
	
	// this is the TargetUniverse behavior that DashboardColumnModel#saveTargets() relies upon
	public void testSaveTargets() throws Exception {
		TargetUniverse targetUniverse = TargetUniverse.getInstance( getContext() );
		
		Portfolio portfolio = Portfolio.createNew("targetTest", getDefaultUser());
		PersistentKey portfolioId = portfolio.getId();
		DashboardLayout layout = new DashboardLayout("targetTest", getContext());
		
		// remember which columns had targets the last time we saved
		Collection<PersistentKey> initialTargets = targetUniverse.getTargetColumns(portfolioId, layout.getId());
		assertTrue(initialTargets.isEmpty());
		
		TargetDef target = TargetDef.createNew(TargetDefConstant.class, getContext());
		target.setPortfolio(portfolio);
		target.setLayout(layout);
		target.setUser( getDefaultUser() );
		target.setColumnId(BUDGET_KEY);
		targetUniverse.setTargetDef(target);
		
		assertSame( target, targetUniverse.getTargetDef(portfolio, layout, BUDGET_KEY, getDefaultUser().getId()) );

		Collection<PersistentKey> addedTargets = targetUniverse.getTargetColumns(portfolioId, layout.getId());
		assertEquals(1, addedTargets.size());
		assertEquals(BUDGET_KEY, addedTargets.iterator().next());
	}

}
