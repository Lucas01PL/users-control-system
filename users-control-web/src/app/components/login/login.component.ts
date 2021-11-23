import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  user!: User;
  errorMessage = '';
  jwt = '';
  CHAVE_USER = "TOKEN_USER";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.user = new User();
    if (localStorage.getItem(this.CHAVE_USER) != null)
      this.router.navigate(["form"]);
    else
      this.router.navigate(["login"]);
  }

  public login() {
    this.userService.login(this.user).subscribe(response => {
        this.jwt = response.body.jwt;
        this.createSession(response.body.jwt);
        this.router.navigate(["form"]);
    }, (response) => {
        this.setErrorMessage(response);
    });
  }

  private createSession(jwt: any) {
    localStorage.setItem(this.CHAVE_USER, jwt);
  }

  private destroySession(jwt: any) {
    localStorage.removeItem(this.CHAVE_USER);
  }

  private setErrorMessage(message: string) {
    this.errorMessage = message;
  }

  private resetErrorMessage() {
    this.errorMessage = '';
  }

  private formatErrorMessage(response: any) {
    console.log(response.error);
    if (response.error.fields == null) {
      return this.errorMessage = response.error.title;
    } else {
      var message = '';
      message = response.error.title;
      message += " Campos:";
      message += "<ul>";
      response.error.fields.forEach((element: { nome: any; mensagem: any; }) => {
        message += `<li>${element.nome}</li>`;
      });
      message += "</ul>"
      return message;
    }

  }

}
