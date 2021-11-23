import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from './models/users';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  
  constructor(private userService: UserService) { }

  users: Array<User> = [];
  user!: User;
  title = 'users-control-web';
  errorMessage = '';
  successMessage = '';
  isFormShow = false;

  ngOnInit(): void {
    this.user = new User();
    this.loadUsers();
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
      });
  }

  public edit() {
    console.log(this.user);
    this.userService.edit(this.user)
      .subscribe(response => {
        console.log(response.status);
          this.setSuccessMessage("Usuário atualizado com sucesso!");
          this.loadUsers();
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

  public showForm() {
    this.isFormShow = true;
  }

  public hideForm() {
    this.isFormShow = false;
    this.resetForm();
  }

  private resetForm() {
    this.user = new User();
    this.setSuccessMessage('');
    this.setErrorMessage('');
  }

  private setSuccessMessage(message: string) {
    this.successMessage = message;
  }

  private resetSucessMessage(message: string) {
    this.successMessage = '';
  }

  private setErrorMessage(message: string) {
    this.errorMessage = message;
  }

  private resetErrorMessage(message: string) {
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
