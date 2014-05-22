
/*
 var active_color = '#000000'; // Color of user provided text
 var inactive_color = '#C2C2C2'; // Color of default text
 var date_default = "mm/dd/yyyy";
 */
$(document).ready(function() {
    $('form:first *:input[type!=hidden]:first').focus();
});  
 
$(document).ready(function () { $(document).find('form[focus]').each(function(index) { var attr = $(this).attr("focus");  if(attr && attr.length && attr.length > 0){ var input = $("#"+attr) ;  if(input){ var f = input.attr('focusable'); if(!((f && f == 'false') || (f && f == false))){ input.focus(); if(input.caretToEnd){input.caretToEnd();}}}}})});

function checkAll(id, name, flag) { $('input[name^=' + name + ']').attr('checked', flag);}
function checkOneOfAll(formid, name, objId){checkAll(formid, name, false);$('#'+objId).attr('checked',true);}

function wideScreen(url) {
    $.ajax({ url:url, dataType:"string", cache:false,
            success:function (direct) {
                var args = [ "#menucontent", "#menuWrapper", "#menu","div#contentWrapper div.leftColumn", "#contentWrapper .rightColumn .rightColumnIndent", "#tooggler .expandArrow"];
                if (direct && direct.length > 0) { $.each(args, function (i, val) { $(val).addClass("wideScreen")});  }
                else { $.each(args, function (i, val) { $(val).removeClass("wideScreen")}); }
            }
        }
    )
}

function messagePreviewFetch(data) {
    $('#iFrameHolder').contents().find('body #messageTitle').append(data.title);
    $('#iFrameHolder').contents().find('body #messageAbstract').append(data.messageAbstract);
    $('#iFrameHolder').contents().find('body #author').append(data.author);
    $('#iFrameHolder').contents().find('body #fromLabel').append(data.labelFrom);
    $('#iFrameHolder').contents().find('body #postedDate').append(data.posted);
    $('#iFrameHolder').contents().find('body #mesageBody').append(data.messageBody);
}

function emailPreviewFetch(data) {

    if (data.object1 && (!data.object2)) {
        $('#iFrameHolder').contents().find('body #subject').append(data.object1.subject);
        $('#iFrameHolder').contents().find('body #text').append(data.object1.text);
    } else if (data.object2 && data.object2[0]) {
        if(data.object2[0].errorTitle)  { $('#iFrameHolder').contents().find('body #error .errorTitle').append(data.object2[0].errorTitle); }
        if(data.object2[0].errorMessages) { $.each(data.object2[0].errorMessages, function(i, val) { $('#iFrameHolder').contents().find('body #error .errorMessages').append(getJsErr(i, val))}); }
        $('#iFrameHolder').contents().find('body #error').show();
    }
}


var previewLayer = {

    layerData:null,

    setLayerData:function (d) { this.layerData = d; },

    getLayerData:function (d) { return this.layerData },

    preview:function (target,  layerData) { try { this.setLayerData(layerData);  this.previewTarget(target); } catch (e) { } finally { } return false; },

    previewOnLoad:function () {

        var layerData = this.getLayerData();

        var complete = function(data) {
            try {layerData.onFetch(data); } finally {
                $('div.popUpWindow div.loader').css('display', 'none');
                $('div.popUpWindow #iFrameHolder').css('visibility', 'visible');
            }
        };

        var fail = function() { complete({}); };
        layerData.fetch(complete, fail);


    },

    //not the best impl, need use jquery-template engine with $.ajax
    previewTarget:function (target) {

        var layerData = this.getLayerData();

        showCoverLayer();

         var preview = "";

        preview += "<div class=\"popUpWindow "+layerData.previewStyle+"\" style=\"visibility: visible; \">";
        preview += "<div class=\"popUpTop\"> </div>";
        preview += "<div xmlns=\"http://www.w3.org/1999/xhtml\" class=\"popUpContent clearfix\"><h1>" + layerData.labelTitle + "</h1><hr/>";
        preview += "<div style=\"position:relative\">" + getLoaderLayer() + "<iframe src='"+layerData.layer+"'  style=\"visibility: visible;\" onload=\"previewLayer.previewOnLoad();\" height=\"1\" frameborder=\"0\" id=\"iFrameHolder\"/></div><hr/><div class=\"buttonRow\" align=\"right\"><button class=\"btn\" onclick=\"closePopUp()\"><span>" + layerData.labelClose + "</span></button></div></div>";
        preview += "<div class=\"popUpBottom\"></div>";
        preview += "</div>";


        $('body').append(preview);

        setPopupLayer(layerData.possitionLeft, layerData.possitionTop);

        $(window).bind('resize', function () {
            setPopupLayer(layerData.possitionLeft, layerData.possitionTop)
        });

        if (layerData.showOnTop) { $('html, body').animate({scrollTop:$('body').offset().top}, 0); }

        $('div.popUpWindow a.closePopUpBtn').bind('click', function () {
            closePopUp();
            if (layerData.showOnTop && target) {
                $('html, body', document).animate({scrollTop:$(target).offset().top}, 0);
            }
        });

        $('div.popUpWindow').css("visibility", "visible");
        $('div.popUpWindow a.closePopUpBtn').focus();
    }
};

