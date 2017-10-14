package ps5.wbs.test.model;

import ps5.wbs.test.beans.Property;
import ps5.wbs.test.beans.WBSBean;

public enum ColumnValue {
    DEPENDENCY {

        @Override
        public String getEditValue(WBSBean bean, Property property) {
            // Set<Dependency> deps = bean.getToDependency();
            // List list = new SortedList(new Comparator<Dependency>() {
            // public int compare(Dependency d1, Dependency d2) {
            // Integer i1 = d1.getFrom().getNumber();
            // Integer i2 = d2.getFrom().getNumber();
            // return i1.intValue() - i2.intValue(); // not null;
            // }
            // });
            //
            // for (Dependency d : deps) {
            // if (d.getFrom().getNumber() != null)
            // list.add(e);
            // }
            // return bean.getName();
            return "";
        }

        @Override
        public void setValue(WBSBean bean, Property property, String value) {
            bean.setName(value);
        }
    },
    NAME {

        @Override
        public String getEditValue(WBSBean bean, Property property) {
            return bean.getName();
        }

        @Override
        public void setValue(WBSBean bean, Property property, String value) {
            bean.setName(value);
        }
    },
    NUMBER {

        @Override
        public String getEditValue(WBSBean bean, Property property) {
            return bean.getNumber() == null ? "" : bean.getName().toString();
        }

        @Override
        public void setValue(WBSBean bean, Property property, String value) {
            bean.setName(value);
        }
    },
    TAGSET {

        @Override
        public String getEditValue(WBSBean bean, Property property) {
            return bean.getName();
        }

        @Override
        public void setValue(WBSBean bean, Property property, String value) {
            // bean.setName(value);
        }
    };

    public static ColumnValue getByProperty(Property property) {
        return Enum.valueOf(ColumnValue.class, property.getField().toString());
    }

    public String getDisplayValue(WBSBean bean, Property property) {
        return getEditValue(bean, property);
    }

    public abstract String getEditValue(WBSBean bean, Property property);

    // {
    // // static error check.
    // for (Property.Field value : Property.Field.values()) {
    // ColumnValue.valueOf(value.name());
    // }
    // }
    public abstract void setValue(WBSBean bean, Property property, String value);
}
