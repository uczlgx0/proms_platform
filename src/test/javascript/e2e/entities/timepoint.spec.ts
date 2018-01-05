import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Timepoint e2e test', () => {

    let navBarPage: NavBarPage;
    let timepointDialogPage: TimepointDialogPage;
    let timepointComponentsPage: TimepointComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Timepoints', () => {
        navBarPage.goToEntity('timepoint');
        timepointComponentsPage = new TimepointComponentsPage();
        expect(timepointComponentsPage.getTitle()).toMatch(/Timepoints/);

    });

    it('should load create Timepoint dialog', () => {
        timepointComponentsPage.clickOnCreateButton();
        timepointDialogPage = new TimepointDialogPage();
        expect(timepointDialogPage.getModalTitle()).toMatch(/Create or edit a Timepoint/);
        timepointDialogPage.close();
    });

    it('should create and save Timepoints', () => {
        timepointComponentsPage.clickOnCreateButton();
        timepointDialogPage.setNameInput('name');
        expect(timepointDialogPage.getNameInput()).toMatch('name');
        timepointDialogPage.setValueInput('5');
        expect(timepointDialogPage.getValueInput()).toMatch('5');
        timepointDialogPage.unitSelectLastOption();
        timepointDialogPage.save();
        expect(timepointDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class TimepointComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-timepoint div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class TimepointDialogPage {
    modalTitle = element(by.css('h4#myTimepointLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    valueInput = element(by.css('input#field_value'));
    unitSelect = element(by.css('select#field_unit'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setValueInput = function (value) {
        this.valueInput.sendKeys(value);
    }

    getValueInput = function () {
        return this.valueInput.getAttribute('value');
    }

    setUnitSelect = function (unit) {
        this.unitSelect.sendKeys(unit);
    }

    getUnitSelect = function () {
        return this.unitSelect.element(by.css('option:checked')).getText();
    }

    unitSelectLastOption = function () {
        this.unitSelect.all(by.tagName('option')).last().click();
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