LocateLayer = function(name){

    var layerData = null,  selected = null;

    this.setLayerData=function(d) { this.layerData = d; };
    this.getLayerData=function() { return this.layerData };
    this.locate=function (target,  layerData) { try { this.setLayerData(layerData);  this.locateTarget(target); } catch (e) { } finally { } return false; };

    this.locateOnLoad = function () {

        var layerData = this.getLayerData();

        var complete = function(data) {
            try { if(layerData.fetch && layerData.onFetch){ layerData.onFetch(data);} } finally {
            $('div.popUpWindow div.loader').css('display', 'none');
            $('div.popUpWindow #iFrameHolder').css('visibility', 'visible');
            if(!layerData.readOnly) {$('#iFrameHolder').contents().find('body #filterValue').focus();}
            }
        };

        var fail = function() { complete({}); };
        if(layerData.fetch)  { layerData.fetch(complete, fail); } else {  complete(); }

    };

    this.returnSelected = function (value) {
        try { this.onReturnSelected(value);  } finally { this.closePopUp(); }
    };

    this.onReturnSelected = function (value) {

        var layerData  = this.getLayerData();
        if(layerData.postHandler){
            layerData.postHandler(value)
        } else {
            if(layerData.target){
                var s = { names:"", ids:""};
                $.each(value, function (i, val) {
                    var name = layerData.nameGetter?val[layerData.nameGetter]:null, id = layerData.idGetter?val[layerData.idGetter]:null;
                    if (id){ if(s.ids.length == 0) { s.ids += "" + id } else {s.ids += "," + id} }
                    if (name) { if(s.names.length == 0) { s.names += "" + name} else { s.names += ", " + name;} }
                });
                if(layerData.target.names){$('#'+layerData.target.names).attr("value", s.names);}
                if(layerData.target.ids){$('#'+layerData.target.ids).attr("value", s.ids); {}}
            }
            if(layerData.finallyHandler){
                layerData.finallyHandler(value);
            }    
        }

    };

    //not the best impl, need use jquery-template engine with $.ajax
    this.locateTarget = function (target) {

        var layerData = this.getLayerData();

        showCoverLayer();

        var locate = "";
        locate += "<div class=\"popUpWindow "+(layerData.previewStyle?layerData.previewStyle:"popUpLocate")+"\" style=\"visibility: hidden; \">";
        locate += "<div class=\"popUpTop\"> </div>";
        locate += "<div xmlns=\"http://www.w3.org/1999/xhtml\" class=\"popUpContent clearfix\"><h1>" + layerData.labelTitle + "</h1><hr/>";
        locate += "<div style=\"position:relative\">" + getLoaderLayer() + "<iframe src='"+layerData.layer+"'  style=\"visibility: hidden;\" onload=\"locateLayerManager.layer('"+name+"').locateOnLoad();\" height=\"1\" frameborder=\"0\" id=\"iFrameHolder\"/></div><hr/><div class=\"buttonRow\" align=\"right\"><button class=\"btn\" onclick=\"closePopUp()\"><span>" + layerData.labelClose + "</span></button></div></div>";
        locate += "<div class=\"popUpBottom\"></div>";
        locate += "</div>";
        $('body').append(locate);

        setPopupLayer(layerData.positionLeft, layerData.positionTop);

        $(window).bind('resize', function () {
            setPopupLayer(layerData.positionLeft, layerData.positionTop)
        });

        if (layerData.showOnTop) { $('html, body').animate({scrollTop:$('body').offset().top}, 0); }

        $('div.popUpWindow a.closePopUpBtn').bind('click', function () {
            closePopUp();
            if (layerData.showOnTop && target) {
                $('html, body', document).animate({scrollTop:$(target).offset().top}, 0);
            }
            if(layerData.target.names){
            	f_setFocus(layerData.target.names);
            }    
            
        });

        $('div.popUpWindow').css("visibility", "visible");
        if(layerData && layerData.readOnly) { $('div.popUpWindow a.closePopUpBtn').focus();  }
    } ;

    this.closePopUp = function() {
        $('body').removeClass('hideSelects');
        $('div#coverLayer, div.loader, div.popUpWindow').css('visibility', 'hidden').remove();
        $('div#coverLayer').remove();
        return false;
    };

    this.loadingStart = function() {  try {  if (!this.getLayerData().skipHiddenBeforeLoading) {  $('div.popUpWindow #iFrameHolder').css('visibility', 'hidden');  } $('div.popUpWindow div.loader').css('display', 'block'); } catch (e) {}};
    this.loadingEnd = function(){  try { $('div.popUpWindow div.loader').css('display', 'none'); $('div.popUpWindow #iFrameHolder').css('visibility', 'visible');} catch (e) {}};
};

