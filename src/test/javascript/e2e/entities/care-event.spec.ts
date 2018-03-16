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

describe('CareEvent e2e test', () => {

    let navBarPage: NavBarPage;
    let careEventDialogPage: CareEventDialogPage;
    let careEventComponentsPage: CareEventComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load CareEvents', () => {
        navBarPage.goToEntity('care-event');
        careEventComponentsPage = new CareEventComponentsPage();
        expect(careEventComponentsPage.getTitle()).toMatch(/Care Events/);

    });

    it('should load create CareEvent dialog', () => {
        careEventComponentsPage.clickOnCreateButton();
        careEventDialogPage = new CareEventDialogPage();
        expect(careEventDialogPage.getModalTitle()).toMatch(/Create or edit a Care Event/);
        careEventDialogPage.close();
    });

   /* it('should create and save CareEvents', () => {
        careEventComponentsPage.clickOnCreateButton();
        careEventDialogPage.typeSelectLastOption();
        careEventDialogPage.timepointSelectLastOption();
        careEventDialogPage.patientSelectLastOption();
        careEventDialogPage.followupPlanSelectLastOption();
        careEventDialogPage.save();
        expect(careEventDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CareEventComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-care-event div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class CareEventDialogPage {
    modalTitle = element(by.css('h4#myCareEventLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    typeSelect = element(by.css('select#field_type'));
    timepointSelect = element(by.css('select#field_timepoint'));
    patientSelect = element(by.css('select#field_patient'));
    followupPlanSelect = element(by.css('select#field_followupPlan'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setTypeSelect = function (type) {
        this.typeSelect.sendKeys(type);
    }

    getTypeSelect = function () {
        return this.typeSelect.element(by.css('option:checked')).getText();
    }

    typeSelectLastOption = function () {
        this.typeSelect.all(by.tagName('option')).last().click();
    }
    timepointSelectLastOption = function () {
        this.timepointSelect.all(by.tagName('option')).last().click();
    }

    timepointSelectOption = function (option) {
        this.timepointSelect.sendKeys(option);
    }

    getTimepointSelect = function () {
        return this.timepointSelect;
    }

    getTimepointSelectedOption = function () {
        return this.timepointSelect.element(by.css('option:checked')).getText();
    }

    patientSelectLastOption = function () {
        this.patientSelect.all(by.tagName('option')).last().click();
    }

    patientSelectOption = function (option) {
        this.patientSelect.sendKeys(option);
    }

    getPatientSelect = function () {
        return this.patientSelect;
    }

    getPatientSelectedOption = function () {
        return this.patientSelect.element(by.css('option:checked')).getText();
    }

    followupPlanSelectLastOption = function () {
        this.followupPlanSelect.all(by.tagName('option')).last().click();
    }

    followupPlanSelectOption = function (option) {
        this.followupPlanSelect.sendKeys(option);
    }

    getFollowupPlanSelect = function () {
        return this.followupPlanSelect;
    }

    getFollowupPlanSelectedOption = function () {
        return this.followupPlanSelect.element(by.css('option:checked')).getText();
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
