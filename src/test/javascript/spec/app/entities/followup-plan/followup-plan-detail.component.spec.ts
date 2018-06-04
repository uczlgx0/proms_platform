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
