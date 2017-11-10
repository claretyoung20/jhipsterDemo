import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Staff } from './staff.model';
import { StaffPopupService } from './staff-popup.service';
import { StaffService } from './staff.service';
import { Department, DepartmentService } from '../department';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-staff-dialog',
    templateUrl: './staff-dialog.component.html'
})
export class StaffDialogComponent implements OnInit {

    staff: Staff;
    isSaving: boolean;

    departments: Department[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private staffService: StaffService,
        private departmentService: DepartmentService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.departmentService.query()
            .subscribe((res: ResponseWrapper) => { this.departments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.staff.id !== undefined) {
            this.subscribeToSaveResponse(
                this.staffService.update(this.staff));
        } else {
            this.subscribeToSaveResponse(
                this.staffService.create(this.staff));
        }
    }

    private subscribeToSaveResponse(result: Observable<Staff>) {
        result.subscribe((res: Staff) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Staff) {
        this.eventManager.broadcast({ name: 'staffListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDepartmentById(index: number, item: Department) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-staff-popup',
    template: ''
})
export class StaffPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private staffPopupService: StaffPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.staffPopupService
                    .open(StaffDialogComponent as Component, params['id']);
            } else {
                this.staffPopupService
                    .open(StaffDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
