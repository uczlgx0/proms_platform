/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CareEventDetailComponent } from '../../../../../../main/webapp/app/entities/care-event/care-event-detail.component';
import { CareEventService } from '../../../../../../main/webapp/app/entities/care-event/care-event.service';
import { CareEvent } from '../../../../../../main/webapp/app/entities/care-event/care-event.model';

describe('Component Tests', () => {

    describe('CareEvent Management Detail Component', () => {
        let comp: CareEventDetailComponent;
        let fixture: ComponentFixture<CareEventDetailComponent>;
        let service: CareEventService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [CareEventDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CareEventService,
                    JhiEventManager
                ]
            }).overrideTemplate(CareEventDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CareEventDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CareEventService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CareEvent(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.careEvent).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
