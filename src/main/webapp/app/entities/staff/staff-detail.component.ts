import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Staff } from './staff.model';
import { StaffService } from './staff.service';

@Component({
    selector: 'jhi-staff-detail',
    templateUrl: './staff-detail.component.html'
})
export class StaffDetailComponent implements OnInit, OnDestroy {

    staff: Staff;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private staffService: StaffService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStaff();
    }

    load(id) {
        this.staffService.find(id).subscribe((staff) => {
            this.staff = staff;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStaff() {
        this.eventSubscriber = this.eventManager.subscribe(
            'staffListModification',
            (response) => this.load(this.staff.id)
        );
    }
}
