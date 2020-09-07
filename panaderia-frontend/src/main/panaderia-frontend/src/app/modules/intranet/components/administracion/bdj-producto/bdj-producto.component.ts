import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bdj-producto',
  templateUrl: './bdj-producto.component.html',
  styleUrls: ['./bdj-producto.component.scss']
})
export class BdjProductoComponent implements OnInit {
  tiposEgreso: Maestra[];
  dias = [];

  bdjEgresoGrp: FormGroup;
  messages = {
    'name': {
      'required': 'Field is required',
      'minlength': 'Insert al least 2 characters',
      'maxlength': 'Max name size 20 characters'
    }
  };
  formErrors = {
    'name': ''
  };

  displayedColumns: string[];
  dataSource: MatTableDataSource<Egreso>;
  isLoading: boolean = false;

  fechaRep: Date = null;

  listaEgresos: Egreso[] = [];
  columnsGrilla = [
    {
      columnDef: 'nombre',
      header: 'Nombre',
      cell: (egreso: Egreso) => `${(egreso.nombre) ? egreso.nombre : ''}`
    }, {
      columnDef: 'nomTipoEgreso',
      header: 'Tipo egreso',
      cell: (egreso: Egreso) => `${(egreso.nomTipoEgreso) ? egreso.nomTipoEgreso : ''}`
    }, {
      columnDef: 'nomUnidadMedida',
      header: 'Unidad medida',
      cell: (egreso: Egreso) => `${(egreso.nomUnidadMedida) ? egreso.nomUnidadMedida : ''}`
    }, {
      columnDef: 'cantidad',
      header: 'Cantidad',
      cell: (egreso: Egreso) => `${(egreso.cantidad) ? this.decimalPipe.transform(egreso.cantidad, '1.1-1') : ''}`
    }, {
      columnDef: 'precio',
      header: 'Precio',
      cell: (egreso: Egreso) => `${(egreso.precio) ? this.decimalPipe.transform(egreso.precio, '1.2-2') : ''}`
    }, {
      columnDef: 'total',
      header: 'Total',
      cell: (egreso: Egreso) => `${(egreso.total) ? this.decimalPipe.transform(egreso.total, '1.2-2') : ''}`
    }, {
      columnDef: 'dia',
      header: 'Dia',
      cell: (egreso: Egreso) => `${(egreso.dia) ? egreso.dia : ''}`
    }, {
      columnDef: 'fecha',
      header: 'Fecha',
      cell: (egreso: Egreso) => this.datePipe.transform(egreso.fecha, 'dd/MM/yyyy')
    }
  ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private fb: FormBuilder, public dialog: MatDialog,) { }

  ngOnInit(): void {
    this.spinnerService.show();

    this.bdjEgresoGrp = this.fb.group({
      tipoEgreso: ['', []],
      dia: ['', []],
      indicio: ['', []],
      fechaInicio: ['', []],
      fechaFin: ['', []],
    });

    this.definirTabla();
    this.inicializarVariables();
  }

  public inicializarVariables(): void {
    if (sessionStorage.getItem('restDias') != null) {
      this.obtFechaBarChart();
      this.bdjEgresoGrp.get('fechaInicio').setValue(this.fechaRep);
      this.bdjEgresoGrp.get('fechaFin').setValue(this.fechaRep);

      this.paginator.pageSize = 50;
    }

    this.comboTiposEgreso();
    this.comboDias();
    this.buscar();
    this.spinnerService.hide();
  }

  comboTiposEgreso(): void {
    let maestra = new Maestra();
    maestra.idMaestraPadre = 1;//10=>TIPOS EGRESO
    this.maestraService.listarMaestra(maestra).subscribe(
      (data: Maestra[]) => {
        this.tiposEgreso = data;
        this.tiposEgreso.unshift(new Maestra({ id: 0, nombre: 'TODOS' }));
        this.bdjEgresoGrp.get('tipoEgreso').setValue(this.tiposEgreso[0]);
      }, error => {
        console.log(error);
      }
    );
  }

  buscar(): void {
    let request = new EgresoRequest();
    request.idTipoEgreso = (!this.bdjEgresoGrp.get('tipoEgreso').value) ? 0 : this.bdjEgresoGrp.get('tipoEgreso').value.id;
    request.dia = (!this.bdjEgresoGrp.get('dia').value || this.bdjEgresoGrp.get('dia').value.id == 0) ? '' : this.bdjEgresoGrp.get('dia').value.nombre;
    request.indicio = this.bdjEgresoGrp.get('indicio').value;
    request.fechaInicio = this.bdjEgresoGrp.get('fechaInicio').value;
    request.fechaFin = this.bdjEgresoGrp.get('fechaFin').value;

    console.log(request);

    this.dataSource = null;
    this.isLoading = true;
    this.egresoService.listarEgreso(request).subscribe(
      (data: ApiResponse[]) => {
        if (typeof data[0] != undefined && data[0].rcodigo == 0) {
          let result = JSON.parse(data[0].result);

          this.listaEgresos = result ? result : [];
          this.agregarFilaResumen();
          this.cargarDatosTabla();
          this.isLoading = false;
        } else {
          console.error('Ocurrio un error al registrar egreso');
          this.isLoading = false;
        }
      },
      error => {
        console.error('Error al consultar datos');
        this.isLoading = false;
      }
    );
  }

  regEgreso(obj): void {
    const dialogRef = this.dialog.open(RegEgresoComponent, {
      width: '600px',
      data: { title: MENSAJES.INTRANET.BANDEJAEGRESOS.EGRESO.REGISTRAR.TITLE, objeto: obj }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.listaEgresos.unshift(result);
        this.cargarDatosTabla();
      }
    });
  }

}
