/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HealthcareProviderDetailComponent } from '../../../../../../main/webapp/app/entities/healthcare-provider/healthcare-provider-detail.component';
import { HealthcareProviderService } from '../../../../../../main/webapp/app/entities/healthcare-provider/healthcare-provider.service';
import { HealthcareProvider } from '../../../../../../main/webapp/app/entities/healthcare-provider/healthcare-provider.model';

describe('Component Tests', () => {

    describe('HealthcareProvider Management Detail Component', () => {
        let comp: HealthcareProviderDetailComponent;
        let fixture: ComponentFixture<HealthcareProviderDetailComponent>;
        let service: HealthcareProviderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [HealthcareProviderDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HealthcareProviderService,
                    JhiEventManager
                ]
            }).overrideTemplate(HealthcareProviderDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HealthcareProviderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HealthcareProviderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new HealthcareProvider(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.healthcareProvider).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
