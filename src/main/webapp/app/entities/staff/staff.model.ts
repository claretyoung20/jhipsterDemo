import { BaseEntity } from './../../shared';

export class Staff implements BaseEntity {
    constructor(
        public id?: number,
        public staffName?: string,
        public password?: string,
        public departmentId?: number,
    ) {
    }
}
