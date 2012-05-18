package com.shinyieva.enpolonia.net.cache;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shinyieva.enpolonia.settings.AppSettings;

public class CacheSQLite {

	static private CacheSQLite _Current;

	static public CacheSQLite Current() {
		if (_Current == null) {
			Log.e("CacheSQLite",
					"No puedes usar CacheSQLite sin hacer antes un CacheSQLite.Init(Context)");
		}
		return _Current;
	}

	static public void Init(Context p_context, int version) {

		if (_Current == null) {
			Log.i("CacheSQLite",
					"Initialize CacheSQlite con version "
							+ String.valueOf(version));
			_DB_VERSION = version;
			_Current = new CacheSQLite(p_context);
		} else {
			Log.i("CacheSQLite", "YA ESTA INICIALIZADA");

		}

	}

	// Datos sobre la base de datos
	private static final String _DB_NAME = "Cache.db";
	private static int _DB_VERSION;

	// Datos sobre la tabla
	private static final String _TABLE_NAME = "entry_cache";

	// Datos sobre los campos de la tabla
	private static final String _FIELD_GUID = "Guid";
	private static final int _FIELD_GUID_POS = 0;

	private static final String _FIELD_TITLE = "Title";

	private static final String _FIELD_LINK = "Link";

	private static final String _FIELD_DESCRIPTION = "Description";

	private static final String _FIELD_DATE = "Date";

	private static final String _FIELD_CREATOR = "Creator";

	private static final String _FIELD_URLCOMM = "UrlComments";

	private static final String _FIELD_NUMCOMM = "NumComments";

	private static final String _FIELD_CONTENT = "Content";

	private static final String _FIELD_UNREAD = "Unread";

	private final DBHelper _DBHelper;

	public CacheSQLite(Context p_context) {

		this._DBHelper = new DBHelper(p_context, _DB_NAME, null, _DB_VERSION);
		Log.d("CacheSQLite", "Constructor de CacheSQLite");
	}

	/**
	 * Devuelve un String con el contenido de la URL
	 * 
	 * @param p_entryId
	 *            Direcci�n URL de la petici�n
	 * @return Devuelve null en caso de no estar dada de alta la URL indicada
	 */
	public Cache Get(String p_entryId) {
		Log.d("CacheSQLite", "GetByServiceUri p_serviceURL[" + p_entryId + "]");

		Cache cacheDto = null;

		SQLiteDatabase db = this._DBHelper.getReadableDatabase();

		// SELECT * FROM entry_cache WHERE Guid = p_entryId;
		Cursor cur = db.query(_TABLE_NAME, null, _FIELD_GUID + "=?",
				new String[] { p_entryId }, null, null, null);

		if (cur.moveToFirst()) {
			cacheDto = new Cache(
					cur.getString(cur.getColumnIndex(_FIELD_GUID)),
					cur.getString(cur.getColumnIndex(_FIELD_TITLE)),
					cur.getString(cur.getColumnIndex(_FIELD_LINK)),
					cur.getString(cur.getColumnIndex(_FIELD_DESCRIPTION)),
					cur.getString(cur.getColumnIndex(_FIELD_DATE)),
					cur.getString(cur.getColumnIndex(_FIELD_CREATOR)),
					cur.getString(cur.getColumnIndex(_FIELD_URLCOMM)),
					cur.getString(cur.getColumnIndex(_FIELD_NUMCOMM)),
					cur.getString(cur.getColumnIndex(_FIELD_CONTENT)),
					cur.getString(cur.getColumnIndex(_FIELD_UNREAD)));
		}

		cur.close();
		db.close();

		return cacheDto;
	}

