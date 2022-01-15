package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.*;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;

import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping("/create")
    public String create(Model model) {
        var authors = authorService.readAll();
        model.addAttribute("authors", authors);
        model.addAttribute("book",
                new Book("book", authors.get(0), new Genre("genre")));
        return "book-edit";
    }

    @GetMapping("/")
    public String read(Model model) {
        List<Book> books = bookService.readAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/edit/{book}")
    public String update(@PathVariable Book book, Model model) {
        model.addAttribute("authors", authorService.readAll());
        model.addAttribute("book", book);
        return "book-edit";
    }

    @PostMapping("/add")
    public String createOrUpdateBook(@ModelAttribute Book book) {
        bookService.createOrUpdate(book);
        return "redirect:/author/view/" + book.getAuthor().getId();
    }

    @PostMapping("/delete/{book}")
    public String delete(@PathVariable Book book) {
        bookService.delete(book);
        return "redirect:/";
    }

    @GetMapping("/view/{book}")
    public String showBook(@PathVariable Book book, Model model) {
        model.addAttribute("book", book);
        return "book-view";
    }

    @GetMapping("/create-comment/{book}")
    public String createComment(@PathVariable Book book, Model model) {
        model.addAttribute("book", book);
        model.addAttribute("comment", new Comment("comment"));
        return "comment-edit";
    }

    @PostMapping("/add-comment/{book}")
    public String addComment(@PathVariable Book book, @ModelAttribute Comment comment) {
        bookService.addComment(book, comment);
        return "redirect:/book/view/" + book.getId();
    }

    @PostMapping("/delete-comment/{book}/{comment}")
    public String deleteComment(@PathVariable("book") Book book,
                                @PathVariable("comment") String commentId) {
        bookService.deleteComment(book, commentId);
        return "redirect:/book/view/" + book.getId();
    }

}
