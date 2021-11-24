import { Injectable } from '@angular/core';
import { Email } from '../models/email';
import { HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private http: HttpClient) { }

  SESSION_USER = "SESSION_USER";

  public send(email: Email) {
      email.sender = this.getEmailUserFromSession();
      return this.http.post<any>(`http://localhost:8080/email`, email, { headers: this.getHeaders(), observe: 'response' });
  }

  private getHeaders() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.getJwt() });
  }

  private getJwt() {
    return JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}").token;
  }

  private getEmailUserFromSession() {
    console.log(JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}"));
    return JSON.parse(localStorage.getItem(this.SESSION_USER) || "{}").email;
  }

}
