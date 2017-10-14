package ps5.psapi.transformations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ps5.psapi.transformations.TypeOfWork.WorkTypeWrapper;
import ps5.psapi.transformations.beans.TransformationWorksCreatedBean;
import ps5.psapi.transformations.eml.Eml;
import ps5.psapi.transformations.eml.Eml.EmlType;
import ps5.psapi.transformations.eml.Eml.Kind;
import ps5.psapi.transformations.eml.TransformationUserDataBean;
import ps5.psapi.transformations.eml.WorkBuilder;
import ps5.psapi.transformations.misc.TransformationResult;
import ps5.psapi.transformations.misc.TransformationWorksCreated;
import ps5.psapi.transformations.misc.UpToDateBuilder;
import ps5.support.PSException;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.ps3.work.Template;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.util.GroupedIndexedSet;
import com.cinteractive.util.Log;

/**
 * Tests for Transformations
 */
public class TransformationsTests extends PSTestBase {
    
	public TransformationsTests(String testName) {
		super(testName);
		// TODO Auto-generated constructor stub
	}

	private Log log = new Log(TransformationsTests.class);
    private static int testId = 0;



    protected void setUp() {
        super.setUp();
    }
    
    public void testTransformationWorkCreated() {
		TransformationDefinition def =TransformationDefinition.createNew(getContext());
    	Work destination1 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	Work destination2 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	destination1.setTransformationDefinition(def);
    	destination1.setTransformationSource(destination2);
		destination1.save();
		destination2.save();
		try{
			TransformationWorksCreated w = new TransformationWorksCreated(def);
			assertTrue(w.hasMoreElements());
			TransformationWorksCreatedBean bean = w.nextElement();
			assertTrue(Nobody.get(getContext()).getId().equals(bean.getCreatedBy()));
			assertTrue(destination1.getId().equals(bean.getCreatedWorkId()));
			assertTrue(destination2.getId().equals(bean.getSourceWorkId()));
			assertTrue(destination1.getCreateDate().getTime() == bean.getCreationDate().getTime());
			
		} catch (Exception e) {
			assertTrue(new PSException(e).toString(), false);
		} finally {
			try {
				if (destination1 != null) {
					destination1.setHardDeleted();
					destination1.save();
				}
			} catch (Exception ignored) {
				
			}
			try {
				if (destination2 != null) {
					destination2.setHardDeleted();
					destination2.save();
				}
			} catch (Exception ignored) {
				
			}
		}
    }
    
