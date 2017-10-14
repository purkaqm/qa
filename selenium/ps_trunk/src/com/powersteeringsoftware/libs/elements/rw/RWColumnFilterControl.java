package com.powersteeringsoftware.libs.elements.rw;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.objects.rw.RWFilterValue;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.RWColumnFilterControlLocators.*;

public abstract class RWColumnFilterControl extends Element {
    public enum RWFilterType {
        FT_NUMERIC,
        FT_NUMERIC_WITH_CLAUSE,
        FT_TEXT,
        FT_TEXT_WITH_CLAUSE,
        FT_TAG,
        FT_DTRANGE,
        FT_SELECTOR,
        FT_PHASE,
        FT_CHBOX,
        FT_CF_CHBOX;
    } // enum RWFilterType

    protected RWColumnFilterControl(ILocatorable container) {
        super(container);
    }

    protected RWColumnFilterControl(Element container) {
        super(container);
    }

    protected void waitForPageToLoad() {
        getDriver().waitForPageToLoad();
        setDefaultElement();
    }

    @Override
    public Element getChildByXpath(String loc) {
        if (!isDEPresent()) {
            setDefaultElement();
        }
        return super.getChildByXpath(loc);
    }

    protected Element getContainerElm() {
        return getChildByXpath("/td[1]");
    }

    protected Element searchElement(String xPath) {
        return getContainerElm().getChildByXpath(xPath);
    }

    protected Element searchElement(ILocatorable locator) {
        return getContainerElm().getChildByXpath(locator);
    }

    protected List<Element> searchElements(String xPath) {
        return Element.searchElementsByXpath(getContainerElm(), xPath);
    }

    protected List<Element> searchElements(ILocatorable locator) {
        return Element.searchElementsByXpath(getContainerElm(), locator);
    }

    public abstract void setValue(RWFilterValue value);

    public abstract RWFilterType getType();

    public static RWColumnFilterControl getRWColumnFilter(Element container, RWFilterType type) {
        RWColumnFilterControl instance = null;
        switch (type) {
            case FT_PHASE:
                instance = new ProcessPhaseFilter(container);
                break;
            case FT_TAG:
                instance = new TagFilter(container);
                break;
            case FT_SELECTOR:
                instance = new ComboBoxSelectableFilter(container);
                break;
            case FT_CHBOX:
                instance = new CheckBoxSelectableFilter(container);
                break;
            case FT_TEXT_WITH_CLAUSE:
            case FT_NUMERIC_WITH_CLAUSE:
                instance = new TextWithClauseFilter(container);
                break;
            case FT_CF_CHBOX:
                instance = new CustomFieldCheckBoxesFilter(container);
                break;
        }

        return instance;
    }

} // class RWColumnFilter 


//============================ ProcessPhaseFilter ==================================
class ProcessPhaseFilter extends RWColumnFilterControl {


    protected ProcessPhaseFilter(ILocatorable container) {
        super(container);
    }

    protected ProcessPhaseFilter(Element container) {
        super(container);
    }

    @Override
    public void setValue(RWFilterValue value) {
        getProcessSelector().select(value.getParameterValue("process"));
        waitForPageToLoad();
        if (value.isValueAllSelected()) {
            checkAllPhases();
        } else {
            for (String phaseName : value.getValues()) {
                getPhaseCheckBox(phaseName).check();
            }
        }
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_PHASE;
    }


    public void checkAllPhases() {
        List<Element> checkBoxes = searchElements(FT_PHASE_ALL_PHASE_CHECKBOX);
        for (Element chbElm : checkBoxes) {
            new CheckBox(chbElm).check();
        }
    }

    private SelectInput getProcessSelector() {
        return new SelectInput(searchElement(FT_PHASE_SELECTOR));
    }


    private CheckBox getPhaseCheckBox(String phaseName) {
        String chBoxId = searchElement(FT_PHASE_CHECKBOX.replace(phaseName)).getAttribute("for");
        return new CheckBox("id=" + chBoxId);
    }

} // class ProcessPhaseFilter 


//============================ TagFilter ==================================
class TagFilter extends RWColumnFilterControl {

    protected TagFilter(ILocatorable container) {
        super(container);
    }

    protected TagFilter(Element container) {
        super(container);
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_TAG;
    }

    @Override
    public void setValue(RWFilterValue value) {
        getClauseSelector().select(value.getClause());
        getCreterionSelector().select(value.getCriterion());
        MultipleTagChooser tagChooser = getTagChooser();
        if ((tagChooser != null) && (tagChooser.isVisible())) {
            tagChooser.openPopup();
            for (String tag : value.getValues()) {
                tagChooser.select(tag);
            }
        }
    }


