package co.iichi.controller;


import co.iichi.SessionHandler;
import co.iichi.domain.Board;
import co.iichi.domain.BoardManager;
import co.iichi.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "boards")
public class BoardsController {

    private static final Logger logger = LoggerFactory.getLogger(BoardsController.class);

    @Autowired
    protected BoardManager boardManager;

    @Autowired
    protected SessionHandler sessionHandler;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ModelAndView getIndex(
            HttpServletRequest httpServletRequest
    ) {
        Optional<User> user = sessionHandler.getUser(httpServletRequest);

        List<Board> boardList = boardManager.getBoardListOrderByLastPostedAt(0, 100);

        ModelAndView modelAndView =  new ModelAndView("boards/index");
        modelAndView.addObject("boardList", boardList);

        return modelAndView;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public RedirectView postBoard(
            HttpServletRequest httpServletRequest,
            @RequestParam @NotNull String boardSubject,
            @RequestParam @NotNull String commentBody
    ) {
        Optional<User> user = sessionHandler.getUser(httpServletRequest);

        if (user.isPresent()
                && !boardSubject.isEmpty()
                && !commentBody.isEmpty()) {
            Board board = boardManager.create(
                    user.get().getUserId(),
                    boardSubject,
                    commentBody
            );

            return new RedirectView("/boards/" + board.getBoardId());
        }

        return new RedirectView("/boards");
    }

    @RequestMapping(path = "{boardId}", method = RequestMethod.GET)
    public ModelAndView getBoard(
        @PathVariable String boardId
    ) {
        ModelAndView modelAndView = new ModelAndView("boards/board");

        Optional<Board> board = boardManager.getBoardByBoardId(boardId);
        if (!board.isPresent()) {
            modelAndView.setView(new RedirectView("/boards"));
            return modelAndView;
        }

        modelAndView.addObject("board", board.get());
        modelAndView.addObject("commentList", boardManager.getCommentListOrderByPostedAt(boardId, 0, 100));

        return modelAndView;
    }

    @RequestMapping(path = "{boardId}", method = RequestMethod.POST)
    public RedirectView postComment(
            HttpServletRequest httpServletRequest,
            @PathVariable String boardId,
            @RequestParam String commentBody

    ) {
        Optional<User> user = sessionHandler.getUser(httpServletRequest);

        if (user.isPresent() && !commentBody.isEmpty()) {
            boardManager.comment(
                    boardId,
                    user.get().getUserId(),
                    commentBody
            );
        }

        return new RedirectView("/boards/" + boardId);
    }
}
