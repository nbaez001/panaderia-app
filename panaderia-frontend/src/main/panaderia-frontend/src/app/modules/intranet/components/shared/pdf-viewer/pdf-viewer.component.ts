import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OutResponse } from '../../../dto/response/out.response';

@Component({
  selector: 'app-pdf-viewer',
  templateUrl: './pdf-viewer.component.html',
  styleUrls: ['./pdf-viewer.component.scss']
})
export class PdfViewerComponent implements OnInit {
  base64Pdf: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: OutResponse<any>,
    // @Inject(GenericService) private genericService: GenericService,
  ) {
  }

  ngOnInit() {
    console.log('DATA REQUEST');
    console.log(this.data.rResult);
    // this.descargarArchivo(this.data.objeto)
    this.base64Pdf = this.data.rResult.data;
  }

  // descargarArchivo(req): void {
  //   this.genericService.descargarArchivo(req.request, req.path).subscribe(
  //     (data: ApiOutResponse) => {
  //       if (data.rCodigo == 0) {
  //         this.base64Pdf = data.objeto.data;
  //         setTimeout(() => {
  //           this.spinner.hide();
  //         }, 700);
  //       }
  //     }, error => {
  //       console.log(error);
  //     });
  // }

}
