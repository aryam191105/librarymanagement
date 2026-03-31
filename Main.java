import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

class Book {
    int id;
    String title;
    boolean isIssued;
    String issuedTo;
    LocalDate issueDate;
    LocalDate returnDate;

    Book(int id, String title) {
        this.id = id;
        this.title = title;
        this.isIssued = false;
    }

    public String toString() {
        return id + "," + title + "," + isIssued + "," +
                issuedTo + "," +
                (issueDate != null ? issueDate : "") + "," +
                (returnDate != null ? returnDate : "");
    }

    public static Book fromString(String data) {
        String[] p = data.split(",");

        Book b = new Book(Integer.parseInt(p[0]), p[1]);

        if (p.length > 2)
            b.isIssued = Boolean.parseBoolean(p[2]);

        if (p.length > 3)
            b.issuedTo = p[3];

        if (p.length > 4 && !p[4].isEmpty())
            b.issueDate = LocalDate.parse(p[4]);

        if (p.length > 5 && !p[5].isEmpty())
            b.returnDate = LocalDate.parse(p[5]);

        return b;
    }
}

class Library {
    protected static double totalPenalty = 0;
}

public class Main extends Library {

    static List<Book> books = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        loadFromFile();

        if (books.isEmpty()) {
            initializeLibrary();
        }

        while (true) {
            System.out.println("\n===== LIBRARY MENU =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. View Available Books");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Show Stats");
            System.out.println("7. Search Book");
            System.out.println("8. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    saveToFile();
                    break;

                case 2:
                    viewBooksPaged(false);
                    break;

                case 3:
                    viewBooksPaged(true);
                    break;

                case 4:
                    issueBook();
                    saveToFile();
                    break;

                case 5:
                    returnBook();
                    saveToFile();
                    break;

                case 6:
                    showStats();
                    break;

                case 7:
                    searchBook();
                    break;

                case 8:
                    saveToFile();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void initializeLibrary() {
        String[] categories = {
                "Data Structures", "Algorithms", "Operating Systems",
                "Computer Networks", "Mathematics", "Physics",
                "History", "Geography", "Literature",
                "Artificial Intelligence", "Machine Learning"
        };

        Random rand = new Random();

        for (int i = 1; i <= 1000; i++) {
            String category = categories[rand.nextInt(categories.length)];
            String title = category + " Vol-" + (rand.nextInt(100) + 1);
            books.add(new Book(i, title));
        }

        System.out.println("Library initialized with 1000 books.");
    }

    static void addBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Book b : books) {
            if (b.id == id) {
                System.out.println("Duplicate ID!");
                return;
            }
        }

        System.out.print("Enter Title: ");
        String title = sc.nextLine();

        books.add(new Book(id, title));
        System.out.println("Book added!");
    }

    static void viewBooksPaged(boolean onlyAvailable) {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        int pageSize = 10;
        int currentPage = 0;

        while (true) {
            int start = currentPage * pageSize;
            int count = 0;

            System.out.println("\n--- Page " + (currentPage + 1) + " ---");

            for (int i = start; i < books.size() && count < pageSize; i++) {
                Book b = books.get(i);

                if (onlyAvailable && b.isIssued) continue;

                System.out.println("ID: " + b.id +
                        " | " + b.title +
                        " | Issued: " + b.isIssued);
                count++;
            }

            System.out.println("\n1. Next | 2. Prev | 3. Exit");
            int ch = sc.nextInt();

            if (ch == 1) currentPage++;
            else if (ch == 2 && currentPage > 0) currentPage--;
            else break;
        }
    }

    static void issueBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Book b : books) {
            if (b.id == id) {
                if (!b.isIssued) {
                    System.out.print("Enter User Name: ");
                    b.issuedTo = sc.nextLine();

                    System.out.print("Enter Issue Date (dd/MM/yyyy): ");
                    b.issueDate = LocalDate.parse(sc.nextLine(), formatter);

                    b.returnDate = b.issueDate.plusDays(14);

                    b.isIssued = true;

                    System.out.println("Issued! Return by: " +
                            b.returnDate.format(formatter));
                } else {
                    System.out.println("Already issued.");
                }
                return;
            }
        }
        System.out.println("Book not found.");
    }

    static void returnBook() {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Book b : books) {
            if (b.id == id) {
                if (b.isIssued) {

                    System.out.print("Enter Actual Return Date (dd/MM/yyyy): ");
                    LocalDate actual = LocalDate.parse(sc.nextLine(), formatter);

                    if (actual.isAfter(b.returnDate)) {
                        long days = ChronoUnit.DAYS.between(b.returnDate, actual);
                        double fine = days * 10;

                        System.out.println("Late by " + days + " days");
                        System.out.println("Penalty: ₹" + fine);

                        totalPenalty += fine;
                    } else {
                        System.out.println("Returned on time.");
                    }

                    b.isIssued = false;
                    b.issuedTo = "";
                    b.issueDate = null;
                    b.returnDate = null;

                    System.out.println("Book returned.");
                } else {
                    System.out.println("Not issued.");
                }
                return;
            }
        }
        System.out.println("Book not found.");
    }

    static void searchBook() {
        System.out.print("Enter keyword: ");
        String key = sc.nextLine().toLowerCase();

        boolean found = false;

        for (Book b : books) {
            if (b.title.toLowerCase().contains(key)) {
                System.out.println("ID: " + b.id +
                        " | " + b.title +
                        " | Issued: " + b.isIssued);
                found = true;
            }
        }

        if (!found) System.out.println("No matches found.");
    }

    static void showStats() {
        int issued = 0;

        for (Book b : books) {
            if (b.isIssued) issued++;
        }

        System.out.println("Total Books: " + books.size());
        System.out.println("Issued: " + issued);
        System.out.println("Available: " + (books.size() - issued));
        System.out.println("Total Penalty: ₹" + totalPenalty);
    }

    static void saveToFile() {
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("books.txt"));
            for (Book b : books) {
                w.write(b.toString());
                w.newLine();
            }
            w.close();
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    static void loadFromFile() {
        try {
            File file = new File("books.txt");
            if (!file.exists()) return;

            BufferedReader r = new BufferedReader(new FileReader(file));
            String line;

            while ((line = r.readLine()) != null) {
                books.add(Book.fromString(line));
            }
            r.close();
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }
}