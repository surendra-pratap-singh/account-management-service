INSERT INTO client_tbl (client_id, first_name, last_name, email)
VALUES ('1712957116411', 'John', 'Doe', 'john.doe@example.com'),
       ('1712957116412', 'Jane', 'Smith', 'jane.smith@example.com'),
       ('1712957116413', 'Alice', 'Johnson', 'alice.johnson@example.com');

INSERT INTO account_tbl (account_id, account_balance, client_id, account_currency)
VALUES ('1712960643757', '100', '1712957116411', 'EUR'),
       ('1712960651227', '100', '1712957116412', 'USD'),
       ('1712960662588', '1000', '1712957116413', 'INR'),
       ('1712960272528', '111', '1712957116411', 'USD'),
       ('1712960272518', '500', '1712957116411', 'INR'),
       ('1713960272588', '100', '1712957116412', 'EUR');

INSERT INTO transaction_tbl (transaction_id, txn_currency, txn_amount, txn_type, txn_date, account_id)
VALUES ('1713132476179', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.179047', '1712960643757'),
       ('1712961479870', 'EUR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1713132276179', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.172047', '1712960643757'),
       ('1713133593705', 'EUR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1713133593705', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.218694', '1712960643757'),
       ('1713133593705', 'EUR', '10', 'CREDIT', '2024-04-15 01:07:56.211694', '1713960272588'),
       ('1713133605106', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.225694', '1712960643757'),
       ('1712963479870', 'EUR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1715961479570', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960643757'),
       ('1812961439870', 'EUR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1712261470870', 'EUR', '10', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960643757'),
       ('1215961469870', 'INR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1215961469870', 'INR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1713960272588'),
       ('1712962479370', 'INR', '10', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960662588'),
       ('1712962879870', 'INR', '10', 'CREDIT', '2024-04-15 01:07:56.215694', '1712960272518'),
       ('1713134510509', 'USD', '5.50', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960272528'),
       ('1713134510539', 'INR', '459.24', 'CREDIT', '2024-04-15 01:07:56.215694', '1712960662588'),
       ('1713134528541', 'USD', '5.00', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960272528'),
       ('1713134528541', 'INR', '417.49', 'CREDIT', '2024-04-15 01:07:56.215694', '1712960662588'),
       ('1713134585993', 'USD', '5.00', 'DEBIT', '2024-04-15 01:07:56.215694', '1712960272528'),
       ('1713134585995', 'USD', '5.00', 'CREDIT', '2024-04-15 01:07:56.215694', '1712960651227');
