/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { FollowupActionDetailComponent } from '../../../../../../main/webapp/app/entities/followup-action/followup-action-detail.component';
import { FollowupActionService } from '../../../../../../main/webapp/app/entities/followup-action/followup-action.service';
import { FollowupAction } from '../../../../../../main/webapp/app/entities/followup-action/followup-action.model';

describe('Component Tests', () => {

    describe('FollowupAction Management Detail Component', () => {
        let comp: FollowupActionDetailComponent;
        let fixture: ComponentFixture<FollowupActionDetailComponent>;
        let service: FollowupActionService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [FollowupActionDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    FollowupActionService,
                    JhiEventManager
                ]
            }).overrideTemplate(FollowupActionDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FollowupActionDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FollowupActionService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new FollowupAction(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.followupAction).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
