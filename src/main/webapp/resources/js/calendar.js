function internationalizeDatePickers(region, format) {
    var r;
    if (!region || $.trim(region).length == 0) { r = $.datepicker.regional[''];  }
    else if ($.datepicker.regional[region]) {  r =  $.datepicker.regional[region]; }
    if(r && r.dateFormat && format)   {  r.dateFormat = format; }
    if(r){$.datepicker.setDefaults(r);}
}

function initDatePickerDefaultValue(t, defaultValue, inactive_color){ if (t.value == '' || t.value == defaultValue) {  t.value = defaultValue; t.style.color = inactive_color; } }

function initI18nDatePickers(language, country, format, defaultValue) {

    var active_color = '#000000'; // Color of user provided text
    var inactive_color = '#C2C2C2'; // Color of default text

    internationalizeDatePickers('', format);
    internationalizeDatePickers(language, format);

    if (country && country.length && country.length > 0) { internationalizeDatePickers(language + "-" + country, format); }

    $("input.standardActiveCal").each(function () {
        if (defaultValue) {
            initDatePickerDefaultValue(this, defaultValue, inactive_color);
            $(this).blur(function () { if (this.value == '') { this.style.color = inactive_color;  this.value = defaultValue }});
            $(this).focus(function () { if (this.value == defaultValue) {   this.value = '' } this.style.color = active_color;});
        }
        $(this).datepicker({ numberOfMonths:2, showButtonPanel:true, color:"black", onSelect:function () {  this.style.color = active_color;$(this).unbind("focus", $.datepicker._showDatepicker); $(this).focus(); var t = $(this); setTimeout(function () { t.bind("focus", $.datepicker._showDatepicker) }, 1); }, onClose:function () { if (defaultValue && this.value == '') {  this.style.color = inactive_color;  this.value = defaultValue } }});
    });
}

