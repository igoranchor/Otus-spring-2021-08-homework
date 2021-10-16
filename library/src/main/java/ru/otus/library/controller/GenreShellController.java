package ru.otus.library.controller;

import org.springframework.shell.standard.*;
import org.springframework.validation.annotation.Validated;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.GenreService;

import static org.springframework.shell.standard.ShellOption.NULL;

@Validated
@ShellComponent("Genre controller")
public class GenreShellController {

    private static final String GROUP_NAME = "Genre group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final GenreService genreService;

    public GenreShellController(InputOutputComponent inputOutputComponent, GenreService genreService) {
        this.inputOutputComponent = inputOutputComponent;
        this.genreService = genreService;
    }

    @ShellMethod(value = "\nCreate genre and save to Database.\n"
            + "Keys: \n * --name or -n, required, uses for specify genre name."
            + SEPARATE_STRING,
            key = {"create-genre", "cg"}, group = GROUP_NAME)
    public void createGenre(@ShellOption({"--name", "-n"}) String name) {
        inputOutputComponent.write(genreService.create(name).toString());
    }

    @ShellMethod(value = "\nRead genres from database. Default returns all genres.\n"
            + "Keys: \n * --id, non-required, uses for specify id of genre\n"
            + " * --name or -n, non-required, uses for specify genre name."
            + SEPARATE_STRING,
            key = {"read-genres", "rg"}, group = GROUP_NAME)
    public void readGenres(@ShellOption(value = "--id", defaultValue = NULL) String id,
                           @ShellOption(value = "--name", defaultValue = NULL) String name) {
        if (id != null) {
            inputOutputComponent.write(genreService.readById(Long.parseLong(id)).toString());
            return;
        }
        if (name != null) {
            inputOutputComponent.write(genreService.readByName(name).toString());
            return;
        }
        genreService.readAll().forEach(x -> inputOutputComponent.write(x.toString()));
    }

    @ShellMethod(value = "\nUpdate genre name.\n"
            + "Keys: \n * --id, required, uses for specify id of genre\n"
            + " * --name or -n, required, uses for specify new genre name."
            + SEPARATE_STRING,
            key = {"update-genre", "ug"}, group = GROUP_NAME)
    public void updateGenreById(@ShellOption("--id") String id,
                                @ShellOption("--name") String name) {
        inputOutputComponent.write(genreService.updateById(Long.parseLong(id), name).toString());
    }

    @ShellMethod(value = "\nDelete genre from Database and !!! ALL !!! books this genre.\n"
            + "Keys:\n * --all non-required, uses for deleting all genres\n"
            + " * --id, non-required, uses for specify genre id which will delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-genre", "dg"}, group = GROUP_NAME)
    public void deleteGenre(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                            @ShellOption(value = "--id", defaultValue = NULL) String id) {
        if (all) {
            genreService.readAll().forEach(x -> genreService.deleteById(x.getId()));
            return;
        }
        if (id != null) {
            genreService.deleteById(Long.parseLong(id));
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all genres "
                + "or \"--id\" for delete one genre");
    }

}
