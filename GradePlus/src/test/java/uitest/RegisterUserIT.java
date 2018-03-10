package uitest;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import uitest.fragments.MessageFragment;
import uitest.fragments.TableFragment;
import uitest.fragments.TopMenuFragment;
import uitest.pages.LoginPage;
import uitest.pages.RegisterUserPage;

/**
 * Testet den Anwendungsfall "Benutzer registrieren".
 *
 * <pre>
 * Vorbedingung:
 * - Benutzer ist als Standard-Benutzer eingeloggt.
 * - Seite "admin/users.xhtml" wird angezeigt. Darauf sind drei Formularfelder zur Eingabe
 * von Username, Passwort und Email-Adresse sowie eine Schaltfläche zum Speichern zu
 * sehen. Darunter erscheint eine Tabelle, in der die im System vorhandenen User
 * zeilenweise dargestellt sind. In jeder Zeile erscheinen User-ID, Username, Passworthash
 * und Email-Adresse sowie eine Schaltfläche zum Löschen des jeweiligen Users.
 * 
 * regulärer Ablauf:
 * 1. User gibt in die Formularfelder Username, Passwort und Email-Adresse ein und klickt
 * die Schaltfläche zum Speichern an. Das Passwort wird dabei nicht im Klartext im
 * Eingabefeld angezeigt.
 * 
 * Nachbedingungen:
 * - Die Seite "admin/users.xhtml" wird angezeigt. Die Formularfelder sind leer und es
 * wird eine User-Tabelle angezeigt. Darin befindet sich nun mindestens der eben registrierte
 * User mit den zugehörigen Daten.
 * - User existiert nun im System und kann sich nun mit Username und Passwort einloggen.
 * 
 * Fehlerfälle:
 * 1a: Ein oder mehrere Formularfelder sind nicht ausgefüllt (also leer) wenn die
 * Schaltfläche zum Speichern betätigt wird.
 * Nachbedingung: Die Seite "admin/users.xhtml" wird angezeigt. Für jedes nicht ausgefüllte
 * Datenfeld erscheint jeweils eine Fehlermeldung, die darauf hinweist, dass das
 * entsprechende Formularfeld ausgefüllt werden muss. Der Datenbestand des Systems ist
 * unverändert gegenüber dem Datenbestand vor Ausführung des Anwendungsfalls.
 * 1b: Der Username ist bereits im System vergeben.
 * Nachbedingung: Die Seite "admin/users.xhtml" wird angezeigt. Darauf erscheint
 * eine Fehlermeldung, dass es den Benutzernamen bereits im System gibt.
 * 1c: Die Email-Adresse ist bereits im System vergeben.
 * Nachbedingung: Die Seite "admin/users.xhtml" wird angezeigt. Darauf erscheint
 * eine Fehlermeldung, dass es die Email-Adresse bereits im System gibt.
 * </pre>
 *
 * @author Karsten Hölscher
 * @version 2017-06-28
 */
@RunWith(Arquillian.class)
public class RegisterUserIT {

    private static final String WEBAPP_SRC = "src/main/webapp/";

    /**
     * Der Username des Standard-Benutzers ( {@link persistence.DBInit#DEFAULT_USER_NAME}
     * ).
     */
    private static final String DEFAULT_USER_NAME = "admin";

    /**
     * Das Passwort des Standard-Benutzers (
     * {@link persistence.DBInit#DEFAULT_USER_PASSWORD}).
     */
    private static final String DEFAULT_USER_PASSWORD = "123";

