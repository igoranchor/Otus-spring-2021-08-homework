package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.*;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.Book;
import ru.otus.library.service.BookService;

import java.util.*;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent("Book controller")
@RequiredArgsConstructor
public class BookShellController {

    private static final String GROUP_NAME = "Book group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final BookService bookService;

    @ShellMethod(value = "\nCreate book and save to Database.\n"
            + "Keys: \n * --title or -t, required, uses for specify book title\n."
            + " * --author or -a, required, uses for specify name of author\n"
            + " * --genre or -g, required, uses for specify genre name."
            + SEPARATE_STRING,
            key = {"create-book", "cb"}, group = GROUP_NAME)
    public void createBook(@ShellOption({"--title", "-t"}) String title,
                           @ShellOption({"--author", "-a"}) String authorName,
                           @ShellOption({"--genre", "-g"}) String genreName) {
        inputOutputComponent.write(bookService.create(title, authorName, genreName).toString());
    }

    @ShellMethod(value = "\nRead books from database. Default returns all authors.\n"
            + "Keys: \n * --title or -t, non-required, uses for specify book title."
            + SEPARATE_STRING,
            key = {"read-books", "rb"}, group = GROUP_NAME)
    public void readBooks(@ShellOption(value = {"--title", "-t"}, defaultValue = NULL) String title,
                          @ShellOption(value = {"--with-comments", "-c"}, defaultValue = "false") boolean withComments) {
        List<Book> books = new ArrayList<>();
        if (title != null) {
            books.add(bookService.readByTitle(title));
        } else {
            books.addAll(bookService.readAll());
        }
        for (Book book : books) {
            inputOutputComponent.write(book.toString());
            if (withComments && Objects.nonNull(book.getComments())) {
                book.getComments()
                        .forEach(x -> inputOutputComponent.write("Comment: " + x.getText()));
            }
        }
    }

    @ShellMethod(value = "\nUpdate book.\n"
            + "Keys:\n * --id, required, uses for specify book id for update.\\n\""
            + " * --title or -t, required, uses for specify new book title\n"
            + " * --author or -a, non-required, uses for specify new book author (name)\n"
            + " * --genre or -g, non-required, uses for specify new book genre (name)."
            + SEPARATE_STRING,
            key = {"update-book", "ub"}, group = GROUP_NAME)
    public void updateBookById(@ShellOption("--id") String id,
                               @ShellOption({"--title", "-t"}) String title,
                               @ShellOption(value = {"--author", "-a"}, defaultValue = NULL) String authorName,
                               @ShellOption(value = {"--genre", "-g"}, defaultValue = NULL) String genreName) {
        Book book = bookService.readByTitle(title);
        inputOutputComponent.write(bookService.updateById(id, title, authorName, genreName).toString());
    }

    @ShellMethod(value = "\nDelete books from Database.\n"
            + "Keys:\n * --all non-required, uses for deleting all books\n"
            + " * --id, non-required, uses for specify book id for delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-books", "db"}, group = GROUP_NAME)
    public void deleteBook(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                           @ShellOption(value = "--id", defaultValue = NULL) String id) {
        if (all) {
            bookService.readAll().forEach(x -> bookService.deleteById(x.getId()));
            return;
        }
        if (id != null) {
            bookService.deleteById(id);
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all books "
                + "or \"--id\" for delete one book");
    }

}
