(function ($) {
    $.caretTo = function (el, index) { if (el.createTextRange) {  var range = el.createTextRange();  range.move("character", index); range.select();  } else if (el.selectionStart != null) { el.focus(); el.setSelectionRange(index, index); }};
    $.caretPos = function (el) {if ("selection" in document) { var range = el.createTextRange();  try { range.setEndPoint("EndToStart", document.selection.createRange());} catch (e) {  return 0; }  return range.text.length; } else if (el.selectionStart != null) { return el.selectionStart; }};
    $.fn.caret = function (index, offset) {  if (typeof(index) === "undefined") { return $.caretPos(this.get(0));} return this.queue(function (next) { if (isNaN(index)) {  var i = $(this).val().indexOf(index); if (offset === true) {  i += index.length; } else if (typeof(offset) !== "undefined") { i += offset; } $.caretTo(this, i); } else {  $.caretTo(this, index); } next(); });};
    $.fn.caretToStart = function () { return this.caret(0); };
    $.fn.caretToEnd = function () { return this.queue(function (next) {  $.caretTo(this, $(this).val().length); next();});};
}(jQuery));
