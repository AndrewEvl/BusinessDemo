<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <g:link uri="/logoff">Logout</g:link>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js">
        $(function() {
            $('#upload-form').ajaxForm({
                success: function(msg) {
                    alert("File has been uploaded successfully");
                },
                error: function(msg) {
                    $("#upload-error").text("Couldn't upload file");
                }
            });
        });
    </script>
</head>
<body>
<form id="upload-form" class="upload-box" action="/company/upload" method="post" enctype="multipart/form-data">
    <input type="file" id="file" name="file1" />
    <span id="upload-error" class="error">${uploadError}</span>
    <input type="submit" id="upload-button" value="Upload" />
</form>
%{--<g:uploadForm action="upload">--}%
%{--<input type="file" name="file1"/>--}%
%{--<input type="submit"/>--}%
%{--</g:uploadForm>--}%
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
<sec:ifAnyGranted roles="ROLE_ADMIN">
    || <a href="/user/index">User List</a> ||
</sec:ifAnyGranted>
<a href="/company/mapEncoder">Company on map</a> ||
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