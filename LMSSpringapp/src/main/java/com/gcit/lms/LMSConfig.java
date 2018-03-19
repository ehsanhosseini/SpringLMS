package com.gcit.lms;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.service.AdminService;

@Configuration
public class LMSConfig {
	
	
	public String driver = "com.mysql.jdbc.Driver";
	public String url = "jdbc:mysql://localhost/library?useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public String username = "myuser";
	public String password = "mypassword";
	
	@Bean
//	@Scope(scopeName="prototype")  // by default it is singlaton scope but if we want to use another scope we need to add like this
	public BasicDataSource dataSource(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		return ds;
	}
	
	@Bean
	public JdbcTemplate mysqlTemplate(){
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public AuthorDAO adao() {
		return new AuthorDAO();
	}
	
	@Bean
	public DataSourceTransactionManager txManager(){
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public BookDAO bdao() {
		return new BookDAO();
	}
	
	@Bean
	public AdminService adminService() {
		return new AdminService();
	}
	
	@Bean
	public BranchDAO brdao() {
		return new BranchDAO();
	}

}
