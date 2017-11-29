/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NorthumbriapromsTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ProcedurelinkDetailComponent } from '../../../../../../main/webapp/app/entities/procedurelink/procedurelink-detail.component';
import { ProcedurelinkService } from '../../../../../../main/webapp/app/entities/procedurelink/procedurelink.service';
import { Procedurelink } from '../../../../../../main/webapp/app/entities/procedurelink/procedurelink.model';

describe('Component Tests', () => {

    describe('Procedurelink Management Detail Component', () => {
        let comp: ProcedurelinkDetailComponent;
        let fixture: ComponentFixture<ProcedurelinkDetailComponent>;
        let service: ProcedurelinkService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NorthumbriapromsTestModule],
                declarations: [ProcedurelinkDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ProcedurelinkService,
                    JhiEventManager
                ]
            }).overrideTemplate(ProcedurelinkDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProcedurelinkDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProcedurelinkService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Procedurelink(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.procedurelink).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
