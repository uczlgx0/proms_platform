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
