import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { EntityDemoDepartmentModule } from './department/department.module';
import { EntityDemoStudentModule } from './student/student.module';
import { EntityDemoStaffModule } from './staff/staff.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        EntityDemoDepartmentModule,
        EntityDemoStudentModule,
        EntityDemoStaffModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EntityDemoEntityModule {}
