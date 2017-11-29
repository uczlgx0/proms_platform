import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('ProcedureBooking e2e test', () => {

    let navBarPage: NavBarPage;
    let procedureBookingDialogPage: ProcedureBookingDialogPage;
    let procedureBookingComponentsPage: ProcedureBookingComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ProcedureBookings', () => {
        navBarPage.goToEntity('procedure-booking');
        procedureBookingComponentsPage = new ProcedureBookingComponentsPage();
        expect(procedureBookingComponentsPage.getTitle()).toMatch(/Procedure Bookings/);

    });

    it('should load create ProcedureBooking dialog', () => {
        procedureBookingComponentsPage.clickOnCreateButton();
        procedureBookingDialogPage = new ProcedureBookingDialogPage();
        expect(procedureBookingDialogPage.getModalTitle()).toMatch(/Create or edit a Procedure Booking/);
        procedureBookingDialogPage.close();
    });

   /* it('should create and save ProcedureBookings', () => {
        procedureBookingComponentsPage.clickOnCreateButton();
        procedureBookingDialogPage.setConsultantNameInput('consultantName');
        expect(procedureBookingDialogPage.getConsultantNameInput()).toMatch('consultantName');
        procedureBookingDialogPage.setHospitalSiteInput('hospitalSite');
        expect(procedureBookingDialogPage.getHospitalSiteInput()).toMatch('hospitalSite');
        procedureBookingDialogPage.setScheduledDateInput(12310020012301);
        expect(procedureBookingDialogPage.getScheduledDateInput()).toMatch('2001-12-31T02:30');
        procedureBookingDialogPage.setPerformedDateInput(12310020012301);
        expect(procedureBookingDialogPage.getPerformedDateInput()).toMatch('2001-12-31T02:30');
        procedureBookingDialogPage.setPrimaryProcedureInput('primaryProcedure');
        expect(procedureBookingDialogPage.getPrimaryProcedureInput()).toMatch('primaryProcedure');
        procedureBookingDialogPage.setOtherProceduresInput('otherProcedures');
        expect(procedureBookingDialogPage.getOtherProceduresInput()).toMatch('otherProcedures');
        procedureBookingDialogPage.patientSelectLastOption();
        procedureBookingDialogPage.save();
        expect(procedureBookingDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ProcedureBookingComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-procedure-booking div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class ProcedureBookingDialogPage {
    modalTitle = element(by.css('h4#myProcedureBookingLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    consultantNameInput = element(by.css('input#field_consultantName'));
    hospitalSiteInput = element(by.css('input#field_hospitalSite'));
    scheduledDateInput = element(by.css('input#field_scheduledDate'));
    performedDateInput = element(by.css('input#field_performedDate'));
    primaryProcedureInput = element(by.css('input#field_primaryProcedure'));
    otherProceduresInput = element(by.css('input#field_otherProcedures'));
    patientSelect = element(by.css('select#field_patient'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setConsultantNameInput = function (consultantName) {
        this.consultantNameInput.sendKeys(consultantName);
    }

    getConsultantNameInput = function () {
        return this.consultantNameInput.getAttribute('value');
    }

    setHospitalSiteInput = function (hospitalSite) {
        this.hospitalSiteInput.sendKeys(hospitalSite);
    }

    getHospitalSiteInput = function () {
        return this.hospitalSiteInput.getAttribute('value');
    }

    setScheduledDateInput = function (scheduledDate) {
        this.scheduledDateInput.sendKeys(scheduledDate);
    }

    getScheduledDateInput = function () {
        return this.scheduledDateInput.getAttribute('value');
    }

    setPerformedDateInput = function (performedDate) {
        this.performedDateInput.sendKeys(performedDate);
    }

    getPerformedDateInput = function () {
        return this.performedDateInput.getAttribute('value');
    }

    setPrimaryProcedureInput = function (primaryProcedure) {
        this.primaryProcedureInput.sendKeys(primaryProcedure);
    }

    getPrimaryProcedureInput = function () {
        return this.primaryProcedureInput.getAttribute('value');
    }

    setOtherProceduresInput = function (otherProcedures) {
        this.otherProceduresInput.sendKeys(otherProcedures);
    }

    getOtherProceduresInput = function () {
        return this.otherProceduresInput.getAttribute('value');
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
