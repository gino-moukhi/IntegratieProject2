import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Http} from "@angular/http";
import {HttpLoginServiceService} from "../../services/http-login-service.service";
import {LoginUser} from "../../model/loginUser";

@Component({
  selector: 'login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  private loginService: HttpLoginServiceService;
  loginUser = new LoginUser('','');

  constructor(loginService: HttpLoginServiceService) {
    this.loginService = loginService;
  }

  form = new FormGroup({
    'username': new FormControl('', [Validators.required, Validators.minLength(3)]),
    'password': new FormControl('', [Validators.required])
  });

  get username() {
    return this.form.get('username');
  }

  get password() {
    return this.form.get('password');
  }

  clickLogin(){
    console.log("cllcked");
    this.loginService.doLogin(this.loginUser);
  }

}
