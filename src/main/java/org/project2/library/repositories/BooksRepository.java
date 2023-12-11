package org.project2.library.repositories;

import org.project2.library.models.Book;
import org.project2.library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByNameStartingWith(String startingWith);

}
