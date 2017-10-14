package ps5.psapi.fields;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ps5.psapi.fields.fields.Field;
import ps5.psapi.fields.fields.FieldCF;
import ps5.psapi.fields.fields.FieldGroup;
import ps5.psapi.fields.fields.FieldList;
import ps5.psapi.fields.fields.FieldRegistry;
import ps5.psapi.fields.fields.FieldTagSet;
import ps5.psapi.fields.fields.GeneralField;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.agents.Agent;
import com.cinteractive.ps3.mockups.CustomField;
import com.cinteractive.ps3.mockups.CustomFieldUniverseEditor;
import com.cinteractive.ps3.mockups.MockupHolder;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.util.Log;

/**
 * Tests for CustomFields functionality
 */

public class CustomFieldTest extends PSTestBase {
    private Log log = new Log(CustomFieldTest.class);
    private static int testId = 0;

    public CustomFieldTest(String name) {
        super(name);
    }

    protected void setUp() {
        super.setUp();
        log.info(String.format("-------------------- %s (%d) --------------------", getName(), ++testId));
    }
   
    private void display(FieldLayout layout) {
        System.out.println("-----------------------------------------");
        display(layout.getRoot(), 0);
        System.out.println("-----------------------------------------");
    }
    
    private void display(Field field,int level) {
        String s = "                                                                                                                                                                        ";

        
        System.out.println(String.format("%s%s\t%s\t%s\t%d", s.substring(0, level * 3),field, field.getId(), field.getSrcId(), ((Field)field).getSequence()));
        if (field instanceof FieldTagSet) {
         //   System.out.println(getContext().getTagSet(field.getSrcId()));
            //if (((FieldTagSet)field).isInTagDependency()) {
            //    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            //}
        }
        if (field instanceof FieldList) {
            for(Field f : ((FieldList)field).getFields()) {
                display(f, level + 1);
                
            }
        }
    }
    
