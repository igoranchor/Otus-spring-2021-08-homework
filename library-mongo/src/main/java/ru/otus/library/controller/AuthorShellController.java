package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.*;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent("Author controller")
@RequiredArgsConstructor
public class AuthorShellController {

    private static final String GROUP_NAME = "Author group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final AuthorService authorService;

    @ShellMethod(value = "\nCreate author and save to Database.\n"
            + "Keys: \n * --name or -n, required"
            + SEPARATE_STRING,
            key = {"create-author", "ca"},
            group = GROUP_NAME)
    public void createAuthor(@ShellOption({"--name", "-n"}) String name) {
        inputOutputComponent.write(authorService.create(name).toString());
    }

    @ShellMethod(value = "\nRead authors from database. Default returns all authors.\n"
            + "Keys: \n * --title or -t, non-required, uses for specify author name."
            + SEPARATE_STRING,
            key = {"read-authors", "ra"}, group = GROUP_NAME)
    public void readAuthors(@ShellOption(value = {"--name", "-n"}, defaultValue = NULL) String name) {
        List<Author> authors = new ArrayList<>();
        if (name != null) {
            authors.add(authorService.readByName(name));
        } else {
            authors.addAll(authorService.readAll());
        }
        for (Author author : authors) {
            inputOutputComponent.write(author.toString());
        }
    }

    @ShellMethod(value = "\nUpdate author.\n"
            + "Keys:\n * --id, required, uses for specify author id for update.\\n\""
            + " * --name or -n, required, uses for specify new author name"
            + SEPARATE_STRING,
            key = {"update-author", "ua"}, group = GROUP_NAME)
    public void updateBookById(@ShellOption("--id") String id,
                               @ShellOption({"--name", "-n"}) String name) {
        var author = authorService.readById(id);
        inputOutputComponent.write(authorService.updateById(id, name).toString());
    }

    @ShellMethod(value = "\nDelete authors from Database.\n"
            + "Keys:\n * --all non-required, uses for deleting all authors\n"
            + " * --id, non-required, uses for specify author id for delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-authors", "da"}, group = GROUP_NAME)
    public void deleteAuthor(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                             @ShellOption(value = "--id", defaultValue = NULL) String id) {
        if (all) {
            authorService.readAll().forEach(x -> authorService.deleteById(x.getId()));
            return;
        }
        if (id != null) {
            authorService.deleteById(id);
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all authors "
                + "or \"--id\" for delete one author");
    }

}
