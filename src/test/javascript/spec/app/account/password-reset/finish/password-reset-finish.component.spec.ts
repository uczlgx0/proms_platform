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
import { ActivatedRoute } from '@angular/router';
import { LoginModalService } from '../../../../../../../main/webapp/app/shared';
import { NorthumbriapromsTestModule } from '../../../../test.module';
import { PasswordResetFinishComponent } from '../../../../../../../main/webapp/app/account/password-reset/finish/password-reset-finish.component';
import { PasswordResetFinishService } from '../../../../../../../main/webapp/app/account/password-reset/finish/password-reset-finish.service';
import { MockActivatedRoute } from '../../../../helpers/mock-route.service';

describe('Component Tests', () => {

    describe('PasswordResetFinishComponent', () => {

        let fixture: ComponentFixture<PasswordResetFinishComponent>;
        let comp: PasswordResetFinishComponent;

        beforeEach(() => {
            fixture = TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [PasswordResetFinishComponent],
                providers: [
                    PasswordResetFinishService,
                    {
                        provide: LoginModalService,
                        useValue: null
                    },
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({'key': 'XYZPDQ'})
                    },
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
            }).overrideTemplate(PasswordResetFinishComponent, '')
            .createComponent(PasswordResetFinishComponent);
            comp = fixture.componentInstance;
        });

        it('should define its initial state', () => {
            comp.ngOnInit();

            expect(comp.keyMissing).toBeFalsy();
            expect(comp.key).toEqual('XYZPDQ');
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

                expect(element.querySelector).toHaveBeenCalledWith('#password');
                expect(node.focus).toHaveBeenCalled();
            })
        );

    });
});
