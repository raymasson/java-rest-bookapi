package com.pluralsight;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import static java.lang.String.format;

import com.mongodb.MongoClient;
import com.pluralsight.models.BookModel;
import com.pluralsight.repositories.BookRepository;

@Path("books")
public class BookController {
	
	private static final String BOOK_NOT_FOUND_MSG = "Book with Id[%s] does not exist";
	private static final String BOOK_EXISTS_MSG = "Book with Id[%s] already exists";
	
	private BookRepository bookRepository = new BookRepository();
	
	public BookController () throws UnknownHostException{
		bookRepository.init(new MongoClient("localhost", 27017), "bookAPI", "books");
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
    	List<BookModel> books = new ArrayList<>(bookRepository.getAll());
        
    	GenericEntity<List<BookModel>> booksEntity = new GenericEntity<List<BookModel>>(books) {};
        return Response.ok(booksEntity).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{bookId}")
    public Response get(@PathParam ("bookId") String bookId) {
    	BookModel book = bookRepository.get(bookId);
    	
    	if(book != null){
    		return Response.ok(book).build();
    	}
        
        return Response.status(NOT_FOUND).entity(format(BOOK_NOT_FOUND_MSG, bookId)).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(BookModel bookModel) {
    	BookModel book = bookRepository.create(bookModel);
    	if (book != null) {
    		return Response.status(Status.CREATED).entity(book).build();
	    }
    	
    	return Response.status(BAD_REQUEST)
    	        .entity(format(BOOK_EXISTS_MSG, bookModel.get_id()))
    	        .type(MediaType.TEXT_PLAIN).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(BookModel bookModel) {
    	BookModel book = bookRepository.update(bookModel);
        if (book != null) {
          return Response.ok(book).build();
        }
        
        return Response.status(NOT_FOUND).entity(format(BOOK_NOT_FOUND_MSG, bookModel.get_id())).build();
    }
    
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{bookId}")
    public Response delete(@PathParam ("bookId") String bookId) {
    	if (bookRepository.delete(bookId)) {
    		return Response.status(NO_CONTENT).build();
	    }
    	
	    return Response.status(NOT_FOUND).entity(format(BOOK_NOT_FOUND_MSG, bookId)).build();
    }
}
