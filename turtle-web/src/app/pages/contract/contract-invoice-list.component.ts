import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Currency } from '../../models/currency.model';
import { Invoice } from '../../models/invoice.model';
import { ContractInvoiceDialogComponent } from './contract-invoice-dialog.component';
import { ConfirmDialogComponent } from '@components/confirmdialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { Contract } from '@app/models/contract.model';
import { Company } from '@app/models/company.model';

@Component({
  selector: 'app-contract-invoice-list',
  templateUrl: './contract-invoice-list.component.html',
  styleUrls: ['./contract-invoice-list.component.scss'],
})
export class ContractInvoiceListComponent implements OnInit {
  @Input() buyer?: Company;
  @Input() seller?: Company;
  @Input() items: Invoice[] = [];
  @Input() currency?: Currency;
  @Input() readOnly = false;
  @Output() itemEdited = new EventEmitter<Invoice>();
  @Output() itemDeleted = new EventEmitter<Invoice>();

  displayedColumns: string[] = [
    'invoiceNo',
    'type',
    'amount',
    'taxRate',
    'taxAmount',
    'totalAmount',
    'invoiceDate',
    'buyerTaxInfo',
    'sellerTaxInfo',
    'cancelled',
    'actions',
  ];

  constructor(private dialog: MatDialog, private translate: TranslateService) {}

  ngOnInit(): void {}

  openInvoiceDialog(item?: Invoice): void {
    this.itemEdited.emit(item);
  }

  deleteInvoice(item: Invoice): void {
    this.itemDeleted.emit(item);
  }
}