    /**
     * Die Email-Adresse des Standard-Benutzers (
     * {@link persistence.DBInit#DEFAULT_USER_EMAIL}).
     */
    private static final String DEFAULT_USER_EMAIL = "admin@offline.de";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "gradelog.war")
                .addPackages(true, "de.unibremen.st.gradelog")
                .addAsWebResource(new File(WEBAPP_SRC + "admin/", "users.xhtml"),
                        "admin/users.xhtml")
                .addAsResource("ui-test-embedded-persistence.xml",
                        "META-INF/persistence.xml")
                .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "grades.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "profile.xhtml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/faces-config.xml"))
                .addAsWebInfResource(new File(WEBAPP_SRC, "WEB-INF/template.xhtml"))
                .setWebXML(new File("src/test/resources/web.xml"));
    }

    @Page
    private RegisterUserPage testPage;

    @Page
    private LoginPage indexPage;

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentURL;

    @Before
    public void loginAndLoadTestPage() {
        // first login as default user
        indexPage = Graphene.goTo(LoginPage.class);
        indexPage.login(DEFAULT_USER_NAME, DEFAULT_USER_PASSWORD);
        // now load the page to test
        testPage = Graphene.goTo(RegisterUserPage.class);
    }

    @After
    public void logout() {
        TopMenuFragment topMenu = testPage.getTopMenu();
        topMenu.logout();
    }

    @Test
    @InSequence(1)
    public void testAllFieldsEmpty() {
        final boolean[] errorDisplay = { true, true, true };
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    /**
     * ******************************************************************
     *
     * ONE field empty
     *
     * *******************************************************************
     */
    @Test
    @InSequence(2)
    public void testUsernameFieldEmpty() {
        final boolean[] errorDisplay = { true, false, false };
        testPage.setPassword("123");
        testPage.setEmail("nobody@online.com");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(3)
    public void testPasswordFieldEmpty() {
        final boolean[] errorDisplay = { false, true, false };
        testPage.setUsername("userA");
        testPage.setEmail("nobody@online.com");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(4)
    public void testEmailFieldEmpty() {
        final boolean[] errorDisplay = { false, false, true };
        testPage.setUsername("userA");
        testPage.setPassword("123");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    /**
     * ******************************************************************
     *
     * TWO fields empty
     *
     * *******************************************************************
     */
    @Test
    @InSequence(5)
    public void testUsernameAndPasswordFieldEmpty() {
        final boolean[] errorDisplay = { true, true, false };
        testPage.setEmail("usera@online.com");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(6)
    public void testUsernameAndEmailFieldEmpty() {
        final boolean[] errorDisplay = { true, false, true };
        testPage.setPassword("123");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(7)
    public void testPasswordAndEmailFieldEmpty() {
        final boolean[] errorDisplay = { false, true, true };
        testPage.setUsername("userA");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(8)
    public void testExistingUsername() {
        final boolean[] errorDisplay = { true, false, false };
        testPage.setUsername(DEFAULT_USER_NAME);
        testPage.setPassword("abc");
        testPage.setEmail("userA@offline.de");
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(9)
    public void testExistingEmail() {
        final boolean[] errorDisplay = { false, false, true };
        testPage.setUsername("userA");
        testPage.setPassword("abc");
        testPage.setEmail(DEFAULT_USER_EMAIL);
        testPage.save();
        checkErrorMessages(errorDisplay);
    }

    @Test
    @InSequence(10)
    public void testRegisterUserAndLogin() {
        final boolean[] errorDisplay = { false, false, false };
        final String[] userInfo = { "userA", "123", "usera@online.com" };
        final String[] expected = { "userA",
                "202cb962ac59075b964b07152d234b70".toUpperCase(), "usera@online.com" };
        final TableFragment userTable = testPage.getUserTable();
        final int rows = userTable.getNumberOfRows();
        assertThat(rows).isEqualTo(1);
        final String existingUserAtPre = userTable.rowToString(0, "##");

        enterUserInfo(userInfo);
        testPage.save();
        // no error messages
        checkErrorMessages(errorDisplay);
        final TableFragment newUserTable = testPage.getUserTable();
        // there should be two rows in the table body
        final int newRows = newUserTable.getNumberOfRows();
        assertThat(newRows).isEqualTo(2);
        // check that user@pre is still there
        final String existingUser = userTable.rowToString(0, "##");
        assertThat(existingUser).isEqualTo(existingUserAtPre);

        for (int i = 0; i < expected.length; i++) {
            final String text = userTable.getTextAt(1, i + 1);
            assertThat(expected[i]).isEqualTo(text);
        }
        // There should also be a remove button in the table.
        assertThat(testPage.isRemoveUserButtonDisplayed(1)).isTrue();

        // now log out
        final TopMenuFragment topMenuFragment = testPage.getTopMenu();
        topMenuFragment.logout();

        // registered user should now be able to login
        final LoginPage loginPage = Graphene.goTo(LoginPage.class);
        loginPage.login(userInfo[0], userInfo[1]);

        // it is ok if we find a logout button...
        try {
            browser.findElement(By.id("menue:logoutButton"));
        } catch (final Exception e) {
            fail("Login was not successful after registration: " + e.getMessage());
        }
    }

    private void enterUserInfo(final String[] userInfo) {
        testPage.setUsername(userInfo[0]);
        testPage.setPassword(userInfo[1]);
        testPage.setEmail(userInfo[2]);
    }

    private void checkErrorMessages(final boolean[] errorDisplay) {
        MessageFragment[] errorMessages = testPage.getErrorMessageFragments();

        for (int i = 0; i < errorMessages.length; i++) {
            if (errorDisplay[i]) {
                assertThat(errorMessages[i].isDisplayed()).isTrue();
                assertThat(errorMessages[i].getMessage()).isNotEmpty();
            } else {
                assertThat(errorMessages[i].isDisplayed()).isFalse();
                assertThat(errorMessages[i].getMessage()).isEmpty();
            }
        }
    }

}
