import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
class Book {
    String title;
    String author;
    String isbn;
    int quantity;
    int issuedCount;

    public Book(String title, String author, String isbn, int quantity) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.issuedCount = 0;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getQuantity() { return quantity; }
    public int getIssuedCount() { return issuedCount; }
    public int getAvailable() { return quantity - issuedCount; }

    public boolean issueBook() {
        if (issuedCount < quantity) {
            issuedCount++;
            return true;
        }
        return false; // No copies left
    }

    public boolean returnBook() {
        if (issuedCount > 0) {
            issuedCount--;
            return true;
        }
        return false; // No issued copies
    }

    @Override
    public String toString() {
        return String.format(
            "Title: %-20s | Author: %-15s | ISBN: %-10s | Available: %d/%d",
            title, author, isbn, getAvailable(), quantity
        );
    }
}

// Library class (Data Management)
class Library {
    private ArrayList<Book> books;
    private HashMap<String, Book> isbnMap;

    public Library() {
        books = new ArrayList<>();
        isbnMap = new HashMap<>();
    }

    public void addBook(Book newBook) {
         Book existingBook = isbnMap.get(newBook.getIsbn());
        
        if (existingBook != null) {
            // Update quantity if book exists
            existingBook.quantity=existingBook.getQuantity() + newBook.getQuantity();
        } else {
            // Add new book if ISBN doesn't exist
            books.add(newBook);
            isbnMap.put(newBook.getIsbn(), newBook);
        }
    }
    

    public Book searchByIsbn(String isbn) {
        return isbnMap.get(isbn);
    }

    public boolean deleteBook(String isbn) {
        Book book = isbnMap.get(isbn);
        if (book != null) {
            books.remove(book);
            isbnMap.remove(isbn);
            return true;
        }
        return false;
    }

    public boolean issueBook(String isbn) {
        Book book = isbnMap.get(isbn);
        return book != null && book.issueBook();
    }

    public boolean returnBook(String isbn) {
        Book book = isbnMap.get(isbn);
        return book != null && book.returnBook();
    }

    public ArrayList<Book> getAllBooks() {
        return books;
    }
}

// Main GUI Class
public class LibrarySystem extends JFrame {
    private Library library = new Library();
    private JTextArea outputArea;
    private JTextField titleField, authorField, isbnField, quantityField;

    public LibrarySystem() {
        setTitle("Library Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); 
        getContentPane().setBackground(Color.GREEN);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10)); 
        inputPanel.setBackground(Color.PINK);
        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        quantityField = new JTextField();

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("ISBN:"));
        inputPanel.add(isbnField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        // Buttons
        JButton addButton = new JButton("Add Book");
        JButton searchButton = new JButton("Search by ISBN");
        JButton displayButton = new JButton("Display All Books");
        JButton deleteButton = new JButton("Delete Book");
        JButton issueButton = new JButton("Issue Book");
        JButton returnButton = new JButton("Return Book");

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); 
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(800, 300)); 

        // Button Actions
        addButton.addActionListener(e -> addBook());
        searchButton.addActionListener(e -> searchBook());
        displayButton.addActionListener(e -> displayBooks());
        deleteButton.addActionListener(e -> deleteBook());
        issueButton.addActionListener(e -> issueBook());
        returnButton.addActionListener(e -> returnBook());

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10)); 
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(issueButton);
        buttonPanel.add(returnButton);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                outputArea.append("[ERROR] Missing fields!\n");
                return;
            }

            Book book = new Book(title, author, isbn, quantity);
            library.addBook(book);
            outputArea.append("[ADDED] " + book + "\n");
            clearFields();
        } catch (NumberFormatException ex) {
            outputArea.append("[ERROR] Invalid quantity!\n");
        }
    }

    private void searchBook() {
        String isbn = isbnField.getText().trim();
        Book book = library.searchByIsbn(isbn);
        outputArea.append(book != null ? "[FOUND] " + book + "\n" : "[ERROR] Book not found!\n");
    }

    private void displayBooks() {
        ArrayList<Book> books = library.getAllBooks();
        outputArea.append("\n--- ALL BOOKS (" + books.size() + ") ---\n");
        if (books.isEmpty()) {
            outputArea.append("No books in the library.\n");
        } else {
            books.forEach(b -> outputArea.append(b + "\n"));
        }
    }

    private void deleteBook() {
        String isbn = isbnField.getText().trim();
        boolean deleted = library.deleteBook(isbn);
        outputArea.append(deleted ? 
            "[DELETED] Book (ISBN: " + isbn + ")\n" : 
            "[ERROR] Book not found!\n");
        clearFields();
    }

    private void issueBook() {
        String isbn = isbnField.getText().trim();
        boolean issued = library.issueBook(isbn);
        outputArea.append(issued ? 
            "[ISSUED] Book (ISBN: " + isbn + ")\n" : 
            "[ERROR] Book unavailable or not found!\n");
    }

    private void returnBook() {
        String isbn = isbnField.getText().trim();
        boolean returned = library.returnBook(isbn);
        outputArea.append(returned ? 
            "[RETURNED] Book (ISBN: " + isbn + ")\n" : 
            "[ERROR] No issued copies found!\n");
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibrarySystem app = new LibrarySystem();
            app.setVisible(true);
        });
    }
}
