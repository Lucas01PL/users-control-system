import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  SESSION_USER = "SESSION_USER";

  findAll() {
    return this.http.get<User[]>(`http://localhost:8080/users`, { headers: this.getHeaders(), observe: 'response' });
  }

  findById(id: number) {
    return this.http.get<User>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  add(user: User) {
    if (user.isAdmin == null) 
      user.isAdmin = false; 
    return this.http.post<any>(`http://localhost:8080/users`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  edit(user: User) {
    return this.http.put<any>(`http://localhost:8080/users/${user.id}`, user, { headers: this.getHeaders(), observe: 'response' });
  }

  delete(id: number) {
    return this.http.delete<any>(`http://localhost:8080/users/${id}`, { headers: this.getHeaders(), observe: 'response' });
  }

  changePassword(id: number, newPassword: string) {
    return this.http.post<any>(`http://localhost:8080/users/${id}/password/reset`, {"newPassword": newPassword}, { headers: this.getHeaders(), observe: 'response' });
  }

  login(user: User) {
    return this.http.post<any>(`http://localhost:8080/authenticate/login`, {"login": user.login, "password": user.password}, { observe: 'response' });
  }

  isCurrentUserAdmin() {
    if (localStorage.getItem(this.SESSION_USER) != null) {
      console.log(JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}").isAdmin);
      return JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}").isAdmin;
    }

    return false;
  }

  private getHeaders() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.getJwt() });
  }

  private getJwt() {
    return JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}").token;
  }
}

