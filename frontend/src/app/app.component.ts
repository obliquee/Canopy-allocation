import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';

//for one investor row in the UI
interface InvestorRow {
  name: string;
  requestedAmount: number | null;
  averageAmount: number | null;
}

//items that are sent to backend API
interface AllocationRequest {
  allocationAmount: number;
  investorAmounts: { name: string; requestedAmount: number; averageAmount: number; }[];
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, MatIconModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Allocation Proration Tool';

  // It is the default starting allocation and investors
  allocationAmount: number | null = 100;
  investors: InvestorRow[] = [
    { name: 'Investor A', requestedAmount: 100, averageAmount: 95 },
    { name: 'Investor B', requestedAmount: 2, averageAmount: 1 },
    { name: 'Investor C', requestedAmount: 1, averageAmount: 4 }
  ];

  // This is Backend Calculation API 
  apiUrl = 'http://localhost:8080/api/allocations/calculate';

  loading = false;
  error: string | null = null;

  // It stores result that is returned from backend
  results: { name: string; amount: number }[] = [];

  constructor(private http: HttpClient) {}

  // method is used to add a new blank investor entry
  addInvestor(): void {
    this.investors.push({
      name: '',
      requestedAmount: null,
      averageAmount: null
    });
  }

  // method for removing investor
  removeInvestor(index: number): void {
    this.investors.splice(index, 1);
  }

  // this is method that will reset to simple defined example
  resetToDefault(): void {
    this.allocationAmount = 100;
    this.investors = [
      { name: 'Investor A', requestedAmount: 100, averageAmount: 100 },
      { name: 'Investor B', requestedAmount: 25, averageAmount: 25 }
    ];
    this.results = [];
    this.error = null;
  }

  // this method will validate and submit the data from frontend to backend API
  submit(): void {
    this.error = null;
    this.results = [];

    // checks either allocation amount is null or negative
    if (this.allocationAmount == null || this.allocationAmount < 0) {
      this.error = 'Allocation amount must be postive number.';
      return;
    }

    // this remove the empty investor entries
    const cleanedInvestors = this.investors.filter(
      (i) => i.name.trim().length > 0
    );

    if (cleanedInvestors.length === 0) {
      this.error = 'Please add at least one investor.';
      return;
    }

    // this part ensure all fields are valid and not negative
    const missing = cleanedInvestors.find(
      (i) =>
        i.requestedAmount == null ||
        i.requestedAmount < 0 ||
        i.averageAmount == null ||
        i.averageAmount < 0
    );
    if (missing) {
      this.error =
        'Each investor needs positive requested and average amount.';
      return;
    }
    
    // here prepare payload for API
    const payload: AllocationRequest = {
      allocationAmount: this.allocationAmount,
      investorAmounts: cleanedInvestors.map((i) => ({
        name: i.name.trim(),
        requestedAmount: i.requestedAmount as number,
        averageAmount: i.averageAmount as number
      }))
    };

    this.loading = true;

    // this will send post request to backend
    this.http.post<Record<string, number>>(this.apiUrl, payload).subscribe({
      next: (data) => {
  this.loading = false;

  // this will convert plain object response into array formt for UI
  this.results = Object.entries(data).map(([name, amount]) => ({
    name,
    amount
  }));
 
},

  // it will throw error if any error is caught
      error: (err) => {
        console.error(err);
        this.loading = false;
        this.error =
          'Something went wrong while calculating.';
      }
    });
  }
}
