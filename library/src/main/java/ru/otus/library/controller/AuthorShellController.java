package ru.otus.library.controller;

import org.springframework.shell.standard.*;
import org.springframework.validation.annotation.Validated;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.AuthorService;

import static org.springframework.shell.standard.ShellOption.NULL;

@Validated
@ShellComponent("Author controller")
public class AuthorShellController {

    private static final String GROUP_NAME = "Author group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final AuthorService authorService;

    public AuthorShellController(InputOutputComponent inputOutputComponent, AuthorService authorService) {
        this.inputOutputComponent = inputOutputComponent;
        this.authorService = authorService;
    }

    @ShellMethod(value = "\nCreate author and save to Database.\n"
            + "Keys: \n * --name or -n, required, uses for specify name of author."
            + SEPARATE_STRING,
            key = {"create-author", "ca"}, group = GROUP_NAME)
    public void createAuthor(@ShellOption({"--name", "-n"}) String name) {
        inputOutputComponent.write(authorService.create(name).toString());
    }

    @ShellMethod(value = "\nRead authors from database. Default returns all authors.\n"
            + "Keys: \n * --id, non-required, uses for specify id of author\n"
            + " * --name or -n, non-required, uses for specify name of author."
            + SEPARATE_STRING,
            key = {"read-authors", "ra"}, group = GROUP_NAME)
    public void readAuthors(@ShellOption(value = "--id", defaultValue = NULL) String id,
                            @ShellOption(value = "--id", defaultValue = NULL) String name) {
        if (id != null) {
            inputOutputComponent.write(authorService.readById(Long.valueOf(id)).toString());
            return;
        }
        if (name != null) {
            inputOutputComponent.write(authorService.readByName(name).toString());
            return;
        }
        authorService.readAll().forEach(x -> inputOutputComponent.write(x.toString()));
    }

    @ShellMethod(value = "\nUpdate author name.\n"
            + "Keys: \n * --id, required, uses for specify id of author\n"
            + " * --name or -n, required, uses for specify new name of author."
            + SEPARATE_STRING,
            key = {"update-author", "ua"}, group = GROUP_NAME)
    public void updateAuthorById(@ShellOption(value = "--id") String id,
                                 @ShellOption(value = "--id") String name) {
        inputOutputComponent.write(authorService.updateById(Long.parseLong(id), name).toString());
    }

    @ShellMethod(value = "\nDelete author from Database and !!! ALL !!! books this author.\n"
            + "Keys:\n * --all non-required, uses for deleting all authors\n"
            + " * --id, non-required, uses for specify author id which will delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-author", "da"}, group = GROUP_NAME)
    public void deleteAuthor(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                             @ShellOption(value = "--id", defaultValue = NULL) String id) {
        if (all) {
            authorService.readAll().forEach(x -> authorService.deleteById(x.getId()));
            return;
        }
        if (id != null) {
            authorService.deleteById(Long.parseLong(id));
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all authors "
                + "or \"--id\" for delete one author");
    }

}
