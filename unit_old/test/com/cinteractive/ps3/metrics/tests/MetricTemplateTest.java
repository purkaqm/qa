package com.cinteractive.ps3.metrics.tests;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.tags.DuplicateNameException;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.ps3.types.PSType;

/** Test class of metric templates.*/
public class MetricTemplateTest extends PSTestBase {
    
    private String name = "AUTO_GEN_METRICS_TAG";
    private String metricName = "AUTO_GEN_METRICS_TEST";
    
    public MetricTemplateTest(String name) { super(name); }
    
    private void cleanupTestTag(String name) {
        TagSet tagSet = getContext().getTagSetScope().get(name);
        tagSet.delete();
    }
    
    private TagSet createTagset() {
        TagSet tagSet = null;
        try {
            tagSet = TagSet.createNew(name, getContext());
        } catch (DuplicateNameException ex) {
            cleanupTestTag(name);
            tagSet = TagSet.createNew(name, getContext());
        }
        
        tagSet.setName(name);
        tagSet.setDescription("some descr");
        tagSet.setAllowMultipleLinks(true);
        tagSet.setIsHierarchical(true);
        tagSet.setEnableAlerts(true);
        tagSet.setIsMandatory(false);
        tagSet.setLocked(false);
        tagSet.setApplyPermissions(true);

        // creation
        tagSet.save();
        
        return tagSet;
    }
    
    private void cleanup() {
        try {
            TagSet tagSet = getContext().getTagSetScope().get(name);
            tagSet.delete();
        } catch(Exception ex) {
        }
        
        try {
            List list = MetricTemplate.getByName(MetricTemplate.TYPE, metricName, false, null, null, getContext());
            if (list != null && !list.isEmpty() ) {
                MetricTemplate template = (MetricTemplate)list.get(0); 
                template.deleteHard();
            }
        } catch(Exception ex) {
        }
    }
    
    public void testMetricCreation() {
        cleanup();
        
        TagSet tagSet = createTagset();
        final PSType type = MetricTemplate.TYPE;
        User owner = (User)User.getUserList(getContext()).get(0);

        final MetricTemplate template = MetricTemplate.createNew(type, metricName, owner);
        //template.setModifiedById(owner.getId());
        template.setBreakdownId(tagSet.getId());
        template.save();
        
        Map tags = new HashMap();
        tags.put(tagSet.getId(), tagSet);
        List metrics = MetricTemplate.getMatricsByBreakdownTag(getContext(), tagSet.getId());
        assertTrue(metrics.size() > 0);
        assertEquals(metrics.get(0), template);
        
        // could't delete tagset just now
        boolean thrown = true;
        try {tagSet.delete(); thrown = false; } catch (Exception ex) { }
        assertTrue(thrown);
        
        template.deleteHard();
        tagSet.delete();
    }
}
