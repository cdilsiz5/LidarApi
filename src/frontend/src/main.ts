import { CommonModule } from "@angular/common";
import { importProvidersFrom } from "@angular/core";
import { bootstrapApplication, BrowserModule } from "@angular/platform-browser";
import { RouterModule } from "@angular/router";
import { provideHttpClient } from '@angular/common/http'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from "./app/app.component";
import { routes } from "./app/router";


bootstrapApplication(AppComponent,{
  providers: [
    provideHttpClient(),
    importProvidersFrom(
      BrowserModule,
      CommonModule,
      BrowserAnimationsModule,
      RouterModule.forRoot(routes), BrowserAnimationsModule
    )
  ]
})