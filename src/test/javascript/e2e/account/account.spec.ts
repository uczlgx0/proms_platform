/*-
 * #%L
 * Proms Platform
 * %%
 * Copyright (C) 2017 - 2018 Termlex
 * %%
 * This software is Copyright and Intellectual Property of Termlex Inc Limited.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation as version 3 of the
 * License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 * #L%
 */
import { browser, element, by } from 'protractor';
import { NavBarPage, SignInPage, PasswordPage, SettingsPage} from './../page-objects/jhi-page-objects';

describe('account', () => {

    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let passwordPage: PasswordPage;
    let settingsPage: SettingsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage(true);
        browser.waitForAngular();
    });

    it('should fail to login with bad password', () => {
        const expect1 = /Welcome, Java Hipster!/;
        element.all(by.css('h1')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
        signInPage = navBarPage.getSignInPage();
        signInPage.autoSignInUsing('admin', 'foo');

        const expect2 = /Failed to sign in!/;
        element.all(by.css('.alert-danger')).first().getText().then((value) => {
            expect(value).toMatch(expect2);
        });
    });

    it('should login successfully with admin account', () => {
        const expect1 = /Login/;
        element.all(by.css('.modal-content label')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
        signInPage.clearUserName();
        signInPage.setUserName('admin');
        signInPage.clearPassword();
        signInPage.setPassword('admin');
        signInPage.login();

        browser.waitForAngular();

        const expect2 = /You are logged in as user "admin"/;
        element.all(by.css('.alert-success span')).getText().then((value) => {
            expect(value).toMatch(expect2);
        });
    });
    it('should be able to update settings', () => {
        settingsPage = navBarPage.getSettingsPage();

        const expect1 = /User settings for \[admin\]/;
        settingsPage.getTitle().then((value) => {
            expect(value).toMatch(expect1);
        });
        settingsPage.save();

        const expect2 = /Settings saved!/;
        element.all(by.css('.alert-success')).first().getText().then((value) => {
            expect(value).toMatch(expect2);
        });
    });

    it('should be able to update password', () => {
        passwordPage = navBarPage.getPasswordPage();

        expect(passwordPage.getTitle()).toMatch(/Password for \[admin\]/);

        passwordPage.setPassword('newpassword');
        passwordPage.setConfirmPassword('newpassword');
        passwordPage.save();

        const expect2 = /Password changed!/;
        element.all(by.css('.alert-success')).first().getText().then((value) => {
            expect(value).toMatch(expect2);
        });
        navBarPage.autoSignOut();
        navBarPage.goToSignInPage();
        signInPage.autoSignInUsing('admin', 'newpassword');

        // change back to default
        navBarPage.goToPasswordMenu();
        passwordPage.setPassword('admin');
        passwordPage.setConfirmPassword('admin');
        passwordPage.save();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
