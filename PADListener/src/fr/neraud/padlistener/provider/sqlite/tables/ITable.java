
package fr.neraud.padlistener.provider.sqlite.tables;

import java.util.List;

/**
 * Interface representing a table information
 * 
 * @author Neraud
 */
public interface ITable {

	/**
	 * @return the query to create the table
	 */
	public String createTable();

	/**
	 * @return the query to drop the table
	 */
	public String dropTable();

	/**
	 * @return the version of the table
	 */
	public int getVersion();

	/**
	 * @param oldVersion the oldVersion
	 * @param newVersion the newVersion
	 * @return the list of queries to upgrade the table
	 */
	public List<String> upgrade(int oldVersion, int newVersion);
}