    private SelectInput getClauseSelector() {
        return new SelectInput(searchElement(FT_TAG_CLAUSE_SELECTOR));
    }

    private SelectInput getCreterionSelector() {
        return new SelectInput(searchElement(FT_TAG_CRITERION_SELECTOR));
    }

    private MultipleTagChooser getTagChooser() {
        return new MultipleTagChooser(searchElement(FT_TAG_TAGCHOOSER_ELEMENT));
    }

} // class TagFilter 


//============================ ComboBoxSelectableFilter ==================================
class ComboBoxSelectableFilter extends RWColumnFilterControl {


    protected ComboBoxSelectableFilter(ILocatorable container) {
        super(container);
    }

    protected ComboBoxSelectableFilter(Element container) {
        super(container);
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_SELECTOR;
    }

    @Override
    public void setValue(RWFilterValue value) {
        getSelector().select(value.getValue());
    }

    private SelectInput getSelector() {
        Element elm = searchElement(FT_SELECTOR_SELECTOR);
        return new SelectInput(elm);
    }

} // class ComboBoxSelectableFilter


//============================ CheckBoxSelectableFilter ==================================
class CheckBoxSelectableFilter extends RWColumnFilterControl {


    protected CheckBoxSelectableFilter(ILocatorable container) {
        super(container);
    }

    protected CheckBoxSelectableFilter(Element container) {
        super(container);
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_CHBOX;
    }

    @Override
    public void setValue(RWFilterValue value) {
        if (value.isValueAllSelected()) {
            checkAll();
        } else {
            for (String valueName : value.getValues()) {
                checkValue(valueName);
            }
        }
    }

    public void checkAll() {
        List<CheckBox> chBoxes = getAllCheckBoxes();
        for (CheckBox chBox : chBoxes) {
            chBox.check();
        }
    }

    public void checkValue(String valueName) {
        getCheckBox(valueName).check();
    }


    private CheckBox getCheckBox(String name) {
        String chBoxId = searchElement(FT_CHBOX_LABEL.replace(name)).getAttribute("for");
        return new CheckBox(searchElement(FT_CHBOX_CHECKBOX.replace(chBoxId)));
    }

    private List<CheckBox> getAllCheckBoxes() {
        List<Element> chBoxElments = searchElements(FT_CHBOX_ALL_CHECKBOXES);
        List<CheckBox> chBoxes = new ArrayList<CheckBox>(chBoxElments.size());
        for (Element elm : chBoxElments) {
            chBoxes.add(new CheckBox(elm));
        }
        return chBoxes;
    }

} // class CheckBoxSelectableFilter


//============================ TextWithClauseFilter ==================================
class TextWithClauseFilter extends RWColumnFilterControl {


    protected TextWithClauseFilter(ILocatorable container) {
        super(container);
    }

    protected TextWithClauseFilter(Element container) {
        super(container);
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_TEXT_WITH_CLAUSE;
    }

    @Override
    public void setValue(RWFilterValue value) {
        setClause(value.getClause());
        if (isTextInputVisible()) {
            setTextField(value.getValue());
        }
    }

    public void setClause(String clause) {
        getClauseSelector().select(clause);
    }

    public void setTextField(String value) {
        getTextInput().type(value);
    }

    public boolean isTextInputVisible() {
        Input input = getTextInput();
        return (input != null) && input.exists() && input.isVisible();
    }

    private SelectInput getClauseSelector() {
        return new SelectInput(searchElement(FT_TEXT_WITH_CLAUSE_SELECTOR));
    }

    private Input getTextInput() {
        return new Input(searchElement(FT_TEXT_WITH_CLAUSE_INPUT));
    }

} // class TextWithClauseFilter


//============================ CustomFieldCheckBoxesFilter ==================================
class CustomFieldCheckBoxesFilter extends RWColumnFilterControl {


    protected CustomFieldCheckBoxesFilter(ILocatorable container) {
        super(container);
    }

    protected CustomFieldCheckBoxesFilter(Element container) {
        super(container);
    }

    @Override
    public void setValue(RWFilterValue value) {
        value.getClause();
    }

    @Override
    public RWFilterType getType() {
        return RWFilterType.FT_CF_CHBOX;
    }

    private SelectInput getClauseSelector() {
        return new SelectInput(searchElement(FT_TAG_CLAUSE_SELECTOR));
    }


} // class CustomFieldCheckBoxesFilter 
