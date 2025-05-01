package com.bookstore.config;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if data already exists
        if (userRepository.count() > 0) {
            return; // Skip initialization if data exists
        }

        loadUsers();
        loadAuthorsAndBooks();
    }

    private void loadUsers() {
        logger.info("Loading users...");

        // Create admin user
        User admin = new User(
                "admin",
                "admin@example.com",
                passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // Create regular user
        User user = new User(
                "user",
                "user@example.com",
                passwordEncoder.encode("user123"));
        user.setRole(Role.USER);
        userRepository.save(user);

        logger.info("Loaded users: " + userRepository.count());
    }

    private void loadAuthorsAndBooks() {
        logger.info("Loading authors and books...");

        // Create authors
        Author rowling = new Author("J.K. Rowling", "British author known for writing the Harry Potter series");
        Author orwell = new Author("George Orwell", "English novelist, essayist, and critic");

        authorRepository.save(rowling);
        authorRepository.save(orwell);

        // Create books for J.K. Rowling
        Book book1 = new Book(
                "Harry Potter and the Philosopher's Stone",
                "9780747532743",
                new BigDecimal("19.99"),
                "The first novel in the Harry Potter series");
        book1.setAuthor(rowling);
        bookRepository.save(book1);

        Book book2 = new Book(
                "Harry Potter and the Chamber of Secrets",
                "9780747538486",
                new BigDecimal("21.99"),
                "The second novel in the Harry Potter series");
        book2.setAuthor(rowling);
        bookRepository.save(book2);

        // Create book for George Orwell
        Book book3 = new Book(
                "1984",
                "9780451524935",
                new BigDecimal("15.99"),
                "A dystopian novel about totalitarianism");
        book3.setAuthor(orwell);
        bookRepository.save(book3);

        logger.info("Loaded authors: " + authorRepository.count());
        logger.info("Loaded books: " + bookRepository.count());
    }
}