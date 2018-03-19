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

public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>>{

	

	public void saveBookCopies(BookCopies bookCopies) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("insert into tbl_book_copies (NoOfCopies) values (?)", new Object[]{ bookCopies.getNoCopies() });
	}
	
	
	public Integer addBookCopiesGetPK(BookCopies bookCopies) throws SQLException, ClassNotFoundException {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		mysqlTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO tbl_book_copies (noOfCopies) VALUES (?) ", Statement.RETURN_GENERATED_KEYS);
		        statement.setInt(1, bookCopies.getNoCopies());
		        return statement;
		    }
		}, holder);

		return holder.getKey().intValue();
	}
	
	
	public Integer saveBookCopiesWithID(BookCopies bookCopies) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.update("insert into tbl_book_copies (NoOfCopies) values (?)", new Object[]{ bookCopies.getNoCopies() });
	}
	
	public void updateBookCopies(BookCopies bookCopies, Book book, Branch branch) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("update tbl_book_copies set NoOfCopies = ? where bookId = ? AND branchId = ?", new Object[]{ bookCopies.getNoCopies(), book.getBookId(), branch.getBranchId() });
	}
	
	public void deleteBookCopies(BookCopies bookCopies) throws SQLException, ClassNotFoundException{
		mysqlTemplate.update("delete from tbl_book_copies where bookId = ? AND branchId = ?", new Object[]{ bookCopies.getBook().getBookId(), bookCopies.getBranch().getBranchId() });
	}
	
	public List<BookCopies> readAllBookCopies() throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select * from tbl_book_copies", this);
	}
	
	public List<BookCopies> readBookCopies(Book book, Branch branch) throws SQLException, ClassNotFoundException{
		return mysqlTemplate.query("select * from tbl_book_copies where bookId = ? AND branchId = ?", new Object[]{ book.getBookId(), branch.getBranchId() }, this);
	}
	

	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		List<BookCopies> listBookCopies = new ArrayList<>();
			while(rs.next()){
				BookCopies bookCopies = new BookCopies();
				bookCopies.setNoCopies(rs.getInt("NoOfCopies"));
				listBookCopies.add(bookCopies);
			}		
		return listBookCopies;
	}
	

}

