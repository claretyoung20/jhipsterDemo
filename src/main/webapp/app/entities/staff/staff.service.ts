import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Staff } from './staff.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StaffService {

    private resourceUrl = SERVER_API_URL + 'api/staff';

    constructor(private http: Http) { }

    create(staff: Staff): Observable<Staff> {
        const copy = this.convert(staff);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(staff: Staff): Observable<Staff> {
        const copy = this.convert(staff);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Staff> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Staff.
     */
    private convertItemFromServer(json: any): Staff {
        const entity: Staff = Object.assign(new Staff(), json);
        return entity;
    }

    /**
     * Convert a Staff to a JSON which can be sent to the server.
     */
    private convert(staff: Staff): Staff {
        const copy: Staff = Object.assign({}, staff);
        return copy;
    }
}
