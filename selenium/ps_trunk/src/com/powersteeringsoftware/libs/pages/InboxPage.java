package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.RadioButton;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.InboxPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.05.11
 * Time: 18:32
 */
public class InboxPage extends PSPage {

    public void open() {
        open(URL);
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        getDocument();
    }

    public List<QuestionTable> getQuestionsTables() {
        List<QuestionTable> res = new ArrayList<QuestionTable>();
        for (Element e : getElements(false, QUESTION_TABLE)) {
            res.add(new QuestionTable(e));
        }
        return res;
    }

    public void submit() {
        Button submit = new Button(SUBMIT);
        submit.submit();
        //new Element(FORM).waitForVisible();
        //refreshBlankPage();
    }

    public class QuestionTable extends Element {
        private String question;

        protected QuestionTable(Element e) {
            super(e);
        }

        public String getQuestion() {
            if (question != null) return question;
            return question = getParentText().trim().replaceAll("\\s+", " ");
        }

        public void accept() {
            new RadioButton(getChildByXpath(QUESTION_YES)).click();
        }

        public void reject() {
            new RadioButton(getChildByXpath(QUESTION_NO)).click();
        }

        public String toString() {
            return getQuestion();
        }

    }
}
