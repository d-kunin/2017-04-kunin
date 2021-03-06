<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8"/>
    <title>Welcome</title>
</head>
<body>
<!-- send new value here -->
<br/>
<form action="/stats" method="post">
    <input type="hidden" name="action" value="write"/>
    New key: <input type="text" name="new_key"><br/>
    New value: <input type="text" name="new_value"><br/>
    <input type="submit" value="Send">
</form>
<br/>
<!-- read value here -->
<form action="/stats" method="get">
    <input type="hidden" name="action" value="read"/>
    Read by key: <input type="text" name="read_key" value="${read_key!}"><br>
    Value is: <input type="text" value="${result_read!}" readonly><br>
    <input type="submit" value="Send">
</form>
<br/>
<!-- show stats here -->
<br/>
<table>
    <#list stats as stat>
        <tr>
            <td>${stat.key}</td>
            <td>${stat.value}</td>
        </tr>
    </#list>
</table>
<br/>
<!-- operations log here -->
<textarea readonly cols="80" rows="20">
<#list logs as log>
    ${log}
</#list>
</textarea>
</body>
</html>