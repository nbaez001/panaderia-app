// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  WsPanaderiaBackend: 'http://localhost:8081/panaderia-backend',
  WsPanaderiaAuthorizer: 'http://localhost:8082/panaderia-authorizer',
  WsReniec: 'http://perficon.elnazarenovraem.edu.pe/api/soap/persona',
  clientId: 'pseg',
  clientSecret: 'pseg@2020'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
