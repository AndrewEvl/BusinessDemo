<script src="http://api-maps.yandex.ru/2.1/?load=package.full&lang=ru-RU" type="text/javascript"></script>
<html>
<head>
    <title>Company map</title>
    <meta name="layout" content="main"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <g:link uri="/logoff">Logout</g:link>
    <g:link uri="/#">Home page</g:link>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript">
    </script>
    <script type="text/javascript">
        ymaps.ready(init);

        function init() {
            var myMap = new ymaps.Map("map", {
                center: [53.884052, 27.591475],
                zoom: 15
            });

            var coords = (${data});
            var myCollection = new ymaps.GeoObjectCollection({}, {
                preset: 'islands#redIcon',
                draggable: false
            });

            for (var i = 0; i < coords.length; i++) {
                myCollection.add(new ymaps.Placemark(coords[i]));
            }
            myMap.geoObjects.add(myCollection);
        }
    </script>
</head>
<body>
<div id="map" style="width: 100%; height: 100%"></div>
</body>
</html>
