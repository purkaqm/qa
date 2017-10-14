package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.WorkDependency;
import com.powersteeringsoftware.libs.objects.works.Work;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.WorkDependencyDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 14:00:06
 */
public class WorkDependencyDialog extends Dialog {
    public static final int DEFAULT_NUMBER_OF_ROWS = 5;
    private int numberOfRows = DEFAULT_NUMBER_OF_ROWS;
    private DisplayTextBox input;

    private List<Element> rows;

    public WorkDependencyDialog(DisplayTextBox e) {
        super(e.getIcon());
        input = e;
        setPopup(DIALOG);
    }

    public static WorkDependency parse(String toParse) {
        PSLogger.debug("To parse '" + toParse + "'");
        if (!toParse.matches(DEPENDENCY_REGEXP.getLocator())) return null;
        String sType = toParse.replaceAll(DEPENDENCY_REGEXP.getLocator(), DEPENDENCY_TYPE.getLocator());
        String sIndex = toParse.replaceAll(DEPENDENCY_REGEXP.getLocator(), DEPENDENCY_INDEX.getLocator());
        String sLag = toParse.replaceAll(DEPENDENCY_REGEXP.getLocator(), DEPENDENCY_LAG.getLocator());
        WorkDependency.Type t = sType.isEmpty() ? WorkDependency.Type.FS : WorkDependency.Type.valueOf(sType.toUpperCase());
        Integer index = sIndex.isEmpty() ? 0 : Integer.valueOf(sIndex);
        Integer lag = sLag.isEmpty() ? 0 : Integer.valueOf(sLag.replace("+", ""));
        WorkDependency res = new WorkDependency(null, t, lag);
        res.setIndex(index);
        return res;
    }

    List<Element> getRows() {
        if (rows != null) return rows;
        Element body = getBodyElement();
        body.setDefaultElement();
        return rows = Element.searchElementsByXpath(body, ROW);
    }

    public DisplayTextBox getPred() {
        return getPred(1);
    }

    public Input getLag() {
        return getLag(1);
    }

    public ComboBox getType() {
        return getType(1);
    }

    public DisplayTextBox getPred(int rowNum) {
        return new DisplayTextBox(getCellDiv(rowNum, PRED));
    }

    public ComboBox getType(int rowNum) {
        return new ComboBox(getCellDiv(rowNum, TYPE));
    }

    public Input getLag(int rowNum) {
        Input in = new Input(getCellDiv(rowNum, LAG));
        in.setMax(LAG_MAX);
        in.setMin(LAG_MIN);
        in.setRegExp(LAG_REGEXP);
        return in;
    }


    private Element getCellDiv(int rowNum, ILocatorable loc) {
        Element res = getRows().get(rowNum - 1).getChildByXpath(loc);
        res.setSimpleLocator();
        return res;
    }

    public void submit() {
        PSLogger.save("before submit");
        getPopup().getChildByXpath(BUTTON_OK).click(false);
        getPopup().waitForUnvisible();
        input.waitForValue();
    }

    public void cancel() {
        getPopup().getChildByXpath(BUTTON_CANCEL).click(false);
        getPopup().waitForUnvisible();
    }

    /**
     * @param num      index
     * @param work     Work
     * @param type     DependencyType
     * @param lag      lag
     * @param howToSet if null then type index,  if true use browse,  if false use search
     */
    public void setDependency(int num, Work work, WorkDependency.Type t, String lag, Boolean howToSet) {
        DependencyType type = DependencyType.valueOf(t.name());
        PSLogger.info("Set work " + work + " with type " + type.getLocator() + " and lag " + lag);
        DisplayTextBox pred = getPred(num);
        if (howToSet == null) {
            pred.type(String.valueOf(work.getGeneralIndex()));
        } else {
            WorkChooserDialog dialog = new WorkChooserDialog(pred);
            dialog.open();
            if (howToSet) {
                // set using browse
                dialog.openBrowseTab();
                dialog.chooseWorkOnBrowseTab(work);
            } else {
                //set using searching
                dialog.openSearchTab();
                dialog.chooseWorkOnSearchTab(work);
            }
            pred.waitForValue();
            if (!pred.getValue().contains(work.getName())) {
                PSLogger.warn("Can't find work " + work + " in input after setting using " +
                        (howToSet ? "browse tree" : "searching"));
                PSLogger.save();
            }
        }
        getType(num).select(type);
        getLag(num).type(lag);
    }

    private void doSetDependency(WorkDependency... toSet) {
        for (int i = 0; i < toSet.length; i++) {
            if ((i + 1) % numberOfRows++ == 0) {
                rows = null;
                getRows();
            }
            setDependency(i + 1, toSet[i].getWork(), toSet[i].getType(), toSet[i].getLag(), toSet[i].howToSet());
        }
    }

    public void setDependency(List<WorkDependency> toSet) {
        open();
        try {
            doSetDependency(toSet.toArray(new WorkDependency[toSet.size()]));
        } catch (RuntimeException re) {
            PSLogger.fatal(re);
            throw re;
        }
        submit();
    }

}
