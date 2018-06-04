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

describe('Address e2e test', () => {

    let navBarPage: NavBarPage;
    let addressDialogPage: AddressDialogPage;
    let addressComponentsPage: AddressComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Addresses', () => {
        navBarPage.goToEntity('address');
        addressComponentsPage = new AddressComponentsPage();
        expect(addressComponentsPage.getTitle()).toMatch(/Addresses/);

    });

    it('should load create Address dialog', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage = new AddressDialogPage();
        expect(addressDialogPage.getModalTitle()).toMatch(/Create or edit a Address/);
        addressDialogPage.close();
    });

    it('should create and save Addresses', () => {
        addressComponentsPage.clickOnCreateButton();
        addressDialogPage.setStreetInput('street');
        expect(addressDialogPage.getStreetInput()).toMatch('street');
        addressDialogPage.setLineInput('line');
        expect(addressDialogPage.getLineInput()).toMatch('line');
        addressDialogPage.setCityInput('city');
        expect(addressDialogPage.getCityInput()).toMatch('city');
        addressDialogPage.setCountyInput('county');
        expect(addressDialogPage.getCountyInput()).toMatch('county');
        addressDialogPage.setPostalCodeInput('postalCode');
        expect(addressDialogPage.getPostalCodeInput()).toMatch('postalCode');
        addressDialogPage.setCountryInput('country');
        expect(addressDialogPage.getCountryInput()).toMatch('country');
        addressDialogPage.patientSelectLastOption();
        addressDialogPage.save();
        expect(addressDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AddressComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-address div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getText();
    }
}

export class AddressDialogPage {
    modalTitle = element(by.css('h4#myAddressLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    streetInput = element(by.css('input#field_street'));
    lineInput = element(by.css('input#field_line'));
    cityInput = element(by.css('input#field_city'));
    countyInput = element(by.css('input#field_county'));
    postalCodeInput = element(by.css('input#field_postalCode'));
    countryInput = element(by.css('input#field_country'));
    patientSelect = element(by.css('select#field_patient'));

    getModalTitle() {
        return this.modalTitle.getText();
    }

    setStreetInput = function (street) {
        this.streetInput.sendKeys(street);
    }

    getStreetInput = function () {
        return this.streetInput.getAttribute('value');
    }

    setLineInput = function (line) {
        this.lineInput.sendKeys(line);
    }

    getLineInput = function () {
        return this.lineInput.getAttribute('value');
    }

    setCityInput = function (city) {
        this.cityInput.sendKeys(city);
    }

    getCityInput = function () {
        return this.cityInput.getAttribute('value');
    }

    setCountyInput = function (county) {
        this.countyInput.sendKeys(county);
    }

    getCountyInput = function () {
        return this.countyInput.getAttribute('value');
    }

    setPostalCodeInput = function (postalCode) {
        this.postalCodeInput.sendKeys(postalCode);
    }

    getPostalCodeInput = function () {
        return this.postalCodeInput.getAttribute('value');
    }

    setCountryInput = function (country) {
        this.countryInput.sendKeys(country);
    }

    getCountryInput = function () {
        return this.countryInput.getAttribute('value');
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
