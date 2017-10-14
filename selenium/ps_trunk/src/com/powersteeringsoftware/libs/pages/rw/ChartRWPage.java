package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.objects.rw.RWChart.ChartType;
import com.powersteeringsoftware.libs.objects.rw.RWChart.DataSeries;
import org.testng.Assert;

import java.awt.*;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class ChartRWPage extends ReportWizardPage {

    protected ChartRWPage() {
        super(ReportWizardPageLocators.RWTab.Chart);
    }

    public void setChartType(ChartType chartType) {
        ILocatorable typeLocator = null;
        switch (chartType) {
            case NO_CHART:
                typeLocator = CHART_TAB_NOCHART_RBUTTON;
                break;
            case BAR:
                typeLocator = CHART_TAB_BAR_RBUTTON;
                break;
            case LINE:
                typeLocator = CHART_TAB_LINE_RBUTTON;
                break;
            case PIE:
                typeLocator = CHART_TAB_PIE_RBUTTON;
                break;
        }
        new RadioButton(typeLocator).click(true);
        setDocument();
    }


    public void setAxis(String axis) {
        getAxisSelector().select(axis);
        waitForPageToLoad();
    }

    public void setBaseDataSeries(DataSeries series) {
        getBaseSeriesSelector().select(series.getSeries());
        waitForPageToLoad();
    }

    public void addDatSeries() {
        getAddSeriesLink().click(true);
        setDocument();
    }

    public boolean isAdditionalSeriesAdded(int seriesNum) {
        return getAdditionalSeriesSelector(seriesNum).exists();
    }

    public void setAdditionalDataSeries(DataSeries series, int seriesNum) {
        Assert.assertTrue(seriesNum > 0, "Additional series number must be more '0'");
        getAdditionalSeriesSelector(seriesNum).select(series.getSeries());
        if (series.isDrawAsLine()) {
            getAsLineCheckBox(seriesNum).check();
        }
        if (series.isNewScale()) {
            getNewScaleCheckBox(seriesNum).check();
        }
    }

    public void setPosition(String pos) {
        getPositionSelector().select(pos);
    }

    public void setSize(String size) {
        getSizeSelector().select(size);
    }

    public void setTextFont(String font) {
        getTextFontSelector().select(font);
    }

    public void setLegendPosition(String pos) {
        getLegendPositionSelector().select(pos);
    }

    public void getLabelPosition(String pos) {
        getLabelPosirionSelector().select(pos);
    }

    public void setBackgroundFade(String value) {
        getBackgroundFadeSelector().select(value);
    }

    public void setFromColor(String color) {
        ColorPaletteSelector colorChooser = getFromColorChooser();
        colorChooser.setUsingPopup(Color.decode(color));
    }

    public void setToColor(String color) {
        ColorPaletteSelector colorChooser = getToColorChooser();
        colorChooser.setUsingPopup(Color.decode(color));
    }

    private SelectInput getAxisSelector() {
        return new SelectInput(CHART_TAB_AXIS_SELECTOR);
    }

    private SelectInput getBaseSeriesSelector() {
        return new SelectInput(CHART_TAB_BASE_SERIES_SELECTOR);
    }

    private Link getAddSeriesLink() {
        Element linkElm = searchElement(CHART_TAB_ADDSERIES_LINK);
        return new Link(linkElm);
    }

    private SelectInput getAdditionalSeriesSelector(int seriesNum) {
        String selectorId = CHART_TAB_ADDITIONAL_SERIES_SELECTOR.getLocator();
        for (int i = 2; i < seriesNum; i++) {
            selectorId = selectorId + "_" + String.valueOf(i - 2);
        }
        return new SelectInput("id=" + selectorId);
    }

    private CheckBox getAsLineCheckBox(int seriesNum) {
        String baseId = CHART_TAB_ADDITIONAL_SERIES_ASLINE_CHBOX.getLocator();
        return new CheckBox("id=" + getId(baseId, seriesNum));
    }

    private CheckBox getNewScaleCheckBox(int seriesNum) {
        String baseId = CHART_TAB_ADDITIONAL_SERIES_NEWSCALE_CHBOX.getLocator();
        return new CheckBox("id=" + getId(baseId, seriesNum));
    }

    private String getId(String baseId, int seriesNum) {
        String id = baseId;
        for (int i = 1; i <= seriesNum; i++) {
            id = baseId + (i - 1);
        }
        return id;
    }

    private SelectInput getPositionSelector() {
        return new SelectInput(CHART_TAB_POSITION_SELECTOR);
    }

    private SelectInput getSizeSelector() {
        return new SelectInput(CHART_TAB_SIZE_SELECTOR);
    }

    private SelectInput getTextFontSelector() {
        return new SelectInput(CHART_TAB_TEXT_FONT_SELECTOR);
    }

    private SelectInput getLegendPositionSelector() {
        return new SelectInput(CHART_TAB_LEGEND_POSITION);
    }

    private SelectInput getLabelPosirionSelector() {
        return new SelectInput(CHART_TAB_LABEL_POSITION);
    }

    private SelectInput getBackgroundFadeSelector() {
        Element selectorElm = Element.searchElementByXpath(getBackgrountFadeRow(), CHART_TAB_BACKGROUNDFADE_SELECTOR);
        return new SelectInput(selectorElm);
    }

    private ColorPaletteSelector getFromColorChooser() {
        Element colorChooserElm = Element.searchElementByXpath(getBackgrountFadeRow(), CHART_TAB_FROM_COLOR_ELEMENT);
        return new ColorPaletteSelector(colorChooserElm);
    }

    private ColorPaletteSelector getToColorChooser() {
        Element colorChooserElm = Element.searchElementByXpath(getBackgrountFadeRow(), CHART_TAB_TO_COLOR_ELEMENT);
        return new ColorPaletteSelector(colorChooserElm);
    }

    private Element getBackgrountFadeRow() {
        Element trElm = searchElement(CHART_TAB_BACKGROUNDFADE_ELEMENT);
        return trElm;
    }


} // class ChartRWPage
