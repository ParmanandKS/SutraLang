package com.sutralang.ide.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.sutralang.ide.data.entity.Snippet;
import java.util.List;

@Dao
public interface SnippetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSnippet(Snippet snippet);

    @Update
    void updateSnippet(Snippet snippet);

    @Delete
    void deleteSnippet(Snippet snippet);

    @Query("SELECT * FROM snippets WHERE userId = :userId ORDER BY timestamp DESC")
    List<Snippet> getSnippetsForUser(String userId);

    @Query("SELECT * FROM snippets WHERE id = :snippetId LIMIT 1")
    Snippet getSnippetById(int snippetId);
}
