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
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;


public class BranchDAO extends BaseDAO<Branch> implements ResultSetExtractor<List<Branch>> {

	public void saveBranch(Branch branch) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("insert into tbl_library_branch (branchName) values (?)", new Object[] { branch.getBranchName() });
	}

	
	public Integer addBranchGetPK(Branch branch) throws SQLException, ClassNotFoundException {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		mysqlTemplate.update(new PreparedStatementCreator() {
		    @Override
		    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		        PreparedStatement statement = con.prepareStatement("INSERT INTO tbl_library_branch (branchName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
		        statement.setString(1, branch.getBranchName());
		        return statement;
		    }
		}, holder);

		return holder.getKey().intValue();
	}
		
	public void updateBranch(Branch branch) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { branch.getBranchName(), branch.getAddress(), branch.getBranchId() });
	}

	public void deleteBranch(Branch branch) throws SQLException, ClassNotFoundException {
		mysqlTemplate.update("delete from tbl_library_branch where branchId = ?", new Object[] { branch.getBranchId() });
	}

	public List<Branch> readAllBranch() throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_library_branch", this);
	}
	
	public List<Branch> readBranchesByName(String branchName) throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select * from tbl_library_branch where branchName = ?", new Object[] { branchName },this);
	}
	
	public Integer getBranchesCount(String branchName) throws ClassNotFoundException, SQLException{
		return mysqlTemplate.queryForObject("SELECT COUNT(*) FROM tbl_library_branch", Integer.class);
	}
	
	public Integer addBranchWithID(Branch branch) throws SQLException, ClassNotFoundException {
		List<Object> list = new ArrayList<>();
		list.add(branch.getBranchName());
		return mysqlTemplate.update("insert into tbl_library_branch (branchName) values (?)", list);
	}
	
	public Branch readBranchByPk(Integer branchId) throws ClassNotFoundException, SQLException{
		List<Branch> branches =  mysqlTemplate.query("SELECT * FROM tbl_library_branch WHERE branchId = ?", new Object[]{branchId},this);
		if(branches!=null){
			return branches.get(0);
		}
		return null;
	}
	
	public List<Branch> readBranchHaveLoanedBook(Integer cardNo) throws SQLException, ClassNotFoundException {
		return mysqlTemplate.query("select distinct tbl_library_branch.* from tbl_library_branch Join tbl_book_loans on tbl_library_branch.branchId = tbl_book_loans.branchId where tbl_book_loans.dateIn= \"1900-01-01 00:00\" and tbl_book_loans.cardNo = ?", new Object[] { cardNo },this);
	}

	
	
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		while (rs.next()) {
			Branch branch = new Branch();
			branch.setBranchId(rs.getInt("branchId"));
			branch.setBranchName(rs.getString("branchName"));
			branch.setAddress(rs.getString("branchAddress"));

			branches.add(branch);
		}
		return branches;
		
	}

}

