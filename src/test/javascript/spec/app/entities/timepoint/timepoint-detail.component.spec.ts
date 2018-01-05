/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TimepointDetailComponent } from '../../../../../../main/webapp/app/entities/timepoint/timepoint-detail.component';
import { TimepointService } from '../../../../../../main/webapp/app/entities/timepoint/timepoint.service';
import { Timepoint } from '../../../../../../main/webapp/app/entities/timepoint/timepoint.model';

describe('Component Tests', () => {

    describe('Timepoint Management Detail Component', () => {
        let comp: TimepointDetailComponent;
        let fixture: ComponentFixture<TimepointDetailComponent>;
        let service: TimepointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [TimepointDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TimepointService,
                    JhiEventManager
                ]
            }).overrideTemplate(TimepointDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TimepointDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TimepointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Timepoint(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.timepoint).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
