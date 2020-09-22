import { Injectable } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class FormService {
  constructor() { }

  getValidationErrors(group: FormGroup, formErrors: any, type: boolean): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.getValidationErrors(abstractControl, formErrors[key], type);
      } else {
        if (type) {
          abstractControl.markAsTouched();
        }
        formErrors[key] = '';
        if (abstractControl && abstractControl.invalid && (abstractControl.touched || abstractControl.dirty)) {
          for (let errorKey in abstractControl.errors) {
            if (errorKey) {
              formErrors[key] += this.choseMessage(errorKey, abstractControl);
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

  buildFormErrors(group: FormGroup, formErrors: any): any {
    formErrors = {};
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.buildFormErrors(abstractControl, formErrors[key]);
      } else {
        formErrors[key] = '';
      }
    });
    return formErrors;
  }

  choseMessage(error: string, control: any): string {
    let errorCtrl = control['errors'];
    let msg = '';
    switch (error) {
      case 'required':
        msg = 'Campo obligatorio';
        break;
      case 'maxlength':
        msg = `Maximo ${errorCtrl['maxlength']['requiredLength']} caracteres`;
        break;
      case 'min':
        msg = `Minimo valor permitido es ${errorCtrl['min']['min']}`;
        break;
      case 'max':
        msg = `Maximo valor permitido es ${errorCtrl['max']['max']}`;
        break;
      default:
        msg = 'Valor incorrecto';
        console.log('Mensaje no encontrado');
        console.log(error);
        console.log(errorCtrl);
        break;
    }
    return msg;
  }
}
