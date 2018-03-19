package com.gcit.lms.service;

import java.sql.Connection;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Publisher;


@RestController
public class AdminService {
	
	@Autowired
	AuthorDAO adao;
	
	@Autowired
	BookDAO bdao;
	
	@Autowired
	BranchDAO brdao;
	
	
	ConnectionUtil connUtil = new ConnectionUtil();
	
	@Transactional
	@RequestMapping(value = "/saveAuthor", method = RequestMethod.POST, consumes="application/json")
	public void saveAuthor(@RequestBody Author author) throws SQLException, ClassNotFoundException{
		
			if(author.getAuthorId()!=null && author.getAuthorName()!=null){ 
				adao.updateAuthor(author);
			}else if (author.getAuthorId()!=null){
				adao.deleteAuthor(author);
			}
			else{
				adao.addAuthor(author);
			}
		
	}

	/**
	 * read all branch which have at least one copy
	 * @throws ClassNotFoundException 
	 */
	
	/*
	 * read/y/1
	 */
	@RequestMapping(value = "/readAuthors/{authorName}/{pageNo}", method = RequestMethod.GET, consumes="application/json", produces="application/json")
	public List<Author> getAuthors(@PathVariable String authorName,@PathVariable Integer pageNo) throws SQLException, ClassNotFoundException{
	
		List<Author> authors = new ArrayList<>();
			if(authorName!=null){
				authors = adao.readAuthorsByName(authorName);
			}else{
				authors =  adao.readAllAuthors(pageNo);
			}
			for (Author a : authors) {
				a.setBooks(bdao.readBooksByAuthorId(a.getAuthorId()));
			}
			
			return authors;
		
	}
	
	@RequestMapping(value = "/saveBook", method = RequestMethod.POST, consumes="application/json")
	public void saveBook(@RequestBody Book book) throws SQLException, ClassNotFoundException{

			Integer bookId = bdao.addBookGetPK(book);
			for(Author a: book.getAuthors()){
				adao.addBookAuthors(bookId, a.getAuthorId());
			}
	}
			
	//read author by PK
	public Author getAuthorsByPK(Integer authorId) throws SQLException, ClassNotFoundException{
		
			return adao.readAuthorByPk(authorId);		
	}
	
	
	public Integer getAuthorsCount() throws SQLException, ClassNotFoundException{
	
			return adao.getAuthorsCount(null);
	}

	public Book getBookByPK(Integer bookId) throws SQLException, ClassNotFoundException{
		
			return bdao.readBookByPK(bookId);
	}
		
	
	@RequestMapping(value = "/readBooks/{bookTitle}", method = RequestMethod.GET, consumes="application/json", produces="application/json")
	public List<Book> getBooks(@PathVariable String bookTitle) throws SQLException, ClassNotFoundException{
		
			return bdao.readAllBooks();
	}
	
	
	
	public Integer getBooksCount() throws SQLException, ClassNotFoundException{
		
		return bdao.getBooksCount(null);
}
	
	public void updateBook(Book book) throws SQLException, ClassNotFoundException{
			bdao.updateBook(book);
	}
	
	public void deleteBook(Book book) throws SQLException, ClassNotFoundException{
			bdao.deleteBook(book);		
	}
	
	public void savePublisher(Publisher publisher) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.savePublisher(publisher);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	
	public Integer savePublisherWithID(Publisher publisher) throws SQLException{
		Connection conn = null;
		int id = 0;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			id = bdao.savePublisherWithID(publisher);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return id;
	}
	
	public List<Publisher> readPublisherById(Integer id) throws SQLException{
		Connection conn = null;
		List<Publisher> publisherList = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO publisherDao = new PublisherDAO(conn);
			publisherList = publisherDao.readPublisherById(id);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return publisherList;
	}
	
	public List<Publisher> readAllPublishers() throws SQLException{
		Connection conn = null;
		List<Publisher> publisherList = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO publisherDao = new PublisherDAO(conn);
			publisherList = publisherDao.readAllPublishers();
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return publisherList;
	}
	
	public void updatePublisher(Publisher publisher) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.updatePublisher(publisher);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	
	public void deletePublisher(Publisher publisher) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			PublisherDAO bdao = new PublisherDAO(conn);
			bdao.deletePublisher(publisher);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	

	public void saveBranch(Branch branch) throws SQLException, ClassNotFoundException{
		
			if(branch.getBranchId()!=null && branch.getBranchName()!=null){ 
				brdao.updateBranch(branch);
			}else if (branch.getBranchId()!=null){
				brdao.deleteBranch(branch);
			}else{
				brdao.addBranchWithID(branch);
			}
		
	}
	
	
	

	public List<Branch> getBranches(String branchName) throws SQLException, ClassNotFoundException{

		List<Branch> branches=new ArrayList<>();
			if(branchName!=null){
				branches = brdao.readBranchesByName(branchName);
			}else{
				branches = brdao.readAllBranch();
			}
	
			return branches;
	}
		
	
	public Branch readBranchByPK(Integer branchId) throws SQLException, ClassNotFoundException{
			
			return brdao.readBranchByPk(branchId);
		
	}
	
	
	public void updateBranch(Branch branch) throws SQLException, ClassNotFoundException{
	
			brdao.updateBranch(branch);
		
	}
	
	public void deleteBranch(Branch branch) throws SQLException, ClassNotFoundException{
		
			brdao.deleteBranch(branch);
		
	}
	
	public Integer getBranchesCount() throws SQLException, ClassNotFoundException{
		
			return brdao.getBranchesCount(null);
		
	}
	
	public void saveBorrower(Borrower borrower) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.saveBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	
	public List<Borrower> readAllBorrowers() throws SQLException{
		List<Borrower> borrowerList = null;
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			borrowerList = bdao.readAllBorrowers();
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return borrowerList;
	}
	
	public void updateBorrower(Borrower borrower) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.updateBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	
	public void deleteBorrower(Borrower borrower) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BorrowerDAO bdao = new BorrowerDAO(conn);
			bdao.deleteBorrower(borrower);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
	
	public List<BookLoans> readAllBookLoans() throws SQLException{
		List<BookLoans> bookLoans = null;
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoansDAO bdao = new BookLoansDAO(conn);
			bookLoans = bdao.readAllBookLoans();
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return bookLoans;
	}
	
	public List<BookLoans> readAllBookLoansWithBookTitle() throws SQLException{
		List<BookLoans> bookLoans = null;
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoansDAO bdao = new BookLoansDAO(conn);
			bookLoans = bdao.readAllBookLoansWithBookTitle();
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		return bookLoans;
	}
	
	public void updateDueDateBookLoans(Date dueDate, Integer bookId, Integer branchId, Integer cardNo, Timestamp dateIn) throws SQLException{
		Connection conn = null;
		try {
			conn = connUtil.getConnection();
			BookLoansDAO bdao = new BookLoansDAO(conn);
			bdao.updateDueDateBookLoans(dueDate, bookId, branchId, cardNo, dateIn);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally{
			conn.close();
		}
		
	}
}