var locateLayerManager =  {
    locates:[],
    focus: null,
    init:function(){ $(document).ready(function () { $(document).find('[focusable="true"]').blur(function(){ locateLayerManager.focus =$(this) })})},
    locate:function(name, target,  layerData) { this.locates[name] = new LocateLayer(name);return this.locates[name].locate(target,  layerData) } ,
    layer:function(name) {   return this.locates[name] ; }
};

function efocus(e) {try {  e.focus() } catch (e) {}}

function doLayerReturnSelected(doc, obj, name) {

    try {

        var manager = doc.locateLayerManager, loadingTimeOut = null, layer = manager.layer(name);
        var start = function() {  try { $("button").attr('disabled', 'disabled'); if (layer.getLayerData().loadingIconTimeOut) { loadingTimeOut = setTimeout(layer.loadingStart, layer.getLayerData().loadingIconTimeOut); } else{ layer.loadingStart();} } catch (e) {}};
        var end = function() {  try { if(loadingTimeOut) { clearTimeout(loadingTimeOut); } layer.loadingEnd();$("button").attr('disabled', ''); } catch (e) {}};

        start();

        $.ajax({ url:layer.layerData.returnSelectedAction, dataType:"json", cache:false, async:true,
                data:$("#" + obj).serialize(),
                type:'POST',
                success:function (value) { end(); layer.returnSelected(value);  if(manager.focus){efocus(manager.focus)}},
                error:function () { end();  layer.returnSelected(null);  if(manager.focus){efocus(manager.focus)}}
            }
        );

    } catch(e) { } finally{ }

    return false;
}

function getLoaderLayer() { return  '<div class="loader" style="position:absolute;top:45%;left:45%"><img src="/defman/resources/images/ui/loading.gif" /></div>'}
function getJsErr(i, val) { return '<div> '+val+'</div>' };

function showCoverLayer() {
    $('body').append('<div id=\"coverLayer\">&nbsp;</div>');
    $('body').addClass('hideSelects');
    $('div#coverLayer').css('visibility', 'visible');
}

