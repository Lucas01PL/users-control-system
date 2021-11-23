import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service'; 
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit {
  
  constructor(private userService: UserService, private router: Router) { }

  users: Array<User> = [];
  user!: User;
  title = 'users-control-web';
  errorMessage = '';
  successMessage = '';
  isFormShow = false;
  SESSION_USER = "SESSION_USER";
  fieldFilter = '';
  isCurrentUserAdmin = false;
  isChangePassword = false;

  ngOnInit(): void {
    if (localStorage.getItem(this.SESSION_USER) != null) {
      this.user = new User();
      this.loadUsers();
      this.isCurrentUserAdmin = this.userService.isCurrentUserAdmin();
    } else {
      this.router.navigate(["login"]);
    }
  }

  public loadUsers(): void{
      this.userService.findAll()
      .subscribe(response => {
        this.users = response.body!;
      });      
  }

  public add() {
    console.log(this.user);
    this.userService.add(this.user)
      .subscribe(response => {
          this.setSuccessMessage("Usuário cadastrado com sucesso!");
          this.loadUsers();
      }, (response) => {
        if (response.status == 400) 
          this.setErrorMessage(this.formatErrorMessage(response));
        else
          this.setErrorMessage("Ocorreu um erro ao cadastrar usuário. Procure o administrador do sistema.");
      });
  }

  public prepareEdit(id: number) {
    console.log(id);
    this.userService.findById(id)
      .subscribe(response => {
        this.user = response.body!;
        this.showForm();
        this.isChangePassword = false;
      });
  }

  public edit() {
    console.log(this.user);
    this.userService.edit(this.user)
      .subscribe(response => {
        console.log(response.status);
          this.setSuccessMessage("Usuário atualizado com sucesso!");
          this.loadUsers();
          this.hideForm();
      }, (response) => {
        if (response.status == 400) {
          var msg = this.formatErrorMessage(response);
          console.log(msg);
          this.setErrorMessage(msg);
       } else
        this.setErrorMessage("Ocorreu um erro ao atualizar dados do usuário. Procure o administrador do sistema.");
      });
  }

  public delete(id: number) {
    console.log('ok2');
    console.log(id);
    this.userService.delete(id)
      .subscribe(response => {
          this.setSuccessMessage("Usuário excluído com sucesso!");
          this.loadUsers();
      }, (response) => {
          if (response.status == 400)
          this.setErrorMessage(response.error);
          else
          this.setErrorMessage("Ocorreu um erro ao excluir usuário. Procure o administrador do sistema.");
      });
  }

  public prepareChangePassword(id: number, login: string) {
    this.user.id = id;
    this.user.login = login;
    this.isChangePassword = true;
  }

  public changePassword() {

    this.userService.changePassword(this.user.id, this.user.password||"")
      .subscribe(response => {
        console.log(response.status);
          this.setSuccessMessage("Usuário atualizado com sucesso!");
          this.loadUsers();
          this.hideForm();
      }, (response) => {
        if (response.status == 400 || response.status == 403) {
          this.setErrorMessage(this.formatErrorMessage(response));
       } else
        this.setErrorMessage("Ocorreu um erro ao atualizar dados do usuário. Procure o administrador do sistema.");
      });

  }

  public showForm() {
    this.isFormShow = true;
  }

  public hideForm() {
    this.isFormShow = false;
    this.resetForm();
    this.isChangePassword = false;
  }

  public logout() {
    localStorage.removeItem(this.SESSION_USER);
    this.router.navigate(["login"]);
  }

  private resetForm() {
    this.user = new User();
//    this.resetSuccessMessage();
//    this.resetErrorMessage();
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
