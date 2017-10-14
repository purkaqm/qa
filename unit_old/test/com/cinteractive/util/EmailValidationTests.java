package com.cinteractive.util;

import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.util.StringUtilities;

public class EmailValidationTests extends PSTestBase {

    private String[] validEmails = new String[] {
             "Simple@simple.com"
            ,"abc.Abc@simple.com"
            ,"-xyzabc1923!#$%&'*+/=?^_`{|}~.-xyzabc1923!#$%&'*+/=?^_`{|}~Abc@simple.com"
            ,"?.-.a.1.!.#.$.%.&.'.*.+./.=.?.^._.`.{.|.}.~@simple.com"
            ,"TEST@ai"
            ,"#@com"
            ,"/@com"
            ,"?@aa"
            ,"TEST@museum"
            ,"TEST@TEST.museum"
            ,"test@simple.travel"
            ,"test@simple.travel.museum.com"
            ,"test@s.ru"
            ,"test@1.ru"
            ,"test@1-----1.ru"
            ,"test@s-----s.ru"
            ,"test@9.A---9.ru"
            ,"test@9.A---9.1---B.RU"
            ,"test@1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.g.h.i-j.k-l.m-n.o--p.q---r.s-------t.u-------v.w-------x.y--------------------z.RU"
            ,"-xyzabc1923!#$%&'*+/=?^_`{|}~.-xyzabc1923!#$%&'*+/=?^_`{|}~Abc@1.2.3.4.5.6.7.8.9.a.b.c.d.e.f.g.h.i-j.k-l.m-n.o--p.q---r.s-------t.u-------v.w-------x.y--------------------z.RU"
    };

    private String[] invalidEmails = new String[] {
             null
            ,""
            ,"@"
            ,"@example.com"
            ,"a@"
            ,"a@a"
            ,"a@11"
            ,"a@a1"
            ,"Abc.example.com"
            ,"Abc.@example.com"
            ,"Abc..123@example.com"
            ,"A@b@c@example.com"
            ,"()[]\\;:,<>@example.com"
            ,".test@example.com"
            ,"test.@example.com"
            ,"t.y.u..u@example.com"
            ,"test@museumm"
            ,"test@example.example"
            ,"test@.example"
            ,"test@-b.com"
            ,"test@_b.com"
            ,"test@longlonglonglonglonglonglonglonglonglonglonglonglonglonglonglong.com"
            ,"test@'b.com"
            ,"test@1..com"
            ,"test@b_c.com"
            ,"test@b-.com"
            ,"test@test.t1"
    };

    public EmailValidationTests(String name) { super(name); }
    
    public void testEmails() {
        for (String validEmail : validEmails) {
            assertTrue("failed for valid email: " + validEmail, StringUtilities.isValidEmail(validEmail));
        }
        for (String invalidEmail : invalidEmails) {
            assertTrue("failed for invalid email: " + invalidEmail, !StringUtilities.isValidEmail(invalidEmail));
        }
    }
}
