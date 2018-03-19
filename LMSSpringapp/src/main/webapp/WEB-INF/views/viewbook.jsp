<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@include file="Bootstrap2.html" %>
<%@ taglib prefix="gcit" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


 <script>
	function searchBooks() {
		$.ajax({
			method : "POST",
			url : "searchBooks",
			data : {
				"searchString" : $('#searchString').val()
			}
		}).done(function(data) {
			$('#booksTable').html(data);
		});
	}
</script>




 <div class="jumbotron">
<h2>List of Books in LMS</h2>
	${message}
	<div class="input-group">
		<input type="text" class="form-control" placeholder="Search Books"
			aria-describedby="basic-addon1" id="searchString"
			oninput="searchBooks()">
	</div>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<gcit:forEach begin="1" end="${pageSize}" var="i">
				<li><a href="pageBooks?pageNo=${i}">${i}</a></li>
			</gcit:forEach>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="book book-striped" id="booksTable">
		<tr>
			<th>Book ID</th>
			<th>Book Title</th>
			<th>Author Name</th>
			
			
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<gcit:forEach items="${books}" var="a" varStatus="booksLoop">
			<tr>
				<td>${booksLoop.index+1}</td>
				<td>${a.title}</td>
				<td><gcit:forEach items="${a.books}" var="b">
					${b.authorName} | 
				</gcit:forEach></td>
				<td></td>
				<td></td>
			</tr>
		</gcit:forEach>
	</table>
</div>

<!-- Large modal -->

<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog"
	aria-labelledby="myLargeModalLabel" id="myEditAuthorModal">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content"></div>
	</div>
</div>

<script>
	$(document).ready(function() {

		$('.modal').on('hidden.bs.modal', function(e) {
			$(this).removeData();
		});
	});
</script>