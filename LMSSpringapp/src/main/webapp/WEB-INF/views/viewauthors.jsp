<%@include file="Bootstrap2.html" %>
<%@ taglib prefix="gcit" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring"
	uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 <script>
	function searchAuthors() {
		$.ajax({
			method : "POST",
			url : "searchAuthors",
			data : {
				"searchString" : $('#searchString').val()
			}
		}).done(function(data) {
			$('#authorsTable').html(data);
		});
	}
</script>

<div class="jumbotron">
	<h2>List of Authors in LMS</h2>
	${message}
	<div class="input-group">
		<input type="text" class="form-control" placeholder="Search Authors"
			aria-describedby="basic-addon1" id="searchString"
			oninput="searchAuthors()">
	</div>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<gcit:forEach begin="1" end="${pageSize}" var="i">
				<li><a href="pageAuthors?pageNo=${i}">${i}</a></li>
			</gcit:forEach>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table table-striped" id="authorsTable">
		<tr>
			<th>Author ID</th>
			<th>Author Name</th>
			<th>Books Written</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<gcit:forEach items="${authors}" var="a" varStatus="authorsLoop">
			<tr>
				<td>${authorsLoop.index+1}</td>
				<td>${a.authorName}</td>
				<td><gcit:forEach items="${a.books}" var="b">
					${b.title} | 
				</gcit:forEach></td>
				
				
				<td><button class="btn btn-primary" type="button" data-toggle="modal" data-target="#editAuthorModal"
						href="editauthor?authorId=${a.authorId}">Edit</button></td>
						<td><button id="deleteButton" class="btn btn-danger" type="button" onclick= "deleteAuthor(${a.authorId})">Delete</button></td>
				
				
				<%-- <td><button class="btn btn-warning" onclick="javascript:location.href='editAuthor?authorId=(${a.authorId})'">Edit</button></td>
				<td><button class="btn btn-danger" onclick="javascript:location.href='deleteAuthor?authorId=(${a.authorId})'">Delete</button></td> --%>
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