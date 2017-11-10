import { BaseEntity } from './../../shared';

export class Student implements BaseEntity {
    constructor(
        public id?: number,
        public studentName?: string,
        public password?: string,
        public departmentId?: number,
    ) {
    }
}
