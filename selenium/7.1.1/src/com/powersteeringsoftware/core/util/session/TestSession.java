package com.powersteeringsoftware.core.util.session;

import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;


public final class TestSession {

	private static HashMap<String,Object> params = new HashMap<String, Object>(100);


	//private static TestSession session = new TestSession();
	private TestSession(){

	}

//	public static TestSession getSession() {
//		return session;
//	}

	public static void putObject(String key, Object value) {
		if(StringUtils.isBlank(key)){
			throw new IllegalArgumentException("Session parameter name can't be null or blank.");
		}
		if(value==null){
			throw new IllegalArgumentException("Parameter can't be null.");
		}
		params.put(key, value);
	}

	public static Object getObject(String key) {
		if(params.get(key)==null){
			throw new TestSessionException("There is now object with name "+key+" in the session");
		}
		return params.get(key);
	}

	private static boolean isObjectNull(String key){
		return  null==params.get(key);
	}

	private static  boolean isObjectString(String key){
		return params.get(key) instanceof String;
	}

	public static String getApplicationVersionAsString() throws ObjectSessionException{
		if(isObjectNull(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey())
				|| !isObjectString(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey())){
			throw new ObjectSessionException("Object with name "
					+ TestSessionObjectNames.APPLICATION_VERSION.getObjectKey()
					+ " is empty, null or not a String");
		}
//		return (String)getObject(SESSION_KEY_APP_VERSION);
		return (String)getObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey());
	}

	public static boolean isApplicationVersion70(){
		try{
			return getApplicationVersionAsString().contains("7.0");
		} catch (ObjectSessionException ose){
			return false;
		}
	}

	public static boolean isApplicationVersion71(){
		try{
			return getApplicationVersionAsString().contains("7.1");
		} catch (ObjectSessionException ose){
			return false;
		}
	}

	public static void assertIsApplicationVerison71(){
		Assert.assertEquals(true, isApplicationVersion71(),"Application version must be 7.1");
	}

	public static void assertIsApplicationVerison70(){
		Assert.assertEquals(true, isApplicationVersion70(),"Application version must be 7.0");
	}




}
