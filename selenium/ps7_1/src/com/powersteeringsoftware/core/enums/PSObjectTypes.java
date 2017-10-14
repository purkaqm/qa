package com.powersteeringsoftware.core.enums;

public enum PSObjectTypes {
	PS_OBJECT(0),
	WORK_ITEM(1),
	MEASURE(2),
	PROCESS(3),
	ISSUE(4),
	TAG(5),
	TASK(6),
	ATTACHMENT(7),
	WORK_TEMPLATE(8),
	WORK_TEMPLATE_FRAMEWORK(9);

	private int id = 0;

	private  PSObjectTypes(int _id) {
		id = _id;
	}

	public int getId() {
		return id;
	}
}
