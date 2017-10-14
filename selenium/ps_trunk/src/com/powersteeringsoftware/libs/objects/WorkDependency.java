package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.objects.works.Work;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.09.12
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class WorkDependency implements Comparable {
    // if howToSet=null then type index
    // if howToSet=true use browse
    // if howToSet=false use search
    public enum Type {
        FS,
        SS,
        FF,
        SF,;
    }

    private Boolean howToSet;
    private Work work;
    private int index = -1;
    private Type type;
    private int lag;

    public Work getWork() {
        return work;
    }

    public Boolean howToSet() {
        return howToSet;
    }

    public Type getType() {
        return type;
    }

    public void setIndex(int i) {
        index = i;
    }

    public WorkDependency(Work work, Type type, int lag) {
        this.work = work;
        if (work != null)
            this.index = work.getGeneralIndex();
        this.type = type;
        this.lag = lag;
    }

    public WorkDependency(Work work) {
        this(work, Type.FS, 0);
    }

    public String getLag() {
        if (lag == 0) return "";
        return String.valueOf(lag);
    }

    /**
     * @param howToSet true(browse), false(search), null(index)
     */
    public void setHowToSet(Boolean howToSet) {
        this.howToSet = howToSet;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        StringBuffer res = new StringBuffer(String.valueOf(getIndex()));
        if (lag == 0) {
            if (!type.equals(Type.FS)) res.append(type.name());
        } else {
            res.append(type.name());
            res.append((lag > 0 ? "+" : "") + lag);
        }
        return res.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (!(o instanceof WorkDependency)) return -1;
        return toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof WorkDependency)) return false;
        return toString().equals(o.toString());
    }
}