function setPopupLayer(fixedMarginWidth, fixedMarginTop) {

    // determine the width and height of the pop up window for centering
    var popUpWidth = $('div.popUpWindow').width();
    var popUpHeight = $('div.popUpWindow').height();
    // reset size and positioning variables
    var docWidth = 0; var docHeight = 0; var marginWidth = 0; var marginTop = 0;  var currentScrollPos = 0;

    // get the window's scrolled position
    if (document.documentElement.scrollTop == 0) {  currentScrollPos = document.body.scrollTop; }
    else {  currentScrollPos = document.documentElement.scrollTop }

    docWidth = documentWidth();

    // determine the height of the page
    var wrapperHeight = $('#headerWrapper').outerHeight() + $('#contentWrapper').outerHeight() + $('#footerWrapper').outerHeight()+20;

    // get height of the document compare it to the page's height and determine whichever is taller
    if ($(window).height() > (wrapperHeight)) { docHeight = $(window).height() }
    else { docHeight = wrapperHeight; }

    // set the the width of the coverlayer
    $('div#coverLayer').width(docWidth);
    $('div#coverLayer').height(docHeight);

    if (!fixedMarginWidth) {
        // Determine the distance the window should be from the left
        if (docWidth > popUpWidth) { marginWidth = Math.round((docWidth - popUpWidth) / 2); }
    }

    if (!fixedMarginTop) {
        // Determine the distance the window should be from the top
        if (($(window).height() - popUpHeight) > 0) { marginTop = ($(window).height() - popUpHeight) / 2 + currentScrollPos; }
        else { marginTop = currentScrollPos;}
    }

    // move the pop up div to the center
    $('div.popUpWindow').css("margin-left", marginWidth);
    $('div.popUpWindow').css("margin-top", fixedMarginTop ? fixedMarginTop : marginTop);

    // get the total height of the pop up including the distance to the top of the browser.
    var totalHeight = $('div.popUpWindow').height() + marginTop;

    // if the total height is greater than the height of the document adjust the height of the coverlayer
    if (totalHeight > docHeight) {
        docHeight = totalHeight;
        $('div#coverLayer').height(docHeight);
    }
}

function coverLayerSize() {
    var docHeight;
    var wrapperHeight = $('#headerWrapper').outerHeight() + $('#contentWrapper').outerHeight() + $('#footerWrapper').outerHeight() + 20;
    if ($(window).height() > (wrapperHeight)) {
        docHeight = $(window).height()
    } else {
        docHeight = wrapperHeight;
    }
// set the the height and width of the cover layer
    $('div#coverLayer').width(documentWidth());
    $('div#coverLayer').height(docHeight);
}


function closePopUp() {
    $('body').removeClass('hideSelects');
    $('div#coverLayer, div.loader, div.popUpWindow').css('visibility', 'visible').remove();
    $('div#coverLayer').remove();
    return false;
}

function documentWidth() {
    var w = document.documentElement;var d = document.body; var docWidth;
    if (w.scrollWidth < d.scrollWidth) { docWidth = d.scrollWidth } else { docWidth = w.scrollWidth  }
    if (docWidth < w.clientWidth) { docWidth = w.clientWidth }
    return docWidth;
}

function countryUsesState(value ,label, jscountries, onload, displaystyle){ try { var usesState = jscountries[value]; if (usesState && usesState == 'true') { $(label).removeClass(!displaystyle?"hide":displaystyle) } else if (!onload) { $(label).addClass(!displaystyle?"hide":displaystyle)} } catch (e) { }}
function copyValues(from , to){ $.each(from,  function (i, val) { var f = to[i] ? document.getElementById(to[i]):null; var t = val?document.getElementById(val):null; if(f && t) { f.value = t.value;}} )}

function f_setFocus(elId) {	var ie9=navigator.appVersion.search("MSIE 9"); 
//if ((navigator.appName=="Microsoft Internet Explorer")&&(ie9>=0)){
//	$("#"+elId).focus();
//}
  //modified to set focus after returning from popup wins 
  $("#"+elId).focus();
}

// MANTA-504 Disable "Enter" key on all screens
document.onkeypress = function (a) {
	var searchEl = $("#search")	;
//alert ("searchEl=" + searchEl + ", searchEl.attr('id')=" + searchEl.attr('id'));

  a = a || window.event;

// MANTA-568, MANTA-559 Enable "Enter" key on all Search screens
  if (a.keyCode == 13 || a.which == 13) {
//	  alert ($("form:first").attr('id')+": searchEl.attr('id')=" + searchEl.attr('id'));
	  if (searchEl.attr('id') == 'search' ) {	
    	  $("form:first").submit();
      } else {
          var b = document.activeElement.type;
          return ("submit" == b);
      }  
  }	  

};  

