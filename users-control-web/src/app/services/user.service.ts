import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  CHAVE_USER = "TOKEN_USER";

  findAll() {
    return this.http.get<User[]>(`http://localhost:8080/users`, { headers: this.getHeaders(), observe: 'response' });
  }

  findById(id: number) {
    return this.http.get<User>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  add(user: User) {
    return this.http.post<any>(`http://localhost:8080/users`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  edit(user: User) {
    return this.http.put<any>(`http://localhost:8080/users/${user.id}`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  delete(id: number) {
    return this.http.delete<any>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  login(user: User) {
    return this.http.post<any>(`http://localhost:8080/authenticate/login`, {"login": user.login, "password": user.password}, { observe: 'response' });
  }

  private getHeaders() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.getJwt() });
  }

  private getJwt() {
    return localStorage.getItem(this.CHAVE_USER);
  }
}

