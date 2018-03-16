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


describe('Procedurelink e2e test', () => {

    let navBarPage: NavBarPage;
    let procedurelinkDialogPage: ProcedurelinkDialogPage;
    let procedurelinkComponentsPage: ProcedurelinkComponentsPage;


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Procedurelinks', () => {
        navBarPage.goToEntity('procedurelink');
        procedurelinkComponentsPage = new ProcedurelinkComponentsPage();
        expect(procedurelinkComponentsPage.getTitle()).toMatch(/Procedurelinks/);

    });

    it('should load create Procedurelink dialog', () => {
        procedurelinkComponentsPage.clickOnCreateButton();
        procedurelinkDialogPage = new ProcedurelinkDialogPage();
        expect(procedurelinkDialogPage.getModalTitle()).toMatch(/Create or edit a Procedurelink/);
        procedurelinkDialogPage.close();
    });

   /* it('should create and save Procedurelinks', () => {
        procedurelinkComponentsPage.clickOnCreateButton();
        procedurelinkDialogPage.procedureSelectLastOption();
        procedurelinkDialogPage.questionnaireSelectLastOption();
        procedurelinkDialogPage.save();
        expect(procedurelinkDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProcedurelinkComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-procedurelink div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class ProcedurelinkDialogPage {
    modalTitle = element(by.css('h4#myProcedurelinkLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    procedureSelect = element(by.css('select#field_procedure'));
    questionnaireSelect = element(by.css('select#field_questionnaire'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    procedureSelectLastOption = function () {
        this.procedureSelect.all(by.tagName('option')).last().click();
    }

    procedureSelectOption = function (option) {
        this.procedureSelect.sendKeys(option);
    }

    getProcedureSelect = function () {
        return this.procedureSelect;
    }

    getProcedureSelectedOption = function () {
        return this.procedureSelect.element(by.css('option:checked')).getText();
    }

    questionnaireSelectLastOption = function () {
        this.questionnaireSelect.all(by.tagName('option')).last().click();
    }

    questionnaireSelectOption = function (option) {
        this.questionnaireSelect.sendKeys(option);
    }

    getQuestionnaireSelect = function () {
        return this.questionnaireSelect;
    }

    getQuestionnaireSelectedOption = function () {
        return this.questionnaireSelect.element(by.css('option:checked')).getText();
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
