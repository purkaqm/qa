package com.powersteeringsoftware.core.objects.measures;

import org.testng.Assert;
import com.powersteeringsoftware.core.objects.measures.collection_method.AbstractCollectionMethod;
import com.powersteeringsoftware.core.objects.measures.collection_method.DisplayFormat;
import com.powersteeringsoftware.core.objects.measures.collection_method.EffectiveDates;
import com.powersteeringsoftware.core.objects.measures.collection_method.FormulaCollectionMethod;
import com.powersteeringsoftware.core.objects.measures.collection_method.ManualCollectionMethod;
import com.powersteeringsoftware.core.objects.measures.indicator_type.GoalIndicatorType;
import com.powersteeringsoftware.core.objects.measures.indicator_type.IIndicatorType;


public class AbstractMeasure {
	private MeasureDetails measureDetails;
	private AbstractCollectionMethod collectionMetod;
	private IIndicatorType indicatorType;

	public AbstractMeasure(String _name, String _description, String _unit){
		measureDetails = new MeasureDetails(_name, _description, _unit);
		collectionMetod = new ManualCollectionMethod();
		indicatorType = new GoalIndicatorType();
	}

	public MeasureDetails getMeasureDetails() {
		return measureDetails;
	}

	public void setMeasureDetails(MeasureDetails details) {
		measureDetails = details;
	}

	public AbstractCollectionMethod getCollectionMetod() {
		return collectionMetod;
	}

	public void setCollectionMetod(AbstractCollectionMethod collectionMetod) {
		this.collectionMetod = collectionMetod;
	}

	public IIndicatorType getIndicatorType() {
		return indicatorType;
	}

	public void setIndicatorType(IIndicatorType indicatorType) {
		this.indicatorType = indicatorType;
	}


	public void setName(String name){
		measureDetails.setName(name);
	}

	public void setDescription(String description){
		measureDetails.setDescription(description);
	}

	public void setUnits(String unitsOfMeasure){
		measureDetails.setUnitsOfMeasure(unitsOfMeasure);
	}

	public void setDisplayFormat(DisplayFormat displayformat){
		collectionMetod.setDisplayFormat(displayformat);
	}

	public void setEffectiveDates(EffectiveDates effectiveDates){
		collectionMetod.setEffectiveDates(effectiveDates);
	}

	public boolean isManual(){
		return (collectionMetod instanceof ManualCollectionMethod);
	}

	public boolean isFormula(){
		return (collectionMetod instanceof FormulaCollectionMethod);
	}

	public void assertIsManual(){
		Assert.assertTrue(isManual());
	}

	public void assertIsFormula(){
		Assert.assertTrue(isFormula());
	}



}
