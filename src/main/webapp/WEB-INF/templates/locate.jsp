<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page  contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<jsp:include page="../views/declare.jsp"/>
<jsp:include page="../views/includes.jsp"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<div id="content">
    <tiles:insertAttribute name="errors"/>
    <tiles:insertAttribute name="content"/>
    <div>
</html>

