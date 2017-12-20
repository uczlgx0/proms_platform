import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Procedure e2e test', () => {

    let navBarPage: NavBarPage;
    let procedureDialogPage: ProcedureDialogPage;
    let procedureComponentsPage: ProcedureComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Procedures', () => {
        navBarPage.goToEntity('procedure');
        procedureComponentsPage = new ProcedureComponentsPage();
        expect(procedureComponentsPage.getTitle()).toMatch(/Procedures/);

    });

    it('should load create Procedure dialog', () => {
        procedureComponentsPage.clickOnCreateButton();
        procedureDialogPage = new ProcedureDialogPage();
        expect(procedureDialogPage.getModalTitle()).toMatch(/Create or edit a Procedure/);
        procedureDialogPage.close();
    });

    it('should create and save Procedures', () => {
        procedureComponentsPage.clickOnCreateButton();
        procedureDialogPage.setNameInput('name');
        expect(procedureDialogPage.getNameInput()).toMatch('name');
        procedureDialogPage.setExternalCodeInput('externalCode');
        expect(procedureDialogPage.getExternalCodeInput()).toMatch('externalCode');
        procedureDialogPage.setLocalCodeInput('5');
        expect(procedureDialogPage.getLocalCodeInput()).toMatch('5');
        procedureDialogPage.save();
        expect(procedureDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProcedureComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-procedure div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class ProcedureDialogPage {
    modalTitle = element(by.css('h4#myProcedureLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    externalCodeInput = element(by.css('input#field_externalCode'));
    localCodeInput = element(by.css('input#field_localCode'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setExternalCodeInput = function (externalCode) {
        this.externalCodeInput.sendKeys(externalCode);
    }

    getExternalCodeInput = function () {
        return this.externalCodeInput.getAttribute('value');
    }

    setLocalCodeInput = function (localCode) {
        this.localCodeInput.sendKeys(localCode);
    }

    getLocalCodeInput = function () {
        return this.localCodeInput.getAttribute('value');
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
