package com.bookstore.service;

import com.bookstore.dto.BookDto;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<BookDto> getAllBooks() {
        logger.info("Retrieving all books");
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        logger.info("Retrieving book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }

    public List<BookDto> getBooksByAuthorId(Long authorId) {
        logger.info("Retrieving books by author ID: {}", authorId);
        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        logger.info("Creating book: {}", bookDto);
        Author author = authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + bookDto.getAuthorId()));

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPrice(bookDto.getPrice());
        book.setDescription(bookDto.getDescription());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully: {}", savedBook);
        return convertToDto(savedBook);
    }

    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        logger.info("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        if (!book.getAuthor().getId().equals(bookDto.getAuthorId())) {
            Author author = authorRepository.findById(bookDto.getAuthorId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Author not found with id: " + bookDto.getAuthorId()));
            book.setAuthor(author);
        }

        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        book.setPrice(bookDto.getPrice());
        book.setDescription(bookDto.getDescription());

        Book updatedBook = bookRepository.save(book);
        logger.info("Book updated successfully: {}", updatedBook);
        return convertToDto(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            logger.error("Book not found with id: {}", id);
            throw new EntityNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
        logger.info("Book deleted successfully with ID: {}", id);
    }

    private BookDto convertToDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }

    @Transactional
    public void deleteAllBooks() {
        logger.info("Deleting all books");
        bookRepository.deleteAll();
        logger.info("All books deleted successfully");
    }
}