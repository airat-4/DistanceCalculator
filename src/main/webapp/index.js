$(function () {
    $.getJSON("GetCities", function (data){
        var fromCity = $("#fromCity");
        var toCity = $("#toCity");
        for(var i = 0; i < data.allCities.length; ++i){
            var option = $("<option>");
            option.attr("value", data.allCities[i].ID);
            option.html(data.allCities[i].name);
            fromCity.append(option);
            option = $("<option>");
            option.attr("value", data.allCities[i].ID);
            option.html(data.allCities[i].name);
            toCity.append(option);
        }
    });
    $("#add").click(function(){
        var tbody = $("#tbody");
        var tr = $("<tr>");
        var td = $("<td>");
        td.attr("name", $("#fromCity :selected").val());
        td.html($("#fromCity :selected").text());
        tr.append(td);
        td = $("<td>");
        td.attr("name", $("#toCity :selected").val());
        td.html($("#toCity :selected").text());
        tr.append(td);
        td = $("<td>");
        tr.append(td);
        td = $("<td>");
        tr.append(td);
        tbody.append(tr);
    });
    $("#calculate").click(function(){

    });
});