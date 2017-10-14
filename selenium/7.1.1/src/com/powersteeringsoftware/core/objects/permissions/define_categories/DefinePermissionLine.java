package com.powersteeringsoftware.core.objects.permissions.define_categories;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.powersteeringsoftware.core.objects.permissions.BasicPermissionRolesEnum;
import com.powersteeringsoftware.core.tc.PSTestCaseException;

public class DefinePermissionLine {

	HashMap<BasicPermissionRolesEnum, Boolean> basicRolesMap;
	HashMap<String, Boolean> customRolesMap;

	public DefinePermissionLine(){
		setBasicRolesMap(true);
		customRolesMap = new HashMap<String, Boolean>(10);
	}

	public void setBasicRolesMap(boolean status){
		basicRolesMap = new HashMap<BasicPermissionRolesEnum, Boolean>(7);
		setPermissionAll(status);
		setPermissionNone(status);
		setPermissionEdit(status);
		setPermissionFinance(status);
		setPermissionGuest(status);
		setPermissionView(status);
		setPermissionWork(status);
	}

	private void setPermissionAll(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.ALL, status);
	}

	private void setPermissionNone(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.NONE, status);
	}

	public void setPermissionEdit(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.EDIT, status);
	}

	public void setPermissionFinance(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.FINANCE, status);
	}

	public void setPermissionGuest(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.GUEST, status);
	}

	public void setPermissionView(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.VIEW, status);
	}

	public void setPermissionWork(boolean status) {
		basicRolesMap.put(BasicPermissionRolesEnum.WORK, status);
	}

	public void setCustomRole(String roleName, boolean status){
		if(null==roleName) throw new PSTestCaseException("Role name can't be null");
		this.customRolesMap.put(roleName,status);
	}

	public boolean getCustomRole(String roleName){
		return null!=this.customRolesMap.get(roleName)?this.customRolesMap.get(roleName):false;
	}

	public Map<String, Boolean> getCustomRoleStatuses(){
		return (Map<String, Boolean>) this.customRolesMap;
	}

	public Set<String> getCustomRoleNamesSet(){
		return (Set<String>)customRolesMap.keySet();
	}

}
