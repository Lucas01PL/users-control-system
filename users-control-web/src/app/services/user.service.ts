import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { User } from '../models/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  private jwt: string = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYzNzYzMjY0NiwiaWF0IjoxNjM3NjE0NjQ2fQ.Kp_zLYNTg3TUWtj-CDoiyzuY05SMWFjZmRzA5j7MDI1Fr0UvRWfARl0id5zodDsVNMw7OTHvxjWmPz2trC9pBQ";

  findAll() {
    httpHeader: new HttpHeaders({ 'Authorization': 'Bearer ' + this.jwt });
    return this.http.get<User[]>(`http://localhost:8080/users`, { headers: this.getHeaders(), observe: 'response' });
  }

  findById(id: number) {
    httpHeader: new HttpHeaders({ 'Authorization': 'Bearer ' + this.jwt });
    return this.http.get<User>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  add(user: User) {
    httpHeader: new HttpHeaders({ 'Authorization': 'Bearer ' + this.jwt });
    return this.http.post<any>(`http://localhost:8080/users`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  edit(user: User) {
    httpHeader: new HttpHeaders({ 'Authorization': 'Bearer ' + this.jwt });
    return this.http.put<any>(`http://localhost:8080/users/${user.id}`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  delete(id: number) {
    httpHeader: new HttpHeaders({ 'Authorization': 'Bearer ' + this.jwt });
    return this.http.delete<any>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  private getHeaders() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.getJwt() });
  }

  private getJwt() {
    return this.jwt;
  }
}

