package org.project2.library.services;

import org.project2.library.models.Book;
import org.project2.library.models.Person;
import org.project2.library.repositories.BooksRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int bookPerPage) {
        return booksRepository.findAll(PageRequest.of(page, bookPerPage)).getContent();
    }

    public List<Book> findAll(boolean sort_by_year) {
        return booksRepository.findAll(Sort.by("year"));
    }

    public List<Book> findAll(int page, int bookPerPage, boolean sort_by_year) {
        return booksRepository.findAll(PageRequest.of(page, bookPerPage, Sort.by("year"))).getContent();
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void set(int id, Person person) {
        Book book = booksRepository.findById(id).orElse(null);
        book.setOwner(person);
    }

    @Transactional
    public void clear(int id) {
        Book book = booksRepository.findById(id).orElse(null);
        book.setOwner(null);
    }

    public Person join(int id) {
        return booksRepository.findById(id).orElse(null).getOwner();
    }

    public Optional<Book> findByNameStartingWith(String wordInput) {
        return booksRepository.findByNameStartingWith(wordInput);
    }
}
