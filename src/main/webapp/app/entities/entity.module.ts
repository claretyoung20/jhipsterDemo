import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EntityDemoDepartmentModule } from './department/department.module';
import { EntityDemoStudentModule } from './student/student.module';
import { EntityDemoStaffModule } from './staff/staff.module';
import { EntityDemoRequestModule } from './request/request.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EntityDemoDepartmentModule,
        EntityDemoStudentModule,
        EntityDemoStaffModule,
        EntityDemoRequestModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityDemoEntityModule {}
