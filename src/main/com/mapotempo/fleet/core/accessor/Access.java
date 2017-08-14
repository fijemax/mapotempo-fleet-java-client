package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.*;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Access.
 */
public class Access<T extends MapotempoModelBase> {
    private DatabaseHandler mDatabaseHandler;

    private Class<T> mClazz;

    protected View mView;

    private LiveQuery mLiveQuery;

    private DocumentBase mDocumentAnnotation;

    private Constructor<T> mConstructorFromDocument;

    private Constructor<T> mConstructorFromDatabase;

    private List<T> mItems;

    public interface ChangeListener<T> {
        void changed(List<T> items);
    }

    private List<ChangeListener> mChangeListenerList;

    public Access(Class<T> clazz, DatabaseHandler dbHandler) throws CoreException {
        mChangeListenerList = new ArrayList<>();

        mClazz = clazz;

        // TODO ERROR MESSAGE !!
        try {
            mConstructorFromDocument = mClazz.getConstructor(Document.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new CoreException("In Class : " + mClazz.getName() + " wrong definition of constructor define.");
        }

        try {
            mConstructorFromDatabase = mClazz.getConstructor(Database.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new CoreException("In Class : " + mClazz.getName() + " wrong definition of constructor define.");
        }


        mDatabaseHandler = dbHandler;

        mClazz = clazz;

        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);

        // TODO complete
        if(mDocumentAnnotation == null)
            throw new CoreException("TODO Exception");

        mView = mDatabaseHandler.mDatabase.getView(mClazz.getSimpleName());
        boolean test = mView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object type_found = document.get(mDocumentAnnotation.type_field());
                if(type_found != null && type_found.toString().equals(mDocumentAnnotation.type()))
                    emitter.emit(document, document.get("_id"));
            }
        }, "2");

        mLiveQuery = mView.createQuery().toLiveQuery();
        mLiveQuery.start();
        mLiveQuery.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(LiveQuery.ChangeEvent event) {
                mItems = runQuery(event.getRows());
                // todo
                //for(MapotempoModelBase mapotempoModelBase: mItems) {
                //    mapotempoModelBase.addConflictSolver();
                //}

                for(ChangeListener changeListener : mChangeListenerList) {
                    changeListener.changed(mItems);
                }
                mLiveQuery.start();
            }
        });
    }

    public T getNew () {
        T res = null;
        try {
            res = mConstructorFromDatabase.newInstance(mDatabaseHandler.mDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public T get(String id) {
        Document doc = mDatabaseHandler.mDatabase.getExistingDocument(id);
        if (doc != null)
            return getInstance(doc);
        return null;
    }

    /**
     * getAll.
     * Type view filter
     * @return all data T in a list
     */
    public List<T> getAll() {
        Query query = mView.createQuery();
        try {
            QueryEnumerator result = query.run();
            mItems = runQuery(result);
            return mItems;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void addChangeListener(Access.ChangeListener<T> changeListener) {
        mChangeListenerList.add(changeListener);
    }

    public void removeChangeListener(Access.ChangeListener<T> changeListener) {
        mChangeListenerList.remove(changeListener);
    }

    /**
     * runQuery.
     * @param result the query row enumerator result
     * @return a list of T
     **/
    protected List<T> runQuery(QueryEnumerator result)//Query query)
    {
        List<T> res = new ArrayList<T>();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = row.getDocument();
            T data = getInstance(doc);
            if(data != null)
                res.add(data);
        }

        return res;
    }

    private T getInstance(Document document) {
        T res = null;
        try {
            res = mConstructorFromDocument.newInstance(document);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
