package data;

public interface DAO<T> {
    /**
     * this method is used to create a new object in the database
     *
     * @param databaseName
     * @param tableName
     * @return the id of the object created or -1 if the creation fails
     */
    int create(String databaseName, String tableName);
    /**
     * this method is used to read an object from the database
     * @param databaseName
     * @param tableName
     * @param id
     * @return the object read or null if the object does not exist
     */
    T read(String databaseName, String tableName, int id);
    /**
     *  this method is used to update an object in the database
     * @param databaseName
     * @param tableName
     * @param o
     */
    void update(String databaseName, String tableName, T o);
    /**
     * this method is used to delete an object from the database
     * @param databaseName
     * @param tableName
     * @param id
     * @return true if the object is deleted or false if the object does not exist
     */
    boolean delete(String databaseName, String tableName, int id);

}
