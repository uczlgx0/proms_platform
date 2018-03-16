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
import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('HealthcareProvider e2e test', () => {

    let navBarPage: NavBarPage;
    let healthcareProviderDialogPage: HealthcareProviderDialogPage;
    let healthcareProviderComponentsPage: HealthcareProviderComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load HealthcareProviders', () => {
        navBarPage.goToEntity('healthcare-provider');
        healthcareProviderComponentsPage = new HealthcareProviderComponentsPage();
        expect(healthcareProviderComponentsPage.getTitle()).toMatch(/Healthcare Providers/);

    });

    it('should load create HealthcareProvider dialog', () => {
        healthcareProviderComponentsPage.clickOnCreateButton();
        healthcareProviderDialogPage = new HealthcareProviderDialogPage();
        expect(healthcareProviderDialogPage.getModalTitle()).toMatch(/Create or edit a Healthcare Provider/);
        healthcareProviderDialogPage.close();
    });

    it('should create and save HealthcareProviders', () => {
        healthcareProviderComponentsPage.clickOnCreateButton();
        healthcareProviderDialogPage.setNameInput('name');
        expect(healthcareProviderDialogPage.getNameInput()).toMatch('name');
        healthcareProviderDialogPage.save();
        expect(healthcareProviderDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class HealthcareProviderComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-healthcare-provider div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class HealthcareProviderDialogPage {
    modalTitle = element(by.css('h4#myHealthcareProviderLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
