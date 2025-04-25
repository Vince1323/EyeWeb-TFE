import { Directive, HostListener, ElementRef, Renderer2 } from '@angular/core';

@Directive({
  selector: '[appAllowNegative]'
})
export class AllowNegativeDirective {

  private isComposing = false;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    const allowedKeys = [
      'Backspace', 'ArrowLeft', 'ArrowRight', 'Tab', 'Delete',
      '-', '.', 'Home', 'End'
    ];
    const isNumber = /[0-9]/.test(event.key);

    if (!isNumber && !allowedKeys.includes(event.key)) {
      event.preventDefault();
    }

    const inputElement = event.target as HTMLInputElement;

    // Prevent multiple negative signs or decimal points
    if (event.key === '-' && inputElement.value.includes('-')) {
      event.preventDefault();
    }
    if (event.key === '.' && inputElement.value.includes('.')) {
      event.preventDefault();
    }

    // Prevent minus sign if it's not at the beginning
    const caretPosition = inputElement.selectionStart;
    if (event.key === '-' && caretPosition !== 0) {
      event.preventDefault();
    }
  }

  @HostListener('compositionstart', ['$event'])
  onCompositionStart(event: CompositionEvent) {
    this.isComposing = true;
  }

  @HostListener('compositionend', ['$event'])
  onCompositionEnd(event: CompositionEvent) {
    this.isComposing = false;
    this.handleInput(event);
  }

  @HostListener('input', ['$event'])
  onInput(event: InputEvent) {
    if (this.isComposing) return;
    this.handleInput(event);
  }

  private handleInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    let value = inputElement.value;

    // Remove any non-numeric characters except minus and comma
    value = value.replace(/[^0-9.-]/g, '');

    // Ensure the value is within the allowed range and format correctly
    let numValue = parseFloat(value);

    if (!isNaN(numValue)) {
      // Restrict to 2 decimal places
      const parts = value.split('.');
      if (parts.length === 2) {
        parts[1] = parts[1].slice(0, 2);
        value = parts.join('.');
      }
    }

    this.renderer.setProperty(inputElement, 'value', value);
  }
}