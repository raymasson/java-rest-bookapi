package com.pluralsight.repositories;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.pluralsight.models.BookModel;
import com.pluralsight.utils.AppUtils;

public class BookRepository {
	
	private MongoClient mongoClient;
	private String dbName;
	private String collectionName;
	private DBCollection booksCollection;
	
	public void init(MongoClient mongoClient, String dbName, String collectionName) throws UnknownHostException {
		this.mongoClient = mongoClient;
		this.dbName = dbName;
		this.collectionName = collectionName;
		DB bookDatabase = mongoClient.getDB(dbName);
		booksCollection = bookDatabase.getCollection(collectionName);
	}

	public BookModel create(@NotNull final BookModel bookToCreate) {
		try {
			DBObject book = AppUtils.toDBObject(bookToCreate);
			booksCollection.insert(book);
			ObjectId id = (ObjectId)book.get("_id");
			bookToCreate.set_id(id.toString());
			return bookToCreate;
		} catch (MongoException.DuplicateKey e) {
			return null; // book already exists
		}
	}

	public List<BookModel> getAll() {
		final List<BookModel> books = new ArrayList<>();

		try (DBCursor cursor = booksCollection.find()) {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				String objectId = dbObject.get("_id").toString();
				BookModel book = (BookModel) AppUtils.fromDBObject(dbObject, BookModel.class);
				book.set_id(objectId);
				books.add(book);
			}
		}

		return books;
	}

	public BookModel get(@NotNull final String id) {
		DBObject query = new BasicDBObject("_id", new ObjectId(id));
		DBObject dbObject = booksCollection.findOne(query);

		if (dbObject != null) {
			BookModel book = (BookModel) AppUtils.fromDBObject(dbObject, BookModel.class);
			book.set_id(id);
			return book;
		}

		return null;
	}

	public BookModel update(@NotNull final BookModel bookToUpdate) {
		DBObject query = new BasicDBObject("_id", new ObjectId(bookToUpdate.get_id()));
		WriteResult result = booksCollection.update(query, AppUtils.toDBObject(bookToUpdate));

		if (result.getN() == 1) {
			return bookToUpdate;
		}

		return null; // book does not exist
	}

	public boolean delete(@NotNull final String id) {
		DBObject query = new BasicDBObject("_id", new ObjectId(id));
		WriteResult result = booksCollection.remove(query);

		if (result.getN() == 1) {
			return true;
		}

		return false;
	}
	
	public void setMongoClient(final MongoClient mongoClient) {
		this.mongoClient = mongoClient;
    }

	public void setDbName(final String dbName) {
		this.dbName = dbName;
	}

	public void setCollectionName(final String collectionName) {
		this.collectionName = collectionName;
	}
	
	public DBCollection getBooksCollection() {
		return booksCollection;
	}

	public void setBooksCollection(DBCollection booksCollection) {
		this.booksCollection = booksCollection;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public String getDbName() {
		return dbName;
	}

	public String getCollectionName() {
		return collectionName;
	}
}
