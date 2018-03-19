package com.gcit.lms;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.service.AdminService;

/**
 * Handles requests for the application home page.
 */




@Controller
public class HomeController {
	
	@Autowired
	AdminService adminService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {		
		return "default";
	}
	
	@RequestMapping(value = "/gotoAdmin", method = RequestMethod.GET)
	public String admin() {		
		return "admin";
	}
	
	@RequestMapping(value = "/gotoAuthor", method = RequestMethod.GET)
	public String author() {		
		return "author";
	}
	
	@RequestMapping(value = "/gotoAddAuthor", method = RequestMethod.GET)
	public String addAuthor() {		
		return "addauthor";
	}
	
	@RequestMapping(value = "/gotoBook", method = RequestMethod.GET)
	public String book() {		
		return "book";
	}
	
	@RequestMapping(value = "/gotoAddBook", method = RequestMethod.GET)
	public String addBook() {		
		return "addbook";
	}
	
	
	@RequestMapping(value = "/gotoBranch", method = RequestMethod.GET)
	public String branch() {		
		return "branch";
	}
	
	@RequestMapping(value = "/gotoAddBranch", method = RequestMethod.GET)
	public String addBranch() {		
		return "addbranch";
	}
	
	@RequestMapping(value = "/gotoViewBranches", method = RequestMethod.GET)
	public String viewBranches() {		
		return "viewbranches";
	}
	
	@RequestMapping(value = "/gotoViewAuthors", method = RequestMethod.GET)	
		public String viewAuthors(Model model) {
			List<Author> authors = new ArrayList<>();
			try {
				authors = adminService.getAuthors(null, 1);
				model.addAttribute("authors", authors);
				Integer totalAuthors = adminService.getAuthorsCount();
				Integer pageSize = (int) Math.ceil(totalAuthors / 10 + 1);
				model.addAttribute("pageSize", pageSize);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
			return "viewauthors";
		}
	
	@RequestMapping(value = "/gotoViewBooks", method = RequestMethod.GET)
		
		public String viewBooks(Model model) {
			List<Book> books = new ArrayList<>();
			try {
				books = adminService.getBooks(null);
				model.addAttribute("books", books);
				Integer totalBooks = adminService.getBooksCount();
				Integer pageSize = (int) Math.ceil(totalBooks / 10 + 1);
				model.addAttribute("pageSize", pageSize);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		
		return "viewbook";
	}
	
	
	
	
	
	
	
}
