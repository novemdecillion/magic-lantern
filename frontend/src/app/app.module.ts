import { BrowserModule } from '@angular/platform-browser';
import { Injectable, NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { GraphQLModule } from './graphql.module';
import { HttpClientModule, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MaterialModule } from './material.module';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { HomeComponent } from './root/home/home.component';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { reducers, metaReducers } from './root/store';
import { AppEffects } from './root/store/effects/app.effect';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!req.headers.has('X-Requested-With')) {
      req.headers.append('X-Requested-With', 'XMLHttpRequest');
    }

    return next.handle(req).pipe(
      // tapオペレータでレスポンスの流れを傍受する
      tap(resp => {
          // リクエスト成功時のログ出力
          console.log(resp);
          if (resp instanceof HttpResponse) {
            if(resp.status == 302) {
              location.href = resp.headers.get('location') ?? '/';
            }
          }
      },
      error => {
          // エラー時の共通処理やログ出力
          console.error(error);
      }),
  );  }

}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    GraphQLModule,
    HttpClientModule,
    MaterialModule,
    StoreModule.forRoot(reducers, {
      metaReducers
    }),
    EffectsModule.forRoot([AppEffects])
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
