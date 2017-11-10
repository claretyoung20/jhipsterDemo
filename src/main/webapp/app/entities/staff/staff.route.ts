import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StaffComponent } from './staff.component';
import { StaffDetailComponent } from './staff-detail.component';
import { StaffPopupComponent } from './staff-dialog.component';
import { StaffDeletePopupComponent } from './staff-delete-dialog.component';

@Injectable()
export class StaffResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const staffRoute: Routes = [
    {
        path: 'staff',
        component: StaffComponent,
        resolve: {
            'pagingParams': StaffResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'entityDemoApp.staff.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'staff/:id',
        component: StaffDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'entityDemoApp.staff.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const staffPopupRoute: Routes = [
    {
        path: 'staff-new',
        component: StaffPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'entityDemoApp.staff.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'staff/:id/edit',
        component: StaffPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'entityDemoApp.staff.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'staff/:id/delete',
        component: StaffDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'entityDemoApp.staff.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
