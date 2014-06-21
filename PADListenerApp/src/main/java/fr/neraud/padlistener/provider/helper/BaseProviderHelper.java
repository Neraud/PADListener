
package fr.neraud.padlistener.provider.helper;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import fr.neraud.padlistener.provider.descriptor.IField;

/**
 * Base prodiver helper<br/>
 * Provides helper method the access data in a Cursor and to put data in a ContentValues.
 * 
 * @author Neraud
 */
public class BaseProviderHelper {

	public static final int BOOLEAN_FALSE = 0;
	public static final int BOOLEAN_TRUE = 1;

	/**
	 * @param field the field to sum
	 * @return the string containing the sql sum
	 */
	public static String getSumColName(IField field) {
		return "sum(" + field.getColName() + ")";
	}

	/**
	 * @return the string containing the sql count
	 */
	public static String getCountAllColName() {
		return "count(*)";
	}

	/**
	 * @param field the field to count
	 * @return the string containing the sql count
	 */
	public static String getCountColName(IField field) {
		return "count(" + field.getColName() + ")";
	}

	/**
	 * @param field the field to max
	 * @return the string containing the sql max
	 */
	public static String getMaxColName(IField field) {
		return "max(" + field.getColName() + ")";
	}

	/**
	 * @param cursor the Cursor
	 * @param field the int field to get
	 * @return the requested int value
	 */
	public static int getIntSum(Cursor cursor, IField field) {
		return cursor.getInt(cursor.getColumnIndex(getSumColName(field)));
	}

	/**
	 * @param cursor the Cursor
	 * @return the requested int value
	 */
	public static int getIntCountAll(Cursor cursor) {
		return cursor.getInt(cursor.getColumnIndex(getCountAllColName()));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the int field to get
	 * @return the requested int value
	 */
	public static int getIntCount(Cursor cursor, IField field) {
		return cursor.getInt(cursor.getColumnIndex(getCountColName(field)));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the int field to get
	 * @return the requested int value
	 */
	public static int getIntMax(Cursor cursor, IField field) {
		return cursor.getInt(cursor.getColumnIndex(getMaxColName(field)));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the int field to get
	 * @return the requested int value
	 */
	public static int getInt(Cursor cursor, IField field) {
		return cursor.getInt(cursor.getColumnIndex(field.getColName()));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the long field to get
	 * @return the requested long value
	 */
	public static long getLong(Cursor cursor, IField field) {
		return cursor.getLong(cursor.getColumnIndex(field.getColName()));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the String field to get
	 * @return the requested String value
	 */
	public static String getString(Cursor cursor, IField field) {
		return cursor.getString(cursor.getColumnIndex(field.getColName()));
	}

	/**
	 * @param cursor the Cursor
	 * @param field the boolean field to get
	 * @return the requested boolean value
	 */
	public static boolean getBoolean(Cursor cursor, IField field) {
		return cursor.getInt(cursor.getColumnIndex(field.getColName())) == BOOLEAN_TRUE;
	}

	/**
	 * @param cursor the Cursor
	 * @param field the date field to get
	 * @return the requested date value
	 */
	public static Date getDate(Cursor cursor, IField field) {
		final long time = cursor.getLong(cursor.getColumnIndex(field.getColName()));
		return new Date(time * 1000);
	}

	/**
	 * @param cursor the Cursor
	 * @param field the date field to get
	 * @return the requested date value
	 */
	public static Date getDateMax(Cursor cursor, IField field) {
		final long time = cursor.getLong(cursor.getColumnIndex(getMaxColName(field)));
		return new Date(time * 1000);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the Integer value to set
	 */
	public static void putValue(ContentValues values, IField field, Integer value) {
		values.put(field.getColName(), value);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the Long value to set
	 */
	public static void putValue(ContentValues values, IField field, Long value) {
		values.put(field.getColName(), value);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the Float value to set
	 */
	public static void putValue(ContentValues values, IField field, Float value) {
		values.put(field.getColName(), value);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the String value to set
	 */
	public static void putValue(ContentValues values, IField field, String value) {
		values.put(field.getColName(), value);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the long boolean to set
	 */
	public static void putValue(ContentValues values, IField field, boolean value) {
		values.put(field.getColName(), value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
	}

	/**
	 * @param values the ContentValues
	 * @param field the IField to set
	 * @param value the Date value to set
	 */
	public static void putValue(ContentValues values, IField field, Date value) {
		if (value != null) {
			values.put(field.getColName(), value.getTime() / 1000);
		} else {
			values.put(field.getColName(), (Integer) null);
		}
	}

}
