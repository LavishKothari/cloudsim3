function loadGraphs() {
<!-- GRAPH TEMPLATE -->

/*$('#cloudlet1Graph').highcharts({
        title: {
            text: 'Cloudlet Logs',
            x: -20 //center
        },
        subtitle: {
            text: '<!-- CLOUDLET ID HERE -->',
            x: -20
        },
		xAxis: {
			title : {
				text: 'Clock Tick'
			},
            categories: [<!-- Clock Ticks Here -->]
        },
        yAxis: {
            title: {
                text: 'Temperature (°C)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: '<!-- SERIES NAME -->',
            data: [<!-- SERIES VALUES -->]
        }/*, {
            name: 'New York',
            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
        }, {
            name: 'Berlin',
            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
        }, {
            name: 'London',
            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
        }]
    });*/

}


$(function() {
	$('#overviewli').addClass('active');    
	$('#reports').hide();

	$('#overviewli').click(function(e) {
        e.preventDefault();
        $('#overview').show();
		$('#reports').hide();
        
    });	

	$('#reportsli').click(function(e) {
        e.preventDefault();
		
        $('#reports').show();
		$('#overview').hide();
		window.dispatchEvent(new Event('resize'));
        
    });	
	
	$('.cloudlet-id').click(function(e) {
        e.preventDefault();
        $('.cloudlet-id').removeClass('active');
        $(this).addClass('active');
		$('#cloudletNum').html($(this).text()+' <span class="caret"></span>');	
		$('.cloudlet-graph').hide();
		$('.cloudlet-log').hide();
		loadGraphs();
		$('#'+$(this).attr('id')+'Graph').show();
		$('#'+$(this).attr('id')+'Log').show();
		$('.cloudlet-log-header').text($(this).text() + " Logs");
		window.dispatchEvent(new Event('resize'));	
		console.log($(this).text()+$(this).attr('id'));
    });

	

	$('.left-side-panel').click(function(e) {
        e.preventDefault();
        $('.left-side-panel').removeClass('active');
        $(this).addClass('active');
		console.log($(this).text());
    });
	
	
});
