import { Component, OnInit,  } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-slideshow',
  templateUrl: './slideshow.component.html',
  styleUrls: ['./slideshow.component.scss']
})
export class SlideshowComponent implements OnInit {
  slideUrl: SafeResourceUrl;

  constructor(
    private activatedRoute: ActivatedRoute,
    sanitizer: DomSanitizer
  ) {
    let courseId = this.activatedRoute.snapshot.paramMap.get('courseId');
    this.slideUrl = sanitizer.bypassSecurityTrustResourceUrl(`/slideshow/${courseId}`);
  }

  ngOnInit(): void {
  }

}
