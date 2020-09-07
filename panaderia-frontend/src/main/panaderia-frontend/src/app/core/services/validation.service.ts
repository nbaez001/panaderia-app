import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {
  constructor() { }

  getValidationErrors(group: FormGroup, messages: any, formErrors: any, type: boolean): void {
    Object.keys(group.controls).forEach((key: string) => {
      let abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.getValidationErrors(abstractControl, messages[key], formErrors[key], type);
      } else {
        if (type) {
          abstractControl.markAsTouched();
        }
        formErrors[key] = '';
        if (abstractControl && abstractControl.invalid && (abstractControl.touched || abstractControl.dirty)) {
          let msg = messages[key];
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
}