    public void testParametrizedWorkCreation() {
		boolean result = true;
		// set the id of work where all necessary objects are configured.
		PersistentKey sourceId = null;//Uuid.get("o2k228g0000fsah5ljfg000000");
		if (sourceId != null) {
			Work destination1 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
			destination1.save();
			Work source = (Work) PSObject.get(sourceId, getContext());
			try{
				TransformationDefinitions definitions = new TransformationDefinitions(getContext()); 
				TransformationDefinition def =TransformationDefinition.createNew(getContext());
				def.setSourceType(TypeOfWork.get(source));
				def.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper(destination1));
				def.getDetails().setName("test", Locale.getDefault());
				TransformationChain chain = def.getTransformationChain();
				Eml e = null;
				e = EmlType.CAN_USER_DO.createNewEml(getContext());
				e.setKind(Kind.YES);
				chain.addPersistent(e);
				e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
				e.setKind(Kind.YES);
				chain.addPersistent(e);
				e = EmlType.CAN_USE_DESTINATION.createNewEml(getContext());
				e.setKind(Kind.CONDITIONAL);
				chain.addPersistent(e);
				e = EmlType.DESTINATION.createNewEml(getContext());
				e.setKind(Kind.PROVIDED_BY_USER_AT_RUNTIME);
				chain.addPersistent(e);
				e = EmlType.WORK_NAME.createNewEml(getContext());
				e.setKind(Kind.PROVIDED_BY_USER_AT_RUNTIME);
				chain.addPersistent(e);
				e = EmlType.CURRENCY.createNewEml(getContext());
				e.setKind(Kind.SAME_AS_SOURCE);
				chain.addPersistent(e);
				
				UpToDateBuilder upToDate  = new UpToDateBuilder(def, Nobody.get(getContext()));
				upToDate.build();
				for (Eml eml : chain.getByEmlType(EmlType.ROLE)) {
					eml.setKind(Kind.SAME_AS_SOURCE);
		    	}
				for (Eml eml : chain.getByEmlType(EmlType.TAG)) {
					eml.setKind(Kind.SAME_AS_SOURCE);
		    	}
				for (Eml eml : chain.getByEmlType(EmlType.CUSTOM_FIELD)) {
					eml.setKind(Kind.SAME_AS_SOURCE);
		    	}
				for (Eml eml : chain.getByEmlType(EmlType.METRIC_TEMPLATE)) {
					eml.setKind(Kind.SAME_AS_SOURCE);
		    	}
	
				
				
				TransformationUserDataBean bean = new TransformationUserDataBean();
				String name = "trolololo";
				bean.setWorkName(name);
				bean.setSourceWorkId(source.getId());
				Set<PersistentKey> destinations = new HashSet<PersistentKey>();
				destinations.add(destination1.getId());
				
				bean.setDestinations(destinations);
				WorkBuilder builder = new WorkBuilder(def, Nobody.get(getContext()));
				builder.setUserBean(bean);
				TransformationResult res = builder.createWorks();
				assertTrue(true);
			} catch (Exception e) {
				assertTrue(new PSException(e).toString(), false);
			} finally {
				try {
					if (destination1 != null) {
						destination1.setHardDeleted();
						destination1.save();
					}
				} catch (Exception ignored) {
					
				}
			}
		}
    }

    public void testTemplatedWorkCreation () {
    	List<Template> keys = Template.getRootTemplates(getContext(), null);
    	for(Template template : keys) {
    		if (template.isDeleted()) {
    			continue;
    		}
    		WorkTypeWrapper type = TypeOfWork.TEMPLATE_BASED.getWrapper(template.getId().toString());
    		assertTrue(type.createWork("d",Nobody.get(getContext())) != null);
    		break;
    	}
    }
    
    public void testWorkCreation () {
		WorkTypeWrapper type = TypeOfWork.PS_TYPE_BASED.getWrapper("Work");
		assertTrue(type.createWork("d",Nobody.get(getContext())) != null);
    }
    
    public void testClone() {
    	TransformationDefinition t = TransformationDefinition.createNew(getContext());
    	t.getDetails().setName("1", getContext().getDefaultLocale());
    	t.getDetails().setComment("11", getContext().getDefaultLocale());
    	t.getDetails().setName("22", Locale.ITALIAN);
    	t.getDetails().setComment("2", Locale.ITALIAN);
    	t.setSourceType(TypeOfWork.PS_TYPE_BASED.getWrapper("Work"));
    	t.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper("Work"));
    	UpToDateBuilder builder = new UpToDateBuilder(t, Nobody.get(getContext()));
    	for (Eml eml : builder.getValidRoleEmls()) {
    		eml.setKind(Kind.EVALUATE_EML);
    		t.getTransformationChain().addPersistent(eml);
    	}
    	try {
			TransformationDefinition clonned = builder.buildUpToDateClone();
			
			t.getDetails().setName("4", getContext().getDefaultLocale());
	    	t.getDetails().setComment("5", Locale.ITALIAN);
	    	t.getSourceType().setKey("Tollgate");
	    	t.getTargetType().setKey("Tollgate");
	    	for (Eml eml : builder.getValidRoleEmls()) {
	    		eml.setKind(Kind.SAME_AS_SOURCE);
	    	}
	    	assertTrue("1".equals(clonned.getDetails().getName(getContext().getDefaultLocale())));
	    	assertTrue("2".equals(clonned.getDetails().getComment(Locale.ITALIAN)));
	    	assertTrue("Work".equals(clonned.getSourceType().getKey()));
	    	assertTrue("Work".equals(clonned.getTargetType().getKey()));
	    	for (Eml eml : clonned.getTransformationChain().getByEmlType(EmlType.ROLE)) {
	    		assertTrue(Kind.EVALUATE_EML.equals(eml.getKind()));
	    	}
		} catch (CloneNotSupportedException e) {
			assertTrue(false);
		}
    }
    
    public void testUpToDateBuilder() {
    	TransformationDefinition t = TransformationDefinition.createNew(getContext());
    	t.setSourceType(TypeOfWork.PS_TYPE_BASED.getWrapper("Work"));
    	t.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper("Work"));
    	UpToDateBuilder builder = new UpToDateBuilder(t, Nobody.get(getContext()));
    	assertTrue(builder.isOutOfDate());
    	assertFalse(builder.isValid());
    	for (Eml eml : builder.getValidRoleEmls()) {
    		eml.setKind(Kind.EVALUATE_EML);
    		t.getTransformationChain().addPersistent(eml);
    	}
    	builder.build();
    	for (Eml eml : t.getTransformationChain().getByEmlType(EmlType.ROLE) ){
    		assertTrue(Kind.EVALUATE_EML.equals(eml.getKind()));
    	}
    	assertFalse(builder.isOutOfDate());
    	assertTrue(builder.isValid());
    	
    }
    
    public void testGroupableCollection() {
    	
    	WorkTypeWrapper wrapper1 = TypeOfWork.PS_TYPE_BASED.getWrapper("1");
    	WorkTypeWrapper wrapper2 = TypeOfWork.TEMPLATE_BASED.getWrapper("2");
    	WorkTypeWrapper wrapper3 = TypeOfWork.PS_TYPE_BASED.getWrapper("3");
    	
    	
    	
    	TransformationDefinition d1 = TransformationDefinition.createNew(getContext());
    	d1.setSourceType(wrapper1);
    	TransformationDefinition d2 = TransformationDefinition.createNew(getContext());
    	d2.setSourceType(wrapper2);
    	TransformationDefinition d3 = TransformationDefinition.createNew(getContext());
    	d3.setSourceType(wrapper1);
    	TransformationDefinition d4 = TransformationDefinition.createNew(getContext());
    	d4.setSourceType(wrapper3);
    	TransformationDefinition d5 = TransformationDefinition.createNew(getContext());
    	d5.setSourceType(wrapper2);
    	TransformationDefinition d6 = TransformationDefinition.createNew(getContext());
    	d6.setSourceType(wrapper2);
    	TransformationDefinition d7 = TransformationDefinition.createNew(getContext());
    	d7.setSourceType(wrapper3);
    	
    	GroupedIndexedSet set = new GroupedIndexedSet(); 
    	set.add(d7);
    	set.add(d6);
    	set.add(d5);
    	set.add(d4);
    	set.add(d2);
    	set.add(d3);
    	set.add(d1);
    	assertTrue("[1, 1, 3, 3, 2, 2, 2]".equals(set.toString()));
    	assertTrue("[1, 3, 2]".equals(set.getGroupingKeys().toString()));
    	assertTrue("[1, 1]".equals(set.getByGroupingId(wrapper1).toString()));
    	assertTrue("[2, 2, 2]".equals(set.getByGroupingId(wrapper2).toString()));
    	assertTrue("[3, 3]".equals(set.getByGroupingId(wrapper3).toString()));
    	set.remove(d2);
    	assertTrue("[1, 1, 3, 3, 2, 2]".equals(set.toString()));
    	assertTrue(set.size() == 6);
    	set.removeByGroupingId(wrapper3);
    	assertTrue("[1, 1, 2, 2]".equals(set.toString()));
    	assertTrue(set.size() == 4);
    }
    
    public void testCanDoTransformationResult() {
    	boolean result = true;
    	Work destination1 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	destination1.save();
    	Work source = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	source.save();
    	try{
			TransformationDefinitions definitions = new TransformationDefinitions(getContext()); 
			TransformationDefinition def =TransformationDefinition.createNew(getContext());
			def.setSourceType(TypeOfWork.PS_TYPE_BASED.getWrapper(source));
			def.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper(destination1));
			def.getDetails().setName("test", Locale.getDefault());
			TransformationChain chain = def.getTransformationChain();
			
			Eml e = null;
			e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
			e.setKind(Kind.CONDITIONAL);
				
			TransformationUserDataBean bean = new TransformationUserDataBean();
			String name = "trolololo";
			bean.setWorkName(name);
			bean.setSourceWorkId(source.getId());
			Set<PersistentKey> destinations = new HashSet<PersistentKey>();
			destinations.add(destination1.getId());
			bean.setDestinations(destinations);
			
			WorkBuilder builder = new WorkBuilder(def, Nobody.get(getContext()));
			builder.setUserBean(bean);
			
			TransformationResult res = builder.createWorks();
			
    	} catch (Exception e) {
    		assertTrue(new PSException(e).toString(), false);
    	} finally {
    		try {
    			if (destination1 != null) {
    				destination1.setHardDeleted();
    				destination1.save();
    			}
			} catch (Exception ignored) {
				
			}
			try {
    			if (source != null) {
    				source.setHardDeleted();
    				source.save();
    			}
			} catch (Exception ignored) {
				
			}
    	}
    	assertTrue(result);
    }
    
    public void testEmlByTypeCycle() {
    	TransformationDefinition def =TransformationDefinition.createNew(getContext());
		TransformationChain chain = def.getTransformationChain();
		Eml e = null;
		e = EmlType.CAN_USER_DO.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_USER_DO.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_USER_DO.createNewEml(getContext());
		chain.addPersistent(e);

		e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
		chain.addPersistent(e);
		
		e = EmlType.CAN_USE_DESTINATION.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_USE_DESTINATION.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.CAN_USE_DESTINATION.createNewEml(getContext());
		chain.addPersistent(e);
		
		e = EmlType.WORK_NAME.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.WORK_NAME.createNewEml(getContext());
		chain.addPersistent(e);
		e = EmlType.WORK_NAME.createNewEml(getContext());
		chain.addPersistent(e);

		Set<Eml> eml = chain.getByEmlType(EmlType.CREATE_NEW_WORK);
		assertTrue("Wrong eml indexing", eml != null && eml.size() == 1 && eml.iterator().next().getEmlType().equals(EmlType.CREATE_NEW_WORK));
		eml = chain.getByEmlType(EmlType.CAN_DO_TRANSFORMATION);
		assertTrue("Wrong eml indexing", eml != null && eml.size() == 3  && eml.iterator().next().getEmlType().equals(EmlType.CAN_DO_TRANSFORMATION));
		eml = chain.getByEmlType(EmlType.CAN_USE_DESTINATION_TYPE);
		assertTrue("Wrong eml indexing", eml != null && eml.size() == 1  && eml.iterator().next().getEmlType().equals(EmlType.CAN_USE_DESTINATION_TYPE));
		eml = chain.getByEmlType(EmlType.WORK_NAME);
		assertTrue("Wrong eml indexing", eml != null && eml.size() == 3 && eml.iterator().next().getEmlType().equals(EmlType.WORK_NAME));
		
    }
    
    public void testEmlCycle() {
    	boolean result = true;
    	Work destination1 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	destination1.save();
    	Work destination2 = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	destination2.save();
    	Work source = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
    	source.save();
    	try{
			TransformationDefinitions definitions = new TransformationDefinitions(getContext()); 
			TransformationDefinition def =TransformationDefinition.createNew(getContext());
			def.setSourceType(TypeOfWork.PS_TYPE_BASED.getWrapper(source));
			def.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper(destination1));
			def.getDetails().setName("test", Locale.getDefault());
			TransformationChain chain = def.getTransformationChain();
			assertFalse("Transformation chain must have non-persistable Emls ", chain.getEmls().isEmpty());
			
			Eml e = null;
			e = EmlType.CAN_USER_DO.createNewEml(getContext());
			e.setKind(Kind.YES);
			chain.addPersistent(e);
			e = EmlType.CAN_DO_TRANSFORMATION.createNewEml(getContext());
			e.setKind(Kind.YES);
			chain.addPersistent(e);
			e = EmlType.CAN_USE_DESTINATION.createNewEml(getContext());
			e.setKind(Kind.CONDITIONAL);
			chain.addPersistent(e);
			e = EmlType.DESTINATION.createNewEml(getContext());
			e.setKind(Kind.PROVIDED_BY_USER_AT_RUNTIME);
			chain.addPersistent(e);
			e = EmlType.WORK_NAME.createNewEml(getContext());
			e.setKind(Kind.PROVIDED_BY_USER_AT_RUNTIME);
			chain.addPersistent(e);
			
			Set<Eml> emls  = chain.getEmls();
			int priority = 0;
			
			for (Eml eml : emls) {
				assertTrue("Emls are not priority ordered ", eml.getEmlType().getPriority() >= priority);
				priority = eml.getEmlType().getPriority();
			}
				
			TransformationUserDataBean bean = new TransformationUserDataBean();
			String name = "trolololo";
			bean.setWorkName(name);
			bean.setSourceWorkId(source.getId());
			Set<PersistentKey> destinations = new HashSet<PersistentKey>();
			destinations.add(destination1.getId());
			destinations.add(destination2.getId());
			bean.setDestinations(destinations);
			WorkBuilder builder = new WorkBuilder(def, Nobody.get(getContext()));
			builder.setUserBean(bean);
			builder.createWorks();
    	} catch (Exception e) {
    		assertTrue(new PSException(e).toString(), false);
    	} finally {
    		try {
    			if (destination1 != null) {
    				destination1.setHardDeleted();
    				destination1.save();
    			}
			} catch (Exception ignored) {
				
			}
			try {
    			if (destination2 != null) {
    				destination2.setHardDeleted();
    				destination2.save();
    			}
			} catch (Exception ignored) {
				
			}
			try {
    			if (source != null) {
    				source.setHardDeleted();
    				source.save();
    			}
			} catch (Exception ignored) {
				
			}
    	}
    	assertTrue(result);
    }
    
    public void testCreationSavingAndDeleting() {
    	Work w = null;
    	boolean result = false;
    	try {
	    	w = Work.createNew(Work.TYPE, "test", Nobody.get(getContext()));
	    	w.save();
	    	Connection conn = null;
			try {
				TransformationDefinitions definitions = new TransformationDefinitions(getContext()); 
				TransformationDefinition def =TransformationDefinition.createNew(getContext());
				// check insert
				def.setSourceType(TypeOfWork.PS_TYPE_BASED.getWrapper(w));
				def.setTargetType(TypeOfWork.PS_TYPE_BASED.getWrapper(w));
				def.getDetails().setName("test", Locale.getDefault());
				def.setLastEdited(new Date());
				def.setLastEditorId(Nobody.get(getContext()).getId());
				UpToDateBuilder builder = new UpToDateBuilder(def, Nobody.get(getContext()));
				builder.build();
				definitions.addPersistent(def);
				// add some EMLs
				conn = getContext().getConnection();
				definitions.save(conn);
				conn.commit();
				// check update
				def.setSourceType(TypeOfWork.TEMPLATE_BASED.getWrapper(w));
				def.getDetails().setName("unit_test", Locale.getDefault());
				definitions.save(conn);
				conn.commit();
				// check load
				definitions.reload();
				// check delete
				def.setHardDeleted();
				definitions.save(conn);
				conn.commit();
				result = true;
			} catch (Exception e) {
				System.out.println(new PSException(e).toString());
			} catch(ExceptionInInitializerError e) {
				System.out.println(e);
			}
			finally {
				try {
					conn.close();
				} catch (SQLException ignored) {
				}
			}
    	} finally {
    		try {
    			if (w != null) {
	    			w.setHardDeleted();
	    			w.save();
    			}
			} catch (Exception ignored) {
				
			}
    	}
		assertTrue(result);
    }
 }
