Database Transaction Anomalies Demo

This project demonstrates two common database transaction anomalies:

1️⃣ Dirty Read
2️⃣ Phantom Read

These problems occur when multiple transactions run concurrently without proper isolation levels.

1️⃣ Dirty Read
Definition

A Dirty Read occurs when a transaction reads data written by another transaction that has not committed yet.

If the first transaction rolls back, the value read by the second transaction becomes invalid.

Flow
Transaction T1                Transaction T2

START
UPDATE account
SET balance = 500
WHERE id = 1
(not committed)

                               START
                               SELECT balance
                               FROM account
                               WHERE id = 1
                               → 500  (Dirty Read)

ROLLBACK
Result
Actual value in database = 100

Transaction T2 read 500, but that value never actually existed permanently because T1 rolled back.

2️⃣ Phantom Read
Definition

A Phantom Read occurs when the same query is executed twice in a transaction, but the number of rows returned changes because another transaction inserted or deleted rows.

The new rows that appear are called phantom rows.

Flow
Transaction T1                    Transaction T2

START
SELECT * FROM account
WHERE balance > 100
→ 2 rows

                                   START
                                   INSERT account(balance = 300)
                                   COMMIT

SELECT * FROM account
WHERE balance > 100
→ 3 rows
Result

The extra row returned in the second query is called a Phantom Row.

If you want, I can also give you a much cleaner GitHub-style flow diagram version like this (very good for README):

T1:  SELECT balance > 100 → 2 rows
T2:                 INSERT balance=300
T2:                 COMMIT
T1:  SELECT balance > 100 → 3 rows

### SERIALIZABLE
Simple Example

Assume two transactions:

Without SERIALIZABLE
T1: read accounts with balance > 100 → 1 row

T2: insert new account with balance 200
commit

T1: read again → 2 rows   (phantom row)

The result changes inside the same transaction.

With SERIALIZABLE
T1: read accounts with balance > 100 → 1 row

T2: tries to insert → must wait

T1: read again → still 1 row
commit

T2: insert happens after T1 commits

Now the system behaves as if T1 finished before T2 started.

One-Line Definition (Best for Interviews)

SERIALIZABLE guarantees that concurrent transactions produce the same result as if they were executed sequentially.

What It Prevents

SERIALIZABLE prevents all anomalies:

Problem	Prevented
Dirty Read	✅
Non-Repeatable Read	✅
Phantom Read	✅