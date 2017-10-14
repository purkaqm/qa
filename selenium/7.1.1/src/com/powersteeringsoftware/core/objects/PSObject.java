package com.powersteeringsoftware.core.objects;

import com.powersteeringsoftware.core.enums.PSObjectTypes;

public abstract class PSObject {

	protected PSObjectTypes objectType = PSObjectTypes.PS_OBJECT;
	protected String name;
	private String uid;

	public PSObject(String _name, PSObjectTypes _objectType){
		if (null == _objectType){
			throw new IllegalArgumentException("Object type can't be null");
		}

		if (null == _name){
			throw new IllegalArgumentException("Object name can't be null");
		}
		setObjectType(_objectType);
		setName(_name);
	}

	public PSObjectTypes getObjectType(){
		return objectType;
	}

	private void setObjectType(PSObjectTypes _objectType){
		objectType = _objectType;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}


}
