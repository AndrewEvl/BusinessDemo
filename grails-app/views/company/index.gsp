<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <g:link controller="logout">Logout</g:link>
</head>

<body>
<g:uploadForm action="upload" method="post">
    <input type="file" name="filecsv"/>
    <input type="submit"/>
</g:uploadForm>
<g:form action="findByName" method="post">
    <div class="dialog">
        <label for="name">Scratch by name:</label>
        <input type="text" id="name" name="name"/>
    </div>
</g:form>
<g:form action="findByStreet" method="post">
    <div class="dialog">
        <label for="street">Scratch by street:</label>
        <input type="text" id="street" name="street"/>
    </div>
</g:form>
<g:form action="findByEmail" method="post">
    <div class="dialog">
        <label for="email">Scratch by Email:</label>
        <input type="text" id="email" name="email"/>
    </div>
</g:form>
<a href="#list-company" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                              default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-company" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <f:table collection="${companyList}"/>

    <div class="pagination">
        <g:paginate total="${companyCount ?: 0}"/>
    </div>
</div>
</body>
</html>