function clSubLevelListDynamicBox (subLevelEmptyMessage, selectElementId, displayFl) {

    this.display=displayFl;
    this.subLevelSelectElementId=selectElementId;
    this.subLevelEmptyMessage=subLevelEmptyMessage;
    this.subLevelSelectElement=document.getElementById(this.subLevelSelectElementId);
    this.subLevelSelectElementLabel=document.getElementById(this.subLevelSelectElementId+'Label');

    this.updateSubLevelBody = function (array, currentValue) {
        loadDataToSelectObject(this.subLevelSelectElement, this.subLevelSelectElementLabel, array, currentValue, this.subLevelEmptyMessage);
    };

    this.clear=function(){
        clearDataOfSelectObject(this.subLevelSelectElement, this.subLevelSelectElementLabel, this.subLevelEmptyMessage);
    };

    this.populateAndReDraw=function(subLevelJson, thiDiv) {
        var array;
        var currentValue = "";
        if (subLevelJson.list && subLevelJson.list.length) {
            currentValue = subLevelJson.selected;
            array = new Array();
            for (var i = 0,j = 0; i < subLevelJson.list.length; i++) {
                var subLevelVal = subLevelJson.list[i].object2;
                var subLevelId = subLevelJson.list[i].object1;
                array[j] = new Array();
                array[j][0] = subLevelId;
                array[j][1] = subLevelVal;
                j++;
            }
        }
        this.updateSubLevelBody(array, currentValue);
    }
}

var dynamicBoxes ={

    boxArray:[],
    maxElements:"",

    initDynamicBoxArray:function(maxLevels) {
        maxElements = maxLevels;
        boxArray = new Array();
    },

    init:function(array, currentValue, categEmptyMessage, selectElementName, levelIdx){
        var displayFl = (levelIdx==0)? true : false;
        boxArray[levelIdx] = new clSubLevelListDynamicBox( categEmptyMessage, selectElementName, displayFl);
        boxArray[levelIdx].updateSubLevelBody(array, currentValue);
    },

    initShow:function(array, currentValue, categEmptyMessage, selectElementName, levelIdx){
        boxArray[levelIdx] = new clSubLevelListDynamicBox(categEmptyMessage, selectElementName, true);
        boxArray[levelIdx].updateSubLevelBody(array, currentValue);
    },

    change:function(subLevelJson, thiDiv, levelIdx){
        for (var ii = 0; ii < maxElements; ii++) {
            var  id = boxArray[ii].subLevelSelectElementId;
            if ('undefined' != typeof id ) {
                if (ii == levelIdx ){
                    boxArray[ii].populateAndReDraw(subLevelJson, thiDiv);
                } else if (ii > levelIdx) {
                    boxArray[ii].clear();
                }
            }
        }
    },

    check:function (levelIdx) {
        if (levelIdx < maxElements){
            //alert ("check " + levelIdx + ", Id = " +boxArray[levelIdx].subLevelSelectElementId );
        }
    },
    checkAll:function () {
        for (var jj = 0; jj < maxElements; jj++) {
            var str = dynamicBoxes.toStringMy(boxArray[jj]);
            //alert ("checkAll jj=" + jj + ", Id = " + str);
        }
    },
    toStringMy:function (boxArrayObject){
        var objectStr='';
        if ('undefined' != typeof boxArrayObject) {
            objectStr += 'display=' + boxArrayObject.display;
            objectStr += ', subLevelSelectElementId=' +boxArrayObject.subLevelSelectElementId;
            objectStr += ', subLevelEmptyMessage=' + boxArrayObject.subLevelEmptyMessage;
            objectStr += ', subLevelSelectElement='+ boxArrayObject.subLevelSelectElement.name;
        } else {
            objectStr= 'undefined';
        }
        return objectStr;
    }

}

var selectableObject = {

    update:function(responseJson, thiDiv) {
        if (responseJson.subLevels && responseJson.subLevels.redraw ) {
            var levelIdx = responseJson.subLevels.level+1;
            dynamicBoxes.change(responseJson.subLevels, thiDiv, levelIdx);
        }
    }
}

function addOptionToSelectObject(oListbox, text, value, isDefaultSelected, isSelected) {
    var oOption = document.createElement("option");

    oOption.appendChild(document.createTextNode(text));
    oOption.setAttribute("value", value);

    if (isDefaultSelected) {
        oOption.defaultSelected = true;
    } else if (isSelected) {
        oOption.selected = true;
    }

    oListbox.appendChild(oOption);
};

function addEmpty(oListbox, text, value,isDefaultSelected, isSelected) {
    $(oListbox)
        .append("<option "+(isDefaultSelected?"defaultSelected='true'":isSelected?"selected='true'":"")+" value='"+value+"'>"+text+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>");
};


function removeAllSelectObjectOptions(oListbox) {
    if (oListbox.options) {
        oListbox.options.length = 0;
    }
};
function clearDataOfSelectObject(object, objectLabel, emptyMessage) {
    removeAllSelectObjectOptions(object);
    addEmpty(object, emptyMessage, "", true);
    object.style.visibility="hidden";
    if (objectLabel) {
       objectLabel.style.visibility="hidden";
    }
}

function loadDataToSelectObject(object, objectLabel, array, currentValue, emptyMessage) {
    var displayFl = false;
    if (object) {
        clearDataOfSelectObject(object, objectLabel, emptyMessage);
        if (array != null && 'undefined' != typeof array
            && array.length != null && 'undefined' != typeof array.length) {
            displayFl = (array.length > 0 ) ? true : false;
            for (var i = 0; i < array.length; i++) {
                if (currentValue == array[i][0]) {
                    addOptionToSelectObject(object, array[i][1], array[i][0], false, true);
                } else {
                    addOptionToSelectObject(object, array[i][1], array[i][0]);
                }
            }
        }

        if (displayFl && object.style.visibility == "hidden") {
            object.style.visibility ="visible";
            if (objectLabel && object.options[object.selectedIndex].value.charAt(0) == '_') {
                objectLabel.style.visibility = "visible";
            }
        } else if (!displayFl && object.style.visibility == "visible") {
            object.style.visibility="hidden";
            if (objectLabel) {
                objectLabel.style.visibility = "hidden";
            }
        }
    }
};

