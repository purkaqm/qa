package com.cinteractive.ps3.tags.tests;

/* Copyright © 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.util.List;

import com.cinteractive.ps3.contexts.ContextTypes;
import com.cinteractive.ps3.tags.DuplicateNameException;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.util.AssertionException;

/** Test case of tags management.*/
public class TagsManagementTests extends PSTestBase {
    
    private String TAG_PREFIX = "AUTO_GEN_";
    private String getTagName(int i) {
        return String.format("%s%04d", TAG_PREFIX, i);
    }
    
    public TagsManagementTests(String name) { super(name); }
    
    private List<TagSet> getTagSets() {
        return getContext().listTagSets(null);
    }
    
    private void cleanupTestTag(String name) {
        TagSet tagSet = getContext().getTagSetScope().get(name);
        tagSet.delete();
    }
    
    public void testCRUD() {
        TagSet tagSet = null;
        try {
            tagSet = TagSet.createNew(getTagName(0), getContext());
        } catch (DuplicateNameException ex) {
            cleanupTestTag(getTagName(0));
            tagSet = TagSet.createNew(getTagName(0), getContext());
        }
        
        tagSet.setName(getTagName(0));
        tagSet.setDescription("some descr");
        tagSet.setAllowMultipleLinks(true);
        tagSet.setIsHierarchical(true);
        tagSet.setEnableAlerts(true);
        tagSet.setIsMandatory(false);
        tagSet.setLocked(false);
        tagSet.setApplyPermissions(true);

        // creation
        tagSet.save();
                
        TagSet saved = getContext().getTagSet(tagSet.getId());
        assertEquals(saved, tagSet);
        
        PSTag tag1 = tagSet.addTag(getTagName(1));
        tag1.setDisplaySequence(new Integer(10));
        tagSet.save();
        
        PSTag tag2 = tagSet.addTag(getTagName(2));
        tag2.setDisplaySequence(new Integer(5));
        tag2.setParent(tag1);
        tagSet.save();

        getContext().getTagSetScope().reload(tagSet.getId());
       
        // deletion
        tagSet.delete();
        
        assertNotNull(tagSet.getId());
        assertNull(getContext().getTagSet(tagSet.getId()));
        
        boolean thrown = true;
        try {getContext().getTagSetScope().reload(tagSet.getId()); thrown = false; } catch (Exception ex) { }
        assertTrue(thrown);
    }
}
