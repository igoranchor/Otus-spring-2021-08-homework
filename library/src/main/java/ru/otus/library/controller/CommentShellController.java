package ru.otus.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.*;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.service.CommentService;

import java.math.BigInteger;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent("Comment controller")
@RequiredArgsConstructor
public class CommentShellController {

    private static final String GROUP_NAME = "Comment group";
    private static final String SEPARATE_STRING = "\n---------------";

    private final InputOutputComponent inputOutputComponent;
    private final CommentService commentService;

    @ShellMethod(value = "\nCreate comment and save to Database.\n"
            + "Keys: \n * --text or -t, required, comment text\n"
            + " * --book or -b, required, uses for specify book id."
            + SEPARATE_STRING,
            key = {"create-comment", "cc"}, group = GROUP_NAME)
    public void createComment(@ShellOption({"--text", "-t"}) String text,
                              @ShellOption({"--book", "-b"}) BigInteger bookId) {
        inputOutputComponent.write(commentService.create(text, bookId).toString());
    }

    @ShellMethod(value = "\nRead comments from database. Default returns all comments.\n"
            + "Keys: \n * --book, non-required, uses for specify book id."
            + SEPARATE_STRING,
            key = {"read-comment", "rc"}, group = GROUP_NAME)
    public void readComments(@ShellOption(value = "--book", defaultValue = NULL) BigInteger bookId) {
        if (bookId != null) {
            inputOutputComponent.write(commentService.readByBookId(bookId).toString());
            return;
        }
        commentService.readAll().forEach(x -> inputOutputComponent.write(x.toString()));
    }

    @ShellMethod(value = "\nUpdate genre name.\n"
            + "Keys: \n * --id, required, uses for specify id of comment\n"
            + " * --name or -n, required, uses for specify new comment text."
            + SEPARATE_STRING,
            key = {"update-comment", "uc"}, group = GROUP_NAME)
    public void updateCommentById(@ShellOption("--id") BigInteger id,
                                  @ShellOption("--text") String text) {
        var commentAfterUpdate = commentService.updateById(id, text);
        if (commentAfterUpdate != null) {
            inputOutputComponent.write(commentAfterUpdate.toString());
        }
    }

    @ShellMethod(value = "\nDelete comments from Database\n"
            + "Keys:\n * --all non-required, uses for deleting all comments\n"
            + " * --id, non-required, uses for specify comment id which will delete.\n"
            + "You must specify one of these keys."
            + SEPARATE_STRING,
            key = {"delete-comment", "dc"}, group = GROUP_NAME)
    public void deleteComment(@ShellOption(value = "--all", defaultValue = "false") boolean all,
                              @ShellOption(value = "--id", defaultValue = NULL) BigInteger id) {
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
