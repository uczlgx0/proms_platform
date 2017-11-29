/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcedureDetailComponent } from '../../../../../../main/webapp/app/entities/procedure/procedure-detail.component';
import { ProcedureService } from '../../../../../../main/webapp/app/entities/procedure/procedure.service';
import { Procedure } from '../../../../../../main/webapp/app/entities/procedure/procedure.model';

describe('Component Tests', () => {

    describe('Procedure Management Detail Component', () => {
        let comp: ProcedureDetailComponent;
        let fixture: ComponentFixture<ProcedureDetailComponent>;
        let service: ProcedureService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [ProcedureDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcedureService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcedureDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcedureDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcedureService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Procedure(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.procedure).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
