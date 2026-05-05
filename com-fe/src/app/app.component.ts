import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  constructor(private route: ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      const aff = params['aff'];
      if (aff) {
        sessionStorage.setItem('aff_code', aff);
        console.log('Affiliate code captured:', aff);
      }
    });
  }
}
