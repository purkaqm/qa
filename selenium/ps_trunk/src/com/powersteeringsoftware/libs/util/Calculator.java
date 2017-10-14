package com.powersteeringsoftware.libs.util;

import com.powersteeringsoftware.libs.logger.PSLogger;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 22.05.12
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class Calculator {
    private boolean checkPow = true;

    public static Double calculate(String formula) {
        try {
            return new Calculator(formula).doCalculate();
        } catch (IllegalArgumentException e) {
            PSLogger.fatal("Incorrect formula '" + formula + "':" + e.getMessage());
            throw e;
        }
    }

    /**
     * "([1]) ^ 2 - 1.0 / [2]" -> "[1]^2.0-1.0/[2]"
     *
     * @param formula
     * @return
     */
    public static String formatFormula(String formula) {
        return formula.replaceAll("\\(\\[(\\d+)\\]\\)", "[$1]").replace(" ", "").replaceAll("([^\\[\\.\\d])(\\d+)([^\\.\\d]|$)", "$1$2.0$3");
    }

    private enum Operator {
        POW('^'),
        MUL('*'),
        DIV('/'),
        PLUS('+'),
        MINUS('-'),;
        private char c;

        Operator(char c) {
            this.c = c;
        }

        public String toString() {
            return String.valueOf(c);
        }

        public String toRegexp() {
            return "\\" + toString();
        }
    }

    private String input;

    public Calculator(String line) {
        if (line.contains("(")) {
            checkPow = false;
            if (StringUtils.countMatches(line, "(") != StringUtils.countMatches(line, ")")) {
                throw new IllegalArgumentException("Incorrect input line: '" + line + "'");
            }
        }
        input = line.trim().replaceAll("\\s+\\+\\s+", "+").replaceAll("\\s+\\-\\s+", "-");
        while (input.contains("(")) {
            String innerLine = input.replaceAll(".*\\(([^\\)]+)\\).*", "$1");
            String part0 = input.replaceAll("(.*)\\([^\\)]+\\).*", "$1");
            String part1 = input.replaceAll(".*\\([^\\)]+\\)(.*)", "$1");
            Calculator c = new Calculator(innerLine);
            Double res = c.doCalculate();
            input = part0 + res + part1;
        }
    }

    /**
     * Examples:
     * 2 + 4	 = 6.0
     * 12.2332 * 44.5454 + 12	 = 556.93278728
     * 2 - 3 + 5454 * 1 * 2.33 +333	 = 13039.82
     * 2.323^4.555	 = 46.489422425918264
     * 2*-5	 = -10.0
     * (3.43434+(323.55*333) + (22))*3	 = 323302.75302000006
     * 2^2*(3 + 1  )	 = 16.0
     * -2-5.5665^1.02	 = -7.760946706618784
     * -2+ (-5.5665)^1.02	 = NaN
     * (-2)*(-2)	 = 4.0
     *
     * @return Double res
     */
    public Double doCalculate() {
        return doCalculate(checkPow);
    }

    private Double doCalculate(boolean checkPow) {
        String line = input;
        for (Operator o : Operator.values()) {
            String regexp;
            if (o.equals(Operator.PLUS) || o.equals(Operator.MINUS)) {
                regexp = ".*[^Ee]" + o.toRegexp() + ".+";
            } else {
                regexp = ".+" + o.toRegexp() + ".+";
            }
            while (line.matches(regexp)) {
                String line0 = line.replaceFirst("(.+)" + o.toRegexp() + ".+", "$1").trim();
                String line1 = line.replaceFirst(".+" + o.toRegexp() + "(.+)", "$1").trim();
                String part0 = line0.replaceAll("((\\+|\\-)*\\d+(\\.\\d+)*((E|e)(-)*\\d+)*)$", ""); // without last number
                String part1 = line1.replaceAll("^((\\+|\\-)*\\d+(\\.\\d+)*((E|e)(-)*\\d+)*)", ""); // without first number
                String sVal0 = line0.replace(part0, "");
                String sVal1 = line1.replace(part1, "");
                if (sVal0.startsWith("+") || sVal0.startsWith("-") && part0.matches(".*\\d$")) {
                    part0 += "+";
                }
                if (sVal1.startsWith("+") || sVal1.startsWith("-") && part1.matches("^\\d.*")) {
                    part1 = "+" + part1;
                }

                Double val0 = Double.parseDouble(sVal0);
                Double val1 = Double.parseDouble(sVal1);
                if (checkPow && val0 < 0 && o.equals(Operator.POW)) {
                    val0 = -val0;
                    part0 += "-";
                }
                Double res = operate(o, val0, val1);
                if (res == null || Double.isNaN(res) || Double.isInfinite(res)) {
                    return res;
                }
                line = part0 + res + part1;
            }
        }
        return Double.parseDouble(line);
    }

    private Double operate(Operator operator, Double val1, Double val2) {
        switch (operator) {
            case POW: {
                return Math.pow(val1, val2);
            }
            case MUL:
                return val1 * val2;
            case DIV:
                return val1 / val2;
            case PLUS:
                return val1 + val2;
            case MINUS:
                return val1 - val2;
        }
        return null;
    }

}
