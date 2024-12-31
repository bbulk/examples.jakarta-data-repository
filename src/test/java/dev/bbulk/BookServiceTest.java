package dev.bbulk;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.data.Limit;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class BookServiceTest {

    @Inject
    BookRepository bookRepository;

    @Test
    @TestTransaction
    void databaseShouldBeEmpty() {
        List<Book> books = bookRepository.findAll().toList();
        assertThat(books).isEmpty();
    }

    @Test
    @TestTransaction
    void shouldReturnPagedList() {
        Book book1 = new Book();
        book1.setTitle("My first book!");
        Book book2 = new Book();
        book2.setTitle("My second book -_-");
        Book book3 = new Book();
        book3.setTitle("Boring book :(");
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        Page<Book> page = bookRepository.getAllBooksContaining("%book%", new Pagination(1, 2, false));

        assertThat(page.content()).hasSize(2);
    }

    @Test
    @TestTransaction
    void shouldReturnList() {
        Book book1 = new Book();
        book1.setTitle("My first book!");
        Book book2 = new Book();
        book2.setTitle("My second book -_-");
        Book book3 = new Book();
        book3.setTitle("Boring book :(");
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        List<Book> page = bookRepository.getAllBooksContaining("%book%");

        assertThat(page).hasSize(3);
    }

    @Test
    @TestTransaction
    void shouldReturnOrderedAndLimitedList() {
        Book book1 = new Book();
        book1.setTitle("My first book!");
        Book book2 = new Book();
        book2.setTitle("My second book -_-");
        Book book3 = new Book();
        book3.setTitle("Boring book :(");
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        List<Book> list = bookRepository.getAllOrderedBy(Order.by(_Book.title.asc()), Limit.of(1));

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getTitle()).isEqualTo("Boring book :(");
    }

    @Test
    @TestTransaction
    void shouldPersistOneBook() {
        Book book = new Book();
        book.setTitle("My first book!");

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getTitle()).isEqualTo("My first book!");
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getCreatedAt()).isNotNull();
        assertThat(savedBook.getModifiedAt()).isNotNull();
    }

    @Test
    void shouldCreateAndDeleteBook() {
        QuarkusTransaction.begin();
        Book book = new Book();
        book.setTitle("My first book!");
        bookRepository.save(book);
        QuarkusTransaction.commit();

        List<Book> booksAfterCreate = bookRepository.findAll().toList();
        assertThat(booksAfterCreate).hasSize(1);

        QuarkusTransaction.begin();
        bookRepository.deleteAll();
        QuarkusTransaction.commit();

        List<Book> booksAfterDelete = bookRepository.findAll().toList();
        assertThat(booksAfterDelete).hasSize(0);
    }

    @Test
    @TestTransaction
    void shouldReturnBooksInOrder() {
        Book book1 = new Book();
        book1.setTitle("My first book!");
        Book book2 = new Book();
        book2.setTitle("My second book -_-");
        Book book3 = new Book();
        book3.setTitle("Boring book :(");
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        List<Book> books = bookRepository.getAll();
        assertThat(books).hasSize(3);

        assertThat(books).map(Book::getTitle).containsExactly("Boring book :(", "My first book!", "My second book -_-");
    }

}