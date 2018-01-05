/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcedureTimepointDetailComponent } from '../../../../../../main/webapp/app/entities/procedure-timepoint/procedure-timepoint-detail.component';
import { ProcedureTimepointService } from '../../../../../../main/webapp/app/entities/procedure-timepoint/procedure-timepoint.service';
import { ProcedureTimepoint } from '../../../../../../main/webapp/app/entities/procedure-timepoint/procedure-timepoint.model';

describe('Component Tests', () => {

    describe('ProcedureTimepoint Management Detail Component', () => {
        let comp: ProcedureTimepointDetailComponent;
        let fixture: ComponentFixture<ProcedureTimepointDetailComponent>;
        let service: ProcedureTimepointService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [ProcedureTimepointDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcedureTimepointService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcedureTimepointDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcedureTimepointDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcedureTimepointService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ProcedureTimepoint(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.procedureTimepoint).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