    public void testAttributes() {
        FieldLayout layout = getDefaultLayout(Agent.TYPE);
        Builder builder = Builder.create(layout);
        FieldCF field = (FieldCF)builder.add(layout.getRoot().getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        field.getAttributes().setRequired(true);
        builder.build();
        layout.save();
        
        FieldLayoutUniverse univ2 = forciblyRestore();
        FieldLayout layout2 = univ2.getLayout(layout.getType());
        compare(layout, layout2);
        
        field.setLocked(true);
        field.setRequired(false);
        
        layout.save();
        
        univ2 = forciblyRestore();
        layout2 = univ2.getLayout(layout.getType());
        compare(layout, layout2);

        field.getAttributes().setHardDeleted();
        layout.save();
        
        
        field.getAttributes().setLocked(false);
        univ2 = forciblyRestore();
        layout2 = univ2.getLayout(layout.getType());
        compare(layout, layout2);
    }
    
    public void testCFLoading() {
        //getContext().generateFieldLayoutUniverse();
        CustomFieldUniverse univ = getContext().getCustomFieldsUniverse();
        Set<CustomField> fields = univ.getCustomFields(Work.TYPE);
        for(CustomField f : fields) {
            System.out.println(f.getName());
        }
    }
    
    private void removeFields(PSType type) {
        CustomFieldUniverse univ = getContext().getCustomFieldsUniverse();
        CustomFieldUniverseEditor editor = univ.getEditor();
        Set<CustomField> fields = univ.getCustomFields(type);
        for(CustomField f : fields) {
            System.out.println("1: " + f.getName());
            editor.disassociatePSTypes(f.getId(), f.getRelatedTypes().getPSTypes());
        }
        editor.save();
        
        fields = univ.getCustomFields(type);
        assertTrue(fields == null || fields.isEmpty());
    }
    
    public void testCFLoading2() {
        
        CustomFieldUniverse univ = getContext().getCustomFieldsUniverse();
        //-------------------------------------------------------
        
        //univ.createDefaultMockup();
        
        //-------------------------------------------------------
        
        getContext().getFieldLayoutUniverse().save();
        
        removeFields(Agent.TYPE);
        CustomFieldUniverseEditor editor = univ.getEditor();
        Set<CustomField> fields = null;
       
        String name = "TEST_NAME" + System.currentTimeMillis();
        PersistentKey id = editor.createCustomField(CustomField.BOOLEAN_CODE);
        editor.setNameAndDescription(id, name, "");
        
        editor.associatePSTypes(id, (Collection)Collections.singletonList((Agent.TYPE)));
        editor.associatePSTypes(id, (Collection)Collections.singletonList((Work.TYPE)));
        fields = univ.getCustomFields(Agent.TYPE);
        assertTrue(fields == null || fields.isEmpty());
        
        editor.save();
        
        fields = univ.getCustomFields(Agent.TYPE);
        
        assertEquals(1, fields.size());
        assertEquals(name, new ArrayList<CustomField>(fields).get(0).getName());
        
        editor.disassociatePSTypes(id, (Collection)Collections.singletonList((Agent.TYPE)));
        fields = univ.getCustomFields(Agent.TYPE);
        assertEquals(1, fields.size());
        
        editor.save();
        fields = univ.getCustomFields(Agent.TYPE);
        assertEquals(0, fields.size());
        
    }
    
    public void testCreation() {
        FieldLayoutUniverse univ = getContext().getFieldLayoutUniverse();
        if (univ.getLayout(Work.TYPE) == null) {
            univ = FieldLayoutUniverse.createFromTagsAndMockups(null, getContext());
            univ.save();
        }
        display(univ.getLayout(Work.TYPE));
    }
   
    public void testSeq() {
        FieldLayout layout = getDefaultLayout(Agent.TYPE);
        layout.save();
        Builder builder = Builder.create(layout);
        Field group = builder.add(layout.getRoot().getId(), builder.createNew(FieldRegistry.FIELD_GROUP, null));
        ((Field)group).setSequence(10);
        builder.add(group.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        
        Field group2 = builder.add(layout.getRoot().getId(), builder.createNew(FieldRegistry.FIELD_GROUP, null));
        builder.add(group2.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group2.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group2.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        builder.add(group2.getId(), builder.createNew(FieldRegistry.FIELD_CF, Uuid.create()));
        
        builder.build();
        display(layout);
    }
    
    private FieldLayout getDefaultLayout(PSType type) {
        FieldLayoutUniverse univ = getContext().getFieldLayoutUniverse();
        FieldLayout layout = univ.getLayout(type);
        if (layout == null) {
            layout = FieldLayout.createNew(getContext(), type, null);
        } else {
            display(layout);
            layout.deleteLayout();
            layout.save();
            
            layout = FieldLayout.createNew(getContext(), layout.getType(), null);
            layout.save();
            
            FieldLayoutUniverse univ2 = forciblyRestore();
            FieldLayout layout2 = univ2.getLayout(type);
            compare(layout, layout2);
            
        }
        return layout;
    }
    
    public void testUpdate() {
        FieldLayout layout = getDefaultLayout(Agent.TYPE);
        Builder builder = Builder.create(layout);
        List<Field> fields = new ArrayList<Field>();
        for(int i = 0; i <10; ++i) {
            Field field = builder.createNew(FieldRegistry.FIELD_CF, Uuid.create());
            fields.add(field);
            field.setTitle("" + i, getContext().getDefaultLocale());
        }
        FieldGroup group1 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        FieldGroup group2 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
       
        builder.add(layout.getRoot().getId(), fields.get(0));
        builder.add(layout.getRoot().getId(), group1);
        builder.add(layout.getRoot().getId(), fields.get(1));
        builder.add(layout.getRoot().getId(), group2);
        
        builder.add(group1.getId(), fields.get(2));
        builder.add(group1.getId(), fields.get(3));

        builder.add(group2.getId(), fields.get(4));
        builder.add(group2.getId(), fields.get(5));
        
        builder.build();
        display(layout);
        layout.save();
        
        FieldLayoutUniverse univ2 = forciblyRestore();
        FieldLayout layout2 = univ2.getLayout(Agent.TYPE);
        display(layout2);
        compare(layout, layout2);
        
        display(layout);
        layout.deleteGroup(group1.getId());
        layout.save();
        display(layout);
        assertEquals(5, layout.getRoot().getSize());
        assertEquals(layout.getRoot().get(1), fields.get(2));
        assertEquals(layout.getRoot().get(2), fields.get(3));
        
        layout.deleteGroup(group2.getId());
        layout.save();
        display(layout);
        assertEquals(layout.getRoot().get(4), fields.get(4));
        assertEquals(layout.getRoot().get(5), fields.get(5));
        
        univ2 = forciblyRestore();
        layout2 = univ2.getLayout(Agent.TYPE);
        display(layout2);
        compare(layout, layout2);
        
        //--------------------------------------------------------
        
        Map<PersistentKey, List<PersistentKey>> hierarchy = new LinkedHashMap<PersistentKey, List<PersistentKey>>();
        List<PersistentKey> l = new ArrayList<PersistentKey>();
        for(int i = 5; i >= 0; --i) {
            l.add(fields.get(i).getId());
        }
        hierarchy.put(layout.getRoot().getId(), l);
        layout.updateHierarchy(hierarchy);
        layout.save();
        
        for(int i = 0; i < 6; ++i) {
            assertEquals(layout.getRoot().get(i), fields.get(5-i));
        }
        
        univ2 = forciblyRestore();
        layout2 = univ2.getLayout(Agent.TYPE);
        compare(layout, layout2);
        
        //------------------------------------------------------
        
        builder = Builder.create(layout);
        FieldGroup group3 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        FieldGroup group4 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        builder.add(layout.getRoot().getId(), group3);
        builder.add(layout.getRoot().getId(), group4);
        builder.build();
        
        hierarchy.clear();
        l.clear();
        l.add(group3.getId());
        for(int i = 1; i < 6; ++i) {
            l.add(fields.get(i).getId());
        }
        
        List<PersistentKey> l2 = new ArrayList<PersistentKey>();
        l2.add(group4.getId());
        
        List<PersistentKey> l3 = new ArrayList<PersistentKey>();
        l3.add(fields.get(0).getId());
        
        hierarchy.put(group3.getId(), l2);
        hierarchy.put(layout.getRoot().getId(), l);
        hierarchy.put(group4.getId(), l3);
        
        layout.updateHierarchy(hierarchy);
        layout.save();
        display(layout);
        
        for(int i = 1; i < 6; ++i) {
            assertEquals(layout.getRoot().get(i), fields.get(i));
        }
        
        assertEquals(layout.getRoot().get(0), group3);
        assertEquals(group3.get(0), group4);
        assertEquals(group4.get(0), fields.get(0));
        assertEquals(6, layout.getRoot().getSize());
        
        univ2 = forciblyRestore();
        layout2 = univ2.getLayout(Agent.TYPE);
        display(layout2);
        compare(layout, layout2);
    }
    
    public void testCopyLayout() {
        PSType type = Agent.TYPE;
        FieldLayout layout = getDefaultLayout(type);
        
        FieldLayout layout2 = layout.copy();
        compare(layout, layout2);
        assertEquals(1, layout2.getAllFields().size());
        
        Builder builder = Builder.create(layout);
        FieldGroup group1 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        FieldGroup group2 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        builder.add(layout.getRoot().getId(), group1);
        builder.add(layout.getRoot().getId(), group2);
        builder.build();
        display(layout);
        layout.save();
        FieldLayoutUniverse univ = forciblyRestore();
        FieldLayout storedLayout = univ.getLayout(Agent.TYPE);
        
        FieldLayout layout3 = layout.copy();
        compare(layout, layout3);
        compare(layout, storedLayout);
        assertEquals(1, layout2.getAllFields().size());
        assertEquals(3, layout3.getAllFields().size());
        assertEquals(layout, getContext().getFieldLayoutUniverse().getLayout(type));
        
        builder = Builder.create(layout3);
        FieldGroup group3 = (FieldGroup)builder.createNew(FieldRegistry.FIELD_GROUP, null);
        builder.add(layout3.getRoot().getId(), group3);
        builder.build();
        compare(storedLayout, layout);
       
        getContext().getFieldLayoutUniverse().replace(layout3);
        assertEquals(layout3, getContext().getFieldLayoutUniverse().getLayout(type));
        layout3.save();
        
        univ = forciblyRestore();
        storedLayout = univ.getLayout(Agent.TYPE);
        compare(layout3, storedLayout);
        
    }
    
    private void compare(FieldLayout layout1, FieldLayout layout2) {
        compareLayouts(layout1, layout2);
        compareLayouts(layout2, layout1);
    }
    
    private void compareLayouts(FieldLayout layout1, FieldLayout layout2) {
        for(Field f : layout1.getAllFields()) {
            Field field = (Field)f;
            Field field2 = (Field)layout2.getById(field.getId());
            
            assertTrue(field2 != null);
            assertEquals(field.getId(), field2.getId());
            assertEquals(field.getSrcId(), field2.getSrcId());
            assertTrue(field.getParentId() == null || field.getSequence() != null);
            assertEquals(field.getSrcId(), field2.getSrcId());
            assertEquals(field.getParentId(), field2.getParentId());
            assertEquals(field.getTitle(getContext().getDefaultLocale()), field.getTitle(getContext().getDefaultLocale()));
            
            if (f instanceof FieldList) {
                FieldList l1 = (FieldList)f;
                FieldList l2 = (FieldList)field2;
                assertEquals(l1.getSize(), l2.getSize());
                for(int i = 0; i < l1.getSize(); ++i) {
                    assertEquals(l1.get(i), l2.get(i));
                }
            }
            
            if (f instanceof GeneralField) {
                GeneralField f1 = (GeneralField)f;
                GeneralField f2 = (GeneralField)field2;
                assertEquals(f1.isRequired(), f2.isRequired());
                assertEquals(f1.isLocked(), f2.isLocked());
                assertEquals(f1.getRules().getRule(), f2.getRules().getRule());
            }
        }
    }
    
    private FieldLayoutUniverse forciblyRestore() {
        Connection conn = null;
        try {
            conn = getContext().getLongConnection();
            FieldLayoutUniverse univ = FieldLayoutUniverse.create(getContext());
            univ.restore(conn);
            return univ;
        } catch (Exception ex) {
            assertTrue(false);
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                assertTrue(false);
            }
        }
        return null;
    }
    
    private CustomFieldUniverse loadCustomFieldUniverse() {
        Connection conn = null;
        try {
            MockupHolder newMockup = new MockupHolder(getContext());
            conn = getContext().getLongConnection();
            newMockup.restore(conn);
            CustomFieldUniverse univ = null;//CustomFieldUniverse.createFromMockups(getContext());
            //univ.restore(conn);
            return univ;
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                assertTrue(ex.getMessage(), false);
            }
        }
        return null;
    }
    
}
