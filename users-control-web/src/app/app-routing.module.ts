import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component'; 
import { FormComponent } from './components/form/form.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'form', component: FormComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full'}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
