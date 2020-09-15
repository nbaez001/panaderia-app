import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormService {
  constructor() { }

  getValidationErrors(group: FormGroup, formMessages: any, formErrors: any, type: boolean): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.getValidationErrors(abstractControl, formMessages[key], formErrors[key], type);
      } else {
        if (type) {
          abstractControl.markAsTouched();
        }
        formErrors[key] = '';
        if (abstractControl && abstractControl.invalid && (abstractControl.touched || abstractControl.dirty)) {
          let msg = formMessages[key];
          for (let errorKey in abstractControl.errors) {
            if (errorKey) {
              formErrors[key] += msg[errorKey] + ' ';
            }
          }
        }
      }
    });
  }

  setAsUntoched(group: FormGroup, formErrors: any, exclusions?: string[]): void {
    group.markAsUntouched();
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.setAsUntoched(abstractControl, formErrors[key]);
      } else {
        if (typeof exclusions != 'undefined') {
          let ex = exclusions.find(el => el == key);
          if (!ex) {
            abstractControl.setValue('');
            abstractControl.markAsUntouched();
          }
        } else {
          abstractControl.setValue('');
          abstractControl.markAsUntouched();
        }
        formErrors[key] = '';
      }
    });
  }

  disableControls(group: FormGroup, exclusions?: [string]): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.disableControls(abstractControl, exclusions);
      } else {
        if (typeof exclusions != 'undefined') {
          let ex = exclusions.find(el => el == key);
          if (!ex) {
            abstractControl.disable()
          }
        } else {
          abstractControl.disable();
        }
      }
    });
  }

  removeErrors(group: FormGroup, exclusions?: [string]): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.removeErrors(abstractControl, exclusions);
      } else {
        if (typeof exclusions != 'undefined') {
          let ex = exclusions.find(el => el == key);
          if (!ex) {
            abstractControl.setErrors(null)
          }
        } else {
          abstractControl.setErrors(null);
        }
      }
    });
  }

  buildFormErrors(group: FormGroup, formMessages: any, formErrors: any): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.buildFormErrors(abstractControl, formMessages[key], formErrors[key]);
      } else {
        //CONSTRUCCION DE LOS ERRORES DE FORMULARIO
        formErrors[key] = '';

        //CONSTRUCCION DE LOS MENSAJES DE LOS ERRORES DE FORMULARIO
        let msgs = {};
        for (let errorKey in abstractControl.errors) {
          if (errorKey) {
            msgs[errorKey] = this.choseMessage(errorKey);
          }
        }
        formMessages[key] = msgs;
      }
    });
  }

  choseMessage(error: string): string {
    let msg = '';
    switch (error) {
      case 'required':
        msg = 'Campo obligatorio';
        break;
      default:
        msg = 'Valor incorrecto';
        break;
    }
    return msg;
  }
}
