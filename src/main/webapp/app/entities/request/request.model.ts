import { BaseEntity } from './../../shared';

export class Request implements BaseEntity {
    constructor(
        public id?: number,
        public requestTitle?: string,
        public dateCreated?: any,
        public dateClosed?: any,
        public requestContent?: string,
        public requestStatus?: boolean,
        public requestSolution?: string,
        public attachmentName?: string,
        public departmentId?: number,
        public staffId?: number,
        public studentId?: number,
    ) {
        this.requestStatus = false;
    }
}
