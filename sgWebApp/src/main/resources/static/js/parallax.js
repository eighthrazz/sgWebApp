var background = background || (function(){
    var _args = {}; // private

    return {
        init : function(Args) {
            _args = Args;
        },
        set : function() {
            var url = '../img/bg/' + _args[0];
            $('.background')
            .css('background', 'url(' + url + ') no-repeat center fixed')
            .css('background-size', 'cover')
            .css('-webkit-background-size', 'cover')
            .css('-moz-background-size', 'cover')
            .css('-o-background-size', 'cover');
        }
    };
}());

function ready() {
    console.log("ready");
    
    //
    $(".homeCalendar").load("calendar .calendar");
    
    //
    $(".homeFacebook").load("facebook .facebook");
}