package co.iichi.board.domain;

import co.iichi.common.util.DateTimeUtils;
import co.iichi.board.entity.BoardEntity;
import co.iichi.board.entity.BoardEntityManager;
import co.iichi.board.entity.CommentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BoardManager {
    private static final Logger logger = LoggerFactory.getLogger(BoardManager.class);

    public Board create(
            String userId,
            String boardSubject,
            String commentBody
    ) {
        EntityManager entityManager = BoardEntityManager.INSTANCE.createEntityManager();
        try {
            Date now = DateTimeUtils.toDate(DateTimeUtils.now());

            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            BoardEntity boardEntity = new BoardEntity();
            boardEntity.setBoardId(UUID.randomUUID().toString());
            boardEntity.setBoardSubject(boardSubject);
            boardEntity.setCommentCount(1);
            boardEntity.setCreatedByUserId(userId);
            boardEntity.setLastPostedAt(now);
            entityManager.persist(boardEntity);

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setPrimaryKey(new CommentEntity.PrimaryKey(boardEntity.getBoardId(), 1));
            commentEntity.setPostedByUserId(userId);
            commentEntity.setPostedAt(now);
            commentEntity.setBody(commentBody);
            entityManager.persist(commentEntity);

            entityTransaction.commit();

            return BoardFactory.create(boardEntity);
        } finally {
            entityManager.close();
        }
    }

    public List<Board> getBoardListOrderByLastPostedAt(Integer offset, Integer limit) {
        EntityManager entityManager = BoardEntityManager.INSTANCE.createEntityManager();

        try {
            List<BoardEntity> boardEntityList = (List<BoardEntity>) entityManager
                    .createQuery("from BoardEntity be ORDER BY be.lastPostedAt DESC")
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();

            return boardEntityList.stream()
                    .map(s -> BoardFactory.create(s))
                    .collect(Collectors.toList());
        } finally {
            entityManager.close();
        }
    }

    public List getCommentListOrderByPostedAt(String boardId, Integer offset, Integer limit) {
        EntityManager entityManager = BoardEntityManager.INSTANCE.createEntityManager();

        try {
            List<CommentEntity> commentEntityList = entityManager
                    .createQuery("from CommentEntity ce JOIN FETCH ce.userInfoEntity WHERE ce.primaryKey.boardId = :boardId ORDER BY ce.primaryKey.commentSeq DESC")
                    .setParameter("boardId", boardId)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();

            return commentEntityList.stream()
                    .map(s -> CommentFactory.create(s))
                    .collect(Collectors.toList());
        } finally {
            entityManager.close();
        }
    }

    public void comment(String boardId, String userId, String commentBody) {
        EntityManager entityManager = BoardEntityManager.INSTANCE.createEntityManager();

        try {
            Date now = DateTimeUtils.toDate(DateTimeUtils.now());
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            BoardEntity boardEntity = entityManager.find(BoardEntity.class, boardId, LockModeType.PESSIMISTIC_WRITE);
            boardEntity.setCommentCount(boardEntity.getCommentCount() + 1);
            boardEntity.setLastPostedAt(now);

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setPrimaryKey(new CommentEntity.PrimaryKey(boardEntity.getBoardId(), boardEntity.getCommentCount()));
            commentEntity.setPostedByUserId(userId);
            commentEntity.setPostedAt(now);
            commentEntity.setBody(commentBody);
            entityManager.persist(commentEntity);

            entityTransaction.commit();
        } finally {
            entityManager.close();
        }
    }

    public Optional<Board> getBoardByBoardId(String boardId) {
        EntityManager entityManager = BoardEntityManager.INSTANCE.createEntityManager();

        try {
            BoardEntity boardEntity = entityManager.find(BoardEntity.class, boardId);

            if (boardEntity == null) {
                return Optional.empty();
            }

            return Optional.of(BoardFactory.create(boardEntity));
        } finally {
            entityManager.close();
        }
    }

    static class BoardFactory {
        static Board create(BoardEntity boardEntity) {
            return new Board(
                    boardEntity.getBoardId(),
                    boardEntity.getBoardSubject(),
                    boardEntity.getCommentCount(),
                    boardEntity.getCreatedByUserId(),
                    boardEntity.getLastPostedAt()
            );
        }
    }

    static class CommentFactory {
        static Comment create(CommentEntity commentEntity) {
            return new Comment(
                    commentEntity.getPrimaryKey().getBoardId(),
                    commentEntity.getPrimaryKey().getCommentSeq(),
                    commentEntity.getPostedByUserId(),
                    commentEntity.getUserInfoEntity().getNickname(),
                    commentEntity.getBody(),
                    commentEntity.getPostedAt()
            );
        }
    }
}
