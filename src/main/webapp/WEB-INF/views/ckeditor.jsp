<script type="text/javascript"   src="${resources}/ckeditor/ckeditor.js"></script>
<script type="text/javascript">$(function(){ CKEDITOR.replace("${param['id']}", {
    language: "${requestScope['appUser'].locale}", uiColor:"rgb(159, 176, 196)",readOnly:${param['readonly']} });
})</script>