# Canopy Allocation Proration Project

This folder contains Spring Boot backend and Angular frontend
implementing the allocation proration tool as described in initial Readme file provided to me by Jared.

## Backend (Spring Boot)

- Location: backend/
- Endpoint: /api/allocations/calculate

Request body:

{
"allocationAmount": 100,
"investorAmounts": [
{ "name": "Investor A", "requestedAmount": 100, "averageAmount": 95 },
{ "name": "Investor B", "requestedAmount": 2, "averageAmount": 1 },
{ "name": "Investor C", "requestedAmount": 1, "averageAmount": 4 }
]
}

Response body:

{
"Investor A": 97.96875,
"Investor B": 1.03125,
"Investor C": 1.0
}

### TO Run backend Service Follow

cd backend
mvn spring-boot:run

## Frontend (Angular)

- Location: frontend/

### Run frontend

cd frontend
npm install
npm start # or: npx ng serve

UI follows: a card with allocation input at the top,
a dynamic investor table, and a results table at the right side.
