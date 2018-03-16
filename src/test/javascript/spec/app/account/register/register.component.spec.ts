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
import { ComponentFixture, TestBed, async, inject, tick, fakeAsync } from '@angular/core/testing';
import { Renderer, ElementRef } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { LoginModalService, EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '../../../../../../main/webapp/app/shared';
import { Register } from '../../../../../../main/webapp/app/account/register/register.service';
import { RegisterComponent } from '../../../../../../main/webapp/app/account/register/register.component';

describe('Component Tests', () => {

    describe('RegisterComponent', () => {
        let fixture: ComponentFixture<RegisterComponent>;
        let comp: RegisterComponent;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [RegisterComponent],
                providers: [
                    Register,
                    {
                        provide: LoginModalService,
                        useValue: null
                    },
                    {
                        provide: Renderer,
                        useValue: null
                    },
                    {
                        provide: ElementRef,
                        useValue: null
                    }
                ]
            }).overrideTemplate(RegisterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RegisterComponent);
            comp = fixture.componentInstance;
            comp.ngOnInit();
        });

        it('should ensure the two passwords entered match', () => {
            comp.registerAccount.password = 'password';
            comp.confirmPassword = 'non-matching';

            comp.register();

            expect(comp.doNotMatch).toEqual('ERROR');
        });

        it('should update success to OK after creating an account',
            inject([Register],
                fakeAsync((service: Register) => {
                    spyOn(service, 'save').and.returnValue(Observable.of({}));
                    comp.registerAccount.password = comp.confirmPassword = 'password';

                    comp.register();
                    tick();

                    expect(service.save).toHaveBeenCalledWith({
                        password: 'password',
                        langKey: 'en'
                    });
                    expect(comp.success).toEqual(true);
                    expect(comp.registerAccount.langKey).toEqual('en');
                    expect(comp.errorUserExists).toBeNull();
                    expect(comp.errorEmailExists).toBeNull();
                    expect(comp.error).toBeNull();
                })
            )
        );

        it('should notify of user existence upon 400/login already in use',
            inject([Register],
                fakeAsync((service: Register) => {
                    spyOn(service, 'save').and.returnValue(Observable.throw({
                        status: 400,
                        json() {
                            return {type : LOGIN_ALREADY_USED_TYPE}
                        }
                    }));
                    comp.registerAccount.password = comp.confirmPassword = 'password';

                    comp.register();
                    tick();

                    expect(comp.errorUserExists).toEqual('ERROR');
                    expect(comp.errorEmailExists).toBeNull();
                    expect(comp.error).toBeNull();
                })
            )
        );

        it('should notify of email existence upon 400/email address already in use',
            inject([Register],
                fakeAsync((service: Register) => {
                    spyOn(service, 'save').and.returnValue(Observable.throw({
                        status: 400,
                        json() {
                            return {type : EMAIL_ALREADY_USED_TYPE}
                        }
                    }));
                    comp.registerAccount.password = comp.confirmPassword = 'password';

                    comp.register();
                    tick();

                    expect(comp.errorEmailExists).toEqual('ERROR');
                    expect(comp.errorUserExists).toBeNull();
                    expect(comp.error).toBeNull();
                })
            )
        );

        it('should notify of generic error',
            inject([Register],
                fakeAsync((service: Register) => {
                    spyOn(service, 'save').and.returnValue(Observable.throw({
                        status: 503
                    }));
                    comp.registerAccount.password = comp.confirmPassword = 'password';

                    comp.register();
                    tick();

                    expect(comp.errorUserExists).toBeNull();
                    expect(comp.errorEmailExists).toBeNull();
                    expect(comp.error).toEqual('ERROR');
                })
            )
        );
    });
});
