import { Injectable } from '@angular/core';
import { Email } from '../models/email';
import { HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private http: HttpClient) { }

  CHAVE_USER = "TOKEN_USER";

  public send(message: Email) {
      console.log(message);
      return this.http.post<any>(`http://localhost:8080/email`, message, { headers: this.getHeaders(), observe: 'response' });
  }

  private getHeaders() {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.getJwt() });
  }

  private getJwt() {
    return localStorage.getItem(this.CHAVE_USER);
  }

}
