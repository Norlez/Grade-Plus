/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 AG Softwaretechnik, University of Bremen:
 * Karsten Hölscher, Sebastian Offermann, Dennis Schürholz, Marcel Steinbeck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uitest.pages;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import uitest.UITestUtil;
import uitest.fragments.MessageFragment;
import uitest.fragments.TableFragment;
import uitest.fragments.TopMenuFragment;

/**
 * Klasse für die Entkopplung der UI-Tests von der konkreten HTML-Seite (Pattern: Page
 * Object).
 *
 * @author Karsten Hölscher
 * @version 2016-07-26
 */
@Location(UITestUtil.REGISTER_USER_URL)
public class RegisterUserPage {

    private final String REMOVE_BUTTON_ID_FORMAT = "userTable:%d:removeUserForm:remove";

    @FindBy(id = "registerUserForm:username")
    private WebElement userName;

    @FindBy(id = "registerUserForm:password")
    private WebElement password;

    @FindBy(id = "registerUserForm:email")
    private WebElement email;

    @FindBy(id = "registerUserForm:save")
    private WebElement saveButton;

    @FindBy(id = "registerUserForm:usernameMessage")
    private MessageFragment userNameMessage;

    @FindBy(id = "registerUserForm:passwordMessage")
    private MessageFragment passwordMessage;

    @FindBy(id = "registerUserForm:emailMessage")
    private MessageFragment emailMessage;

    @FindBy(id = "userTable")
    private TableFragment userTable;

    @FindBy(id = "menue")
    private TopMenuFragment topMenu;

    private MessageFragment[] errorMessageFragments;

    public String getUsername() {
        return userName.getText();
    }

    public void setUsername(final String input) {
        userName.sendKeys(input);
    }

    public String getPassword() {
        return password.getTagName();
    }

    public void setPassword(final String input) {
        password.sendKeys(input);
    }

    public String getEmail() {
        return email.getText();
    }

    public void setEmail(final String input) {
        email.sendKeys(input);
    }

    public MessageFragment getUsernameMessage() {
        return userNameMessage;
    }

    public MessageFragment getPasswordMessage() {
        return passwordMessage;
    }

    public MessageFragment getEmailMessage() {
        return emailMessage;
    }

    public TableFragment getUserTable() {
        return userTable;
    }

    public TopMenuFragment getTopMenu() {
        return topMenu;
    }

    public MessageFragment[] getErrorMessageFragments() {
        if (errorMessageFragments == null) {
            errorMessageFragments = new MessageFragment[3];
            errorMessageFragments[0] = userNameMessage;
            errorMessageFragments[1] = passwordMessage;
            errorMessageFragments[2] = emailMessage;
        }
        return errorMessageFragments;
    }

    public void save() {
        guardHttp(saveButton).click();
    }

    public List<WebElement> getRemoveUserButtons() {
        List<WebElement> buttons = new ArrayList<>();
        if (userTable.isDisplayed()) {
            final int rows = userTable.getNumberOfRows();
            for (int i = 0; i < rows; i++) {
                WebElement button = userTable.getRoot().findElement(
                        By.id(String.format(REMOVE_BUTTON_ID_FORMAT, i)));
                buttons.add(button);
            }
        }
        return buttons;
    }

    public boolean isRemoveUserButtonDisplayed(final int index) {
        try {
            userTable.getRoot().findElement(
                    By.id(String.format(REMOVE_BUTTON_ID_FORMAT, index)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
