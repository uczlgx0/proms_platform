/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { QuestionnaireDetailComponent } from '../../../../../../main/webapp/app/entities/questionnaire/questionnaire-detail.component';
import { QuestionnaireService } from '../../../../../../main/webapp/app/entities/questionnaire/questionnaire.service';
import { Questionnaire } from '../../../../../../main/webapp/app/entities/questionnaire/questionnaire.model';

describe('Component Tests', () => {

    describe('Questionnaire Management Detail Component', () => {
        let comp: QuestionnaireDetailComponent;
        let fixture: ComponentFixture<QuestionnaireDetailComponent>;
        let service: QuestionnaireService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [QuestionnaireDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    QuestionnaireService,
                    JhiEventManager
                ]
            }).overrideTemplate(QuestionnaireDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(QuestionnaireDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(QuestionnaireService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Questionnaire(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.questionnaire).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
