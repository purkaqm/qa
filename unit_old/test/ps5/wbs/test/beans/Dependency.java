package ps5.wbs.test.beans;

import ps5.support.Util;

import com.cinteractive.ps3.relationships.DependencyType;

public class Dependency {

    private WBSBean from;

    private int lag;

    private WBSBean to;

    private DependencyType type;

    public Dependency(WBSBean from, WBSBean to, DependencyType type) {
        this.type = type;
        this.to = to;
        this.from = from;
        lag = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dependency)) {
            return false;
        }
        Dependency dw = (Dependency) o;
        return Util.checkObjectsEquals(dw.getFrom(), getFrom()) && Util.checkObjectsEquals(dw.getTo(), getTo())
                && Util.checkObjectsEquals(dw.getType(), getType());
    }

    public WBSBean getFrom() {
        return from;
    }

    public int getLag() {
        return lag;
    }

    public WBSBean getTo() {
        return to;
    }

    public DependencyType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode() + type.hashCode();
    }
}
