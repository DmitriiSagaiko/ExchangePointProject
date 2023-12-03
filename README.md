 
## Project "Currency Exchange Office"

**Project description:**
Develop a console application to simulate the operation of a currency exchange bureau.
Users can register accounts, open accounts in different currencies, fund accounts, withdraw funds from accounts, exchange currencies, and view the history of their transactions.

**Technical task:**

1. **Multilayer Application Architecture:**
    - **Model:** Define entities such as user, account, transaction, currency, exchange rate and other required entities.
    - **Service:** Develop the business logic of the application, including managing user data, performing currency transactions, etc.
    - **Repository:** Create a layer to interact with the data repository (e.g., in memory or file(s)).

2. **Data Organization and Account Management:**
    - Implement mechanisms to manage a user's data, including their accounts and account management.
    - A user can have accounts in at least 3 different currencies.

3. **Currency Transaction Management:**
    - Provide functionality for currency transactions, including deposits, exchanges, and viewing transaction history.

4. **Data Validation:**
    - When registering a new user, implement validation of the entered email and password. Validation should ensure that the email corresponds to the standard e-mail format and the password meets the specified security criteria (e.g. minimum length, presence of letters and digits).

5. **User Interface:**
    - Implement a console menu to access application functionality.

6. **Testing:**
    - Develop JUnit tests to test all aspects of the application functionality, with special attention to testing the service layer.

7. **Use Map collection:**
    - During the application development process, actively use the `Map` collection to organize and manage data such as user accounts, invoices, exchange rates and other suitable scenarios.
    - For example:
        - To store a list of user accounts, where the key would be the user ID and the value would be the list of user accounts.
        - To store currency rates, where the key would be the currency code and the value would be the current rate.

**Optional:**
- If validation fails, the application should throw an exception, which should then be handled correctly. This may include informing the user of the reason for the error and prompting them to retry data entry.
- Functionality to view historical exchange rates.


## User functionality:
- New user registration (with email and password validation)
- Account login
- View balance (balance on all accounts or a specific account)
- Replenishment of the account in the selected currency (check if the user has such an account. If there is no such account - open a corresponding account)
- Withdrawal of funds from an account (with corresponding checks on the possibility of the operation)
- Opening a new account
- Closing an account (with checks: if there are funds on the account? what to do?)
- Viewing transaction history (for all accounts / for a specific currency)
- Currency exchange (transferring funds from one account to another with the corresponding cross-rate)

## Administrator functionality
- Change currency rate
- Ability to add or delete currencies from the list (When deleting, check if users have open accounts in this currency? If there are - what to do?)
- View user transactions
- View operations by currency
- Assigning another user as an administrator (moderator / cashier)

## Organize data storage (lists of users, currencies, rates, etc. etc.)

## Optional
- Currency rate history
- Custom exception classes and their handling
