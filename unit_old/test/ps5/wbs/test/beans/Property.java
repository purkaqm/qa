package ps5.wbs.test.beans;

import ps5.support.Util;

public class Property {

    static public Property Dependency = new Property(Field.DEPENDENCY);

    static public Property Id = new Property(Field.ID);

    static public Property Name = new Property(Field.NAME);

    static public Property Number = new Property(Field.NUMBER);

    private Field field;

    private Object id;

    public Property(Field f) {
        this(f, null);
    }

    public Property(Field field, Object id) {
        this.field = field;
        this.id = id;
    }

    public static enum Field {
        DEPENDENCY, ID, NAME, NUMBER, TAGSET;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Property)) {
            return false;
        }
        Property prop = (Property) object;
        return field.equals(prop.field) && Util.checkObjectsEquals(id, prop.id);
    }

    public Field getField() {
        return field;
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }
    // public void set(WBSBean bean, Object value) {
    // Object oldValue = get(bean);
    // boolean modified = !Util.checkObjectsEquals(value, oldValue);
    // if (id == null) {
    // field.set(bean, value);
    // } else {
    // field.set(bean, value, id);
    // }
    // bean.setModified(this);
    // }
    //
    // public Object get(WBSBean bean) {
    // return id == null
    // ? field.get(bean)
    // : field.get(bean, id);
    // }
}
