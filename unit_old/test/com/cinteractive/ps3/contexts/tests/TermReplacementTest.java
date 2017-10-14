package com.cinteractive.ps3.contexts.tests;

/* Copyright © 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.contexts.Term;
import com.cinteractive.ps3.entities.PSLocale;
import com.cinteractive.ps3.test.PSTestBase;

public class TermReplacementTest extends PSTestBase
{
    private InstallationContext context;
    private Locale locale;
    private List<Locale> locales = new ArrayList<Locale>();
    public TermReplacementTest(String name)
    {
        super(name);
    }

    public void setUp() {
        super.setUp();
        context = getContext();

        for(PSLocale o : context.getConfiguredLocales()) {
            locales.add(o.getJavaLocale());
        }
    }

    private String formatTerms(String templ, String ... terms) {
        for (int i = 0; i < terms.length; ++i) {
            terms[i] = context.replaceTerm(terms[i], locale);
        }
        return String.format(templ, terms);
    }

    private void test(String test, String result, String message) {
        String converted = null;
        try {
            converted = MessageFormat.format(context.formatTerms(test, locale), "a", "b", "c");
        } catch (RuntimeException ex) {
            if (result == null) {
                System.out.printf("\"%s\" -> Exception Expected exception: \n", test, converted, result);
                return;
            }
            throw ex;
        }

        if (result == null) {
            System.out.printf("\"%s\" -> \"%s\" Expected: Exception\n", test, converted, result);
            fail(message);
        }

        System.out.printf("\"%s\" -> \"%s\" Expected: \"%s\"\n", test, converted, result);
        assertEquals(message, converted, result);
    }

    private void checkWithLocale() {

        String planned = context.replaceTerm("Planned", locale);
        String plannedPL = context.replaceTermPlural("Planned", locale);
        String plannedLC = planned.toLowerCase(locale);
        String task = context.replaceTerm("Task", locale);
        String taskLC = task.toLowerCase(locale);
        String plannedPLLC = plannedPL.toLowerCase(locale);

        final String simple="[Planned] date: {0} {2} {1} '{}' '{' '}' [Task]";
        final String simpleResult=String.format("%s date: a c b {} { } %s", planned, task);

        final String simple2="[Planned] date: {0} {2} {1} '{' '{' '{' [Task]";
        final String simpleResult2=String.format("%s date: a c b { { { %s", planned, task);

        final String simple3="[[Planned]] date: {0} {2} {1} '{' '{' '{' [[Task]]";
        final String simpleResult3=String.format("[%s] date: a c b { { { [%s]", planned, task);
        
        final String lowCase="[-Planned] date: {0} {2} {1} [Task]";
        final String lowCaseResult=formatTerms("%s date: a c b %s", plannedLC, task);

        final String lowCase2="[-Planned] date: {0} {2} {1} [-Task]";
        final String lowCaseResult2=formatTerms("%s date: a c b %s", plannedLC, taskLC);

        final String dontExist="[NotExits] date: {0} {2} {1}";
        final String dontExistResult=formatTerms("[NotExits] date: a c b");

        final String plural="[*Task]";

        final String plural2="[*Planned] date: {0} {2} {1} [-Task]";
        final String pluralResult2=formatTerms("%s date: a c b %s", plannedPL, taskLC);

        final String plural3="[*-Planned] date: {0} {2} {1} [-*Planned]";
        final String pluralResult3=formatTerms("%s date: a c b %s", plannedPLLC, plannedPLLC);

        test(simple, simpleResult, "Simple test");
        test(simple2, simpleResult2, "Simple test - 2");
        test(simple3, simpleResult3, "Simple test - 3");
        test(lowCase, lowCaseResult, "Lowercase terms");
        test(lowCase2, lowCaseResult2, "Lowercase terms -2");
        test(dontExist, dontExistResult, "Not existing terms");

        test(plural, null, "Only 'option' term can have a plural form");
        test(plural2, pluralResult2, "Plural form 2");
        test(plural3, pluralResult3, "Plural form 3");
        
        context.getTerms().updateTerm(Term.OWNER, locale.toString(), "$Owner$0$1");
        
        String owner = context.replaceTerm("Owner", locale);
        test("[Owner]-[Owner]", "$Owner$0$1-$Owner$0$1", "'$' replacement test");
    }

    public void testTermsFormat() {
        for (Locale locale : locales) {
            this.locale = locale;
            checkWithLocale();
        }
    }
}
