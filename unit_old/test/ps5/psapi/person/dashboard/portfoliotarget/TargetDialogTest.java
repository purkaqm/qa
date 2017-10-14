package ps5.psapi.person.dashboard.portfoliotarget;

import junit.framework.TestCase;
import net.sf.json.JSONObject;
import ps5.psapi.person.dashboard.portfoliotarget.TargetDef.TargetDefConstant;
import ps5.support.grid.model2.DefaultGridEnvironment;
import ps5.support.grid.model2.GridEnvironment;

import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.test.PSTestBase;

public class TargetDialogTest extends PSTestBase {
	
	public TargetDialogTest() {
		super("TargetDialog");
	}

	private static final String BUDGET_COLUMN = "BUDGET";
	
	private GridEnvironment env;
	
	@Override
	protected void setUp() {
		super.setUp();
		
		env = new DefaultGridEnvironment() {{
			this.context = TargetDialogTest.this.getContext();
		}};
	}

	public void testCreateTarget() throws Exception {
		TargetDialog dialog = new TargetDialog(env, BUDGET_COLUMN);
		
		dialog.apply( new JSONObject("{\"colId\":\"cost_actual\",\"targetType\":\"const\",\"cvalueId\":\"100\",\"visibility\":false}") );
		TargetDef newTarget = dialog.getTarget();
		assertTrue( newTarget.isNew() );
		assertEquals( Uuid.get(BUDGET_COLUMN), newTarget.getColumnId() );
		assertEquals( TargetDefConstant.class, newTarget.getClass() );
		TargetDefConstant constTarget = (TargetDefConstant) newTarget;
		assertEquals( 100.0, constTarget.getValue() );
	}
	
	public void testChangeTarget() throws Exception {
		//TODO especially changing target type
	}

}
