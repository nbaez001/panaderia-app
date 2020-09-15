import { NgModule } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MatIconRegistry } from '@angular/material/icon';
import { SharedModule } from './shared.module';
import { MaterialModule } from './material.module';



@NgModule({
  declarations: [],
  imports: [],
  providers: [
    MatIconRegistry
  ]
})
export class IconModule {
  private path: string = 'assets/images/svg-icons';

  constructor(private domSanitizer: DomSanitizer, public matIconRegistry: MatIconRegistry) {
    this.matIconRegistry
      .addSvgIcon('excel', this.setIconPath(`${this.path}/icons8-microsoft-excel.svg`));
  }

  private setIconPath(url: string): SafeResourceUrl {
    return this.domSanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
