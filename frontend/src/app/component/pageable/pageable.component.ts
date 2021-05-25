import { Page } from 'src/app/entity/page';

export abstract class PageableComponent<T> {
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;
  elems: T[] = [];

  constructor() { }

  printPage(response: Page<T>): void {
    this.elems = response.content;
    this.totalPages = response.totalPages;
    this.page = response.number;
    this.last = response.last;
    this.first = response.first;
    this.empty = response.empty;
  }

  getPagesFull(): number[] {
    return this.getNPages(7);
  }

  getPagesMin(): number[] {
    return this.getNPages(3);
  }

  getNPages(pages: number): number[] {
    let result = [];
    let pagesToShow = Math.min(this.totalPages, pages);
    let leftOffset = this.page - Math.floor(pagesToShow/2);
    leftOffset = leftOffset - Math.min(0, 0+leftOffset);
    let rightOffset = Math.max(0, this.page + Math.ceil(pagesToShow/2)-this.totalPages);

    for(var i=0; i<pagesToShow; i++) {
      result.push(i+leftOffset-rightOffset);
    }

    return result;
  }

}
