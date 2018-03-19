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

public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {


	public void saveAuthor(Author author) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("insert into tbl_author (authorName) values (?)", new Object[] { author.getAuthorName() });
	}
	
	public Integer addAuthorGetPK(Author author) throws SQLException, ClassNotFoundException {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		mysqlTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO tbl_author (authorName) VALUES (?) ", Statement.RETURN_GENERATED_KEYS);
		        statement.setString(1, author.getAuthorName());
		        return statement;
		    }
		}, holder);

		return holder.getKey().intValue();
	}
	
	public void updateAuthor(Author author) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });
	}

	public void deleteAuthor(Author author) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("delete from tbl_author where authorId = ?", new Object[] { author.getAuthorId() });
	}

//	public void addAuthor(Author author) throws SQLException, ClassNotFoundException {
//		mysqlTemplate.update("INSERT INTO tbl_author (authorName) VALUES (?)", new Object[] {author.getAuthorName()});
//	}
	
	public void addAuthor(Author author) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("INSERT INTO tbl_author (authorName) VALUES (?)", new Object[] {author.getAuthorName()});
	}
	
	public List<Author> readAllAuthors(Integer pageNo) throws SQLException, ClassNotFoundException {
		setPageNo(pageNo);
		return mysqlTemplate.query("select * from tbl_author", this);
	}

	public List<Author> readAuthorsByName(String authorName) throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_author where authorName = ?", new Object[] { authorName },this);
	}
	
	public void addBookAuthors(Integer bookId, Integer authorId) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("INSERT INTO tbl_book_authors (?, ?)", new Object[]{bookId, authorId});
	}
	
	
	public Integer getAuthorsCount(String authorName) throws ClassNotFoundException, SQLException{
		return mysqlTemplate.queryForObject("SELECT COUNT(*) FROM tbl_author", Integer.class);
	}
	
	
//	public Integer getAuthorsCount(String authorName) throws ClassNotFoundException, SQLException{
	//	return getCount("SELECT COUNT(*) COUNT FROM tbl_author", null);
//	}
	
//	public Integer addAuthorWithID(Author author) throws SQLException, ClassNotFoundException {
//		List<Object> list = new ArrayList<>();
//		list.add(author.getAuthorName());
//		return saveWithID2("insert into tbl_author (authorName) values (?)", list);
//	}
//	
	//add book authors
	//public void addBookAuthors(Integer bookId, Integer authorId) throws SQLException, ClassNotFoundException {
	//	save("INSERT INTO tbl_book_authors (?, ?)", new Object[]{bookId, authorId});
	//}

	//read author by key
	public Author readAuthorByPk(Integer authorId) throws ClassNotFoundException, SQLException{
		List<Author> authors =  mysqlTemplate.query("SELECT * FROM tbl_author WHERE authorId = ?", new Object[]{authorId}, this);
		if(authors!=null){
			return authors.get(0);
		}
		return null;
	}
	
	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author a = new Author();
			a.setAuthorId(rs.getInt("authorId"));
			a.setAuthorName(rs.getString("authorName"));
			authors.add(a);
		}
		return authors;
	}


}
