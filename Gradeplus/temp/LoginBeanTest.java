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
package controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import common.model.Session;
import common.model.User;
import persistence.UserDAO;
import common.util.Assertion;

/**
 * Komponententest für die Klasse LoginBean. Abhängigkeiten werden gemockt.
 *
 * Hier werden aktuell lediglich die Methode zum login und logout getestet, da alle
 * anderen Methoden keinerlei Funktion zusätzlich zu verwendeten Funktionen enthalten,
 * also einfach Werte weiterreichen bzw. zurückgeben.
 *
 * Die Klasse muss mit dem {@link PowerMockRunner} ausgeführt werden, da sie statische
 * Methoden mockt.
 *
 * @author Karsten Hölscher
 * @version 2017-07-03
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class, Assertion.class, ResourceBundle.class })
public class LoginBeanTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private User theUser;

    @Mock
    private Session session;

    @Mock
    private User dbUser;

    @Mock
    private FacesContext facesContext;

    @Mock
    private UIViewRoot uiViewRoot;

    @Mock
    private ExternalContext externalContext;

    private LoginBean loginBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(FacesContext.class);
        PowerMockito.mockStatic(Assertion.class);

        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);

        when(Assertion.assertNotNull(any())).then(returnsFirstArg());

        when(theUser.getLanguage()).thenReturn(Locale.GERMAN);
        when(facesContext.getViewRoot()).thenReturn(uiViewRoot);
        when(facesContext.getExternalContext()).thenReturn(externalContext);

        loginBean = new LoginBean(session, userDAO);
    }

    @Test
    public void testLoginSuccess() {
        when(session.isLoggedIn()).thenReturn(false);
        when(userDAO.getUserForUsername("Maria")).thenReturn(dbUser);
        when(dbUser.getPassword()).thenReturn("4321");

        loginBean.setUsername("Maria");
        Whitebox.setInternalState(loginBean, "password", "4321");

        final String expected = "grades.xhtml";
        assertThat(loginBean.login()).isEqualTo(expected);
    }

    @Test
    public void testLoginWithAlreadyLoggedInUser() {
        when(session.isLoggedIn()).thenReturn(true);

        assertThat(loginBean.login()).isNull();
    }

    @Test
    public void testLoginForUnknownUser() {
        when(session.isLoggedIn()).thenReturn(false);
        when(userDAO.getUserForUsername("Maria")).thenReturn(null);
        when(session.getUser()).thenReturn(theUser);

        loginBean.setUsername("Maria");

        assertThat(loginBean.login()).isNull();
    }

    @Test
    public void testLoginForWrongPassword() {
        when(session.isLoggedIn()).thenReturn(false);
        when(userDAO.getUserForUsername("Maria")).thenReturn(dbUser);
        when(dbUser.getPassword()).thenReturn("123");

        loginBean.setUsername("Maria");
        Whitebox.setInternalState(loginBean, "password", "abc");

        assertThat(loginBean.login()).isNull();
    }

    @Test
    public void testLogoutWithoutLogin() {
        when(session.isLoggedIn()).thenReturn(false);

        assertThat(loginBean.logout()).isNull();
    }

    @Test
    public void testLogout() {
        when(session.isLoggedIn()).thenReturn(true);

        final String expected = "/index.xhtml?faces-redirect=true";
        assertThat(loginBean.logout()).isEqualTo(expected);
    }

}
