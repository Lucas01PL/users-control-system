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
  SESSION_USER = "SESSION_USER";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.user = new User();
    if (localStorage.getItem(this.SESSION_USER) != null)
      this.router.navigate(["form"]);
    else
      this.router.navigate(["login"]);
  }

  public login() {
    this.userService.login(this.user).subscribe(response => {
        this.createSession(response.body);
        this.router.navigate(["form"]);
    }, (response) => {
        this.setErrorMessage(this.formatErrorMessage(response));
    });
  }

  private createSession(dataUser: any) {
    localStorage.setItem(this.SESSION_USER, JSON.stringify(dataUser));
  }

  private destroySession(jwt: any) {
    localStorage.removeItem(this.SESSION_USER);
  }

  private setErrorMessage(message: string) {
    this.errorMessage = message;
  }

  private resetErrorMessage() {
    this.errorMessage = '';
  }

  private formatErrorMessage(response: any) {
    if (response.status == 401) {
      return "Falha na autenticação, usuário ou senha inválidos.";
    } else {
      if (response.error.fields == null) {
        return this.errorMessage = response.error.title;
      } else {
        var message = '';
        message = response.error.title;
        message += " Campos:";
        message += "<ul>";
        response.error.fields.forEach((element: { name: any; message: any; }) => {
          message += `<li>${element.name}</li>`;
        });
        message += "</ul>"
        return message;
      }
    }

  }

}
