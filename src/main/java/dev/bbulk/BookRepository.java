package dev.bbulk;

import jakarta.data.Limit;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.*;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Delete
    void deleteAll();

    @Find
    Optional<Book> findBy(String title);

    @Find
    @OrderBy(Book_.TITLE)
    List<Book> getAll();

    @Find
    List<Book> getAllOrderedBy(Order<Book> order, Limit limit);

    @Find
    Page<Book> getAllBooksContaining(@Pattern String title, PageRequest page);

    @Query("FROM Book WHERE title LIKE :title")
    List<Book> getAllBooksContaining(@Param("title") String title);

}
