import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LiteratureService } from "./literature.service";
import { ServiceWithDelete } from "./service-with-delete";

@Injectable({
    providedIn: 'root'
})
export class GroupLiteratureService extends LiteratureService implements ServiceWithDelete {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public delete(id: number): Observable<any> {
        return this.deleteGroupLiterature(id);
    }
}