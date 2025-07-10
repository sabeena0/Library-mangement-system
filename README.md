# 📚 Java Library Management System

A GUI-based application to manage book inventory, issuances, and returns using Java Swing and core data structures.

<img width="973" height="673" alt="image" src="https://github.com/user-attachments/assets/3667ec73-882c-424a-bda9-5ecadfff08d6" />


## ✨ Features
- **Book Management**: Add/update books (auto-merges quantities for same ISBN)
- **Dual Search**: Find books by ISBN (O(1) time) or title
- **Transaction Tracking**: Issue/return books with validation
- **Persistent Data**: All operations run in-memory (easy to extend to databases)

## 🛠 Tech Stack
- **Java 17+**
- **Swing** (GUI)
- **Data Structures**: `HashMap`, `ArrayList`
- **OOP Principles**: Encapsulation, Abstraction

## 🚀 Quick Start
1. Ensure Java JDK 17+ is installed
   ```bash
   java --version
2.Clone the repository then compile and run
 ```bash
   git clone https://github.com/yourusername/library-management.git
   cd library-management/src/
   javac LibrarySystem.java
   java LibrarySystem
