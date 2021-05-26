import { Component, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {
  @Input() totalPages: number = 0;
  @Input() page: number = 0;
  @Input() last: boolean = true;
  @Input() first: boolean = true;
  @Input() pagesMin: number[] = [];
  @Input() pagesFull: number[] = [];

  @Output() newPageEvent = new EventEmitter<number>();

  constructor() { }

  getPage(page: number): void {
    this.newPageEvent.emit(page);
  }

  getPagesMin(): number[] {
    return this.pagesMin;
  }

  getPagesFull(): number[] {
    return this.pagesFull;
  }

}
