package com.powersteeringsoftware.libs.objects.metrics;

import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 29.11.13
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public interface Period {
    String getStart(int year);

    String getEnd(int year);

    String getName();

    String getName(int year);

    int ordinal();

    static final Calendar CALENDAR = GregorianCalendar.getInstance();

    enum Month implements Period {
        Jan,
        Feb,
        Mar,
        Apr,
        May,
        Jun,
        Jul,
        Aug,
        Sep,
        Oct,
        Nov,
        Dec,;

        public String getName() {
            return name();
        }

        public String getName(int y) {
            return name();
        }

        public String getStart(int year) {
            Calendar c = (Calendar) CALENDAR.clone();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, ordinal());
            c.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sf = new SimpleDateFormat(CoreProperties.DEFAULT_DATE_FORMAT_NO_YEAR);
            return sf.format(c.getTime()) + String.valueOf(year);
        }

        public String getEnd(int year) {
            Calendar c = (Calendar) CALENDAR.clone();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, ordinal() + 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
            SimpleDateFormat sf = new SimpleDateFormat(CoreProperties.DEFAULT_DATE_FORMAT_NO_YEAR);
            return sf.format(c.getTime()) + String.valueOf(year);

        }

    }

    enum Quarter implements Period {
        Q1,
        Q2,
        Q3,
        Q4,;

        public String getName() {
            return name();
        }

        public String getName(int y) {
            return name();
        }

        public String getStart(int y) {
            int ord = (ordinal() + 1) * 3 - 3;
            Calendar c = (Calendar) CALENDAR.clone();
            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, ord);
            c.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sf = new SimpleDateFormat(CoreProperties.DEFAULT_DATE_FORMAT_NO_YEAR);
            return sf.format(c.getTime()) + String.valueOf(y);
        }

        public String getEnd(int y) {
            int ord = (ordinal() + 1) * 3;
            Calendar c = (Calendar) CALENDAR.clone();
            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, ord);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 1);
            SimpleDateFormat sf = new SimpleDateFormat(CoreProperties.DEFAULT_DATE_FORMAT_NO_YEAR);
            return sf.format(c.getTime()) + String.valueOf(y);
        }
    }


    /**
     * !hardcode for PS
     */
    enum Month13 implements Period {
        _1("Month 1", "01/01", "01/28"),
        _2("Month 2", "01/29", "02/25"),
        _3("Month 3", "02/26", "03/25"),

        _4("Month 4", "03/26", "04/22"),
        _5("Month 5", "04/23", "05/20"),
        _6("Month 6", "05/21", "06/17"),

        _7("Month 7", "06/18", "07/15"),
        _8("Month 8", "07/16", "08/12"),
        _9("Month 9", "08/13", "09/09"),

        _10("Month 10", "09/10", "10/07"),
        _11("Month 11", "10/08", "11/04"),
        _12("Month 12", "11/05", "12/02"),
        _13("Month 13", "12/03", "12/31"),;
        private String _name;
        private String _start;
        private String _end;

        private Month13(String n, String s, String e) {
            _name = n;
            _start = s;
            _end = e;
        }

        @Override
        public String getStart(int year) {
            return _start + "/" + year;
        }

        @Override
        public String getEnd(int year) {
            return _end + "/" + year;
        }

        @Override
        public String getName() {
            return _name;
        }

        public String getName(int y) {
            return _name + " " + y;
        }
    }

    /**
     * !hardcode for PS
     */
    enum Quarter13 implements Period {
        Q1("Qtr 1", "01/01", "03/25"),
        Q2("Qtr 2", "03/26", "06/17"),
        Q3("Qtr 3", "06/18", "09/09"),
        Q4("Qtr 4", "09/10", "12/31"),;
        private String _name;
        private String _start;
        private String _end;

        private Quarter13(String n, String s, String e) {
            _name = n;
            _start = s;
            _end = e;
        }

        @Override
        public String getStart(int year) {
            return _start + "/" + year;
        }

        @Override
        public String getEnd(int year) {
            return _end + "/" + year;
        }

        @Override
        public String getName() {
            return _name;
        }

        public String getName(int y) {
            return _name + " " + y;
        }
    }

}
