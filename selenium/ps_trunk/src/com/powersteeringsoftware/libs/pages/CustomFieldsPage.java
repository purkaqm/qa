package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.07.2010
 * Time: 13:11:30
 */
public abstract class CustomFieldsPage extends PSPage {
    protected CustomFieldsTemplate template;

    public void open() {
        clickAdminCustomFields();
    }

    public void setTemplate(CustomFieldsTemplate template) {
        this.template = template;
    }

    public abstract void create(CustomFieldsTemplate template);

    public abstract void deleteAll();

    public abstract void pushAddNew();

    public abstract void typeDescription(String desc);

    public abstract void typeName(String name);

    public abstract void submitCreation();

    public abstract void checkObjects();


}
