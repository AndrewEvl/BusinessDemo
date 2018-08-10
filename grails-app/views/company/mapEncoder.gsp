<script src="http://api-maps.yandex.ru/2.1/?load=package.full&lang=ru-RU" type="text/javascript"></script>
<script type="text/javascript">
    var myMap;
    ymaps.ready(init); // Ожидание загрузки API с сервера Яндекса
    function init () {
        var myMap = new ymaps.Map('map', {
                center: [53.8840516, 27.5892717],
                zoom: 10
            }, {
                searchControlProvider: 'yandex#search'
            }),
            objectManager = new ymaps.ObjectManager({
                // Чтобы метки начали кластеризоваться, выставляем опцию.
                clusterize: true,
                // ObjectManager принимает те же опции, что и кластеризатор.
                gridSize: 32,
                clusterDisableClickZoom: true
            });

        // Чтобы задать опции одиночным объектам и кластерам,
        // обратимся к дочерним коллекциям ObjectManager.
        objectManager.objects.options.set('preset', 'islands#greenDotIcon');
        objectManager.clusters.options.set('preset', 'islands#greenClusterIcons');
        myMap.geoObjects.add(objectManager);

        $.ajax({
            url: "data.json"
        }).done(function(data) {
            objectManager.add(data);
        });

    }
</script>
<html>
<head>
    <title>Примеры. Добавление на карту большого числа объектов</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script src="https://api-maps.yandex.ru/2.1/?lang=ru-RU" type="text/javascript"></script>
    <script src="https://yandex.st/jquery/2.2.3/jquery.min.js" type="text/javascript"></script>
    <script src="object_manager.js" type="text/javascript"></script>
    <style>
    html, body, #map {
        width: 100%; height: 100%; padding: 0; margin: 0;
    }
    a {
        color: #04b; /* Цвет ссылки */
        text-decoration: none; /* Убираем подчеркивание у ссылок */
    }
    a:visited {
        color: #04b; /* Цвет посещённой ссылки */
    }
    a:hover {
        color: #f50000; /* Цвет ссылки при наведении на нее курсора мыши */
    }
    </style>
</head>
<body>
<div id="map" style="width: 1500px; height: 900px"></div>
</body>
</html>
