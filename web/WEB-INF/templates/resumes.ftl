<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Резюме</title>
</head>
<body>
<ol>
    <h1>Я НА ВЕТКЕ JSP</h1>
    <h2>RESUME LIST</h2>
    <#list resumes as r>
        <li>
            <strong>Uuid : <em>${r.uuid}</em> <br> Full Name : <em>${r.fullName}</em></strong>
            <br><strong>CONTACTS</strong>
            <ul>
                <#list r.contacts as type, value>
                    <li>${type} : <em>${value}</em></li>
                </#list>
            </ul>
            <strong>SECTIONS</strong>
            <ul>
                <#list r.sections as sectionType, section>
                    <#attempt>
                        <li>${sectionType} :
                            <ol>
                                <#list section.lines as line>
                                    <li><em>${line}</em></li>
                                </#list>
                            </ol>
                        </li>
                        <#recover>
                            <li><em>${sectionType} : ${section.text}</em></li>
                    </#attempt>
                </#list>
            </ul>
        </li>
    </#list>
</ol>
</body>
</html>
