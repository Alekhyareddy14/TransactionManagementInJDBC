Transaction Handling in JDBC:
     Transaction handling in JDBC ensures that multiple SQL operations either complete successfully together or roll back if something goes wrong. This prevents inconsistent data in the database.
Key Concepts:
1.	Auto-commit Mode:
o	By default, JDBC runs in auto-commit mode, meaning every SQL operation is immediately committed to the database.
o	To use transactions, we must disable auto-commit using con.setAutoCommit(false);
2.	Commit & Rollback:
o	con.commit(); → Saves all changes permanently.
o	con.rollback(); → Reverts all changes if an error occurs.
Savepoint in JDBC
      Savepoints in JDBC allow partial rollbacks within a transaction. Instead of rolling back the entire transaction, we can roll back to a specific point, preserving successful operations before the savepoint.
Key Concepts:
1.	con.setAutoCommit(false); → Start a transaction.
2.	Savepoint sp = con.setSavepoint("MySavepoint"); → Create a savepoint.
3.	con.rollback(sp); → Roll back only to the savepoint.
4.	con.commit(); → Commit the successful operations.
