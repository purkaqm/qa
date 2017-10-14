package ps5.psapi.person.dashboard.portfoliotarget;

import java.awt.Color;
import java.util.Arrays;

import ps5.psapi.person.dashboard.portfoliotarget.ColorRuleSet.ColorRule;
import ps5.psapi.person.dashboard.portfoliotarget.TargetDef.TargetDefConstant;

import com.cinteractive.ps3.test.PSTestBase;

public class TargetVarianceTest extends PSTestBase {

	public TargetVarianceTest() {
		super("TargetVariance");
	}

	private ColorRuleSet makeColorRules() {
		ColorRule over49 = new ColorRule();
		over49.setValue(0.5);
		over49.setColor(Color.YELLOW);
		ColorRule over100 = new ColorRule();
		over100.setValue(1.0);
		over100.setColor(Color.RED);
		over100.setOp(true);
		ColorRuleSet colorRules = new ColorRuleSet(Arrays.asList(over49, over100), Color.GREEN);
		return colorRules;
	}
	
	public void testColorRules() {
		ColorRuleSet colorRules = makeColorRules();
		
		assertEquals(Color.GREEN, colorRules.colorOf(0.0));
		assertEquals(Color.GREEN, colorRules.colorOf(0.49));
		assertEquals(Color.GREEN, colorRules.colorOf(0.5));
		assertEquals(Color.YELLOW, colorRules.colorOf(0.51));
		assertEquals(Color.YELLOW, colorRules.colorOf(0.99));
		assertEquals(Color.RED, colorRules.colorOf(1.0));
		assertEquals(Color.RED, colorRules.colorOf(1.01));
	}
	
	public void testVariance() {
		TargetDefConstant target = new TargetDefConstant();
		target.setNumericValue(10.);
		target.setVarianceType(true);	// summary minus target
		
		TargetValue value = target.evaluate(null, null);
		assertEquals(10., value.getValue());
		
		VarianceState variance = VarianceState.get(value, 11.);
		assertEquals(1., variance.getDifferenceAbsolute());
		assertEquals(0.1, variance.getDifferencePercent());
		
		target.setVarianceType(false);	// target minus summary
		VarianceState reverseVariance = VarianceState.get(value, 11.);
		assertEquals(-1., reverseVariance.getDifferenceAbsolute());
		assertEquals(-0.1, reverseVariance.getDifferencePercent());
	}
	
	public void testVarianceColors() {
		TargetDefConstant target = new TargetDefConstant();
		target.setNumericValue(10.);
		target.setVarianceType(true);	// summary minus target
		target.setColorRules( makeColorRules() );
		
		TargetValue value = target.evaluate(null, null);
		assertEquals(10., value.getValue());
		
		VarianceState variance10 = VarianceState.get(value, 11.);
		assertEquals(1., variance10.getDifferenceAbsolute());
		assertEquals(0.1, variance10.getDifferencePercent());
		assertEquals(Color.GREEN, variance10.getColor());
		
		VarianceState variance60 = VarianceState.get(value, 16.);
		assertEquals(6., variance60.getDifferenceAbsolute());
		assertEquals(0.6, variance60.getDifferencePercent());
		assertEquals(Color.YELLOW, variance60.getColor());
		
		VarianceState variance110 = VarianceState.get(value, 21.);
		assertEquals(11., variance110.getDifferenceAbsolute());
		assertEquals(1.1, variance110.getDifferencePercent());
		assertEquals(Color.RED, variance110.getColor());
	}
}
