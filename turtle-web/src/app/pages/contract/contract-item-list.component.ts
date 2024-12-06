import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ContractItem } from '../../models/contract-item.model';
import { Currency } from '../../models/currency.model';

@Component({
  selector: 'app-contract-item-list',
  templateUrl: './contract-item-list.component.html',
  styleUrls: ['./contract-item-list.component.scss']
})
export class ContractItemListComponent {
  @Input() items: ContractItem[] = [];
  @Input() currency?: Currency;
  @Output() itemDeleted = new EventEmitter<ContractItem>();
  @Output() itemEdited = new EventEmitter<ContractItem>();

  displayedColumns: string[] = ['productName', 'modelNumber', 'quantity', 'unitPrice', 'totalAmount', 'remarks', 'actions'];

  constructor() {}

  editItem(item: ContractItem): void {
    this.itemEdited.emit(item);
  }

  deleteItem(item: ContractItem): void {
    this.itemDeleted.emit(item);
  }
}
