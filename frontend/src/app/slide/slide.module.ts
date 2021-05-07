import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SlideRoutingModule } from './slide-routing.module';
import { SlideListComponent } from './slide-list/slide-list.component';
import { ShareModule } from '../share/share.module';
import { AddSlideComponent } from './add-slide/add-slide.component';

@NgModule({
  declarations: [SlideListComponent, AddSlideComponent],
  imports: [
    CommonModule,
    SlideRoutingModule,
    ShareModule
  ]
})
export class SlideModule { }
