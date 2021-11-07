package ru.otus.library.controller;

import org.springframework.shell.standard.*;
import org.springframework.validation.annotation.Validated;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.BookService;

import static org.springframework.shell.standard.ShellOption.NULL;

@Validated
@ShellComponent("Book controller")
public class BookShellController {

    private static final String GROUP_NAME = "Book group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final BookService bookService;

    public BookShellController(InputOutputComponent inputOutputComponent, BookService bookService) {
        this.inputOutputComponent = inputOutputComponent;
        this.bookService = bookService;
    }

    @ShellMethod(value = "\nCreate book and save to Database.\n"
            + "Keys: \n * --title or -t, required, uses for specify book title\n."
            + " * --author or -a, required, uses for specify name of author\n"
            + " * --genre or -g, required, uses for specify genre name."
            + SEPARATE_STRING,
            key = {"create-book", "cb"}, prefix = "--title", group = GROUP_NAME)
    public void createBook(@ShellOption({"--title", "-t"}) String title,
                           @ShellOption({"--author", "-a"}) String authorName,
                           @ShellOption({"--genre", "-g"}) String genreName) {
        inputOutputComponent.write(bookService.create(title, authorName, genreName).toString());
    }

    @ShellMethod(value = "\nRead books from database. Default returns all authors.\n"
            + "Keys: \n * --id, non-required, uses for specify id of book\n"
            + " * --title or -t, non-required, uses for specify book title."
            + SEPARATE_STRING,
            key = {"read-books", "rb"}, group = GROUP_NAME)
    public void readBooks(@ShellOption(value = "--id", defaultValue = NULL) String id,
                          @ShellOption(value = {"--title", "-t"}, defaultValue = NULL) String title) {
        if (id != null) {
            inputOutputComponent.write(bookService.readById(Long.parseLong(id)).toString());
            return;
        }
        if (title != null) {
            inputOutputComponent.write(bookService.readByTitle(title).toString());
            return;
        }
        bookService.readAll().forEach(x -> inputOutputComponent.write(x.toString()));
    }

    @ShellMethod(value = "\nUpdate book.\n"
            + "Keys: \n * --id, required, uses for specify id of author\n"
            + " * --title or -t, required, uses for specify new book title\n"
            + " * --author or -a, non-required, uses for specify new book author (name)\n"
            + " * --genre or -g, non-required, uses for specify new book genre (name)."
            + SEPARATE_STRING,
            key = {"update-book", "ub"}, group = GROUP_NAME)
    public void updateBookById(@ShellOption("--id") String id,
                               @ShellOption({"--title", "-t"}) String title,
                               @ShellOption(value = {"--author", "-a"}, defaultValue = NULL) String authorName,
                               @ShellOption(value = {"--genre", "-g"}, defaultValue = NULL) String genreName) {
        inputOutputComponent.write(bookService.updateById(Long.parseLong(id), title, authorName, genreName).toString());
    }

    @ShellMethod(value = "\nDelete books from Database.\n"
            + "Keys:\n * --all non-required, uses for deleting all books\n"
            + " * --id, non-required, uses for specify book id which will delete.\n"
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
            bookService.deleteById(Long.parseLong(id));
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all books "
                + "or \"--id\" for delete one book");
    }
}
