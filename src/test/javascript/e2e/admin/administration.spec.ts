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
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('administration', () => {

    let navBarPage: NavBarPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage(true);
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    beforeEach(() => {
        navBarPage.clickOnAdminMenu();
    });
    it('should load user management', () => {
        navBarPage.clickOnAdmin("user-management");
        const expect1 = /Users/;
        element.all(by.css('h2 span')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    it('should load metrics', () => {
        navBarPage.clickOnAdmin("jhi-metrics");
        const expect1 = /Application Metrics/;
        element.all(by.css('h2 span')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    it('should load health', () => {
        navBarPage.clickOnAdmin("jhi-health");
        const expect1 = /Health Checks/;
        element.all(by.css('h2 span')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    it('should load configuration', () => {
        navBarPage.clickOnAdmin("jhi-configuration");
        const expect1 = /Configuration/;
        element.all(by.css('h2')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    it('should load audits', () => {
        navBarPage.clickOnAdmin("audits");
        const expect1 = /Audits/;
        element.all(by.css('h2')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    it('should load logs', () => {
        navBarPage.clickOnAdmin("logs");
        const expect1 = /Logs/;
        element.all(by.css('h2')).first().getText().then((value) => {
            expect(value).toMatch(expect1);
        });
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
