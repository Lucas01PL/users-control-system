import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from './models/user';
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

  ngOnInit(): void { }

}
