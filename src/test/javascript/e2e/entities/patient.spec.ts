import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Patient e2e test', () => {

    let navBarPage: NavBarPage;
    let patientDialogPage: PatientDialogPage;
    let patientComponentsPage: PatientComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Patients', () => {
        navBarPage.goToEntity('patient');
        patientComponentsPage = new PatientComponentsPage();
        expect(patientComponentsPage.getTitle()).toMatch(/Patients/);

    });

    it('should load create Patient dialog', () => {
        patientComponentsPage.clickOnCreateButton();
        patientDialogPage = new PatientDialogPage();
        expect(patientDialogPage.getModalTitle()).toMatch(/Create or edit a Patient/);
        patientDialogPage.close();
    });

    it('should create and save Patients', () => {
        patientComponentsPage.clickOnCreateButton();
        patientDialogPage.setFamilyNameInput('familyName');
        expect(patientDialogPage.getFamilyNameInput()).toMatch('familyName');
        patientDialogPage.setGivenNameInput('givenName');
        expect(patientDialogPage.getGivenNameInput()).toMatch('givenName');
        patientDialogPage.setBirthDateInput(12310020012301);
        expect(patientDialogPage.getBirthDateInput()).toMatch('2001-12-31T02:30');
        patientDialogPage.genderSelectLastOption();
        patientDialogPage.setNhsNumberInput('5');
        expect(patientDialogPage.getNhsNumberInput()).toMatch('5');
        patientDialogPage.setEmailInput('email');
        expect(patientDialogPage.getEmailInput()).toMatch('email');
        patientDialogPage.save();
        expect(patientDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PatientComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-patient div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class PatientDialogPage {
    modalTitle = element(by.css('h4#myPatientLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    familyNameInput = element(by.css('input#field_familyName'));
    givenNameInput = element(by.css('input#field_givenName'));
    birthDateInput = element(by.css('input#field_birthDate'));
    genderSelect = element(by.css('select#field_gender'));
    nhsNumberInput = element(by.css('input#field_nhsNumber'));
    emailInput = element(by.css('input#field_email'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setFamilyNameInput = function (familyName) {
        this.familyNameInput.sendKeys(familyName);
    }

    getFamilyNameInput = function () {
        return this.familyNameInput.getAttribute('value');
    }

    setGivenNameInput = function (givenName) {
        this.givenNameInput.sendKeys(givenName);
    }

    getGivenNameInput = function () {
        return this.givenNameInput.getAttribute('value');
    }

    setBirthDateInput = function (birthDate) {
        this.birthDateInput.sendKeys(birthDate);
    }

    getBirthDateInput = function () {
        return this.birthDateInput.getAttribute('value');
    }

    setGenderSelect = function (gender) {
        this.genderSelect.sendKeys(gender);
    }

    getGenderSelect = function () {
        return this.genderSelect.element(by.css('option:checked')).getText();
    }

    genderSelectLastOption = function () {
        this.genderSelect.all(by.tagName('option')).last().click();
    }
    setNhsNumberInput = function (nhsNumber) {
        this.nhsNumberInput.sendKeys(nhsNumber);
    }

    getNhsNumberInput = function () {
        return this.nhsNumberInput.getAttribute('value');
    }

    setEmailInput = function (email) {
        this.emailInput.sendKeys(email);
    }

    getEmailInput = function () {
        return this.emailInput.getAttribute('value');
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
