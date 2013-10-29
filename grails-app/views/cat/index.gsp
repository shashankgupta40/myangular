<%@ page import="com.impl.catapp.Cat" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="ng-app">
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'cat.label', default: 'Cat')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body data-ng-app="scaffolding" data-base-url="${createLink(uri: '/cat/')}">
        <a href="#list-cat" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="list" href="#list"><g:message code="default.list.label" args="[entityName]" /></a></li>
                <li><a class="create" href="#create"><g:message code="default.new.label" args="[entityName]" /></a></li>
            </ul>
        </div>
            <div class="content" role="main" data-ng-view>
            </div>
    </body>
</html>
