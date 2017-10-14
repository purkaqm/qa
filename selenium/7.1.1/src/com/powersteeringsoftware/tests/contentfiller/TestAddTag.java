package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.core.managers.TagManager;
import com.powersteeringsoftware.core.objects.Tag;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.TimeStampName;

/**
 * Add tag
 *
 */
public class TestAddTag extends PSTest {
	public TestAddTag(){
		name = "Add Tag";
	}

	public void run(){
		addTagPlanet();
		addTagLocation();
	}

	private void addTagPlanet(){
		Tag tag = new Tag(new TimeStampName("Planet").getTimeStampedName(),"This should be a non-hierarchical tag.");
		tag.addTypeOfPeople("Users");
		tag.addTypeOfWorks("Work Items");
		tag.addTypeOfWorks("Gated Projects");
		tag.addTagValue("Earth");
		tag.addTagValue("Mercury");
		tag.addTagValue("Venus");
		tag.addTagValue("Jupiter");
		tag.addTagValue("Mars");
		tag.addTagValue("Saturn");

		new TagManager().addTag(tag);
	}

	private void addTagLocation() {
		Tag tag = new Tag(new TimeStampName("Location").getTimeStampedName(),"This should be a hierarchical, multi-select tag.");
		tag.addTypeOfPeople("Users");
		tag.addTypeOfWorks("Work Items");
		tag.addTypeOfWorks("Gated Projects");
		tag.setPropMultiple(true);
		tag.setPropHierarchical(true);
		tag.addTagValue("North America");
		tag.addTagValue("United States", "North America");
		tag.addTagValue("Massachusetts", "United States");
		tag.addTagValue("Boston", "Massachusetts");
		tag.addTagValue("Asia");
		tag.addTagValue("Russia","Asia");
		tag.addTagValue("Moscow","Russia");
		tag.addTagValue("Australia");
		tag.addTagValue("Sydney","Australia");
		tag.addTagValue("Melbourne","Australia");
		tag.addTagValue("Perth","Australia");

		new TagManager().addTag(tag);
	}
}
