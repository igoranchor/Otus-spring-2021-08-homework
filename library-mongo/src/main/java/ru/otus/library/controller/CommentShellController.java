package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.*;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.CommentService;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent("Comment controller")
@RequiredArgsConstructor
public class CommentShellController {

    private static final String GROUP_NAME = "Comment group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final CommentService commentService;

    @ShellMethod(value = "\nCreate book comment and save to Database.\n"
            + "Keys: \n * --text or -t, required, it's text comment\n."
            + " * --book or -b, required, uses for specify book id"
            + SEPARATE_STRING,
            key = {"create-comment", "cc"},
            group = GROUP_NAME)
    public void CommentBook(@ShellOption({"--text", "-t"}) String text,
                            @ShellOption({"--book", "-b"}) String bookId) {
        inputOutputComponent.write(commentService.create(text, bookId).toString());
    }

    @ShellMethod(value = "\nDelete comment from Database.\n"
            + "Keys:\n * --all non-required, uses for deleting all comments\n"
            + " * --id, non-required, uses for specify comment id for delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-comment", "dc"}, group = GROUP_NAME)
    public void deleteComment(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                              @ShellOption(value = "--id", defaultValue = NULL) String id) {
        if (all) {
            commentService.readAll().forEach(x -> commentService.deleteById(x.getId()));
            return;
        }
        if (id != null) {
            commentService.deleteById(id);
            return;
        }
        inputOutputComponent.write("You must specify key \"--all\" for deleting all comments "
                + "or \"--id\" for delete one comment");
    }

}
