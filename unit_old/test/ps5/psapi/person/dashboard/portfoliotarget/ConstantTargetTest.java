package ps5.psapi.person.dashboard.portfoliotarget;

import java.util.Date;

import ps5.support.grid.model2.DefaultGridEnvironment;
import ps5.support.grid.model2.GridEnvironment;

import com.cinteractive.ps3.currency.Currency;
import com.cinteractive.ps3.currency.ExchangeRate;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.test.PSTestBase;

public class ConstantTargetTest extends PSTestBase {

	public ConstantTargetTest() {
		super("ConstantTarget");
	}

	User user;
	
	Currency USD;
	Currency EUR;
	Date conversionDate;
	ExchangeRate conversionRate;
	
	GridEnvironment env;
	
	TargetDef.TargetDefConstant target;
	
	@Override
	protected void setUp() {
		super.setUp();
		env = new DefaultGridEnvironment() {{
			this.context = ConstantTargetTest.this.getContext();
		}};
		
		user = Nobody.get( this.getContext() );
		assertNotNull(user);
	
		int offset = this.getContext().getTimeZone().getOffset(0);
		conversionDate = new Date(-offset);
		
		USD = Currency.get(this.getContext(), "USD");
		assertNotNull(USD);
		EUR = Currency.get(this.getContext(), "EUR");
		assertNotNull(EUR);
		
		conversionRate = ExchangeRate.getDerivedRate(getContext(), "USD", "EUR", conversionDate);
		assertNotNull(conversionRate);
		
		target = new TargetDef.TargetDefConstant();
	}

	public void testNumericConstantTarget() {
		target.setNumericValue(42.);
		
		TargetValue value = target.evaluate(null, null);
		assertEquals(42., value.getValue());
	}
	
	public void testMonetaryConstantTarget() {
		target.setMonetaryValue(42., USD, null);
		
		TargetValue value = target.evaluate(user, USD);
		assertEquals(42., value.getValue());
	}
	
	public void testMonetaryConstantTargetWithConversion() {
		target.setMonetaryValue(42., USD, null);
		
		TargetValue value = target.evaluate(user, EUR);
		assertEquals(42. * conversionRate.getRate(), value.getValue());
	}
}
