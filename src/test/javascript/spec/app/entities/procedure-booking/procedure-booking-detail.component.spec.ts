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
/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcedureBookingDetailComponent } from '../../../../../../main/webapp/app/entities/procedure-booking/procedure-booking-detail.component';
import { ProcedureBookingService } from '../../../../../../main/webapp/app/entities/procedure-booking/procedure-booking.service';
import { ProcedureBooking } from '../../../../../../main/webapp/app/entities/procedure-booking/procedure-booking.model';

describe('Component Tests', () => {

    describe('ProcedureBooking Management Detail Component', () => {
        let comp: ProcedureBookingDetailComponent;
        let fixture: ComponentFixture<ProcedureBookingDetailComponent>;
        let service: ProcedureBookingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [ProcedureBookingDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcedureBookingService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcedureBookingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcedureBookingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcedureBookingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProcedureBooking(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.procedureBooking).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
