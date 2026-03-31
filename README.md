Student Name: Aryam Singh 
Registration Number: 24BAI10624  
Course: Programming in Java
# Library Management System

A Java-based command-line application designed to manage and simulate a real-world library system. The application supports book inventory management, issuance tracking, and penalty calculation for overdue returns, while ensuring persistent data storage across sessions.

# Key Features

* Inventory management of books across multiple domains
* Book issue and return tracking with status updates
* Automatic return deadline (14 days from issue date)
* Penalty calculation (₹10 per day for late returns)
* View available (non-issued) books
* Keyword-based book search
* Pagination for handling large datasets
* File-based data persistence using `books.txt`

# System Architecture

* Main.java: Entry point and menu-driven interaction
* Book class: Represents book data and status
* Library class: Maintains shared data such as total penalty

# Technologies Used

* Java (Core Java)
* Object-Oriented Programming (Encapsulation, Inheritance)
* Collections Framework (ArrayList)
* File Handling (BufferedReader, BufferedWriter)
* Java Time API (LocalDate, DateTimeFormatter, ChronoUnit)

# Installation and Execution

1. Navigate to the project directory
2. Compile the program:
   javac Main.java 
3. Run the program:
   java Main
   
# Functional Overview

1. Add Book
2. View All Books
3. View Available Books
4. Issue Book
5. Return Book
6. Show Statistics
7. Search Book
8. Exit
   
# Data Persistence

The system stores data in a local file (`books.txt`). All updates are written to the file to ensure that the library state is preserved between program executions.


# Future Enhancements

* Graphical user interface (GUI)
* Database integration
* User authentication
* Advanced filtering and sorting
