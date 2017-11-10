import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EntityDemoSharedModule } from '../../shared';
import {
    StaffService,
    StaffPopupService,
    StaffComponent,
    StaffDetailComponent,
    StaffDialogComponent,
    StaffPopupComponent,
    StaffDeletePopupComponent,
    StaffDeleteDialogComponent,
    staffRoute,
    staffPopupRoute,
    StaffResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...staffRoute,
    ...staffPopupRoute,
];

@NgModule({
    imports: [
        EntityDemoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StaffComponent,
        StaffDetailComponent,
        StaffDialogComponent,
        StaffDeleteDialogComponent,
        StaffPopupComponent,
        StaffDeletePopupComponent,
    ],
    entryComponents: [
        StaffComponent,
        StaffDialogComponent,
        StaffPopupComponent,
        StaffDeleteDialogComponent,
        StaffDeletePopupComponent,
    ],
    providers: [
        StaffService,
        StaffPopupService,
        StaffResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityDemoStaffModule {}
