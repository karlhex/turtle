export interface Currency {
  id?: number;
  code: string; // ISO 4217 currency code (e.g., USD, EUR, CNY)
  name: string; // Full name of the currency
  symbol: string; // Currency symbol (e.g., $, €, ¥)
  decimalPlaces: number; // Number of decimal places for the currency
  country?: string; // Country where the currency is primarily used
  active: boolean; // Whether the currency is active in the system
  exchangeRate?: number; // Exchange rate relative to the base currency
  isBaseCurrency: boolean; // Whether this is the system's base currency
  createdTime?: Date;
  updatedTime?: Date;
}

export interface CurrencyQuery {
  page: number;
  size: number;
  search?: string;
  active?: boolean;
}
