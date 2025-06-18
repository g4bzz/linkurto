import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResolveUrlComponent } from './resolve-url.component';

describe('ResolveUrlComponent', () => {
  let component: ResolveUrlComponent;
  let fixture: ComponentFixture<ResolveUrlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResolveUrlComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ResolveUrlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
