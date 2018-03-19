package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Branch;

public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>>{


	public void addBookAuthor(Book book, Author author) throws ClassNotFoundException, SQLException {
		List<Object> list = new ArrayList<>();
		list.add(book.getBookId());
		list.add(author.getAuthorId());
		mysqlTemplate.update("insert into tbl_book_authors (bookId, authorId) values (?, ?)", list);
	}
	
	
	public void addBook(Book book) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("INSERT INTO tbl_book (bookName) VALUES (?)", new Object[] {book.getTitle()});
	}

	
	public Integer addBookGetPK(Book book) throws SQLException, ClassNotFoundException {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		mysqlTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO tbl_book (bookName) VALUES (?) ", Statement.RETURN_GENERATED_KEYS);
		        statement.setString(1, book.getTitle());
		        return statement;
		    }
		}, holder);

		return holder.getKey().intValue();
	}
	
	public void saveBook(Book book) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("insert into tbl_book (title) values (?)", new Object[]{book.getTitle()});
	}
	
	public Integer getBooksCount(String bookName) throws ClassNotFoundException, SQLException{
		return mysqlTemplate.queryForObject("SELECT COUNT(*) FROM tbl_book", Integer.class);
	}
	
	
	
	public Integer saveBookWithID(Book book) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.update("insert into tbl_book (title) values (?)", new Object[]{book.getTitle()});
	}
	
	// change from title to bookName
	public void updateBook(Book book) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("update tbl_book set bookName = ? where bookId = ?", new Object[]{book.getTitle(), book.getBookId()});
	}
	
	public void deleteBook(Book book) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("delete from tbl_book where bookId = ?", new Object[]{book.getBookId()});
	}
	
	public List<Book> readAllBooks() throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select * from tbl_book", this);
	}


	public List<Book> readBooksByAuthorId(Integer authorId) throws ClassNotFoundException, SQLException {
		return mysqlTemplate.query(
				"select * from tbl_book where bookId IN (select bookId from tbl_book_authors where authorId = ?) ",
				new Object[] { authorId }, this);
	}
	

	public List<Book> readAllBooksInBranch(Branch branch) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select * from tbl_book left join tbl_book_copies on tbl_book.bookId = tbl_book_copies.bookId left join tbl_library_branch on tbl_library_branch.branchId = tbl_book_copies.branchId where tbl_library_branch.branchId = ?", new Object[]{branch.getBranchId()}, this);
	}
	
	public List<Book> readAllBooksAvailable(Branch branch) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select b.* from tbl_book_copies a join tbl_book b on a.bookId = b.bookId left join (select l.bookId, count(l.bookId) as rented from tbl_book_loans l where l.branchId = ? and l.dateIn= \"1900-01-01 00:00\" group by l.bookId) c on a.bookId = c.bookId where a.noOfCopies > 0 and a.branchId = ? and a.noOfCopies > ifnull(rented,0)", new Object[]{branch.getBranchId(), branch.getBranchId()}, this);
	}
	
	public List<Book> readBooksInBranchInBorrowerCheckedout(Branch branch, Integer cardNo) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select * from tbl_book left join tbl_book_loans on tbl_book.bookId = tbl_book_loans.bookId left join tbl_library_branch on tbl_library_branch.branchId = tbl_book_loans.branchId where tbl_library_branch.branchId = ? and tbl_book_loans.cardNo = ? and tbl_book_loans.dateIn= \"1900-01-01 00:00\"", new Object[]{branch.getBranchId(), cardNo}, this);
	}
	
	public Book readBookByPK(Integer bookId) throws ClassNotFoundException, SQLException{
		List<Book> books =  mysqlTemplate.query("SELECT * FROM tbl_book WHERE bookId = ?", new Object[]{bookId},this);
		if(books!=null){
			return books.get(0);
		}
		return null;
	}

	
	
	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
			while(rs.next()){
				Book b = new Book();
				b.setBookId(rs.getInt("bookId"));
				b.setTitle(rs.getString("title"));
				books.add(b);
			}		
		return books;
	}

}
