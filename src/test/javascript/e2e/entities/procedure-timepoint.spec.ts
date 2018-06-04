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


describe('ProcedureTimepoint e2e test', () => {

    let navBarPage: NavBarPage;
    let procedureTimepointDialogPage: ProcedureTimepointDialogPage;
    let procedureTimepointComponentsPage: ProcedureTimepointComponentsPage;


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ProcedureTimepoints', () => {
        navBarPage.goToEntity('procedure-timepoint');
        procedureTimepointComponentsPage = new ProcedureTimepointComponentsPage();
        expect(procedureTimepointComponentsPage.getTitle()).toMatch(/Procedure Timepoints/);

    });

    it('should load create ProcedureTimepoint dialog', () => {
        procedureTimepointComponentsPage.clickOnCreateButton();
        procedureTimepointDialogPage = new ProcedureTimepointDialogPage();
        expect(procedureTimepointDialogPage.getModalTitle()).toMatch(/Create or edit a Procedure Timepoint/);
        procedureTimepointDialogPage.close();
    });

   /* it('should create and save ProcedureTimepoints', () => {
        procedureTimepointComponentsPage.clickOnCreateButton();
        procedureTimepointDialogPage.procedureSelectLastOption();
        procedureTimepointDialogPage.timepointSelectLastOption();
        procedureTimepointDialogPage.save();
        expect(procedureTimepointDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProcedureTimepointComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-procedure-timepoint div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class ProcedureTimepointDialogPage {
    modalTitle = element(by.css('h4#myProcedureTimepointLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    procedureSelect = element(by.css('select#field_procedure'));
    timepointSelect = element(by.css('select#field_timepoint'));

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
