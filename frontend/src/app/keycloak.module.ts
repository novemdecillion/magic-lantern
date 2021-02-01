import { APP_INITIALIZER, NgModule } from '@angular/core';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { environment } from '../environments/environment';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: environment.keycloakConfig,
      initOptions: {
        onLoad: 'check-sso',
        checkLoginIframe: false,
        // checkLoginIframeInterval: 15
      },
    });
}

@NgModule({
  imports: [
    KeycloakAngularModule,
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
  ]
})
export class KeycloakModule { }
