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
import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { PasswordStrengthBarComponent } from '../../../../../../main/webapp/app/account/password/password-strength-bar.component';

describe('Component Tests', () => {

    describe('PasswordStrengthBarComponent', () => {

        let comp: PasswordStrengthBarComponent;
        let fixture: ComponentFixture<PasswordStrengthBarComponent>;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                declarations: [PasswordStrengthBarComponent]
            }).overrideTemplate(PasswordStrengthBarComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PasswordStrengthBarComponent);
            comp = fixture.componentInstance;
        });

        describe('PasswordStrengthBarComponents', () => {
            it('should initialize with default values', () => {
                expect(comp.measureStrength('')).toBe(0);
                expect(comp.colors).toEqual(['#F00', '#F90', '#FF0', '#9F0', '#0F0']);
                expect(comp.getColor(0).idx).toBe(1);
                expect(comp.getColor(0).col).toBe(comp.colors[0]);
            });

            it('should increase strength upon password value change', () => {
                expect(comp.measureStrength('')).toBe(0);
                expect(comp.measureStrength('aa')).toBeGreaterThanOrEqual(comp.measureStrength(''));
                expect(comp.measureStrength('aa^6')).toBeGreaterThanOrEqual(comp.measureStrength('aa'));
                expect(comp.measureStrength('Aa090(**)')).toBeGreaterThanOrEqual(comp.measureStrength('aa^6'));
                expect(comp.measureStrength('Aa090(**)+-07365')).toBeGreaterThanOrEqual(comp.measureStrength('Aa090(**)'));
            });

             it('should change the color based on strength', () => {
                expect(comp.getColor(0).col).toBe(comp.colors[0]);
                expect(comp.getColor(11).col).toBe(comp.colors[1]);
                expect(comp.getColor(22).col).toBe(comp.colors[2]);
                expect(comp.getColor(33).col).toBe(comp.colors[3]);
                expect(comp.getColor(44).col).toBe(comp.colors[4]);
            });
        });
    });
});
