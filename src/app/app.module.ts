import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import {SignupFormComponent} from "../components/signup-form/signup-form.component";
import {FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NavigationBarComponent} from "../components/navigation-bar/navigation-bar.component";
import {LoginFormComponent} from "../components/login-form/login-form.component";
import {FormComponent} from "../components/form/form.component";
import {HttpLoginServiceService} from "../services/http-login-service.service";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    AppComponent,
    SignupFormComponent,
    NavigationBarComponent,
    LoginFormComponent,
    FormComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    HttpLoginServiceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
