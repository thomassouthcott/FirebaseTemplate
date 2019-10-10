package com.thomassouthcott.firebasetemplate.repositorys;

import com.google.android.gms.tasks.Task;
import com.thomassouthcott.firebasetemplate.models.Model;

import java.util.List;

/**
 * @title Repository Interface
 * @author Thomas Edward Southcott
 *
 * @param <IModel>
 */
public interface Repository<IModel extends Model> {
    /**
     * @title Exists
     *
     * @param id
     * @return
     */
    Task<Boolean> exists(final String id);

    /**
     * @title Create
     *
     * @param IModel
     * @return
     */
    Task<Boolean> create(final IModel IModel);

    /**
     * @title Read
     *
     * @return
     */
    Task<List<IModel>> read();

    /**
     * @title Read Document Key
     *
     * @param id
     * @return
     */
    Task<IModel> read(final String id);

    /**
     * @title Update
     *
     * @param IModel
     * @return
     */
    Task<Void> update(final IModel IModel);

    /**
     * @title Delete
     *
     * @param id
     * @return
     */
    Task<Void> delete(final String id);
}
