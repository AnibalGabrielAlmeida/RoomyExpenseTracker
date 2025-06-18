export interface Expense {
  id: number;
  name: string;
  description: string;
  amount: number;
  category: string;
  date: string;      // ISO string
  houseName: string;
  houseId: number;
}
