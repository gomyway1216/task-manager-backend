# task-manager-backend
This is a backend for task-manager. It utilizes Spling framework and stores the record in MySQL.

# How to run MySQL server
1. Install MySQL
2. Run MySQL in local. Better way would be using Homebrew which I used when initially setting up. But that way seems not working. Instead, running MySQL from the System Preference 
3. ![Screen Shot 2023-06-10 at 10 08 20 PM](https://github.com/gomyway1216/task-manager-backend/assets/32227575/1bd84f15-05e2-4e66-a232-5b0f6612e628)
4. One point is that, if this is a different instance from what I used initially, the data is not shared. In Homebrew, I was using
mysql-8.0.20-macos10.15-x86_64
But in System Preference, We see 8.0.32 is running.
5. Open Terminal and run bellow command
```
mysql -u root -p
```
and put the root password. If it doesn't work. Make sure to instsall MySQL in terminal.
6. Create database by running following command.
```
CREATE DATABASE task_manager_db;
```
otherwise we would get below issue while build or running the server code.
```
java.sql.SQLException: Access denied for user 'root'@'localhost' (using password: YES)
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129) ~[mysql-connector-j-8.0.32.jar:8.0.32]
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122) ~[mysql-connector-j-8.0.32.jar:8.0.32]
```
