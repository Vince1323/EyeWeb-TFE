import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'decimalFormat'
})
export class DecimalFormatPipe implements PipeTransform {

  transform(value: any): string {
    if (value == null || isNaN(value)) {
      return '-';
    }
    return Number(value).toFixed(3).replace(/\.?0+$/, '');
  }

}
