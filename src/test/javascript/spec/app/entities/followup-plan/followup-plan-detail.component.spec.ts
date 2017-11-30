/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FollowupPlanDetailComponent } from '../../../../../../main/webapp/app/entities/followup-plan/followup-plan-detail.component';
import { FollowupPlanService } from '../../../../../../main/webapp/app/entities/followup-plan/followup-plan.service';
import { FollowupPlan } from '../../../../../../main/webapp/app/entities/followup-plan/followup-plan.model';

describe('Component Tests', () => {

    describe('FollowupPlan Management Detail Component', () => {
        let comp: FollowupPlanDetailComponent;
        let fixture: ComponentFixture<FollowupPlanDetailComponent>;
        let service: FollowupPlanService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [FollowupPlanDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FollowupPlanService,
                    JhiEventManager
                ]
            }).overrideTemplate(FollowupPlanDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FollowupPlanDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FollowupPlanService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new FollowupPlan(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.followupPlan).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
