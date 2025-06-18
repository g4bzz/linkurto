import { TestBed } from '@angular/core/testing';

import { LinkurtoService } from './linkurto.service';

describe('LinkurtoService', () => {
  let service: LinkurtoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LinkurtoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
