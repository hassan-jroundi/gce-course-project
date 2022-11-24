import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root"
})
export class HttpService {

  constructor(private http: HttpClient) { }

  // Localhost http requests
  get(endPoint: string) {
    return this.http.get(`${endPoint}`);
  }
  put(endPoint: string, id: any, body: any) {
    return this.http.put(`${endPoint}/${id}`, body);
  }
  post(endPoint: string, body: any) {
    return this.http.post(`${endPoint}`, body);
  }
  search(endPoint: string, data: any) {
    let param = this.jsonToQueryString(data);
    return this.http.get(`${endPoint}?${param}`);
  }
  getOne(endPoint: string, variable: string) {
    return this.http.get(`${endPoint}/${variable}`);
  }

  getOneOfType<T>(endPoint: string, variable: string) {
    return this.http.get<T>(`${endPoint}/${variable}`);
  }

  getOfType<T>(endPoint: string) {
    return this.http.get<T>(`${endPoint}`);
  }
  postOfType<T>(endPoint: string, body: any) {
    return this.http.post<T>(`${endPoint}`, body);
  }


  postFile(endPoint: string, formData: any) {
    return this.http.post(`${endPoint}`, formData, {
      reportProgress: true,
      observe: "events"
    });
  }

  getFile(endPoint: string) {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', '*/*');
    return this.http.get(`${endPoint}`, { headers: headers, responseType: 'blob' })
  }
  putOfType<T>(endPoint: string, id: any, body: any) {
    return this.http.put<T>(`${endPoint}/${id}`, body);
  }

  delete(endPoint: string, id: number) {
    return this.http.delete(`${endPoint}/${id}`);
  }

  deleteWithIdSession(endPoint: string, id: number, idSession: string) {
    return this.http.delete(`${endPoint}/${id}/${idSession}`);
  }

  find(endPoint: string, data: any) {
    let param = this.jsonToQueryString(data);
    return this.http.get(`${endPoint}/find?${param}`);
  }

  printReport(endPoint: string, body: any): Observable<Blob> {
    return this.http.post(`${endPoint}`, body, { responseType: 'blob' });
  }

  printReportBis(endPoint: string): Observable<Blob> {
    return this.http.get(`${endPoint}`, { responseType: 'blob' });
  }

  postWithParams(endPoint: string, data: any) {
    let param = this.jsonToQueryString(data);
    return this.http.get(`${endPoint}?${param}`);
  }

  private jsonToQueryString(values: Object): string {

    let params = new URLSearchParams();
    for (let key in values) {
      params.set(key, values[key]);
    }

    return params.toString();
  }
}
