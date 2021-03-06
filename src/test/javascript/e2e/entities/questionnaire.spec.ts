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

describe('Questionnaire e2e test', () => {

    let navBarPage: NavBarPage;
    let questionnaireDialogPage: QuestionnaireDialogPage;
    let questionnaireComponentsPage: QuestionnaireComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Questionnaires', () => {
        navBarPage.goToEntity('questionnaire');
        questionnaireComponentsPage = new QuestionnaireComponentsPage();
        expect(questionnaireComponentsPage.getTitle()).toMatch(/Questionnaires/);

    });

    it('should load create Questionnaire dialog', () => {
        questionnaireComponentsPage.clickOnCreateButton();
        questionnaireDialogPage = new QuestionnaireDialogPage();
        expect(questionnaireDialogPage.getModalTitle()).toMatch(/Create or edit a Questionnaire/);
        questionnaireDialogPage.close();
    });

    it('should create and save Questionnaires', () => {
        questionnaireComponentsPage.clickOnCreateButton();
        questionnaireDialogPage.setNameInput('name');
        expect(questionnaireDialogPage.getNameInput()).toMatch('name');
        questionnaireDialogPage.setCopyrightInput('copyright');
        expect(questionnaireDialogPage.getCopyrightInput()).toMatch('copyright');
        questionnaireDialogPage.setReferenceInput('reference');
        expect(questionnaireDialogPage.getReferenceInput()).toMatch('reference');
        questionnaireDialogPage.save();
        expect(questionnaireDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class QuestionnaireComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-questionnaire div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class QuestionnaireDialogPage {
    modalTitle = element(by.css('h4#myQuestionnaireLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    copyrightInput = element(by.css('input#field_copyright'));
    referenceInput = element(by.css('input#field_reference'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setCopyrightInput = function (copyright) {
        this.copyrightInput.sendKeys(copyright);
    }

    getCopyrightInput = function () {
        return this.copyrightInput.getAttribute('value');
    }

    setReferenceInput = function (reference) {
        this.referenceInput.sendKeys(reference);
    }

    getReferenceInput = function () {
        return this.referenceInput.getAttribute('value');
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
