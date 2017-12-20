import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('FollowupAction e2e test', () => {

    let navBarPage: NavBarPage;
    let followupActionDialogPage: FollowupActionDialogPage;
    let followupActionComponentsPage: FollowupActionComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load FollowupActions', () => {
        navBarPage.goToEntity('followup-action');
        followupActionComponentsPage = new FollowupActionComponentsPage();
        expect(followupActionComponentsPage.getTitle()).toMatch(/Followup Actions/);

    });

    it('should load create FollowupAction dialog', () => {
        followupActionComponentsPage.clickOnCreateButton();
        followupActionDialogPage = new FollowupActionDialogPage();
        expect(followupActionDialogPage.getModalTitle()).toMatch(/Create or edit a Followup Action/);
        followupActionDialogPage.close();
    });

   /* it('should create and save FollowupActions', () => {
        followupActionComponentsPage.clickOnCreateButton();
        followupActionDialogPage.phaseSelectLastOption();
        followupActionDialogPage.setScheduledDateInput(12310020012301);
        expect(followupActionDialogPage.getScheduledDateInput()).toMatch('2001-12-31T02:30');
        followupActionDialogPage.setNameInput('name');
        expect(followupActionDialogPage.getNameInput()).toMatch('name');
        followupActionDialogPage.typeSelectLastOption();
        followupActionDialogPage.setOutcomeScoreInput('5');
        expect(followupActionDialogPage.getOutcomeScoreInput()).toMatch('5');
        followupActionDialogPage.setOutcomeCommentInput('outcomeComment');
        expect(followupActionDialogPage.getOutcomeCommentInput()).toMatch('outcomeComment');
        followupActionDialogPage.setCompletedDateInput(12310020012301);
        expect(followupActionDialogPage.getCompletedDateInput()).toMatch('2001-12-31T02:30');
        followupActionDialogPage.followupPlanSelectLastOption();
        followupActionDialogPage.patientSelectLastOption();
        followupActionDialogPage.questionnaireSelectLastOption();
        followupActionDialogPage.save();
        expect(followupActionDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FollowupActionComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-followup-action div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class FollowupActionDialogPage {
    modalTitle = element(by.css('h4#myFollowupActionLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    phaseSelect = element(by.css('select#field_phase'));
    scheduledDateInput = element(by.css('input#field_scheduledDate'));
    nameInput = element(by.css('input#field_name'));
    typeSelect = element(by.css('select#field_type'));
    outcomeScoreInput = element(by.css('input#field_outcomeScore'));
    outcomeCommentInput = element(by.css('input#field_outcomeComment'));
    completedDateInput = element(by.css('input#field_completedDate'));
    followupPlanSelect = element(by.css('select#field_followupPlan'));
    patientSelect = element(by.css('select#field_patient'));
    questionnaireSelect = element(by.css('select#field_questionnaire'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setPhaseSelect = function (phase) {
        this.phaseSelect.sendKeys(phase);
    }

    getPhaseSelect = function () {
        return this.phaseSelect.element(by.css('option:checked')).getText();
    }

    phaseSelectLastOption = function () {
        this.phaseSelect.all(by.tagName('option')).last().click();
    }
    setScheduledDateInput = function (scheduledDate) {
        this.scheduledDateInput.sendKeys(scheduledDate);
    }

    getScheduledDateInput = function () {
        return this.scheduledDateInput.getAttribute('value');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
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
    setOutcomeScoreInput = function (outcomeScore) {
        this.outcomeScoreInput.sendKeys(outcomeScore);
    }

    getOutcomeScoreInput = function () {
        return this.outcomeScoreInput.getAttribute('value');
    }

    setOutcomeCommentInput = function (outcomeComment) {
        this.outcomeCommentInput.sendKeys(outcomeComment);
    }

    getOutcomeCommentInput = function () {
        return this.outcomeCommentInput.getAttribute('value');
    }

    setCompletedDateInput = function (completedDate) {
        this.completedDateInput.sendKeys(completedDate);
    }

    getCompletedDateInput = function () {
        return this.completedDateInput.getAttribute('value');
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
