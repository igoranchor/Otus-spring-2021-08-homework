package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;

import java.util.List;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("author", new Author("author name"));
        return "author-edit";
    }

    @GetMapping("/")
    public String read(Model model) {
        List<Author> authors = authorService.readAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/edit/{author}")
    public String update(@PathVariable Author author, Model model) {
        model.addAttribute("author", author);
        return "author-edit";
    }

    @PostMapping("/add")
    public String createOrUpdate(@ModelAttribute Author author) {
        authorService.createOrUpdate(author);
        return "redirect:/";
    }

    @PostMapping("/delete/{author}")
    public String delete(@PathVariable Author author) {
        authorService.delete(author);
        return "redirect:/";
    }

    @GetMapping("/view/{author-id}")
    public String showAuthor(@PathVariable("author-id") String authorId, Model model) {
        Author author = authorService.readById(authorId);
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.readByAuthor(author));
        return "books";
    }

}
