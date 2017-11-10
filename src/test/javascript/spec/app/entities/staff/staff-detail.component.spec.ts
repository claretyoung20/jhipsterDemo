/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { EntityDemoTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StaffDetailComponent } from '../../../../../../main/webapp/app/entities/staff/staff-detail.component';
import { StaffService } from '../../../../../../main/webapp/app/entities/staff/staff.service';
import { Staff } from '../../../../../../main/webapp/app/entities/staff/staff.model';

describe('Component Tests', () => {

    describe('Staff Management Detail Component', () => {
        let comp: StaffDetailComponent;
        let fixture: ComponentFixture<StaffDetailComponent>;
        let service: StaffService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [EntityDemoTestModule],
                declarations: [StaffDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StaffService,
                    JhiEventManager
                ]
            }).overrideTemplate(StaffDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StaffDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StaffService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Staff(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.staff).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
