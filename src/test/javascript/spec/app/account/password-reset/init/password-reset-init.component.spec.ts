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
import { ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { Renderer, ElementRef } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { NorthumbriapromsTestModule } from '../../../../test.module';
import { PasswordResetInitComponent } from '../../../../../../../main/webapp/app/account/password-reset/init/password-reset-init.component';
import { PasswordResetInitService } from '../../../../../../../main/webapp/app/account/password-reset/init/password-reset-init.service';
import { EMAIL_NOT_FOUND_TYPE } from '../../../../../../../main/webapp/app/shared';

describe('Component Tests', () => {

    describe('PasswordResetInitComponent', () => {
        let fixture: ComponentFixture<PasswordResetInitComponent>;
        let comp: PasswordResetInitComponent;

        beforeEach(() => {
            fixture = TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [PasswordResetInitComponent],
                providers: [
                    PasswordResetInitService,
                    {
                        provide: Renderer,
                        useValue: {
                            invokeElementMethod(renderElement: any, methodName: string, args?: any[]) {}
                        }
                    },
                    {
                        provide: ElementRef,
                        useValue: new ElementRef(null)
                    }
                ]
            }).overrideTemplate(PasswordResetInitComponent, '')
            .createComponent(PasswordResetInitComponent);
            comp = fixture.componentInstance;
            comp.ngOnInit();
        });

        it('should define its initial state', () => {
            expect(comp.success).toBeUndefined();
            expect(comp.error).toBeUndefined();
            expect(comp.errorEmailNotExists).toBeUndefined();
            expect(comp.resetAccount).toEqual({});
        });

        it('sets focus after the view has been initialized',
            inject([ElementRef], (elementRef: ElementRef) => {
                const element = fixture.nativeElement;
                const node = {
                    focus() {}
                };

                elementRef.nativeElement = element;
                spyOn(element, 'querySelector').and.returnValue(node);
                spyOn(node, 'focus');

                comp.ngAfterViewInit();

                expect(element.querySelector).toHaveBeenCalledWith('#email');
                expect(node.focus).toHaveBeenCalled();
            })
        );

        it('notifies of success upon successful requestReset',
            inject([PasswordResetInitService], (service: PasswordResetInitService) => {
                spyOn(service, 'save').and.returnValue(Observable.of({}));
                comp.resetAccount.email = 'user@domain.com';

                comp.requestReset();

                expect(service.save).toHaveBeenCalledWith('user@domain.com');
                expect(comp.success).toEqual('OK');
                expect(comp.error).toBeNull();
                expect(comp.errorEmailNotExists).toBeNull();
            })
        );

        it('notifies of unknown email upon email address not registered/400',
            inject([PasswordResetInitService], (service: PasswordResetInitService) => {
                spyOn(service, 'save').and.returnValue(Observable.throw({
                    status: 400,
                    json() {
                        return {type : EMAIL_NOT_FOUND_TYPE}
                    }
                }));
                comp.resetAccount.email = 'user@domain.com';

                comp.requestReset();

                expect(service.save).toHaveBeenCalledWith('user@domain.com');
                expect(comp.success).toBeNull();
                expect(comp.error).toBeNull();
                expect(comp.errorEmailNotExists).toEqual('ERROR');
            })
        );

        it('notifies of error upon error response',
            inject([PasswordResetInitService], (service: PasswordResetInitService) => {
                spyOn(service, 'save').and.returnValue(Observable.throw({
                    status: 503,
                    data: 'something else'
                }));
                comp.resetAccount.email = 'user@domain.com';

                comp.requestReset();

                expect(service.save).toHaveBeenCalledWith('user@domain.com');
                expect(comp.success).toBeNull();
                expect(comp.errorEmailNotExists).toBeNull();
                expect(comp.error).toEqual('ERROR');
            })
        );

    });
});
