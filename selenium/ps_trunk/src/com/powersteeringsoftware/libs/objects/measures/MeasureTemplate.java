package com.powersteeringsoftware.libs.objects.measures;

import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests_data.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 26.08.12
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class MeasureTemplate extends Measure {
    private List<MeasureInstance> instances = new ArrayList<MeasureInstance>();

    public MeasureTemplate(Config conf) {
        super(conf);
    }

    public MeasureTemplate(String name) {
        super(name);
        setIndicationType(IndicatorType.NONE);
    }

    @Override
    public boolean isLibrary() {
        return true;
    }

    public void addInstance(MeasureInstance instance) {
        instances.add(instance);
        //getConfig().addConfig(instance.getConfig()); // is it need?
    }

    public List<MeasureInstance> getInstances() {
        return instances;
    }

    public MeasureInstance toInstance() {
        return new MeasureInstance(copy().getConfig());
    }

    public List<Work> getWorks() {
        List<Work> res = new ArrayList<Work>();
        for (MeasureInstance i : instances) {
            res.add(i.getWork());
        }
        return res;
    }

    public MeasureTemplate copy() {
        MeasureTemplate res = (MeasureTemplate) super.copy();
        while (res.getConfig().hasChild(NAME)) {
            res.getConfig().removeChild(NAME); // remove instance
        }
        return res;
    }

}
