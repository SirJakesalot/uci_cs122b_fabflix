<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page session="true" %>
<%@ page isELIgnored="false" %>

<c:set var="context" value="${pageContext.request.contextPath}" />

<ul style="list-style: none;">
  <c:forEach var="title" items="${autocomplete_titles}">
    <li><p class='select' onmouseover="$('#autocomplete_title').html('${title}');">${title}</p></li>
  </c:forEach>
</ul>
