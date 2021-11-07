package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.*;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.AuthorService;

import java.math.BigInteger;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent("Author controller")
@RequiredArgsConstructor
public class AuthorShellController {

    private static final String GROUP_NAME = "Author group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final AuthorService authorService;

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
    public void readAuthors(@ShellOption(value = "--id", defaultValue = NULL) BigInteger id,
                            @ShellOption(value = "--name", defaultValue = NULL) String name) {
        if (id != null) {
            inputOutputComponent.write(authorService.readById(id).toString());
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
    public void updateAuthorById(@ShellOption(value = "--id") BigInteger id,
                                 @ShellOption(value = "--name") String name) {
        var authorAfterUpdate = authorService.updateById(id, name);
        if (authorAfterUpdate != null) {
            inputOutputComponent.write(authorAfterUpdate.toString());
        }
    }

    @ShellMethod(value = "\nDelete author from Database and !!! ALL !!! books this author.\n"
            + "Keys:\n * --all non-required, uses for deleting all authors\n"
            + " * --id, non-required, uses for specify author id which will delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-author", "da"}, group = GROUP_NAME)
    public void deleteAuthor(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                             @ShellOption(value = "--id", defaultValue = NULL) BigInteger id) {
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
