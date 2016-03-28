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
        var td = $("<td class = 'from'>");
        td.attr("name", $("#fromCity :selected").val());
        td.html($("#fromCity :selected").text());
        tr.append(td);
        td = $("<td class = 'to'>");
        td.attr("name", $("#toCity :selected").val());
        td.html($("#toCity :selected").text());
        tr.append(td);
        td = $("<td id = 'C_"+$("#fromCity :selected").val()+"_"+$("#toCity :selected").val()+"'>");
        tr.append(td);
        td = $("<td id = 'M_"+$("#fromCity :selected").val()+"_"+$("#toCity :selected").val()+"'>");
        tr.append(td);
        tbody.append(tr);
    });
    $("#calculate").click(function(){
        var fromCities = [];
        var toCities = [];
        var from = $(".from").toArray();
        var to =$(".to").toArray();
        for(var i = 0; i < from.length; ++i){
            fromCities[i] = from[i].getAttribute("name");
            toCities[i] = to[i].getAttribute("name");
        }
        $.getJSON("Calculate", {type: $("#type :selected").val(), from: fromCities, to: toCities},
        function (data){
            for(var i = 0; i < data.result.length; ++i){
                var crowflight = data.result[i].crowflight;
                if(crowflight == undefined)
                    $("#C_"+fromCities[i]+"_"+toCities[i]).html("-");
                else
                    $("#C_"+fromCities[i]+"_"+toCities[i]).html(crowflight);
                var distanceMatrix = data.result[i].distanceMatrix;
                if(distanceMatrix == undefined)
                    $("#M_"+fromCities[i]+"_"+toCities[i]).html("-");
                else
                    $("#M_"+fromCities[i]+"_"+toCities[i]).html(distanceMatrix);
            }
        });

    });
});