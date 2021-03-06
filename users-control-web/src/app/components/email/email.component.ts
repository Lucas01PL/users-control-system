import { Component, OnInit } from '@angular/core';
import { Email } from 'src/app/models/email';
import { Router } from '@angular/router';
import { EmailService } from 'src/app/services/email.service';

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.scss']
})
export class EmailComponent implements OnInit {

  constructor(private router: Router, private emailService: EmailService) { }

  email!: Email;
  errorMessage = '';
  successMessage = '';
  SESSION_USER = "SESSION_USER";

  ngOnInit(): void {
    if (localStorage.getItem(this.SESSION_USER) != null) {
      this.email = new Email();
    } else {
      this.router.navigate(["login"]);
    }
  }

  public send() {
    console.log("ok");
    console.log(this.email);
    this.emailService.send(this.email).subscribe(response => {
      this.setSuccessMessage("Mensagem enviada com sucesso!");
      this.email = new Email();
    }, (response) => {
      this.email = new Email();
      if (response.status == 400) 
        this.setErrorMessage(this.formatErrorMessage(response));
      else
        this.setErrorMessage("Ocorreu um erro ao enviar mensagem. Procure o administrador do sistema.");
    });
  }

  public clearForm() {
    this.email = new Email();
    this.resetSuccessMessage();
    this.resetErrorMessage();
  }

  public logout() {
    localStorage.removeItem(this.SESSION_USER);
    this.router.navigate(["login"]);
  }

  private setSuccessMessage(message: string) {
    this.successMessage = message;
  }

  private resetSuccessMessage() {
    this.successMessage = '';
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
      response.error.fields.forEach((element: { name: any; message: any; }) => {
        message += `<li>${element.name}</li>`;
      });
      message += "</ul>"
      return message;
    }
  }

}
