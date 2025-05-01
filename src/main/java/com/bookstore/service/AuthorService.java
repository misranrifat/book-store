package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.model.Author;
import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorDto> getAllAuthors() {
        logger.info("Retrieving all authors");
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(Long id) {
        logger.info("Retrieving author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return convertToDto(author);
    }

    @Transactional
    public AuthorDto createAuthor(AuthorDto authorDto) {
        logger.info("Creating author: {}", authorDto);
        Author author = new Author();
        author.setName(authorDto.getName());
        author.setBiography(authorDto.getBiography());

        Author savedAuthor = authorRepository.save(author);
        logger.info("Author created successfully: {}", savedAuthor);
        return convertToDto(savedAuthor);
    }

    @Transactional
    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
        logger.info("Updating author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        author.setName(authorDto.getName());
        author.setBiography(authorDto.getBiography());

        Author updatedAuthor = authorRepository.save(author);
        logger.info("Author updated successfully: {}", updatedAuthor);
        return convertToDto(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        logger.info("Deleting author with ID: {}", id);
        if (!authorRepository.existsById(id)) {
            logger.error("Author not found with id: {}", id);
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
        logger.info("Author deleted successfully with ID: {}", id);
    }

    private AuthorDto convertToDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getName(),
                author.getBiography());
    }

    @Transactional
    public void deleteAllAuthors() {
        logger.info("Deleting all authors");
        authorRepository.deleteAll();
        logger.info("All authors deleted successfully");
    }
}