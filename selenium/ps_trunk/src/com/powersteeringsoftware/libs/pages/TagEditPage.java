package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.HierarchicalTagChooser;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.TagsEditEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.11
 * Time: 17:21
 */
public class TagEditPage extends PSPage {

    @Override
    public void open() {
        //TODO
        throw new IllegalMonitorStateException("TODO");
    }

    public TagEditPage addMore() {
        new Button(ADD_MODE_BUTTON).click(false);
        return getPage();
    }

    public TagsetPage updateValues() {
        new Button(UPDATE_VALUES_BUTTON).click(true);
        return new TagsetPage();
    }

    static TagEditPage getPage() {
        TagEditPage page = new TagEditPage();
        page.waitForPageToLoad();
        page.getDocument();
        return page;
    }

    public List<Input> getInputs() {
        List<Input> res = new ArrayList<Input>();
        for (Element e : getElements(false, NAME_COLUMN_INPUT)) {
            res.add(new Input(e));
        }
        return res;
    }

    public List<HierarchicalTagChooser> getParentInputs() {
        List<HierarchicalTagChooser> res = new ArrayList<HierarchicalTagChooser>();
        for (Element e : getElements(false, PARENT_COLUMN_DIV)) {
            res.add(new HierarchicalTagChooser(e));
        }
        return res;
    }

    public HierarchicalTagChooser getParentByName(String name) {
        List<Input> inputs = getInputs();
        List<HierarchicalTagChooser> parents = getParentInputs();
        Assert.assertEquals(parents.size(), inputs.size(), "Incorrect size of parents");
        for (int i = 0; i < inputs.size(); i++) {
            if (inputs.get(i).getDEAttribute("value").equals(name)) {
                return parents.get(i);
            }
        }
        PSLogger.warn("Can't find parent for '" + name + "'");
        return null;
    }

    public int getRowCount() {
        String text = new Element(ROW_COUNT_ELEMENT).getText();
        return Integer.parseInt(text.replaceAll("[^\\d]+", ""));
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad(false);
        Element table = new Element(TABLE);
        table.waitForPresent();
    }

}
