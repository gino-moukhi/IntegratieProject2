import { Component } from '@angular/core';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent {
  loginForm = false;

  clickLoginForm() {
    this.loginForm = true;
  }

  clickSignUpForm() {
    this.loginForm = false;
  }
}
