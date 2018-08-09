%{--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"--}%
%{--"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">--}%
%{--<html xmlns="http://www.w3.org/1999/xhtml">--}%
%{--<head>--}%
    %{--<meta http-equiv="content-type" content="text/html; charset=utf-8"/>--}%
    %{--<title>Google Maps JavaScript API Example</title>--}%
    %{--<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABCDE"--}%
            %{--type="text/javascript"></script>--}%
    %{--<script type="text/javascript">--}%
        %{--var geocoder;--}%
        %{--var map;--}%

        %{--function initialize() {--}%
            %{--geocoder = new google.maps.Geocoder();--}%
            %{--var latlng = new google.maps.LatLng(-34.397, 150.644);--}%
            %{--var mapOptions = {--}%
                %{--zoom: 8,--}%
                %{--center: latlng--}%
            %{--}--}%
            %{--map = new google.maps.Map(document.getElementById('map'), mapOptions);--}%
        %{--}--}%

        %{--function codeAddress() {--}%
            %{--var address = document.getElementById('address').value;--}%
            %{--geocoder.geocode({'address': address}, function (results, status) {--}%
                %{--if (status == 'OK') {--}%
                    %{--map.setCenter(results[0].geometry.location);--}%
                    %{--var marker = new google.maps.Marker({--}%
                        %{--map: map,--}%
                        %{--position: results[0].geometry.location--}%
                    %{--});--}%
                %{--} else {--}%
                    %{--alert('Geocode was not successful for the following reason: ' + status);--}%
                %{--}--}%
            %{--});--}%
        %{--}--}%
    %{--</script>--}%
%{--</head>--}%

%{--<body onload="initialize()">--}%
%{--<div id="map" style="width: 800px; height: 500px;"></div>--}%

%{--<div>--}%
    %{--<input id="address" type="textbox" value="Sydney, NSW">--}%
    %{--<input type="button" value="Encode" onclick="codeAddress()">--}%
%{--</div>--}%
%{--</body>--}%

<g:form action="map" method="post">
    <div class="dialog">
        <label for="address">Scratch by street:</label>
        <input type="text" id="address" name="address"/>
    </div>
</g:form>