	/*
	 * 
	 */
	public ArrayList<Cache> GetAll() {
		Log.d("CacheSQLite", "GetByServiceUri p_serviceURL[" + "*" + "]");

		SQLiteDatabase db = this._DBHelper.getReadableDatabase();

		// SELECT * FROM entry_cache ORDER BY Date DESC;
		Cursor cur = db.query(true, _TABLE_NAME, null, null, null, null, null,
				_FIELD_DATE + " DESC", null);

		ArrayList<Cache> temp = null;
		if (cur != null) {
			temp = new ArrayList<Cache>();
			for (int i = 0; i < cur.getCount(); i++) {
				cur.moveToNext();
				Log.d(AppSettings.TAG_LOG,
						"get("
								+ cur.getString(cur.getColumnIndex(_FIELD_DATE))
								+ ")["
								+ cur.getString(cur.getColumnIndex(_FIELD_GUID))
								+ "]");
				temp.add(new Cache(cur.getString(cur
						.getColumnIndex(_FIELD_GUID)), cur.getString(cur
						.getColumnIndex(_FIELD_TITLE)), cur.getString(cur
						.getColumnIndex(_FIELD_LINK)), cur.getString(cur
						.getColumnIndex(_FIELD_DESCRIPTION)), cur.getString(cur
						.getColumnIndex(_FIELD_DATE)), cur.getString(cur
						.getColumnIndex(_FIELD_CREATOR)), cur.getString(cur
						.getColumnIndex(_FIELD_URLCOMM)), cur.getString(cur
						.getColumnIndex(_FIELD_NUMCOMM)), cur.getString(cur
						.getColumnIndex(_FIELD_CONTENT)), cur.getString(cur
						.getColumnIndex(_FIELD_UNREAD)))); // "Title" is the
															// field
															// name(column) of
															// the Table

			}
		}

		cur.close();
		db.close();

		return temp;
	}

	/**
	 * Si existe un registro para esa URL lo actualiza y si no lo crea
	 * 
	 * @param p_serviceURL
	 *            Direcci�n URL de la petici�n
	 * @param p_content
	 *            Contenido de la direcci�n
	 */
	public void Set(String p_guid, String p_title, String p_link,
			String p_desc, String p_date, String p_creator, String p_urlComm,
			String p_numComm, String p_content, String p_unread) {

		ContentValues values = new ContentValues(10);
		values.put(_FIELD_GUID, p_guid);
		values.put(_FIELD_TITLE, p_title);
		values.put(_FIELD_LINK, p_link);
		values.put(_FIELD_DESCRIPTION, p_desc);
		Log.d(AppSettings.TAG_LOG, "Set.Date = " + String.valueOf(p_date));
		values.put(_FIELD_DATE, p_date);
		values.put(_FIELD_CREATOR, p_creator);
		values.put(_FIELD_URLCOMM, p_urlComm);
		values.put(_FIELD_NUMCOMM, p_numComm);
		values.put(_FIELD_CONTENT, p_content);
		values.put(_FIELD_UNREAD, p_unread);

		SQLiteDatabase db = this._DBHelper.getWritableDatabase();

		Cursor cursor = db.query(_TABLE_NAME, new String[] { _FIELD_GUID },
				_FIELD_GUID + "=?", new String[] { p_guid }, null, null, null);

		if (cursor != null && db.isOpen()) {
			if (cursor.getCount() == 0) {
				// Creamos el registro
				Log.d("CacheSQLite", "SetByServiceUri: CREATE p_serviceURL["
						+ p_guid + "]...");
				db.insert(_TABLE_NAME, null, values);
			}
			cursor.close();
		}

		db.close();
	}

	private static class DBHelper extends SQLiteOpenHelper {

		private static final String _CREATE_TABLE = "CREATE TABLE "
				+ _TABLE_NAME + " (" + _FIELD_GUID
				+ " text primary key not null UNIQUE" + " ," + _FIELD_TITLE
				+ " text not null" + " ," + _FIELD_LINK + " text not null"
				+ " ," + _FIELD_DESCRIPTION + " text not null" + " ,"
				+ _FIELD_DATE + " text not null" + " ," + _FIELD_CREATOR
				+ " text not null" + " ," + _FIELD_URLCOMM + " text not null"
				+ " ," + _FIELD_CONTENT + " text not null" + " ,"
				+ _FIELD_NUMCOMM + " text not null" + " ," + _FIELD_UNREAD
				+ " text not null" + ")";

		public DBHelper(Context p_context, String p_name,
				CursorFactory p_factory, int p_version) {
			super(p_context, p_name, p_factory, p_version);
		}

		/**
		 * Se llama al crear la BBDD por primera vez, lo usamos para crear
		 * nuestra tabla
		 */
		@Override
		public void onCreate(SQLiteDatabase p_db) {
			Log.d("CacheSQLite", "onCreate, Creating cache table...");
			p_db.execSQL(_CREATE_TABLE);
		}

		/**
		 * Se llama al crear la BBDD por primera vez
		 */
		@Override
		public void onUpgrade(SQLiteDatabase p_db, int p_oldVersion,
				int p_newVersion) {
			Log.w("CacheSQLite", "Upgrading from version" + p_oldVersion
					+ " to " + p_newVersion + ". The old data will be deleted.");

			p_db.execSQL("DROP TABLE IF EXISTS " + _TABLE_NAME);

			this.onCreate(p_db);
		}

	}

}
