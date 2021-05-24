import { Component, ElementRef, OnInit, ViewChild,  } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-slideshow',
  templateUrl: './slideshow.component.html',
  styleUrls: ['./slideshow.component.scss']
})
export class SlideshowComponent implements OnInit {
  @ViewChild('viewer', {static: true}) viewer!: ElementRef<HTMLIFrameElement>

  studyId: string
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private hostElementRef: ElementRef<HTMLElement>
  ) {
    this.studyId = this.activatedRoute.snapshot.paramMap.get('studyId')!!;
  }

  ngOnInit(): void {
    this.isLoading = true;

    if (this.studyId) {
      this.viewer.nativeElement.src = `/slideshow/${this.studyId}/`;
    } else {
      let slideId = this.activatedRoute.snapshot.paramMap.get('slideId');
      this.postFromViewer(`/slideshow/start/${slideId}`)
    }
  }

  onPrev() {
    this.postFromViewer(`${this.viewer.nativeElement.contentWindow?.location.href}?action=PREV`)
  }

  onNext() {
    let submitElement = this.viewer.nativeElement.contentDocument?.querySelector<HTMLInputElement>('form input[type="submit"]');
    if (submitElement) {
      submitElement.click();
    } else {
      this.postFromViewer(`${this.viewer.nativeElement.contentWindow?.location.href}?action=NEXT`);
    }
  }

  postFromViewer(url: string) {
    let hostElement = this.hostElementRef.nativeElement;
    let formElement = document.createElement('form');
    formElement.method = 'post';
    formElement.action = url;
    formElement.target = 'slide-viewer';
    hostElement.appendChild(formElement);
    formElement.submit();
    hostElement.removeChild(formElement);
    this.isLoading = true;
  }

  onLoaded() {
    this.isLoading = false;

    let url = this.viewer.nativeElement.contentWindow?.location.href;
    if (url) {
      if (url == 'about:blank') {
        // 何もしない
      } else if (url.endsWith('/login')) {
        window.location.href = url
      } else if (!url.endsWith(`/slideshow/${this.studyId}/`)) {
        this.router.navigateByUrl('/study/list')
      }
    }
  }
}
